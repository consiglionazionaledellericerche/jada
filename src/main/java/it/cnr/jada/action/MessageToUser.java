package it.cnr.jada.action;

import java.io.Serializable;

public class MessageToUser extends RuntimeException
    implements Serializable
{

    public MessageToUser(String s)
    {
        this(s, 1);
    }

    public MessageToUser(String s, int i)
    {
        super(s);
        status = i;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int i)
    {
        status = i;
    }

    private int status;
}