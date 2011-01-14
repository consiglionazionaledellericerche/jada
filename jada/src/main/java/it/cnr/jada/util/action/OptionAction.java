package it.cnr.jada.util.action;

import it.cnr.jada.action.*;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            OptionBP

public class OptionAction extends AbstractAction
    implements Serializable
{

    public OptionAction()
    {
    }

    public Forward doCancel(ActionContext actioncontext)
        throws RemoteException, BusinessProcessException
    {
        return doOption(actioncontext, 2);
    }

    public Forward doClose(ActionContext actioncontext)
        throws RemoteException, BusinessProcessException
    {
        return doOption(actioncontext, 16);
    }

    public Forward doNo(ActionContext actioncontext)
        throws RemoteException, BusinessProcessException
    {
        return doOption(actioncontext, 8);
    }

    public Forward doOk(ActionContext actioncontext)
        throws RemoteException, BusinessProcessException
    {
        return doOption(actioncontext, 1);
    }

    public Forward doOption(ActionContext actioncontext, int i)
        throws RemoteException, BusinessProcessException
    {
        ((OptionBP)actioncontext.getBusinessProcess()).setOption(i);
        actioncontext.closeBusinessProcess();
        HookForward hookforward = (HookForward)actioncontext.findForward("option");
        hookforward.addParameter("option", new Integer(i));
        return hookforward;
    }

    public Forward doYes(ActionContext actioncontext)
        throws RemoteException, BusinessProcessException
    {
        return doOption(actioncontext, 4);
    }
}