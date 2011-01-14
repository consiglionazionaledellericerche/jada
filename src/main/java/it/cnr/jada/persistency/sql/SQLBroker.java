package it.cnr.jada.persistency.sql;

import it.cnr.jada.DetailedException;
import it.cnr.jada.persistency.*;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;

// Referenced classes of package it.cnr.jada.persistency.sql:
//SQLExceptionHandler, ColumnMap, ColumnMapping, SQLConverter, 
//RemoveColumnMapping

public class SQLBroker extends Broker
    implements Serializable
{

    private ResultSet resultSet;
    private ColumnMap columnMap;
    private Statement statement;
    private boolean automaticClose;
	private boolean isClose;

    public SQLBroker(ColumnMap columnmap, Introspector introspector)
    {
        super(introspector);
        automaticClose = true;
        columnMap = columnmap;
		isClose = false;
    }

    public SQLBroker(ColumnMap columnmap, Introspector introspector, PersistentCache persistentcache)
    {
        super(introspector, persistentcache);
        automaticClose = true;
        columnMap = columnmap;
		isClose = false;
    }

    public SQLBroker(ColumnMap columnmap, Introspector introspector, PersistentCache persistentcache, Statement statement1, ResultSet resultset)
    {
        super(introspector, persistentcache);
        automaticClose = true;
		isClose = false;
        statement = statement1;
        columnMap = columnmap;
        resultSet = resultset;
    }

    public SQLBroker(ColumnMap columnmap, Introspector introspector, PersistentCache persistentcache, Statement statement1, ResultSet resultset, FetchPolicy fetchpolicy)
    {
        super(introspector, persistentcache, fetchpolicy);
        automaticClose = true;
		isClose = false;
        statement = statement1;
        columnMap = columnmap;
        resultSet = resultset;
    }

    public void close()
        throws PersistencyException
    {
		if (!isClose()){
	        try
	        {
	            resultSet.close();
	            if(statement != null)
	                statement.close();
				isClose = true;
	        }
	        catch(SQLException sqlexception)
	        {
	            throw SQLExceptionHandler.getInstance().handleSQLException(sqlexception);
	        }
			
		}
    }

    private Object fetchColumnValue(String s, Class class1)
        throws SQLException
    {
        if(class1 == java.lang.String.class)
            return resultSet.getString(s);
        if(class1 == Integer.TYPE || class1 == java.lang.Integer.class)
        {
            int i = resultSet.getInt(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Integer(i);
        }
        if(class1 == Long.TYPE || class1 == java.lang.Long.class)
        {
            long l = resultSet.getLong(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Long(l);
        }
        if(class1 == Short.TYPE || class1 == java.lang.Short.class)
        {
            short word0 = resultSet.getShort(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Short(word0);
        }
        if(class1 == Float.TYPE || class1 == java.lang.Float.class)
        {
            float f = resultSet.getFloat(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Float(f);
        }
        if(class1 == Double.TYPE || class1 == java.lang.Double.class)
        {
            double d = resultSet.getDouble(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Double(d);
        }
        if(class1 == Byte.TYPE || class1 == java.lang.Byte.class)
        {
            byte byte0 = resultSet.getByte(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Byte(byte0);
        }
        if(class1 == Boolean.TYPE || class1 == java.lang.Boolean.class)
        {
            boolean flag = resultSet.getBoolean(s);
            if(resultSet.wasNull())
                return null;
            else
                return new Boolean(flag);
        }
        if(class1 == java.util.Date.class)
        {
            java.sql.Timestamp timestamp = resultSet.getTimestamp(s);
            if(timestamp == null)
                return null;
            else
                return new Date(timestamp.getTime());
        }
        if(class1 == java.sql.Date.class)
            return resultSet.getDate(s);
        if(class1 == java.sql.Time.class)
            return resultSet.getTime(s);
        if(class1 == java.sql.Timestamp.class)
            return resultSet.getTimestamp(s);
        if(class1 == java.math.BigDecimal.class)
        {
            java.math.BigDecimal bigdecimal = resultSet.getBigDecimal(s);
            if(resultSet.wasNull())
                return null;
            else
                return bigdecimal;
        } else
        {
            return resultSet.getObject(s);
        }
    }

    public Object fetchPropertyValue(String s, Class class1)
        throws FetchException
    {
        try
        {
            ColumnMapping columnmapping = columnMap.getMappingForProperty(s);
            if(columnmapping == null)
                throw new PersistentPropertyNotAvailableException("Property named " + s + " is not in current ColumnMap");
            if(columnmapping.getConverter() != null)
                return columnmapping.getConverter().sqlToJava(resultSet, columnmapping.getColumnName());
			if(columnmapping instanceof RemoveColumnMapping || columnmapping instanceof AddColumnMapping)
				return null;
            else
                return fetchColumnValue(columnmapping.getColumnName(), class1);
        }
        catch(SQLException sqlexception)
        {
            throw new FetchException("SQLException while fetchPropertyValue", sqlexception);
        }
    }

    public ColumnMap getColumnMap()
    {
        return columnMap;
    }

    public ResultSet getResultSet()
    {
        return resultSet;
    }

    public boolean isAutomaticClose()
    {
        return automaticClose;
    }

    public boolean next()
        throws FetchException
    {
        try
        {
            try
            {
                boolean flag = resultSet.next();
                if(!flag && automaticClose)
                    close();
                return flag;
            }
            catch(SQLException sqlexception)
            {
                close();
                throw new FetchException(sqlexception);
            }
        }
        catch(PersistencyException persistencyexception)
        {
            throw new FetchException(persistencyexception.getMessage(), persistencyexception.getDetail());
        }
    }

    public void setAutomaticClose(boolean flag)
    {
        automaticClose = flag;
    }

    public void setColumnMap(ColumnMap columnmap)
    {
        columnMap = columnmap;
    }

    public void setResultSet(ResultSet resultset)
    {
        resultSet = resultset;
    }

	public boolean isClose() {
		return isClose;
	}
	
}