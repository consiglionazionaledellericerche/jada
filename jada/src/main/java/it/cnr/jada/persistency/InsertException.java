package it.cnr.jada.persistency;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, Persistent

public class InsertException extends PersistencyException
    implements Serializable
{

    public InsertException()
    {
    }

    public InsertException(String s)
    {
        super(s);
    }

    public InsertException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public InsertException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public InsertException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public InsertException(Throwable throwable)
    {
        super(throwable);
    }

    public InsertException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}