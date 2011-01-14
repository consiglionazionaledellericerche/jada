package it.cnr.jada.persistency.sql;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            SQLConverter

class SQLParameter
    implements Serializable
{

    public SQLParameter(Object obj, int i, int j, SQLConverter sqlconverter)
    {
        value = obj;
        sqlType = i;
        scale = j;
        converter = sqlconverter;
    }

    public SQLConverter getConverter()
    {
        return converter;
    }

    public int getScale()
    {
        return scale;
    }

    public int getSqlType()
    {
        return sqlType;
    }

    public Object getValue()
    {
        return value;
    }

    public void setConverter(SQLConverter sqlconverter)
    {
        converter = sqlconverter;
    }

    public void setInPreparedStatement(int i, PreparedStatement preparedstatement)
        throws SQLException
    {
        setParameterInPreparedStatement(i, preparedstatement, value, sqlType, scale, converter);
    }

    public static final void setParameterInPreparedStatement(int i, PreparedStatement preparedstatement, Object obj, int j, int k, SQLConverter sqlconverter)
        throws SQLException
    {
        if(sqlconverter != null)
            sqlconverter.javaToSql(preparedstatement, obj, i, j);
        else
        if(obj == null)
            preparedstatement.setNull(i, j);
        else
        if(j == 1111)
            preparedstatement.setObject(i, obj);
        else
        if(j == 3 || j == 2 && (obj instanceof Number) && !(obj instanceof BigDecimal))
            preparedstatement.setObject(i, obj);
        else
            preparedstatement.setObject(i, obj, j, k);
    }

    public void setScale(int i)
    {
        scale = i;
    }

    public void setSqlType(int i)
    {
        sqlType = i;
    }

    public void setValue(Object obj)
    {
        value = obj;
    }

    public String toString()
    {
        return value != null ? value.toString() : "";
    }

    private Object value;
    private int sqlType;
    private int scale;
    private SQLConverter converter;
}