package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class ApplicationPersistencyDiscardedException extends PersistencyException
    implements Serializable
{

    public ApplicationPersistencyDiscardedException()
    {
    }

    public ApplicationPersistencyDiscardedException(String s)
    {
        super(s);
    }

    public ApplicationPersistencyDiscardedException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public ApplicationPersistencyDiscardedException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ApplicationPersistencyDiscardedException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public ApplicationPersistencyDiscardedException(Throwable throwable)
    {
        super(throwable);
    }

    public ApplicationPersistencyDiscardedException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}