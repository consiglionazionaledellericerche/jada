package it.cnr.jada.util.sql;

import it.cnr.jada.UserContext;
import it.cnr.jada.util.EventTracer;
import it.cnr.jada.util.EventTracerWriter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

// Referenced classes of package it.cnr.jada.util.sql:
//            LoggedStatement, LoggedCallableStatement, LoggedPreparedStatement

public class LoggedConnection
    implements Serializable, Connection
{

    public LoggedConnection(Connection connection1, EventTracer eventtracer, UserContext usercontext)
    {
        connection = connection1;
        eventTracer = eventtracer;
        userContext = usercontext;
    }

    public void clearWarnings()
        throws SQLException
    {
        connection.clearWarnings();
    }

    public void close()
        throws SQLException
    {
        connection.close();
    }

    public void commit()
        throws SQLException
    {
        connection.commit();
    }

    public Statement createStatement()
        throws SQLException
    {
        return new LoggedStatement(this, connection.createStatement());
    }

    public Statement createStatement(int i, int j)
        throws SQLException
    {
        return new LoggedStatement(this, connection.createStatement(i, j));
    }

    public boolean getAutoCommit()
        throws SQLException
    {
        return connection.getAutoCommit();
    }

    public String getCatalog()
        throws SQLException
    {
        return connection.getCatalog();
    }

    public final EventTracer getEventTracer()
    {
        return eventTracer;
    }

    public DatabaseMetaData getMetaData()
        throws SQLException
    {
        return connection.getMetaData();
    }

    public int getTransactionIsolation()
        throws SQLException
    {
        return connection.getTransactionIsolation();
    }

    public Map getTypeMap()
        throws SQLException
    {
        return connection.getTypeMap();
    }

    public UserContext getUserContext()
    {
        return userContext;
    }

    public SQLWarning getWarnings()
        throws SQLException
    {
        return connection.getWarnings();
    }

    public boolean isClosed()
        throws SQLException
    {
        return connection.isClosed();
    }

    public boolean isReadOnly()
        throws SQLException
    {
        return connection.isReadOnly();
    }

    public String nativeSQL(String s)
        throws SQLException
    {
        return connection.nativeSQL(s);
    }

    public CallableStatement prepareCall(String s)
        throws SQLException
    {
        return new LoggedCallableStatement(this, s, connection.prepareCall(s));
    }

    public CallableStatement prepareCall(String s, int i, int j)
        throws SQLException
    {
        return new LoggedCallableStatement(this, s, connection.prepareCall(s, i, j));
    }

    public PreparedStatement prepareStatement(String s)
        throws SQLException
    {
        return new LoggedPreparedStatement(this, s, connection.prepareStatement(s));
    }

    public PreparedStatement prepareStatement(String s, int i, int j)
        throws SQLException
    {
        return new LoggedPreparedStatement(this, s, connection.prepareStatement(s, i, j));
    }

    public void rollback()
        throws SQLException
    {
        connection.rollback();
    }

    public void setAutoCommit(boolean flag)
        throws SQLException
    {
        connection.setAutoCommit(flag);
    }

    public void setCatalog(String s)
        throws SQLException
    {
        connection.setCatalog(s);
    }

    public void setReadOnly(boolean flag)
        throws SQLException
    {
        connection.setReadOnly(flag);
    }

    public void setTransactionIsolation(int i)
        throws SQLException
    {
        connection.setTransactionIsolation(i);
    }

    public final EventTracerWriter startEventTrace()
    {
        EventTracerWriter eventtracerwriter = eventTracer.startEventTrace(userContext);
        try
        {
            Statement statement = connection.createStatement();
            try
            {
                ResultSet resultset = statement.executeQuery("SELECT DBMS_SESSION.unique_session_id from DUAL");
                resultset.next();
                eventtracerwriter.print("USID: ");
                eventtracerwriter.println(resultset.getString(1));
                resultset = statement.executeQuery("SELECT DBMS_TRANSACTION.local_transaction_id from DUAL");
                resultset.next();
                eventtracerwriter.print("LTID: ");
                eventtracerwriter.println(resultset.getString(1));
            }
            finally
            {
                statement.close();
            }
        }
        catch(SQLException sqlexception)
        {
            eventtracerwriter.print("Can't obtain USER_ID");
            sqlexception.printStackTrace(eventtracerwriter);
        }
        return eventtracerwriter;
    }

    protected void trace(String s)
    {
        EventTracerWriter eventtracerwriter = startEventTrace();
        eventtracerwriter.println(s);
        eventtracerwriter.close();
    }

    private final Connection connection;
    private final UserContext userContext;
    private final EventTracer eventTracer;
	/* (non-Javadoc)
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint()
	 */
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
	}
}