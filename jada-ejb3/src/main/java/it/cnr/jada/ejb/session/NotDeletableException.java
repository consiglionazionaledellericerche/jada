package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

public class NotDeletableException extends DeleteException
    implements Serializable
{

    public NotDeletableException()
    {
    }

    public NotDeletableException(String s)
    {
        super(s);
    }

    public NotDeletableException(String s, OggettoBulk persistent)
    {
        super(s, persistent);
    }

    public NotDeletableException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public NotDeletableException(String s, Throwable throwable, OggettoBulk persistent)
    {
        super(s, throwable, persistent);
    }

    public NotDeletableException(Throwable throwable)
    {
        super(throwable);
    }

    public NotDeletableException(Throwable throwable, OggettoBulk persistent)
    {
        super(throwable, persistent);
    }
}