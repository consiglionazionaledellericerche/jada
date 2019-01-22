package it.cnr.jada.persistency.sql;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.BeanIntrospector;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.OrderedHashMap;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

public class SQLBuilder extends SQLQuery {

    public static final int EQUALS = 8192;
    public static final int NOT_EQUALS = 8193;
    public static final int LIKE_FILTER = 8194;
    public static final int LESS = 16386;
    public static final int LESS_EQUALS = 16387;
    public static final int GREATER = 16388;
    public static final int GREATER_EQUALS = 16389;
    public static final int LIKE = 40966;
    public static final int CONTAINS = 40967;
    public static final int STARTSWITH = 40968;
    public static final int ENDSWITH = 40969;
    public static final int ISNULL = 8201;
    public static final int ISNOTNULL = 8202;
    public static final int BETWEEN = 16395;
    public static final Object ORDER_ASC = null;
    public static final Object ORDER_DESC = null;
    private static final int NUMERIC_OPERATORS = 16384;
    private static final int STRING_OPERATORS = 8192;
    private static final int LIKE_OPERATORS = 40960;
    private static final int OPERATOR_TYPE = 65280;
    private final StringBuffer orderBy;
    private final StringBuffer preOrderBy;
    private final StringBuffer groupBy;
    private StringBuffer clauses;
    private String header;
    private boolean forUpdate;
    private String forUpdateOf;
    private boolean firstClause;
    private StringBuffer fromClause;
    private boolean distinctClause;
    private String command;
    private Map orderByClauses;
    private boolean customStatement;
    private String schema;
    private StringBuffer updateClauses;
    private StringBuffer columns;
    private Map joins;
    private boolean autoJoins;
    private boolean firstHavingClause;
    private StringBuffer havingClauses;

    public SQLBuilder()
            throws IntrospectionException {
        clauses = new StringBuffer();
        orderBy = new StringBuffer();
        preOrderBy = new StringBuffer();
        groupBy = new StringBuffer();
        firstClause = true;
        customStatement = false;
        updateClauses = null;
        columns = null;
        autoJoins = false;
        havingClauses = new StringBuffer();
        firstHavingClause = true;
        super.columnMap = null;
    }

    public SQLBuilder(ColumnMap columnmap) {
        clauses = new StringBuffer();
        orderBy = new StringBuffer();
        preOrderBy = new StringBuffer();
        groupBy = new StringBuffer();
        firstClause = true;
        customStatement = false;
        updateClauses = null;
        columns = null;
        autoJoins = false;
        havingClauses = new StringBuffer();
        firstHavingClause = true;
        super.columnMap = columnmap;
        header = columnmap.getDefaultSelectHeaderSQL();
        command = "SELECT";
        addTableToHeader(columnmap.getTableName());
    }

    public SQLBuilder(Class class1)
            throws IntrospectionException {
        this(class1, "default");
    }

    public SQLBuilder(Class class1, CompoundFindClause compoundfindclause)
            throws IntrospectionException {
        this(class1);
        addClause(compoundfindclause);
    }

