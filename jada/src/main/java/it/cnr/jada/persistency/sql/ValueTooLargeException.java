package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class ValueTooLargeException extends PersistencyException
    implements Serializable
{

    public ValueTooLargeException()
    {
    }

    public ValueTooLargeException(String s)
    {
        super(s);
    }

    public ValueTooLargeException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public ValueTooLargeException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ValueTooLargeException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public ValueTooLargeException(Throwable throwable)
    {
        super(throwable);
    }

    public ValueTooLargeException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}