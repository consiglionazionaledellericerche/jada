package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class BusyRecordException extends PersistencyException
    implements Serializable
{

    public BusyRecordException()
    {
    }

    public BusyRecordException(String s)
    {
        super(s);
    }

    public BusyRecordException(String s, Persistent persistent)
    {
        super(s, persistent);
    }

    public BusyRecordException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public BusyRecordException(String s, Throwable throwable, Persistent persistent)
    {
        super(s, throwable, persistent);
    }

    public BusyRecordException(Throwable throwable)
    {
        super(throwable);
    }

    public BusyRecordException(Throwable throwable, Persistent persistent)
    {
        super(throwable, persistent);
    }
}