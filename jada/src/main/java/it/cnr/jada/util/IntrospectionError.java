package it.cnr.jada.util;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;

public class IntrospectionError extends DetailedRuntimeException
    implements Serializable
{

    public IntrospectionError()
    {
    }

    public IntrospectionError(Exception exception)
    {
        super(exception);
    }

    public IntrospectionError(String s)
    {
        super(s);
    }
}