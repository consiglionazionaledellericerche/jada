package it.cnr.jada.action;

import java.io.Serializable;
import java.util.Hashtable;

public class Config
    implements Serializable
{

    public Config()
    {
        initParameters = new Hashtable();
    }

    public String getInitParameter(String s)
    {
        return (String)initParameters.get(s);
    }

    public void setInitParameter(String s, String s1)
    {
        initParameters.put(s, s1);
    }

    private Hashtable initParameters;
}