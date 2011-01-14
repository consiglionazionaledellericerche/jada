package it.cnr.jada.util.ejb;

import java.io.Serializable;
import java.sql.*;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.sql.DataSource;

// Referenced classes of package it.cnr.jada.util.ejb:
//            EJBCommonServices

public class JndiJdbcDriver
    implements Serializable, Driver
{

    public JndiJdbcDriver()
    {
    }

    public boolean acceptsURL(String s)
        throws SQLException
    {
        return s.startsWith("jdbc:jndi:");
    }

    public Connection connect(String s, Properties properties)
        throws SQLException
    {
        if(!s.startsWith("jdbc:jndi:"))
            throw new SQLException("Invalid driver");
        try
        {
            return EJBCommonServices.getDatasource(s.substring(10)).getConnection(properties.getProperty("user"), properties.getProperty("password"));
        }
        catch(EJBException ejbexception)
        {
            throw new SQLException(ejbexception.getMessage());
        }
    }

    public int getMajorVersion()
    {
        return 1;
    }

    public int getMinorVersion()
    {
        return 0;
    }

    public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties)
        throws SQLException
    {
        return null;
    }

    public boolean jdbcCompliant()
    {
        return true;
    }

    static 
    {
        try
        {
            DriverManager.registerDriver(new JndiJdbcDriver());
        }
        catch(SQLException sqlexception)
        {
            sqlexception.printStackTrace();
        }
    }
}