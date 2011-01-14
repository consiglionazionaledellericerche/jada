package it.cnr.jada.persistency.sql;

import java.sql.*;

public interface SQLConverter
{

    public abstract Class getTargetJavaType(int i, boolean flag);

    public abstract void javaToSql(PreparedStatement preparedstatement, Object obj, int i, int j)
        throws SQLException;

    public abstract Object sqlToJava(ResultSet resultset, String s)
        throws SQLException;
        
	public abstract Object javaToSql(Object obj);

	public abstract Object sqlToJava(Object obj);
        
}