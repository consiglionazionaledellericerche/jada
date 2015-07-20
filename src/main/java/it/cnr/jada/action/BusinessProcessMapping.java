package it.cnr.jada.action;

import it.cnr.jada.util.Introspector;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

// Referenced classes of package it.cnr.jada.action:
//            Config, StaticForward, InitParameter, Forward, 
//            BusinessProcessException, BusinessProcess, ActionContext

public class BusinessProcessMapping
    implements Serializable
{

    public BusinessProcessMapping()
    {
        forwards = new Hashtable();
        config = new Config();
    }

    public void addForward(StaticForward staticforward)
    {
        forwards.put(staticforward.getName(), staticforward);
    }

    public void addInitParameter(InitParameter initparameter)
    {
        config.setInitParameter(initparameter.getName(), initparameter.getValue());
    }

    public Forward findForward(String s)
    {
        return (Forward)forwards.get(s);
    }

    public String getClassName()
    {
        return className;
    }

    public Config getConfig()
    {
        return config;
    }

    public BusinessProcess getInstance(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            BusinessProcess businessprocess = (BusinessProcess)Class.forName(className).newInstance();
            businessprocess.initializeUserTransaction(actioncontext);
            businessprocess.setMapping(this, actioncontext);
            return businessprocess;
        }
        catch(InstantiationException instantiationexception)
        {
            throw new BusinessProcessException(instantiationexception);
        }
        catch(ClassNotFoundException _ex)
        {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": " + className + " not found.");
        }
        catch(IllegalAccessException _ex)
        {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": can't access counstructor.");
        }
    }

    public BusinessProcess getInstance(ActionContext actioncontext, Object aobj[])
        throws BusinessProcessException
    {
        try
        {
            BusinessProcess businessprocess = (BusinessProcess)Introspector.newInstance(Class.forName(className), aobj);
            businessprocess.initializeUserTransaction(actioncontext);
            businessprocess.setMapping(this, actioncontext);
            return businessprocess;
        }
        catch(InstantiationException instantiationexception)
        {
            throw new BusinessProcessException(instantiationexception);
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            throw new BusinessProcessException(invocationtargetexception);
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            throw new BusinessProcessException(nosuchmethodexception);
        }
        catch(ClassNotFoundException _ex)
        {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": " + className + " not found.");
        }
        catch(IllegalAccessException _ex)
        {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": can't access counstructor.");
        }
    }

    public Boolean isSubclassOf(Class clazzIn)  throws BusinessProcessException {
    	try
    	{
    		Class clazz = Class.forName(className);
    		if (clazzIn.isAssignableFrom(clazz)){
    			return true;
    		}
    		return false;
    	}
    	catch(ClassNotFoundException _ex)
    	{
            throw new BusinessProcessException("Error creating Class \"" + className );
    	}
    }

    public String getName()
    {
        return name;
    }

    public void setClassName(String s)
    {
        className = s;
    }

    public void setName(String s)
    {
        name = s;
    }

    private String name;
    private String className;
    private Hashtable forwards;
    private Config config;
}