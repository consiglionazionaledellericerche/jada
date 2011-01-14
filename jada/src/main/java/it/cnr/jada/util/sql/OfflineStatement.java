package it.cnr.jada.util.sql;

import java.io.Serializable;
import java.sql.*;

public class OfflineStatement
    implements Statement, Serializable
{

    public OfflineStatement()
    {
        fetchDirection = 1002;
        fetchSize = -1;
        maxFieldSize = -1;
        maxRows = -1;
        queryTimeout = -1;
        cursorName = null;
        escapeProcessing = 0;
    }

    public void addBatch(String s)
        throws SQLException
    {
        notSupported();
    }

    public void cancel()
        throws SQLException
    {
        notSupported();
    }

    public void clearBatch()
        throws SQLException
    {
        notSupported();
    }

    public void clearWarnings()
        throws SQLException
    {
        notSupported();
    }

    public void close()
        throws SQLException
    {
    }

    public boolean execute(String s)
        throws SQLException
    {
        notSupported();
        return false;
    }

    public int[] executeBatch()
        throws SQLException
    {
        notSupported();
        return null;
    }

    public ResultSet executeQuery(String s)
        throws SQLException
    {
        notSupported();
        return null;
    }

    public int executeUpdate(String s)
        throws SQLException
    {
        notSupported();
        return 0;
    }

    public Connection getConnection()
        throws SQLException
    {
        notSupported();
        return null;
    }

    public int getFetchDirection()
        throws SQLException
    {
        return fetchDirection;
    }

    public int getFetchSize()
        throws SQLException
    {
        return fetchSize;
    }

    public int getMaxFieldSize()
        throws SQLException
    {
        return maxFieldSize;
    }

    public int getMaxRows()
        throws SQLException
    {
        return maxRows;
    }

    public boolean getMoreResults()
        throws SQLException
    {
        return false;
    }

    public int getQueryTimeout()
        throws SQLException
    {
        return queryTimeout;
    }

    public ResultSet getResultSet()
        throws SQLException
    {
        notSupported();
        return null;
    }

    public int getResultSetConcurrency()
        throws SQLException
    {
        notSupported();
        return 0;
    }

    public int getResultSetType()
        throws SQLException
    {
        notSupported();
        return 0;
    }

    public int getUpdateCount()
        throws SQLException
    {
        notSupported();
        return 0;
    }

    public SQLWarning getWarnings()
        throws SQLException
    {
        notSupported();
        return null;
    }

    protected void initializeStatement(Statement statement)
        throws SQLException
    {
        if(cursorName != null)
            statement.setCursorName(cursorName);
        if(escapeProcessing != 0)
            statement.setEscapeProcessing(escapeProcessing > 0);
        if(fetchDirection != 1002)
            statement.setFetchDirection(fetchDirection);
        if(fetchSize >= 0)
            statement.setFetchSize(fetchSize);
        if(maxFieldSize >= 0)
            statement.setMaxFieldSize(maxFieldSize);
        if(maxRows >= 0)
            statement.setMaxRows(maxRows);
        if(queryTimeout >= 0)
            statement.setQueryTimeout(queryTimeout);
    }

    protected void notSupported()
        throws SQLException
    {
        throw new SQLException("Not supported in offline statements");
    }

    public void setCursorName(String s)
        throws SQLException
    {
        cursorName = s;
    }

    public void setEscapeProcessing(boolean flag)
        throws SQLException
    {
        escapeProcessing = flag ? 1 : -1;
    }

    public void setFetchDirection(int i)
        throws SQLException
    {
        fetchDirection = i;
    }

    public void setFetchSize(int i)
        throws SQLException
    {
        fetchSize = i;
    }

    public void setMaxFieldSize(int i)
        throws SQLException
    {
        maxFieldSize = i;
    }

    public void setMaxRows(int i)
        throws SQLException
    {
        maxRows = i;
    }

    public void setQueryTimeout(int i)
        throws SQLException
    {
        queryTimeout = i;
    }

    private int fetchDirection;
    private int fetchSize;
    private int maxFieldSize;
    private int maxRows;
    private int queryTimeout;
    private String cursorName;
    private int escapeProcessing;
	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}