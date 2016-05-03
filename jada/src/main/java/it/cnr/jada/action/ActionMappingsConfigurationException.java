package it.cnr.jada.action;

import it.cnr.jada.DetailedException;

public class ActionMappingsConfigurationException extends DetailedException
{

    public ActionMappingsConfigurationException()
    {
    }

    public ActionMappingsConfigurationException(String s)
    {
        super(s);
    }

    public ActionMappingsConfigurationException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public ActionMappingsConfigurationException(Throwable throwable)
    {
        super(throwable);
    }
}