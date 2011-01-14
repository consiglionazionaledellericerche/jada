package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;
import java.sql.SQLException;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            DefaultSQLExceptionHandler, PersistencySQLException

public class SQLExceptionHandler
    implements Serializable
{

    protected SQLExceptionHandler()
    {
    }

    public static SQLExceptionHandler getInstance()
    {
        return instance;
    }

    public final PersistencyException handleSQLException(SQLException sqlexception)
    {
        return handleSQLException(sqlexception, null);
    }

    public PersistencyException handleSQLException(SQLException sqlexception, Persistent persistent)
    {
        return new PersistencySQLException(sqlexception, persistent);
    }

    public static void setInstance(SQLExceptionHandler sqlexceptionhandler)
    {
        instance = sqlexceptionhandler;
    }

    private static SQLExceptionHandler instance = new DefaultSQLExceptionHandler();

}