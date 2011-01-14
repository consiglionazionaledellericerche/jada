package it.cnr.jada.action;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.action:
//            AbstractAction, ActionContext, BusinessProcess, BusinessProcessException, 
//            Forward

public class StartBusinessProcessAction extends AbstractAction
    implements Serializable
{

    public StartBusinessProcessAction()
    {
    }

    public Forward doDefault(ActionContext actioncontext)
    {
        try
        {
            BusinessProcess businessprocess = actioncontext.createBusinessProcess(businessProcessName);
            actioncontext.getBusinessProcess().closeAllChildren();
            return actioncontext.addBusinessProcess(businessprocess);
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

    public String getBusinessProcessName()
    {
        return businessProcessName;
    }

    public void setBusinessProcessName(String s)
    {
        businessProcessName = s;
    }

    private String businessProcessName;
}