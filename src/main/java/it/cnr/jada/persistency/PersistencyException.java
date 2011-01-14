package it.cnr.jada.persistency;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            Persistent

public class PersistencyException extends DetailedException
    implements Serializable
{

    public PersistencyException()
    {
    }

    public PersistencyException(String s)
    {
        super(s);
    }

    public PersistencyException(String s, Persistent persistent1)
    {
        super(s);
        persistent = persistent1;
    }

    public PersistencyException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public PersistencyException(String s, Throwable throwable, Persistent persistent1)
    {
        super(s, throwable);
        persistent = persistent1;
    }

    public PersistencyException(Throwable throwable)
    {
        super(throwable);
    }

    public PersistencyException(Throwable throwable, Persistent persistent1)
    {
        super(throwable);
        persistent = persistent1;
    }

    public Persistent getPersistent()
    {
        return (Persistent)persistent;
    }

    public void setPersistent(Persistent persistent1)
    {
        persistent = persistent1;
    }

    private Object persistent;
}