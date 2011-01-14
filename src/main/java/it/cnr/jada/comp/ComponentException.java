package it.cnr.jada.comp;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

public class ComponentException extends DetailedException
    implements Serializable
{

    public ComponentException()
    {
    }

    public ComponentException(String s)
    {
        super(s);
    }

    public ComponentException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ComponentException(Throwable throwable)
    {
        super(throwable);
    }
}