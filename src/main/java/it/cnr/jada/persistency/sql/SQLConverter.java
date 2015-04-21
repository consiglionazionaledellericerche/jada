package it.cnr.jada.persistency.sql;

import java.sql.*;

public interface SQLConverter<T>
{

    public abstract Class<T> getTargetJavaType(int i, boolean flag);

    public abstract void javaToSql(LoggableStatement preparedstatement, Object obj, int i, int j)
        throws SQLException;

    public abstract Object sqlToJava(ResultSet resultset, String s)
        throws SQLException;
        
	public abstract Object javaToSql(Object obj);

	public abstract Object sqlToJava(Object obj);
	
	public abstract String columnName(String columnName);    
}