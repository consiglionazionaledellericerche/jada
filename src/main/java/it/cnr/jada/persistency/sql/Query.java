package it.cnr.jada.persistency.sql;

import it.cnr.jada.util.OrderConstants;

import java.io.Serializable;
import java.sql.*;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            ColumnMap

public interface Query
    extends OrderConstants, Serializable
{

    public abstract int executeCountQuery(Connection connection)
        throws SQLException;

    public abstract ColumnMap getColumnMap();

    public abstract int getOrderBy(String s);

    public abstract boolean isOrderableByProperty(String s);

    public abstract LoggableStatement prepareStatement(Connection connection)
        throws SQLException;

    public abstract LoggableStatement prepareStatement(Connection connection, int i, int j)
        throws SQLException;

    public abstract void setOrderBy(String s, int i);

}