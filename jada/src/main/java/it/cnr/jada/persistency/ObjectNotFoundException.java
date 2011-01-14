package it.cnr.jada.persistency;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            FindException, Persistent

public class ObjectNotFoundException extends FindException
    implements Serializable
{

    public ObjectNotFoundException()
    {
    }

    public ObjectNotFoundException(String s)
    {
        super(s);
    }

    public ObjectNotFoundException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public ObjectNotFoundException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ObjectNotFoundException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public ObjectNotFoundException(Throwable throwable)
    {
        super(throwable);
    }

    public ObjectNotFoundException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}