package it.cnr.jada.util;

import javax.servlet.ServletContext;

public abstract class WebSphereServices
{

    public WebSphereServices()
    {
    }

    public abstract String getCloneIndex();

    public static final WebSphereServices getInstance()
    {
        return instance;
    }

    public abstract String getQueueName();

    public abstract String getResourcePath(ServletContext servletcontext, String s);

    //public abstract void suspendTransaction();

    private static final WebSphereServices instance;

    static 
    {
        instance = (WebSphereServices)PlatformObjectFactory.createInstance(it.cnr.jada.util.WebSphereServices.class, "was35");
    }
}