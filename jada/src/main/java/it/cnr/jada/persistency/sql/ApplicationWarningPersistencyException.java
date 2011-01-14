package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            ApplicationPersistencyException

public class ApplicationWarningPersistencyException extends ApplicationPersistencyException
    implements Serializable
{

    public ApplicationWarningPersistencyException()
    {
    }

    public ApplicationWarningPersistencyException(String s)
    {
        super(s);
    }

    public ApplicationWarningPersistencyException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public ApplicationWarningPersistencyException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ApplicationWarningPersistencyException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public ApplicationWarningPersistencyException(Throwable throwable)
    {
        super(throwable);
    }

    public ApplicationWarningPersistencyException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}