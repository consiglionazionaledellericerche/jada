package it.cnr.jada.persistency.sql;

import it.cnr.jada.DetailedException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;
import java.sql.SQLException;

public class PersistencySQLException extends PersistencyException
    implements Serializable
{

    public PersistencySQLException(String s, SQLException sqlexception)
    {
        super(s, sqlexception);
    }

    public PersistencySQLException(String s, SQLException sqlexception, Persistent persistent)
    {
        super(s, sqlexception, persistent);
    }

    public PersistencySQLException(SQLException sqlexception)
    {
        super(sqlexception);
    }

    public PersistencySQLException(SQLException sqlexception, Persistent persistent)
    {
        super(sqlexception, persistent);
    }

    public SQLException getSQLException()
    {
        return (SQLException)getDetail();
    }
}