    public SQLBuilder(Class class1, String s)
            throws IntrospectionException {
        this(((SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo(class1)).getColumnMap(s));
    }

    public static String getSQLOperator(int i) {
        switch (i) {
            case ISNULL:
                return "IS NULL";

            case ISNOTNULL:
                return "IS NOT NULL";

            case EQUALS:
                return "=";

            case LESS:
                return "<";

            case GREATER:
                return ">";

            case LESS_EQUALS:
                return "<=";

            case GREATER_EQUALS:
                return ">=";

            case NOT_EQUALS:
                return "<>";

            case LIKE:
            case CONTAINS:
            case STARTSWITH:
                return "LIKE";
            case ENDSWITH:
                return "LIKE";
        }
        return "";
    }

    public void addBetweenClause(String s, String s1, Object obj, Object obj1) {
        ColumnMapping columnmapping = super.columnMap.getMappingForProperty(s1);
        addSQLBetweenClause(s, Optional.ofNullable(columnmapping.getColumnName())
                .filter(columnName -> columnName.indexOf(".") != -1)
                .map(columnName -> columnName)
                .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                obj, obj1, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter());
    }

    public void addClause(FindClause findclause) {
        if (findclause instanceof SimpleFindClause) {
            SimpleFindClause simplefindclause = (SimpleFindClause) findclause;
            if (simplefindclause.getSqlClause() != null) {
                addSQLClause(simplefindclause.getLogicalOperator(), simplefindclause.getSqlClause());
                if (simplefindclause.getOperator() != ISNULL && simplefindclause.getOperator() != ISNOTNULL) {
                    ColumnMapping columnmapping = super.columnMap.getMappingForProperty(simplefindclause.getPropertyName());
                    if (columnmapping != null)
                        addParameter(simplefindclause.getValue(), columnmapping.getSqlType(), columnmapping.getColumnScale());
                }
            } else {
                addClause(simplefindclause.getLogicalOperator(), simplefindclause.getPropertyName(), simplefindclause.getOperator(), simplefindclause.getValue(), simplefindclause.isCaseSensitive());
            }
        } else if (findclause instanceof CompoundFindClause) {
            CompoundFindClause compoundfindclause = (CompoundFindClause) findclause;
            StringBuffer stringbuffer = clauses;
            boolean flag = firstClause;
            clauses = new StringBuffer();
            firstClause = true;
            for (Enumeration enumeration = compoundfindclause.getClauses(); enumeration.hasMoreElements(); addClause((FindClause) enumeration.nextElement()))
                ;
            String s = clauses.toString();
            clauses = stringbuffer;
            if (!firstClause) {
                firstClause = flag;
                addLogicalOperator(compoundfindclause.getLogicalOperator());
                clauses.append("\t( ");
                clauses.append(s);
                clauses.append(" )");
            } else {
                firstClause = flag;
            }
        }
    }

    public void addClause(String s, String s1, int i, Object obj) {
        addClause(s, s1, i, obj, true);
    }

    /**
     * 1° parametro valore da confrontare dalla decode
     * 2° parametro orderhashtable altre condizioni da confrontare nella decode e comportamento
     * 3° parametro valore di default della decode
     */
    public String addDecode(Object o, OrderedHashtable table, Object obj) {
        String s = new String();
        s = s + "Decode (" + o + ",";
        for (Enumeration e = table.keys(); e.hasMoreElements(); ) {
            s = s + table.get(e.nextElement()) + ",";
        }
        s = s + obj + ")";
        return s;
    }

    /**
     * 1° parametro valore da confrontare nella decode
     * 2° parametro 1a condizione da verificare nella decode
     * 3° parametro comportamento caso 1a condizione
     * 4° parametro 2a condizione da verificare nella decode gestito anche null
     * 5° parametro comportamento 2a condizione  gestito anche null
     * 6° parametro valore di default della decode
     */
    public String addDecode(Object o, String s1, String s2, String s3, String s4, Object obj) {
        OrderedHashtable cond = new OrderedHashtable();
        if (s1 != null && s2 != null) {
            cond.put("1", s1);
            cond.put("2", s2);
        }
        if (s3 != null && s4 != null) {
            cond.put("3", s3);
            cond.put("4", s4);
        }
        return addDecode(o, cond, obj);
    }

    public void addClause(String s, String s1, int i, Object obj, boolean flag) {
        ColumnMapping columnmapping = super.columnMap.getMappingForProperty(s1);
        if (columnmapping != null) {
            addSQLClause(s,
                    Optional.ofNullable(columnmapping.getColumnName())
                    .filter(columnName -> columnName.indexOf(".") != -1)
                    .map(columnName -> columnName)
                    .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                    i, obj, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter(), flag, false);
        } else if (obj instanceof KeyedPersistent)
            try {
                SQLPersistentInfo sqlpersistentinfo = (SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo(obj.getClass());
                boolean flag1 = firstClause;
                firstClause = true;
                StringBuffer stringbuffer = clauses;
                clauses = new StringBuffer();
                String s5;
                for (Iterator iterator = sqlpersistentinfo.getOidPersistentProperties().keySet().iterator(); iterator.hasNext(); addClause("AND", Prefix.prependPrefix(s1, s5), i, sqlpersistentinfo.getIntrospector().getPropertyValue(obj, s5), flag))
                    s5 = (String) iterator.next();

                String s3 = clauses.toString();
                clauses = stringbuffer;
                if (!firstClause) {
                    firstClause = flag1;
                    addLogicalOperator(s);
                    clauses.append("\t( ");
                    clauses.append(s3);
                    clauses.append(" )");
                } else {
                    firstClause = flag1;
                }
            } catch (IntrospectionException introspectionexception) {
                throw new IntrospectionError(introspectionexception);
            }
        else if ((obj instanceof Class) && it.cnr.jada.persistency.KeyedPersistent.class.isAssignableFrom((Class) obj) && (i == ISNULL || i == ISNOTNULL))
            try {
                SQLPersistentInfo sqlpersistentinfo1 = (SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo((Class) obj);
                boolean flag2 = firstClause;
                firstClause = true;
                StringBuffer stringbuffer1 = clauses;
                clauses = new StringBuffer();
                String s6;
                for (Iterator iterator1 = sqlpersistentinfo1.getOidPersistentProperties().keySet().iterator(); iterator1.hasNext(); addClause("AND", Prefix.prependPrefix(s1, s6), i, null, flag))
                    s6 = (String) iterator1.next();

                String s4 = clauses.toString();
                clauses = stringbuffer1;
                if (!firstClause) {
                    firstClause = flag2;
                    addLogicalOperator(s);
                    clauses.append("\t( ");
                    clauses.append(s4);
                    clauses.append(" )");
                } else {
                    firstClause = flag2;
                }
            } catch (IntrospectionException introspectionexception1) {
                throw new IntrospectionError(introspectionexception1);
            }
        else if (isAutoJoins()) {
            int j = s1.indexOf('.');
            if (j >= 0) {
                String s2 = s1.substring(0, j);
                s1 = s1.substring(j + 1);
                try {
                    Class class1 = BeanIntrospector.getSQLInstance().getPropertyType(super.columnMap.getPersistentInfo().getPersistentClass(), s2);
                    SQLPersistentInfo sqlpersistentinfo2 = (SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo(class1);
                    ColumnMap columnmap = sqlpersistentinfo2.getDefaultColumnMap();
                    addJoin(s2, s2, columnmap);
                    ColumnMapping columnmapping1 = columnmap.getMappingForProperty(s1);
                    addSQLClause("AND", s2 + "." + columnmapping1.getColumnName(), i, obj, columnmapping1.getSqlType(), columnmapping1.getColumnScale(), columnmapping1.getConverter(), flag, false);
                } catch (IntrospectionException introspectionexception2) {
                    throw new DetailedRuntimeException(introspectionexception2);
                }
            }
        }
    }

    public void addClausesUsing(Persistent persistent, String as[], boolean flag)
            throws IntrospectionException {
        addClausesUsing(persistent, ((Introspector) (BeanIntrospector.getSQLInstance())), as, flag);
    }

    public void addClausesUsing(Persistent persistent, Introspector introspector, String as[], boolean flag)
            throws IntrospectionException {
        for (int i = 0; i < as.length; i++) {
            String s = as[i];
            ColumnMapping columnmapping = super.columnMap.getMappingForProperty(s);
            Object obj = introspector.getPropertyValue(persistent, s);
            if (flag || obj != null)
                addSQLClause("AND", Optional.ofNullable(columnmapping.getColumnName())
                        .filter(columnName -> columnName.indexOf(".") != -1)
                        .map(columnName -> columnName)
                        .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                        EQUALS, obj, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter(), true, false);
        }

    }

    public void addClausesUsing(Persistent persistent, Introspector introspector, boolean flag) {
        try {
            for (Iterator iterator = super.columnMap.getColumnMappings().iterator(); iterator.hasNext(); ) {
                ColumnMapping columnmapping = (ColumnMapping) iterator.next();
                Object obj = introspector.getPropertyValue(persistent, columnmapping.getPropertyName());
                if (flag || obj != null)
                    addSQLClause("AND", Optional.ofNullable(columnmapping.getColumnName())
                            .filter(columnName -> columnName.indexOf(".") != -1)
                            .map(columnName -> columnName)
                            .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                            EQUALS, obj, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter(), true, false);
            }

        } catch (IntrospectionException introspectionexception) {
            throw new it.cnr.jada.persistency.IntrospectionError(introspectionexception);
        }
    }

    public void addClausesUsing(Persistent persistent, boolean flag) {
        addClausesUsing(persistent, ((Introspector) (BeanIntrospector.getSQLInstance())), flag);
    }

    public void addClauseUsing(String s, Persistent persistent, String s1, String s2, Introspector introspector, boolean flag, boolean flag1)
            throws IntrospectionException {
        Object obj = introspector.getPropertyValue(persistent, s2);
        if (obj == null && !flag)
            return;
        Class class1 = introspector.getPropertyType(persistent.getClass(), s2);
        if (it.cnr.jada.persistency.KeyedPersistent.class.isAssignableFrom(class1)) {
            PersistentProperty persistentproperty;
            for (Iterator iterator = introspector.getPersistentInfo(class1).getOidPersistentProperties().values().iterator(); iterator.hasNext(); addClauseUsing(s, ((Persistent) ((KeyedPersistent) obj)), Prefix.prependPrefix(s1, s2), persistentproperty.getName(), introspector, flag, true))
                persistentproperty = (PersistentProperty) iterator.next();

        } else {
            ColumnMapping columnmapping = super.columnMap.getMappingForProperty(Prefix.prependPrefix(s1, s2));
            if (columnmapping != null)
                addSQLClause(s, Optional.ofNullable(columnmapping.getColumnName())
                        .filter(columnName -> columnName.indexOf(".") != -1)
                        .map(columnName -> columnName)
                        .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                        EQUALS, obj, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter(), flag1, false);
        }
    }

    public void addColumn(String s) {
        addColumn(s, null);
    }

    public void addColumn(String s, String s1) {
        if (columns == null)
            columns = new StringBuffer();
        else
            columns.append(',');
        columns.append(s);
        if (s1 != null) {
            columns.append(' ');
            columns.append(s1);
        }
    }

    public void addGroupBy(String s) {
        addSQLGroupBy(super.columnMap.getMappingForProperty(s).getColumnName());
    }


    public void generateJoin(Class<?> first, Class<?> second, String s, String alias) {
        try {
            SQLPersistentInfo sqlPersistentinfoFirst = (SQLPersistentInfo)
                    BeanIntrospector.getSQLInstance().getPersistentInfo(first);
            SQLPersistentInfo sqlPersistentinfoSecond = (SQLPersistentInfo)
                    BeanIntrospector.getSQLInstance().getPersistentInfo(second);
            addJoin(s, alias, sqlPersistentinfoSecond.getDefaultColumnMap(), sqlPersistentinfoFirst.getDefaultColumnMap());

        } catch (IntrospectionException introspectionexception1) {
            throw new IntrospectionError(introspectionexception1);
        }
    }

    public void generateJoin(String s, String alias) {
        try {
            Class class1 = BeanIntrospector.getSQLInstance().getPropertyType(super.columnMap.getPersistentInfo().getPersistentClass(), s);
            SQLPersistentInfo sqlpersistentinfo2 = (SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo(class1);
            ColumnMap columnmap = sqlpersistentinfo2.getDefaultColumnMap();
            addJoin(s, alias, columnmap);
        } catch (IntrospectionException introspectionexception1) {
            throw new IntrospectionError(introspectionexception1);
        }
    }

    private boolean addJoin(String s, String alias, ColumnMap columnmap, ColumnMap parentColumnmap) {
        StringBuffer stringbuffer = new StringBuffer();
        if (joins == null || !joins.containsKey(alias)) {
            final Stream<ColumnMapping> stream = parentColumnmap.getPropertyMappings().stream()
                    .filter(ColumnMapping.class::isInstance)
                    .map(ColumnMapping.class::cast);
            stream
                    .filter(columnmapping -> columnmapping.getPropertyName().startsWith(s))
                    .forEach(columnmapping -> {
                        String s1 = columnmapping.getPropertyName().substring(s.length() + 1);
                        ColumnMapping columnmapping1 = columnmap.getMappingForProperty(s1);
                        Optional.ofNullable(columnmapping1)
                                .filter(columnMapping -> columnMapping.isPrimary())
                                .ifPresent(columnMapping -> {
                                    if (stringbuffer.length() > 0)
                                        stringbuffer.append(" AND ");
                                    stringbuffer.append(parentColumnmap.getTableName());
                                    stringbuffer.append('.');
                                    stringbuffer.append(columnmapping.getColumnName());
                                    stringbuffer.append(" = ");
                                    stringbuffer.append(alias);
                                    stringbuffer.append('.');
                                    stringbuffer.append(columnMapping.getColumnName());
                                });
                    });
            if (Optional.ofNullable(stringbuffer)
                    .filter(stringBuffer -> stringbuffer.length() == 0).isPresent())
                return false;
            joins = Optional.ofNullable(joins)
                    .orElseGet(() -> {
                        return new HashMap();
                    });
            joins.put(alias, new String[]{
                    columnmap.getTableName(), stringbuffer.toString()});
        }
        return true;
    }

    public boolean addJoin(String s, String alias, ColumnMap columnmap) {
        return addJoin(s, alias, columnmap, super.columnMap);
    }

    public void addJoin(String s, int parametro, String s1) {
        ColumnMapping columnmapping = super.columnMap.getMappingForProperty(s);
        ColumnMapping columnmapping1 = super.columnMap.getMappingForProperty(s1);
        addSQLJoin(columnmapping.getColumnName(), parametro, columnmapping1.getColumnName());
    }

    public void addJoin(String s, String s1) {
        addJoin(s, EQUALS, s1);
    }

    protected void addLogicalOperator(String s) {
        if (firstClause) {
            firstClause = false;
        } else {
            clauses.append(' ');
            clauses.append(s != null ? s : "AND");
            clauses.append('\n');
        }
    }

    public void addOrderBy(String s) {
        if (orderBy.length() > 0)
            orderBy.append(',');
        orderBy.append(s);
        resetStatement();
    }

    public void addPreOrderBy(String s) {
        if (preOrderBy.length() > 0)
            preOrderBy.append(',');
        preOrderBy.append(s);
        resetStatement();
    }

    public void addSQLBetweenClause(String s, String s1, Object obj, Object obj1) {
        addSQLBetweenClause(s, s1, obj, obj1, 1111, 0, null);
    }

    public void addSQLBetweenClause(String s, String s1, Object obj, Object obj1, int i, int j, SQLConverter sqlconverter) {
        if (obj != null || obj1 != null) {
            addLogicalOperator(s);
            clauses.append("\t( ");
            clauses.append(s1);
            clauses.append(' ');
        }
        if (obj != null && obj1 != null) {
            clauses.append("BETWEEN ? AND ? )");
            super.parameters.addElement(new SQLParameter(obj, i, j, sqlconverter));
            super.parameters.addElement(new SQLParameter(obj1, i, j, sqlconverter));
        } else if (obj != null) {
            clauses.append(">= ? )");
            super.parameters.addElement(new SQLParameter(obj, i, j, sqlconverter));
        } else if (obj1 != null) {
            clauses.append("<= ? )");
            super.parameters.addElement(new SQLParameter(obj1, i, j, sqlconverter));
        }
        resetStatement();
    }

    public void addSQLClause(String s, String s1) {
        addLogicalOperator(s);
        clauses.append("\t( ");
        clauses.append(s1);
        clauses.append(" )");
        resetStatement();
    }

    public void addSQLClause(String s, String s1, int i, SQLBuilder sqlbuilder) {
        addLogicalOperator(s);
        clauses.append("\t( ");
        int j = i & 0xff00;
        boolean flag = j == LIKE_OPERATORS;
        if (flag) {
            clauses.append("UPPER(");
            clauses.append(s1);
            clauses.append(")");
        } else {
            clauses.append(s1);
        }
        clauses.append(' ');
        clauses.append(getSQLOperator(i));
        switch (i) {
            case ISNULL:
            case ISNOTNULL:
                clauses.append(" )");
                return;
        }
        clauses.append(" (");
        clauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()))
            ;
        clauses.append(") )");
        resetStatement();
    }

    /**
     * * Aggiunge una clausola SQL, nel caso in cui l'oggetto passato è null
     * * fa fallire tutta la select
     */
    public void addNotNullableSQLClause(String s, String s1, int i, Object obj) {
        addSQLClause(s, s1, i, obj, 1111, 0, null, true, true);
    }

    public void addSQLClause(String s, String s1, int i, Object obj) {
        addSQLClause(s, s1, i, obj, 1111, 0, null, true, false);
    }

    public void addSQLClause(String s, String s1, int i, Object obj, int j, int k, SQLConverter sqlconverter,
                             boolean flag, boolean nullable) {
        if (!nullable && i != ISNULL && i != ISNOTNULL && (obj == null || "".equals(obj)))
            return;
        addLogicalOperator(s);
        clauses.append("\t( ");
        int l = i & 0xff00;
        boolean flag0 = i == LIKE_FILTER;
        if (flag0) {
            s1 = "TO_CHAR(" + s1 + ")";
        }
        boolean flag1 = l == LIKE_OPERATORS || l == EQUALS && !flag;
        if (flag1) {
            clauses.append("UPPER(");
            clauses.append(s1);
            clauses.append(")");
        } else {
            if (sqlconverter != null) {
                clauses.append(sqlconverter.columnName(s1));
            } else {
                clauses.append(s1);
            }
        }
        clauses.append(' ');
        switch (i) {
            case ISNULL:
                clauses.append("IS NULL )");
                return;

            case ISNOTNULL:
                clauses.append("IS NOT NULL )");
                return;

            case EQUALS:
                clauses.append("=");
                break;

            case LESS:
                clauses.append("<");
                break;

            case GREATER:
                clauses.append(">");
                break;

            case LESS_EQUALS:
                clauses.append("<=");
                break;

            case GREATER_EQUALS:
                clauses.append(">=");
                break;

            case NOT_EQUALS:
                clauses.append("<>");
                break;

            case LIKE_FILTER:
                clauses.append("LIKE");
                break;

            case LIKE:
            case CONTAINS:
            case STARTSWITH:
                clauses.append("LIKE");
                break;
            case ENDSWITH:
                clauses.append("LIKE");
                break;
        }
        clauses.append(" ? )");
        if (flag1) {
            String s2 = obj.toString().toUpperCase();
            if (obj == null)
                s2 = "";
            if (i == ENDSWITH || i == CONTAINS || i == LIKE_FILTER)
                s2 = "%" + s2;
            if (i == STARTSWITH || i == CONTAINS || i == LIKE_FILTER)
                s2 = s2 + "%";
            super.parameters.addElement(new SQLParameter(s2, 12, 0, sqlconverter));
        } else {
            if (i == LIKE_FILTER) {
                String s2 = obj.toString();
                s2 = "%" + s2 + "%";
                super.parameters.addElement(new SQLParameter(s2, 12, 0, sqlconverter));
            } else if (i != ISNULL && i != ISNOTNULL)
                super.parameters.addElement(new SQLParameter(obj, j, k, sqlconverter));
        }
        resetStatement();
    }

    public void addSQLExistsClause(String s, SQLBuilder sqlbuilder) {
        addLogicalOperator(s);
        clauses.append(" EXISTS ( ");
        clauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()))
            ;
        clauses.append(" )");
        resetStatement();
    }

    public void addSQLINClause(String s, String columnName, SQLBuilder sqlbuilder) {
        addLogicalOperator(s);
        clauses.append(columnName);
        clauses.append(" IN ( ");
        clauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()))
            ;
        clauses.append(" )");
        resetStatement();
    }

    public void addSQLNOTINClause(String s, String columnName, SQLBuilder sqlbuilder) {
        addLogicalOperator(s);
        clauses.append(columnName);
        clauses.append(" NOT IN ( ");
        clauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()))
            ;
        clauses.append(" )");
        resetStatement();
    }

    public void addSQLGroupBy(String s) {
        if (groupBy.length() > 0)
            groupBy.append(',');
        groupBy.append(s);
        resetStatement();
    }

    /**
     * Converte il parametro per effettuare la Join
     */
    private String convertParameterJoin(int i) {
        switch (i) {
            case SQLBuilder.EQUALS:
                return ("=");
            case SQLBuilder.LESS:
                return ("<");
            case SQLBuilder.GREATER:
                return (">");
            case SQLBuilder.LESS_EQUALS:
                return ("<=");
            case SQLBuilder.GREATER_EQUALS:
                return (">=");
            case SQLBuilder.NOT_EQUALS:
                return ("<>");
            default:
                return ("=");
        }
    }

    /**
     * Costruisce una Join o un'autoJoin di uguaglianza
     */
    public void addSQLJoin(String s, String s1) {
        addSQLJoin(s, this.EQUALS, s1);
    }

    /**
     * Costruisce una Join o un'autoJoin con il parametro, il quale
     * indica se la join da effettuare è di uguaglianza o di disuguaglianza
     */
    public void addSQLJoin(String s, int parametro, String s1) {
        addLogicalOperator("AND");
        clauses.append("( ");
        clauses.append(s);
        clauses.append(convertParameterJoin(parametro));
        clauses.append(s1);
        clauses.append(" )");
        resetStatement();
    }

    public void addSQLNotExistsClause(String s, SQLBuilder sqlbuilder) {
        addLogicalOperator(s);
        clauses.append(" NOT EXISTS ( ");
        clauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()));
        clauses.append(" )");
        resetStatement();
    }

    public void addMINUSClause(SQLBuilder sqlbuilder) {
        clauses.append(" MINUS ");
        clauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()));
        resetStatement();
    }

    public void addTableToHeader(String s) {
        addTableToHeader(s, null);
    }

    public void addTableToHeader(String s, String s1) {
        if (fromClause == null)
            fromClause = new StringBuffer();
        else
            fromClause.append(",\n\t");
        if (schema == null)
            fromClause.append(EJBCommonServices.getDefaultSchema());
        else
            fromClause.append(schema);
        fromClause.append(s);
        if (s1 != null) {
            fromClause.append(' ');
            fromClause.append(s1);
        }
    }

    public void addToHeader(String s) {
        if (fromClause == null)
            fromClause = new StringBuffer("\nFROM\n\t");
        else
            fromClause.append(",\n\t");
        fromClause.append(s);
    }

    public void addUpdateClause(String s, Object obj) {
        ColumnMapping columnmapping = super.columnMap.getMappingForProperty(s);
        if (columnmapping != null)
            addUpdateSQLClause(Optional.ofNullable(columnmapping.getColumnName())
                    .filter(columnName -> columnName.indexOf(".") != -1)
                    .map(columnName -> columnName)
                    .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                    obj, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter());
        else if (obj instanceof KeyedPersistent)
            try {
                SQLPersistentInfo sqlpersistentinfo = (SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo(obj.getClass());
                String s1;
                for (Iterator iterator = sqlpersistentinfo.getOidPersistentProperties().keySet().iterator(); iterator.hasNext(); addUpdateClause(Prefix.prependPrefix(s, s1), sqlpersistentinfo.getIntrospector().getPropertyValue(obj, s1)))
                    s1 = (String) iterator.next();

            } catch (IntrospectionException introspectionexception) {
                throw new IntrospectionError(introspectionexception);
            }
    }

    public void addUpdateNullClause(String s, Class class1) {
        ColumnMapping columnmapping = super.columnMap.getMappingForProperty(s);
        if (columnmapping != null)
            addUpdateSQLClause(Optional.ofNullable(columnmapping.getColumnName())
                    .filter(columnName -> columnName.indexOf(".") != -1)
                    .map(columnName -> columnName)
                    .orElse(super.columnMap.getTableName().concat(".").concat(columnmapping.getColumnName())),
                    null, columnmapping.getSqlType(), columnmapping.getColumnScale(), columnmapping.getConverter());
        else
            try {
                SQLPersistentInfo sqlpersistentinfo = (SQLPersistentInfo) BeanIntrospector.getSQLInstance().getPersistentInfo(class1);
                String s1;
                for (Iterator iterator = sqlpersistentinfo.getOidPersistentProperties().keySet().iterator(); iterator.hasNext(); addUpdateNullClause(Prefix.prependPrefix(s, s1), sqlpersistentinfo.getIntrospector().getPropertyType(class1, s1)))
                    s1 = (String) iterator.next();

            } catch (IntrospectionException introspectionexception) {
                throw new IntrospectionError(introspectionexception);
            }
    }

    public void addUpdateSQLClause(String s) {
        if (updateClauses == null) {
            super.updateParameters = new Vector();
            updateClauses = new StringBuffer();
        } else {
            updateClauses.append(",\n");
        }
        updateClauses.append("\t ");
        updateClauses.append(s);
        resetStatement();
    }

    public void addUpdateSQLClause(String s, Object obj, int i, int j, SQLConverter sqlconverter) {
        if (updateClauses == null) {
            super.updateParameters = new Vector();
            updateClauses = new StringBuffer();
        } else {
            updateClauses.append(",\n");
        }
        updateClauses.append("\t ");
        updateClauses.append(s);
        updateClauses.append(" = ");
        updateClauses.append(" ? ");
        super.updateParameters.addElement(new SQLParameter(obj, i, j, sqlconverter));
        resetStatement();
    }

    public int bindParameters(LoggableStatement preparedstatement, int i)
            throws SQLException {
        if (super.updateParameters != null && "UPDATE".equalsIgnoreCase(command)) {
            for (Enumeration enumeration = super.updateParameters.elements(); enumeration.hasMoreElements(); ) {
                SQLParameter sqlparameter = (SQLParameter) enumeration.nextElement();
                if (sqlparameter != null)
                    sqlparameter.setInPreparedStatement(i, preparedstatement);
                i++;
            }

        }
        return super.bindParameters(preparedstatement, i);
    }

    public void closeParenthesis() {
        if (firstClause)
            clauses.append("\t1=1");
        clauses.append("\t)\n");
        firstClause = false;
    }

    private String createStatement(boolean flag) {
        return createStatement(flag, false);
    }

    private String createStatement(boolean flag, boolean withoutOrderBy) {
        StringBuffer stringbuffer = new StringBuffer();
        boolean flag1 = "UPDATE".equals(command);
        boolean flag2 = "DELETE".equals(command);
        if (command != null) {
            stringbuffer.append(command);
            if (!flag1 && !flag2 && distinctClause)
                stringbuffer.append(" DISTINCT ");
            else
                stringbuffer.append(' ');
        }
        if (!flag1 && !flag2) {
            if (header != null)
                stringbuffer.append(header);
            if (columns != null) {
                if (header != null)
                    stringbuffer.append(',');
                stringbuffer.append(columns);
            }
        }
        if (fromClause != null || joins != null) {
            if (!flag1 && !flag2)
                stringbuffer.append("\nFROM\n\t");
            if (fromClause != null)
                stringbuffer.append(fromClause.toString());
            if (joins != null) {
                if (fromClause != null)
                    stringbuffer.append(',');
                for (Iterator iterator = joins.entrySet().iterator(); iterator.hasNext(); ) {
                    java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                    stringbuffer.append(EJBCommonServices.getDefaultSchema());
                    stringbuffer.append(((String[]) entry.getValue())[0]);
                    stringbuffer.append(' ');
                    stringbuffer.append(entry.getKey());
                    if (iterator.hasNext())
                        stringbuffer.append(',');
                }

            }
        }
        if (updateClauses != null && flag1) {
            stringbuffer.append("\nSET\n");
            stringbuffer.append(updateClauses.toString());
        }
        if (clauses.length() > 0 || joins != null) {
            if (header == null || !header.trim().endsWith("WHERE"))
                stringbuffer.append(" WHERE\n");
            stringbuffer.append(clauses.toString());
            if (joins != null) {
                if (clauses.length() > 0)
                    stringbuffer.append(" AND");
                for (Iterator iterator1 = joins.values().iterator(); iterator1.hasNext(); ) {
                    String as[] = (String[]) iterator1.next();
                    stringbuffer.append(" ( ");
                    stringbuffer.append(as[1]);
                    stringbuffer.append(" ) \n");
                    if (iterator1.hasNext())
                        stringbuffer.append("\tAND");
                }

            }
        } else if (header != null && header.trim().endsWith("WHERE"))
            stringbuffer.append(" 1 = 1");
        if (!flag) {
            String s = " GROUP BY ";
            if (groupBy != null && groupBy.length() > 0) {
                stringbuffer.append(s);
                stringbuffer.append(groupBy.toString());
            }
            s = " HAVING ";
            if (havingClauses != null && havingClauses.length() > 0) {
                stringbuffer.append(s);
                stringbuffer.append(havingClauses.toString());
            }
            if (!withoutOrderBy) {
                s = " ORDER BY ";
                if (preOrderBy.length() > 0) {
                    stringbuffer.append(s);
                    stringbuffer.append(preOrderBy.toString());
                    s = ", ";
                }
                if (orderByClauses != null) {
                    for (Iterator iterator2 = orderByClauses.keySet().iterator(); iterator2.hasNext(); ) {
                        ColumnMapping columnmapping = super.columnMap.getMappingForProperty((String) iterator2.next());
                        if (columnmapping != null) {
                            Integer integer = (Integer) orderByClauses.get(columnmapping.getPropertyName());
                            if (integer != null && integer.intValue() != 0) {
                                stringbuffer.append(s);
                                stringbuffer.append(columnmapping.getColumnName());
                                stringbuffer.append(' ');
                                stringbuffer.append(integer.intValue() != -1 ? "ASC" : "DESC");
                                s = ", ";
                            }
                        }
                    }

                }
                if (orderBy.length() > 0) {
                    stringbuffer.append(s);
                    stringbuffer.append(orderBy.toString());
                    s = ", ";
                }
            }
            if (isForUpdate())
                if (forUpdateOf != null) {
                    stringbuffer.append(" FOR UPDATE OF ");
                    stringbuffer.append(forUpdateOf);
                    stringbuffer.append(" NOWAIT");
                } else {
                    stringbuffer.append(" FOR UPDATE NOWAIT");
                }
        }
        return stringbuffer.toString();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String s) {
        command = s;
        resetStatement();
    }

    public String getExistsStatement() {
        return createStatement(true);
    }

    public int getOrderBy(String s) {
        if (orderByClauses == null) {
            return 0;
        } else {
            Integer integer = (Integer) orderByClauses.get(s);
            return integer != null ? integer.intValue() : 0;
        }
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String s) {
        schema = s;
    }

    public String getStatement() {
        if (super.statement == null)
            super.statement = createStatement(false);
        return super.statement;
    }

    public void setStatement(String s) {
        customStatement = true;
        super.statement = s;
    }

    public String getStatement(boolean withoutOrderBy) {
        return createStatement(false, withoutOrderBy);
    }

    public boolean isAutoJoins() {
        return autoJoins;
    }

    public void setAutoJoins(boolean flag) {
        autoJoins = flag;
    }

    public boolean isDistinctClause() {
        return distinctClause;
    }

    public void setDistinctClause(boolean flag) {
        distinctClause = flag;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(boolean flag) {
        forUpdate = flag;
        resetStatement();
    }

    public boolean isOrderableByProperty(String s)
            throws DetailedRuntimeException {
        if (customStatement) {
            return false;
        } else {
            ColumnMapping columnmapping = getColumnMap().getMappingForProperty(s);
            return columnmapping != null ? columnmapping.isOrderable() : false;
        }
    }

    public void openNotParenthesis(String s) {
        addLogicalOperator(s);
        clauses.append("\tNOT ( \n");
        firstClause = true;
    }

    public void openParenthesis(String s) {
        addLogicalOperator(s);
        clauses.append("\t( \n");
        firstClause = true;
    }

    public void reserveParameters(int i) {
        if (super.parameters.size() < i)
            super.parameters.setSize(i + super.parameters.size());
    }

    public void resetColumns() {
        header = null;
        columns = null;
    }

    private void resetStatement() {
        super.statement = null;
    }

    public void setColumnMap(ColumnMap columnmap) {
        super.columnMap = columnmap;
        header = columnmap.getDefaultSelectHeaderSQL();
        command = "SELECT";
        addTableToHeader(columnmap.getTableName());
    }

    public void setForUpdateOf(String s) {
        if (forUpdate = s != null)
            forUpdateOf = super.columnMap.getTableName() + '.' + super.columnMap.getMappingForProperty(s).getColumnName();
        else
            forUpdateOf = null;
        resetStatement();
    }

    public void setHeader(String s) {
        header = s;
        command = null;
        resetStatement();
    }

    public void setOrderBy(String s, int i) {
        if (orderByClauses == null)
            orderByClauses = new OrderedHashMap();
        orderByClauses.put(s, new Integer(i));
        resetStatement();
    }

    public void setParameter(int i, Object obj, int j, int k) {
        super.parameters.set(i, new SQLParameter(obj, j, k, null));
    }

    public String toString() {
        StringTokenizer stringtokenizer = new StringTokenizer(getStatement(), "?", true);
        StringBuffer stringbuffer = new StringBuffer();
        toString(stringtokenizer, super.updateParameters, stringbuffer);
        toString(stringtokenizer, super.parameters, stringbuffer);
        for (; stringtokenizer.hasMoreTokens(); stringbuffer.append(stringtokenizer.nextToken())) ;
        return stringbuffer.toString();
    }

    /**
     * @return
     */
    public StringBuffer getFromClause() {
        return fromClause;
    }

    /**
     * @param buffer
     */
    public void setFromClause(StringBuffer buffer) {
        fromClause = buffer;
    }

    protected void addLogicalOperatorHavingClause(String s) {
        if (firstHavingClause) {
            firstHavingClause = false;
        } else {
            havingClauses.append(' ');
            havingClauses.append(s != null ? s : "AND");
            havingClauses.append('\n');
        }
    }

    public void addSQLHavingClause(String s, String s1, int i, SQLBuilder sqlbuilder) {
        addLogicalOperatorHavingClause(s);
        havingClauses.append("\t( ");
        int j = i & 0xff00;
        boolean flag = j == LIKE_OPERATORS;
        if (flag) {
            havingClauses.append("UPPER(");
            havingClauses.append(s1);
            havingClauses.append(")");
        } else {
            havingClauses.append(s1);
        }
        havingClauses.append(' ');
        havingClauses.append(getSQLOperator(i));
        switch (i) {
            case ISNULL:
            case ISNOTNULL:
                havingClauses.append(" )");
                return;
        }
        havingClauses.append(" (");
        havingClauses.append(sqlbuilder.getStatement());
        for (Iterator iterator = ((SQLQuery) (sqlbuilder)).parameters.iterator(); iterator.hasNext(); super.parameters.add(iterator.next()))
            ;
        havingClauses.append(") )");
        resetStatement();
    }
}