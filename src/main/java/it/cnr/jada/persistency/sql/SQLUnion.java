package it.cnr.jada.persistency.sql;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.util.OrderedHashMap;

import java.util.*;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLQuery, ColumnMap, ColumnMapping

public class SQLUnion extends SQLBuilder
{

    public SQLUnion() throws IntrospectionException
    {
    	super();
    }

    public SQLUnion(ColumnMap columnmap)
    {
        super(columnmap);
    }

    public void addUnion(SQLQuery sqlquery, boolean flag)
    {
        if(unionStatement == null)
        {
            unionStatement = new StringBuffer(sqlquery.getStatement());
        } else
        {
            unionStatement.append(flag ? "\n UNION ALL (\n" : "\n UNION (\n");
            unionStatement.append(sqlquery.getStatement());
            unionStatement.append(" )");
        }
        super.parameters.addAll(sqlquery.parameters);
        resetStatement();
    }

    public int getOrderBy(String s)
    {
        if(orderByClauses == null)
        {
            return 0;
        } else
        {
            Integer integer = (Integer)orderByClauses.get(s);
            return integer != null ? integer.intValue() : 0;
        }
    }
    public String getStatement(boolean withoutOrderBy){
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(unionStatement);
        return stringbuffer.toString();
    }

    public String getStatement(){
        if(super.statement == null){
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(unionStatement);
            String s = " ORDER BY ";
            if(orderByClauses != null){
                for(Iterator iterator = orderByClauses.keySet().iterator(); iterator.hasNext();){
                    ColumnMapping columnmapping = super.columnMap.getMappingForProperty((String)iterator.next());
                    if(columnmapping != null){
                        Integer integer = (Integer)orderByClauses.get(columnmapping.getPropertyName());
                        if(integer != null && integer.intValue() != 0){
                            stringbuffer.append(s);
                            stringbuffer.append(columnmapping.getColumnName());
                            stringbuffer.append(' ');
                            stringbuffer.append(integer.intValue() != -1 ? "ASC" : "DESC");
                            s = ", ";
                        }
                    }
                }

            }
            super.statement = stringbuffer.toString();
        }
        return super.statement;
    }

    public boolean isOrderableByProperty(String s)
        throws DetailedRuntimeException
    {
        ColumnMapping columnmapping = getColumnMap().getMappingForProperty(s);
        return columnmapping != null ? columnmapping.isOrderable() : false;
    }

    private void resetStatement()
    {
        super.statement = null;
    }

    public void setOrderBy(String s, int i)
    {
        if(orderByClauses == null)
            orderByClauses = new OrderedHashMap();
        orderByClauses.put(s, new Integer(i));
        resetStatement();
    }

    private StringBuffer unionStatement;
    private Map orderByClauses;
}