package it.cnr.jada.action;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

public class ActionMappings implements Serializable {

	private static final long serialVersionUID = 1L;

    private Hashtable<String, ActionMapping> actions;
    private Hashtable<String, BusinessProcessMapping> businessProcesses;
    private Hashtable<String, Forward> forwards;
    
	public ActionMappings() {
        actions = new Hashtable<String, ActionMapping>();
        businessProcesses = new Hashtable<String, BusinessProcessMapping>();
        forwards = new Hashtable<String, Forward>();
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

	public BusinessProcess createBusinessProcess(ActionMapping actionmapping, ActionContext actioncontext) throws BusinessProcessException {
		String bpName = null;
		for (String key : businessProcesses.keySet()) {
			BusinessProcessMapping businessProcessMapping = businessProcesses.get(key);
			if (businessProcessMapping.getConfig() != null && actionmapping.getPath().equals("/" + businessProcessMapping.getConfig().getInitParameter("defaultAction"))){
				bpName = key;	
				break;
			}
		} 		
		return createBusinessProcess(bpName, actioncontext);
	}

}