package it.cnr.jada.util.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;

// Referenced classes of package it.cnr.jada.util.sql:
//            OfflineStatement, OfflineStatementParameter, OfflineStatementArrayParameter, OfflineStatementBigDecimalParameter, 
//            OfflineStatementBooleanParameter, OfflineStatementByteParameter, OfflineStatementBytesParameter, OfflineStatementDateParameter, 
//            OfflineStatementDoubleParameter, OfflineStatementFloatParameter, OfflineStatementIntParameter, OfflineStatementLongParameter, 
//            OfflineStatementNullParameter, OfflineStatementObjectParameter, OfflineStatementTypedObjectParameter, OfflineStatementScaledObjectParameter, 
//            OfflineStatementShortParameter, OfflineStatementStringParameter, OfflineStatementTimeParameter, OfflineStatementTimestampParameter

public class OfflinePreparedStatement extends OfflineStatement
    implements PreparedStatement
{

    public OfflinePreparedStatement(String s)
    {
        sql = s;
    }

    public void addBatch()
        throws SQLException
    {
        notSupported();
    }

    private void addParameter(OfflineStatementParameter offlinestatementparameter)
        throws SQLException
    {
        parameters.add(offlinestatementparameter);
    }

    public void clearParameters()
        throws SQLException
    {
        parameters.clear();
    }

    protected PreparedStatement createStatement(Connection connection)
        throws SQLException
    {
        PreparedStatement preparedstatement = connection.prepareStatement(sql);
        initializeStatement(preparedstatement);
        for(Iterator iterator = parameters.iterator(); iterator.hasNext();)
        {
            OfflineStatementParameter offlinestatementparameter = (OfflineStatementParameter)iterator.next();
            if(offlinestatementparameter != null)
                offlinestatementparameter.set(preparedstatement);
        }

        return preparedstatement;
    }

    public boolean execute()
        throws SQLException
    {
        notSupported();
        return false;
    }

    public boolean execute(Connection connection)
        throws SQLException
    {
        PreparedStatement preparedstatement = createStatement(connection);
        try
        {
            boolean flag = preparedstatement.execute();
            return flag;
        }
        finally
        {
            preparedstatement.close();
        }
    }

    public ResultSet executeQuery()
        throws SQLException
    {
        notSupported();
        return null;
    }

    public ResultSet executeQuery(Connection connection)
        throws SQLException
    {
        notSupported();
        return createStatement(connection).executeQuery();
    }

    public int executeUpdate()
        throws SQLException
    {
        notSupported();
        return 0;
    }

    public int executeUpdate(Connection connection)
        throws SQLException
    {
        PreparedStatement preparedstatement = createStatement(connection);
        try
        {
            int i = preparedstatement.executeUpdate();
            return i;
        }
        finally
        {
            preparedstatement.close();
        }
    }

    public ResultSetMetaData getMetaData()
        throws SQLException
    {
        notSupported();
        return null;
    }

    public void setArray(int i, Array array)
        throws SQLException
    {
        addParameter(new OfflineStatementArrayParameter(i, array));
    }

    public void setAsciiStream(int i, InputStream inputstream, int j)
        throws SQLException
    {
        notSupported();
    }

    public void setBigDecimal(int i, BigDecimal bigdecimal)
        throws SQLException
    {
        addParameter(new OfflineStatementBigDecimalParameter(i, bigdecimal));
    }

    public void setBinaryStream(int i, InputStream inputstream, int j)
        throws SQLException
    {
        notSupported();
    }

    public void setBlob(int i, Blob blob)
        throws SQLException
    {
        notSupported();
    }

    public void setBoolean(int i, boolean flag)
        throws SQLException
    {
        addParameter(new OfflineStatementBooleanParameter(i, flag));
    }

    public void setByte(int i, byte byte0)
        throws SQLException
    {
        addParameter(new OfflineStatementByteParameter(i, byte0));
    }

    public void setBytes(int i, byte abyte0[])
        throws SQLException
    {
        addParameter(new OfflineStatementBytesParameter(i, abyte0));
    }

    public void setCharacterStream(int i, Reader reader, int j)
        throws SQLException
    {
        notSupported();
    }

    public void setClob(int i, Clob clob)
        throws SQLException
    {
        notSupported();
    }

    public void setDate(int i, java.sql.Date date)
        throws SQLException
    {
        addParameter(new OfflineStatementDateParameter(i, date));
    }

    public void setDate(int i, java.sql.Date date, Calendar calendar)
        throws SQLException
    {
        addParameter(new OfflineStatementDateParameter(i, date, calendar));
    }

    public void setDouble(int i, double d)
        throws SQLException
    {
        addParameter(new OfflineStatementDoubleParameter(i, d));
    }

    public void setFloat(int i, float f)
        throws SQLException
    {
        addParameter(new OfflineStatementFloatParameter(i, f));
    }

    public void setInt(int i, int j)
        throws SQLException
    {
        addParameter(new OfflineStatementIntParameter(i, j));
    }

    public void setLong(int i, long l)
        throws SQLException
    {
        addParameter(new OfflineStatementLongParameter(i, l));
    }

    public void setNull(int i, int j)
        throws SQLException
    {
        addParameter(new OfflineStatementNullParameter(i, j));
    }

    public void setNull(int i, int j, String s)
        throws SQLException
    {
        addParameter(new OfflineStatementNullParameter(i, j, s));
    }

    public void setObject(int i, Object obj)
        throws SQLException
    {
        addParameter(new OfflineStatementObjectParameter(i, obj));
    }

    public void setObject(int i, Object obj, int j)
        throws SQLException
    {
        addParameter(new OfflineStatementTypedObjectParameter(i, obj, j));
    }

    public void setObject(int i, Object obj, int j, int k)
        throws SQLException
    {
        addParameter(new OfflineStatementScaledObjectParameter(i, obj, j, k));
    }

    public void setRef(int i, Ref ref)
        throws SQLException
    {
        notSupported();
    }

    public void setShort(int i, short word0)
        throws SQLException
    {
        addParameter(new OfflineStatementShortParameter(i, word0));
    }

    public void setString(int i, String s)
        throws SQLException
    {
        addParameter(new OfflineStatementStringParameter(i, s));
    }

    public void setTime(int i, Time time)
        throws SQLException
    {
        addParameter(new OfflineStatementTimeParameter(i, time));
    }

    public void setTime(int i, Time time, Calendar calendar)
        throws SQLException
    {
        addParameter(new OfflineStatementTimeParameter(i, time, calendar));
    }

    public void setTimestamp(int i, Timestamp timestamp)
        throws SQLException
    {
        addParameter(new OfflineStatementTimestampParameter(i, timestamp));
    }

    public void setTimestamp(int i, Timestamp timestamp, Calendar calendar)
        throws SQLException
    {
        addParameter(new OfflineStatementTimestampParameter(i, timestamp, calendar));
    }

    /**
     * @deprecated Method setUnicodeStream is deprecated
     */

    public void setUnicodeStream(int i, InputStream inputstream, int j)
        throws SQLException
    {
        notSupported();
    }

    private final ArrayList parameters = new ArrayList();
    private final String sql;
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	public void setURL(int parameterIndex, URL x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

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

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		
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