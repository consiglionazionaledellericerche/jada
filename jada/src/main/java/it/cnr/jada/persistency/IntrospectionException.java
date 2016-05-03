package it.cnr.jada.persistency;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

public class IntrospectionException extends DetailedException
    implements Serializable
{

    public IntrospectionException()
    {
    }

    public IntrospectionException(String s)
    {
        super(s);
    }

    public IntrospectionException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public IntrospectionException(Throwable throwable)
    {
        super(throwable);
    }
}