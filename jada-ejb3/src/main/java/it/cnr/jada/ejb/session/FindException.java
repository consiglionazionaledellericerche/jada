package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, Persistent

public class FindException extends PersistencyException
    implements Serializable
{

    public FindException()
    {
    }

    public FindException(String s)
    {
        super(s);
    }

    public FindException(String s, OggettoBulk persistent)
    {
        super(s, persistent);
    }

    public FindException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public FindException(String s, Throwable throwable, OggettoBulk persistent)
    {
        super(s, throwable, persistent);
    }

    public FindException(Throwable throwable)
    {
        super(throwable);
    }

    public FindException(Throwable throwable, OggettoBulk persistent)
    {
        super(throwable, persistent);
    }
}