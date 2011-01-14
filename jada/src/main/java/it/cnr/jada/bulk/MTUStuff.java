package it.cnr.jada.bulk;

import java.io.Serializable;

public class MTUStuff
    implements Serializable, Cloneable
{

    public MTUStuff(String s)
    {
        setMessage(s);
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String s)
    {
        message = s;
    }

    protected String message;
}