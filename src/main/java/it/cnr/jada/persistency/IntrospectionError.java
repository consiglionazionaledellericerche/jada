package it.cnr.jada.persistency;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;

public class IntrospectionError extends DetailedRuntimeException
    implements Serializable
{

    public IntrospectionError()
    {
    }

    public IntrospectionError(String s)
    {
        super(s);
    }

    public IntrospectionError(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public IntrospectionError(Throwable throwable)
    {
        super(throwable);
    }
}