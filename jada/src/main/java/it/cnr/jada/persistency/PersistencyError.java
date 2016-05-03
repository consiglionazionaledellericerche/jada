package it.cnr.jada.persistency;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;

public class PersistencyError extends DetailedRuntimeException
    implements Serializable
{

    public PersistencyError()
    {
    }

    public PersistencyError(String s)
    {
        super(s);
    }

    public PersistencyError(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public PersistencyError(Throwable throwable)
    {
        super(throwable);
    }
}