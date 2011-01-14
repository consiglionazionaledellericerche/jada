package it.cnr.jada.persistency;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, Persistent

public class DeleteException extends PersistencyException
    implements Serializable
{

    public DeleteException()
    {
    }

    public DeleteException(String s)
    {
        super(s);
    }

    public DeleteException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public DeleteException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public DeleteException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public DeleteException(Throwable throwable)
    {
        super(throwable);
    }

    public DeleteException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}