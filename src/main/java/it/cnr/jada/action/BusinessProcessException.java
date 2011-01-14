package it.cnr.jada.action;

import it.cnr.jada.DetailedException;

import java.io.Serializable;

public class BusinessProcessException extends DetailedException
    implements Serializable
{

    public BusinessProcessException()
    {
    }

    public BusinessProcessException(String s)
    {
        super(s);
    }

    public BusinessProcessException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public BusinessProcessException(Throwable throwable)
    {
        super(throwable);
    }
}