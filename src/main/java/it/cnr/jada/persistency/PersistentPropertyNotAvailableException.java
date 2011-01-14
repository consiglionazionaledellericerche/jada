package it.cnr.jada.persistency;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            FetchException, Persistent

public class PersistentPropertyNotAvailableException extends FetchException
    implements Serializable
{

    public PersistentPropertyNotAvailableException()
    {
    }

    public PersistentPropertyNotAvailableException(String s)
    {
        super(s);
    }

    public PersistentPropertyNotAvailableException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public PersistentPropertyNotAvailableException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public PersistentPropertyNotAvailableException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public PersistentPropertyNotAvailableException(Throwable throwable)
    {
        super(throwable);
    }

    public PersistentPropertyNotAvailableException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}