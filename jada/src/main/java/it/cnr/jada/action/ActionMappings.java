package it.cnr.jada.action;

import java.io.Serializable;
import java.util.Hashtable;

// Referenced classes of package it.cnr.jada.action:
//            ActionMapping, BusinessProcessMapping, StaticForward, BusinessProcessException, 
//            ActionForward, Forward, ActionContext, BusinessProcess

public class ActionMappings
    implements Serializable
{

    public ActionMappings()
    {
        actions = new Hashtable();
        businessProcesses = new Hashtable();
        forwards = new Hashtable();
    }

    public void addActionMapping(ActionMapping actionmapping)
    {
        actionmapping.setMappings(this);
        actions.put(actionmapping.getPath(), actionmapping);
    }

    public void addBusinessProcessMapping(BusinessProcessMapping businessprocessmapping)
    {
        businessProcesses.put(businessprocessmapping.getName(), businessprocessmapping);
    }

    public void addForward(StaticForward staticforward)
    {
        forwards.put(staticforward.getName(), staticforward);
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext)
        throws BusinessProcessException
    {
        BusinessProcessMapping businessprocessmapping = (BusinessProcessMapping)businessProcesses.get(s);
        if(businessprocessmapping == null)
            return null;
        else
            return businessprocessmapping.getInstance(actioncontext);
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext, Object aobj[])
        throws BusinessProcessException
    {
        BusinessProcessMapping businessprocessmapping = (BusinessProcessMapping)businessProcesses.get(s);
        if(businessprocessmapping == null)
            return null;
        else
            return businessprocessmapping.getInstance(actioncontext, aobj);
    }

    public Forward findActionForward(String s)
    {
        ActionMapping actionmapping = (ActionMapping)actions.get(s);
        if(actionmapping == null)
            return null;
        try
        {
            return new ActionForward(actionmapping.getActionInstance(), actionmapping);
        }
        catch(InstantiationException _ex)
        {
            return null;
        }
    }

    public ActionMapping findActionMapping(String s)
    {
        return (ActionMapping)actions.get(s);
    }

    public Forward findForward(String s)
    {
        return (Forward)forwards.get(s);
    }

    private Hashtable actions;
    private Hashtable businessProcesses;
    private Hashtable forwards;
}