package it.cnr.jada.action;

import java.io.Serializable;

public class InitParameter
    implements Serializable
{

    public InitParameter()
    {
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setValue(String s)
    {
        value = s;
    }

    private String name;
    private String value;
}