package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class ReferentialIntegrityException extends PersistencyException
    implements Serializable
{

    public ReferentialIntegrityException()
    {
    }

    public ReferentialIntegrityException(String s)
    {
        super(s);
    }

    public ReferentialIntegrityException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public ReferentialIntegrityException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ReferentialIntegrityException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public ReferentialIntegrityException(Throwable throwable)
    {
        super(throwable);
    }

    public ReferentialIntegrityException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}