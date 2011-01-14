package it.cnr.jada.persistency;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, Persistent

public class UpdateException extends PersistencyException
    implements Serializable
{

    public UpdateException()
    {
    }

    public UpdateException(String s)
    {
        super(s);
    }

    public UpdateException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public UpdateException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public UpdateException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public UpdateException(Throwable throwable)
    {
        super(throwable);
    }

    public UpdateException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}