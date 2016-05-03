package it.cnr.jada.persistency.sql;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            FindClause

public class SimpleFindClause
    implements FindClause, Serializable
{

    public SimpleFindClause()
    {
    }

    public SimpleFindClause(String s, int i, Object obj)
    {
        this(null, s, i, obj, true);
    }

    public SimpleFindClause(String s, String s1, int i, Object obj)
    {
        this(s, s1, i, obj, true);
    }

    public SimpleFindClause(String s, String s1, int i, Object obj, boolean flag)
    {
        logicalOperator = s;
        propertyName = s1;
        operator = i;
        value = obj;
        caseSensitive = flag;
    }

    public String getLogicalOperator()
    {
        return logicalOperator;
    }

    public int getOperator()
    {
        return operator;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public String getSqlClause()
    {
        return sqlClause;
    }

    public Object getValue()
    {
        return value;
    }

    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean flag)
    {
        caseSensitive = flag;
    }

    public void setLogicalOperator(String s)
    {
        logicalOperator = s;
    }

    public void setOperator(int i)
    {
        operator = i;
    }

    public void setPropertyName(String s)
    {
        propertyName = s;
    }

    public void setSqlClause(String s)
    {
        sqlClause = s;
    }

    public void setValue(Object obj)
    {
        value = obj;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append('\t');
        if(logicalOperator != null)
        {
            stringbuffer.append(logicalOperator);
            stringbuffer.append(' ');
        }
        stringbuffer.append(propertyName);
        stringbuffer.append(' ');
        switch(operator)
        {
        case 8201: 
            stringbuffer.append("IS NULL )");
            return stringbuffer.toString();

        case 8202: 
            stringbuffer.append("IS NOT NULL )");
            return stringbuffer.toString();

        case 8192: 
            stringbuffer.append("=");
            break;

        case 16386: 
            stringbuffer.append("<");
            break;

        case 16388: 
            stringbuffer.append(">");
            break;

        case 16387: 
            stringbuffer.append("<=");
            break;

        case 16389: 
            stringbuffer.append(">=");
            break;

        case 8193: 
            stringbuffer.append("<>");
            break;

        case 40968: 
            stringbuffer.append("STARTSWITH");
            break;

        case 40967: 
            stringbuffer.append("LIKE");
            break;
        }
        stringbuffer.append(' ');
        stringbuffer.append(value);
        return stringbuffer.toString();
    }

    private int operator;
    private Object value;
    private String logicalOperator;
    private String propertyName;
    private boolean caseSensitive;
    private String sqlClause;
}