package it.cnr.jada.util.upload;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;

public abstract class RemoteHttpServlet extends HttpServlet
    implements Remote
{

    public RemoteHttpServlet()
    {
    }

    protected void bind()
    {
        try
        {
            registry = LocateRegistry.getRegistry(getRegistryPort());
            registry.list();
        }
        catch(Exception _ex)
        {
            registry = null;
        }
        if(registry == null)
            try
            {
                registry = LocateRegistry.createRegistry(getRegistryPort());
            }
            catch(Exception e)
            {
                log("Could not get or create RMI registry on port " + getRegistryPort() + ": " + e.getMessage());
                return;
            }
        try
        {
            registry.rebind(getRegistryName(), this);
        }
        catch(Exception e)
        {
            log("Could not bind to RMI registry: " + e.getMessage());
            return;
        }
    }

    public void destroy()
    {
        unbind();
    }

    protected String getRegistryName()
    {
        String name = getInitParameter("registryName");
        if(name != null)
            return name;
        else
            return getClass().getName();
    }

    protected int getRegistryPort()
    {
        try
        {
            return Integer.parseInt(getInitParameter("registryPort"));
        }
        catch(NumberFormatException _ex)
        {
            return 1099;
        }
    }

    public void init(ServletConfig config)
        throws ServletException
    {
        super.init(config);
        try
        {
            UnicastRemoteObject.exportObject(this);
            bind();
        }
        catch(RemoteException e)
        {
            log("Problem binding to RMI registry: " + e.getMessage());
        }
    }

    protected void unbind()
    {
        try
        {
            if(registry != null)
                registry.unbind(getRegistryName());
        }
        catch(Exception e)
        {
            log("Problem unbinding from RMI registry: " + e.getMessage());
        }
    }

    protected Registry registry;
}