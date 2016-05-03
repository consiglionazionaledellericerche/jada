package it.cnr.jada.persistency;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, Persistent

public class FetchException extends PersistencyException
    implements Serializable
{

    public FetchException()
    {
    }

    public FetchException(String s)
    {
        super(s);
    }

    public FetchException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public FetchException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public FetchException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public FetchException(Throwable throwable)
    {
        super(throwable);
    }

    public FetchException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}