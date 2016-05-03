package it.cnr.jada.action;


public class NoSuchSessionException extends RuntimeException
{

    public NoSuchSessionException()
    {
    }

    public NoSuchSessionException(String s)
    {
        super(s);
    }
}