package it.cnr.jada.util.sql;

import it.cnr.jada.util.EventTracerWriter;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.*;

// Referenced classes of package it.cnr.jada.util.sql:
//            LoggedStatement, LoggedConnection

class LoggedPreparedStatement extends LoggedStatement
    implements Serializable, PreparedStatement
{

    LoggedPreparedStatement(LoggedConnection loggedconnection, String s, PreparedStatement preparedstatement)
    {
        super(loggedconnection, preparedstatement);
        sql = s;
    }

    public void addBatch()
        throws SQLException
    {
        ((PreparedStatement)super.statement).addBatch();
    }

    public void clearParameters()
        throws SQLException
    {
        ((PreparedStatement)super.statement).clearParameters();
    }

    public boolean execute()
        throws SQLException
    {
        trace();
        return ((PreparedStatement)super.statement).execute();
    }

    public ResultSet executeQuery()
        throws SQLException
    {
        trace();
        return ((PreparedStatement)super.statement).executeQuery();
    }

    public int executeUpdate()
        throws SQLException
    {
        trace();
        return ((PreparedStatement)super.statement).executeUpdate();
    }

    public ResultSetMetaData getMetaData()
        throws SQLException
    {
        return ((PreparedStatement)super.statement).getMetaData();
    }

    public void setArray(int i, Array array)
        throws SQLException
    {
        setParameter(i, array);
        ((PreparedStatement)super.statement).setArray(i, array);
    }

    public void setAsciiStream(int i, InputStream inputstream, int j)
        throws SQLException
    {
        setParameter(i, inputstream.getClass().getName());
        ((PreparedStatement)super.statement).setAsciiStream(i, inputstream, j);
    }

    public void setBigDecimal(int i, BigDecimal bigdecimal)
        throws SQLException
    {
        setParameter(i, bigdecimal);
        ((PreparedStatement)super.statement).setBigDecimal(i, bigdecimal);
    }

    public void setBinaryStream(int i, InputStream inputstream, int j)
        throws SQLException
    {
        setParameter(i, inputstream.getClass().getName());
        ((PreparedStatement)super.statement).setBinaryStream(i, inputstream, j);
    }

    public void setBlob(int i, Blob blob)
        throws SQLException
    {
        setParameter(i, blob);
        ((PreparedStatement)super.statement).setBlob(i, blob);
    }

    public void setBoolean(int i, boolean flag)
        throws SQLException
    {
        setParameter(i, new Boolean(flag));
        ((PreparedStatement)super.statement).setBoolean(i, flag);
    }

    public void setByte(int i, byte byte0)
        throws SQLException
    {
        setParameter(i, new Byte(byte0));
        ((PreparedStatement)super.statement).setByte(i, byte0);
    }

    public void setBytes(int i, byte abyte0[])
        throws SQLException
    {
        setParameter(i, abyte0);
        ((PreparedStatement)super.statement).setBytes(i, abyte0);
    }

    public void setCharacterStream(int i, Reader reader, int j)
        throws SQLException
    {
        setParameter(i, reader.getClass().getName());
        ((PreparedStatement)super.statement).setCharacterStream(i, reader, j);
    }

    public void setClob(int i, Clob clob)
        throws SQLException
    {
        setParameter(i, clob);
        ((PreparedStatement)super.statement).setClob(i, clob);
    }

    public void setDate(int i, java.sql.Date date)
        throws SQLException
    {
        setParameter(i, date);
        ((PreparedStatement)super.statement).setDate(i, date);
    }

    public void setDate(int i, java.sql.Date date, Calendar calendar)
        throws SQLException
    {
        setParameter(i, date);
        ((PreparedStatement)super.statement).setDate(i, date, calendar);
    }

    public void setDouble(int i, double d)
        throws SQLException
    {
        setParameter(i, new Double(d));
        ((PreparedStatement)super.statement).setDouble(i, d);
    }

    public void setFloat(int i, float f)
        throws SQLException
    {
        setParameter(i, new Float(f));
        ((PreparedStatement)super.statement).setFloat(i, f);
    }

    public void setInt(int i, int j)
        throws SQLException
    {
        setParameter(i, new Integer(j));
        ((PreparedStatement)super.statement).setInt(i, j);
    }

    public void setLong(int i, long l)
        throws SQLException
    {
        setParameter(i, new Long(l));
        ((PreparedStatement)super.statement).setLong(i, l);
    }

    public void setNull(int i, int j)
        throws SQLException
    {
        setParameter(i, null);
        ((PreparedStatement)super.statement).setNull(i, j);
    }

    public void setNull(int i, int j, String s)
        throws SQLException
    {
        setParameter(i, null);
        ((PreparedStatement)super.statement).setNull(i, j, s);
    }

    public void setObject(int i, Object obj)
        throws SQLException
    {
        setParameter(i, obj);
        ((PreparedStatement)super.statement).setObject(i, obj);
    }

    public void setObject(int i, Object obj, int j)
        throws SQLException
    {
        setParameter(i, obj);
        ((PreparedStatement)super.statement).setObject(i, obj, j);
    }

    public void setObject(int i, Object obj, int j, int k)
        throws SQLException
    {
        setParameter(i, obj);
        ((PreparedStatement)super.statement).setObject(i, obj, j);
    }

    private void setParameter(int i, Object obj)
    {
        i--;
        parameters.ensureCapacity(i);
        for(; i >= parameters.size(); parameters.add(null));
        parameters.set(i, obj);
    }

    public void setRef(int i, Ref ref)
        throws SQLException
    {
        setParameter(i, ref);
        ((PreparedStatement)super.statement).setRef(i, ref);
    }

    public void setShort(int i, short word0)
        throws SQLException
    {
        setParameter(i, new Short(word0));
        ((PreparedStatement)super.statement).setShort(i, word0);
    }

    public void setString(int i, String s)
        throws SQLException
    {
        setParameter(i, s);
        ((PreparedStatement)super.statement).setString(i, s);
    }

    public void setTime(int i, Time time)
        throws SQLException
    {
        setParameter(i, time);
        ((PreparedStatement)super.statement).setTime(i, time);
    }

    public void setTime(int i, Time time, Calendar calendar)
        throws SQLException
    {
        setParameter(i, time);
        ((PreparedStatement)super.statement).setTime(i, time, calendar);
    }

    public void setTimestamp(int i, Timestamp timestamp)
        throws SQLException
    {
        setParameter(i, timestamp);
        ((PreparedStatement)super.statement).setTimestamp(i, timestamp);
    }

    public void setTimestamp(int i, Timestamp timestamp, Calendar calendar)
        throws SQLException
    {
        setParameter(i, timestamp);
        ((PreparedStatement)super.statement).setTimestamp(i, timestamp, calendar);
    }

    /**
     * @deprecated Method setUnicodeStream is deprecated
     */

    public void setUnicodeStream(int i, InputStream inputstream, int j)
        throws SQLException
    {
        //setParameter(i, inputstream);
        //((PreparedStatement)super.statement).setUnicodeStream(i, inputstream, j);
    }

    private void trace()
    {
        EventTracerWriter eventtracerwriter = super.connection.startEventTrace();
        Iterator iterator = parameters.iterator();
        for(StringTokenizer stringtokenizer = new StringTokenizer(sql, "?", true); stringtokenizer.hasMoreTokens();)
        {
            String s = stringtokenizer.nextToken();
            if(s.equals("?") && iterator.hasNext())
            {
                eventtracerwriter.print('?');
                eventtracerwriter.print(iterator.next());
            } else
            {
                eventtracerwriter.print(s);
            }
        }

        eventtracerwriter.println();
        eventtracerwriter.close();
    }

    protected final ArrayList parameters = new ArrayList();
    protected final String sql;
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