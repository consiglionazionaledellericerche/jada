package it.cnr.jada.persistency.sql;

import it.cnr.jada.util.XmlWriter;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            ColumnMapping, SQLPersistentInfo

public class ColumnMap
        implements Serializable, Cloneable {

    private HashMap columns;
    private HashMap properties;
    private HashMap primaryColumns;
    private String tableName;
    private String columnNames[];
    private String primaryColumnNames[];
    private String nonPrimaryColumnNames[];
    private String name;
    private String _extends;
    private String defaultInsertSQL;
    private String defaultUpdateSQL;
    private String defaultDeleteSQL;
    private String defaultSelectSQL;
    private String defaultSelectHeaderSQL;
    private String defaultSelectForUpdateSQL;
    private transient SQLPersistentInfo persistentInfo;

    public ColumnMap() {
        columns = new HashMap();
        properties = new HashMap();
        primaryColumns = new HashMap();
    }

    public ColumnMap(String s) {
        columns = new HashMap();
        properties = new HashMap();
        primaryColumns = new HashMap();
        tableName = s;
    }

    public void addColumnMapping(ColumnMapping columnmapping) {
        columnmapping.fillNullsFrom((ColumnMapping) columns.get(columnmapping.getColumnName()));
        if (!columnmapping.isFetchOnly())
            columns.put(columnmapping.getColumnName(), columnmapping);
        properties.put(columnmapping.getPropertyName(), columnmapping);
        if (columnmapping.isPrimary())
            primaryColumns.put(columnmapping.getColumnName(), columnmapping);
    }

    public void addColumnMappings(ColumnMap columnmap) {
        columns.putAll(columnmap.columns);
        properties.putAll(columnmap.properties);
        primaryColumns.putAll(columnmap.primaryColumns);
    }

    public void addNewColumnMappings(ColumnMap columnmap) {
        for (Iterator iterator = columnmap.columns.entrySet().iterator(); iterator.hasNext(); ) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
            if (!columns.containsKey(entry.getKey()))
                columns.put(entry.getKey(), entry.getValue());
        }

        for (Iterator iterator1 = columnmap.properties.entrySet().iterator(); iterator1.hasNext(); ) {
            java.util.Map.Entry entry1 = (java.util.Map.Entry) iterator1.next();
            if (!properties.containsKey(entry1.getKey()))
                properties.put(entry1.getKey(), entry1.getValue());
        }

        for (Iterator iterator2 = columnmap.primaryColumns.entrySet().iterator(); iterator2.hasNext(); ) {
            java.util.Map.Entry entry2 = (java.util.Map.Entry) iterator2.next();
            if (!primaryColumns.containsKey(entry2.getKey()))
                primaryColumns.put(entry2.getKey(), entry2.getValue());
        }

    }

    public void addPrimaryColumnMapping(ColumnMapping columnmapping) {
        columnmapping.setPrimary(true);
        addColumnMapping(columnmapping);
    }

    public void addNewColumnMapping(ColumnMapping columnmapping) {
        if (columnmapping.isPrimary()) {
            throw new NullPointerException("Una primary property non pu\362 essere usata in una addColumnMapping");
        } else {
            properties.put(columnmapping.getPropertyName(), columnmapping);
            return;
        }
    }

    public void removeColumnMapping(ColumnMapping columnmapping) {
        if (columnmapping.isPrimary()) {
            throw new NullPointerException("Una primary property non pu\362 essere usata in una removeColumnMapping");
        } else {
            properties.remove(columnmapping.getPropertyName());
            columns.remove(columnmapping.getColumnName());
            return;
        }
    }

    private void buildClausesForPrimaryKey(StringBuffer stringbuffer) {
        stringbuffer.append(" WHERE\n");
        String as[] = getPrimaryColumnNames();
        for (int i = 0; i < as.length; ) {
            stringbuffer.append("\t( ");
            stringbuffer.append(getTableName());
            stringbuffer.append('.');
            stringbuffer.append(as[i++]);
            stringbuffer.append(" = ? )");
            if (i < as.length)
                stringbuffer.append(" AND\n");
        }

    }

    private String buildDefaultDeleteSQL() {
        StringBuffer stringbuffer = new StringBuffer("DELETE FROM ");
        stringbuffer.append(EJBCommonServices.getDefaultSchema());
        stringbuffer.append(getTableName());
        buildClausesForPrimaryKey(stringbuffer);
        return stringbuffer.toString();
    }

    private String buildDefaultInsertSQL() {
        StringBuffer stringbuffer = new StringBuffer("INSERT INTO ");
        stringbuffer.append(EJBCommonServices.getDefaultSchema());
        stringbuffer.append(getTableName());
        stringbuffer.append(" (\n");
        String as[] = getColumnNames();
        for (int i = 0; i < as.length; ) {
            stringbuffer.append('\t');
            stringbuffer.append(as[i]);
            if (++i < as.length)
                stringbuffer.append(",\n");
            else
                stringbuffer.append(" )\n");
        }

        stringbuffer.append("VALUES (\n");
        for (int j = 0; j < as.length; ) {
            stringbuffer.append("\t?");
            if (++j < as.length)
                stringbuffer.append(",\n");
            else
                stringbuffer.append(" )\n");
        }

        return stringbuffer.toString();
    }

    private String buildDefaultSelectForUpdateSQL() {
        return buildDefaultSelectSQL() + " FOR UPDATE NOWAIT";
    }

    private String buildDefaultSelectHeaderSQL() {
        return Arrays.asList(getColumnNames()).stream()
                .map(columnName ->  Optional.ofNullable(columnName)
                                        .filter(s -> s.indexOf(".") != -1)
                                        .map(s -> s)
                                        .orElse(getTableName().concat(".").concat(columnName))
                ).collect(Collectors.joining(",")).concat("\n");
    }

    private String buildDefaultSelectSQL() {
        StringBuffer stringbuffer = new StringBuffer("SELECT ");
        stringbuffer.append(buildDefaultSelectHeaderSQL());
        stringbuffer.append("FROM ");
        stringbuffer.append(EJBCommonServices.getDefaultSchema());
        stringbuffer.append(getTableName());
        buildClausesForPrimaryKey(stringbuffer);
        return stringbuffer.toString();
    }

    private String buildDefaultUpdateSQL() {
        StringBuffer stringbuffer = new StringBuffer("UPDATE ");
        stringbuffer.append(EJBCommonServices.getDefaultSchema());
        stringbuffer.append(getTableName());
        stringbuffer.append(" SET\n");
        String as[] = getNonPrimaryColumnNames();
        for (int i = 0; i < as.length; ) {
            stringbuffer.append('\t');
            stringbuffer.append(as[i]);
            stringbuffer.append(" = ?");
            if (++i < as.length)
                stringbuffer.append(",\n");
            else
                stringbuffer.append("\n");
        }

        buildClausesForPrimaryKey(stringbuffer);
        return stringbuffer.toString();
    }

    public Object clone() {
        ColumnMap columnmap = new ColumnMap(tableName);
        columnmap.name = name;
        columnmap.columns = (HashMap) columns.clone();
        columnmap.properties = (HashMap) properties.clone();
        columnmap.primaryColumns = (HashMap) primaryColumns.clone();
        return columnmap;
    }

    public Collection getColumnMappings() {
        return columns.values();
    }

    public String[] getColumnNames() {
        if (columnNames == null)
            columnNames = (String[]) columns.keySet().toArray(new String[columns.size()]);
        return columnNames;
    }

    public String getDefaultDeleteSQL() {
        if (defaultDeleteSQL == null)
            defaultDeleteSQL = buildDefaultDeleteSQL();
        return defaultDeleteSQL;
    }

    public String getDefaultInsertSQL() {
        if (defaultInsertSQL == null)
            defaultInsertSQL = buildDefaultInsertSQL();
        return defaultInsertSQL;
    }

    public String getDefaultSelectForUpdateSQL() {
        if (defaultSelectForUpdateSQL == null)
            defaultSelectForUpdateSQL = buildDefaultSelectForUpdateSQL();
        return defaultSelectForUpdateSQL;
    }

    public String getDefaultSelectHeaderSQL() {
        if (defaultSelectHeaderSQL == null)
            defaultSelectHeaderSQL = buildDefaultSelectHeaderSQL();
        return defaultSelectHeaderSQL;
    }

    public String getDefaultSelectSQL() {
        if (defaultSelectSQL == null)
            defaultSelectSQL = buildDefaultSelectSQL();
        return defaultSelectSQL;
    }

    public String getDefaultUpdateSQL() {
        if (defaultUpdateSQL == null)
            defaultUpdateSQL = buildDefaultUpdateSQL();
        return defaultUpdateSQL;
    }

    public String getExtends() {
        return _extends;
    }

    public void setExtends(String s) {
        _extends = s;
    }

    public ColumnMapping getMappingForColumn(String s) {
        return (ColumnMapping) columns.get(s);
    }

    public ColumnMapping getMappingForProperty(String s) {
        return (ColumnMapping) properties.get(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
        if (_extends == null)
            _extends = name;
    }

    public String[] getNonPrimaryColumnNames() {
        if (nonPrimaryColumnNames == null) {
            nonPrimaryColumnNames = new String[columns.size() - primaryColumns.size()];
            int i = 0;
            for (Iterator iterator = columns.keySet().iterator(); iterator.hasNext(); ) {
                String s = (String) iterator.next();
                if (!primaryColumns.containsKey(s))
                    nonPrimaryColumnNames[i++] = s;
            }

        }
        return nonPrimaryColumnNames;
    }

    public SQLPersistentInfo getPersistentInfo() {
        return persistentInfo;
    }

    void setPersistentInfo(SQLPersistentInfo sqlpersistentinfo) {
        persistentInfo = sqlpersistentinfo;
    }

    public String[] getPrimaryColumnNames() {
        if (primaryColumnNames == null)
            primaryColumnNames = (String[]) primaryColumns.keySet().toArray(new String[primaryColumns.size()]);
        return primaryColumnNames;
    }

    public Collection getPropertyMappings() {
        return properties.values();
    }

    public String getSelectForBlobSQL(String s) {
        StringBuffer stringbuffer = new StringBuffer("SELECT ");
        stringbuffer.append('\t');
        stringbuffer.append(getTableName());
        stringbuffer.append('.');
        stringbuffer.append(s);
        stringbuffer.append("\n");
        stringbuffer.append("FROM ");
        stringbuffer.append(EJBCommonServices.getDefaultSchema());
        stringbuffer.append(getTableName());
        buildClausesForPrimaryKey(stringbuffer);
        stringbuffer.append(" FOR UPDATE NOWAIT");
        return stringbuffer.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String s) {
        tableName = s;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer();
        for (Iterator iterator = columns.values().iterator(); iterator.hasNext(); stringbuffer.append('\n')) {
            ColumnMapping columnmapping = (ColumnMapping) iterator.next();
            stringbuffer.append(columnmapping.toString());
        }

        return stringbuffer.toString();
    }

    public void writeTo(XmlWriter xmlwriter)
            throws IOException {
        if ("default".equals(getName()))
            xmlwriter.openTag("defaultColumnMap");
        else
            xmlwriter.openTag("columnMap");
        xmlwriter.printAttribute("name", getName(), "default");
        xmlwriter.printAttribute("tableName", getTableName(), null);
        for (Iterator iterator = properties.values().iterator(); iterator.hasNext(); ((ColumnMapping) iterator.next()).writeTo(xmlwriter))
            ;
        xmlwriter.closeLastTag();
    }
}