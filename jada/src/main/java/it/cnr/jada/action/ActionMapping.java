package it.cnr.jada.action;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;

// Referenced classes of package it.cnr.jada.action:
//            Config, StaticForward, InitParameter, BusinessProcessException, 
//            ActionMappings, Action, Forward, ActionContext, 
//            BusinessProcess

public class ActionMapping
    implements Serializable
{

    public ActionMapping()
    {
        forwards = new Hashtable();
        config = new Config();
        needExistingSession = true;
    }

    public void addForward(StaticForward staticforward)
    {
        forwards.put(staticforward.getName(), staticforward);
    }

    public void addInitParameter(InitParameter initparameter)
    {
        config.setInitParameter(initparameter.getName(), initparameter.getValue());
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext)
        throws BusinessProcessException
    {
        return mappings.createBusinessProcess(s, actioncontext);
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext, Object aobj[])
        throws BusinessProcessException
    {
        return mappings.createBusinessProcess(s, actioncontext, aobj);
    }

    public Action createNewInstance()
        throws InstantiationException
    {
        try
        {
            instance = (Action)actionClass.newInstance();
            instance.init(config);
            return instance;
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new InstantiationException(illegalaccessexception.getMessage());
        }
    }

    public Forward findActionForward(String s)
    {
        return mappings.findActionForward(s);
    }

    public Forward findDefaultForward()
    {
        return findForward("default");
    }

    public Forward findForward(String s)
    {
        Forward forward = (Forward)forwards.get(s);
        if(forward != null)
            return forward;
        else
            return mappings.findForward(s);
    }

    public Action getActionInstance()
        throws InstantiationException
    {
        if(createMultipleInstances && instance == null)
            return instance = createNewInstance();
        else
            return createNewInstance();
    }

    public ActionMappings getMappings()
    {
        return mappings;
    }

    public String getPath()
    {
        return path;
    }

    public boolean isCreateMultipleInstances()
    {
        return createMultipleInstances;
    }

    public boolean needExistingSession()
    {
        return needExistingSession;
    }

    public void setActionClass(String s)
    {
        try
        {
            actionClass = getClass().getClassLoader().loadClass(s);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setCreateMultipleInstances(boolean flag)
    {
        createMultipleInstances = flag;
    }

    public void setFormClass(String s)
    {
        try
        {
            formClass = Class.forName(s);
            createMultipleInstances = testClassForMultipleInstances(formClass);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setMappings(ActionMappings actionmappings)
    {
        mappings = actionmappings;
    }

    public void setNeedExistingSession(String s)
    {
        needExistingSession = "true".equals(s);
    }

    public void setPath(String s)
    {
        path = s;
    }

    private boolean testClassForMultipleInstances(Class class1)
    {
        for(; class1 != null; class1 = class1.getSuperclass())
        {
            Field afield[] = class1.getDeclaredFields();
            for(int i = 0; i < afield.length; i++){
                if(!Modifier.isStatic(afield[i].getModifiers()))
                    return true;
            }        
        }

        return false;
    }

    private String path;
    private Class actionClass;
    private Class formClass;
    private Hashtable forwards;
    private Config config;
    private ActionMappings mappings;
    private Action instance;
    private boolean needExistingSession;
    private boolean createMultipleInstances;
}