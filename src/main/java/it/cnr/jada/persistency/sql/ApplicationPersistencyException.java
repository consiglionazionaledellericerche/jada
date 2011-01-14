package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class ApplicationPersistencyException extends PersistencyException
    implements Serializable
{

    public ApplicationPersistencyException()
    {
    }

    public ApplicationPersistencyException(String s)
    {
        super(s);
    }

    public ApplicationPersistencyException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public ApplicationPersistencyException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ApplicationPersistencyException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public ApplicationPersistencyException(Throwable throwable)
    {
        super(throwable);
    }

    public ApplicationPersistencyException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}