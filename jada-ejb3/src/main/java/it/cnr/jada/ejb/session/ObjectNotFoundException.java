package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

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

    public ObjectNotFoundException(String s, OggettoBulk persistent)
    {
        super(s, persistent);
    }

    public ObjectNotFoundException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ObjectNotFoundException(String s, Throwable throwable, OggettoBulk persistent)
    {
        super(s, throwable, persistent);
    }

    public ObjectNotFoundException(Throwable throwable)
    {
        super(throwable);
    }

    public ObjectNotFoundException(Throwable throwable, OggettoBulk persistent)
    {
        super(throwable, persistent);
    }
}