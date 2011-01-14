package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.DeleteException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class NotDeletableException extends DeleteException
    implements Serializable
{

    public NotDeletableException()
    {
    }

    public NotDeletableException(String s)
    {
        super(s);
    }

    public NotDeletableException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public NotDeletableException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public NotDeletableException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public NotDeletableException(Throwable throwable)
    {
        super(throwable);
    }

    public NotDeletableException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}