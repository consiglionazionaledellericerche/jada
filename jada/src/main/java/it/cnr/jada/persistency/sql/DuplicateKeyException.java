package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.InsertException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class DuplicateKeyException extends InsertException
    implements Serializable
{

    public DuplicateKeyException()
    {
    }

    public DuplicateKeyException(String s)
    {
        super(s);
    }

    public DuplicateKeyException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public DuplicateKeyException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public DuplicateKeyException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public DuplicateKeyException(Throwable throwable)
    {
        super(throwable);
    }

    public DuplicateKeyException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}