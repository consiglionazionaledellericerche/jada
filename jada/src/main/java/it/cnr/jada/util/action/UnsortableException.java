package it.cnr.jada.util.action;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;

public class UnsortableException extends DetailedRuntimeException
    implements Serializable
{

    public UnsortableException()
    {
    }

    public UnsortableException(String s)
    {
        super(s);
    }

    public UnsortableException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public UnsortableException(Throwable throwable)
    {
        super(throwable);
    }
}