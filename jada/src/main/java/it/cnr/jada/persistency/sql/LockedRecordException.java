package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class LockedRecordException extends PersistencyException
    implements Serializable
{

    public LockedRecordException()
    {
    }

    public LockedRecordException(String s)
    {
        super(s);
    }

    public LockedRecordException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public LockedRecordException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public LockedRecordException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public LockedRecordException(Throwable throwable)
    {
        super(throwable);
    }

    public LockedRecordException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}