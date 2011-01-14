package it.cnr.jada.persistency;

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

    public FindException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public FindException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public FindException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public FindException(Throwable throwable)
    {
        super(throwable);
    }

    public FindException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}