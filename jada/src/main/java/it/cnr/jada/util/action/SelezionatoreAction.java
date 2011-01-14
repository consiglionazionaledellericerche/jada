package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            BulkAction, AbstractSelezionatoreBP, SelezionatoreListaBP, BulkListPrintBP

public class SelezionatoreAction extends BulkAction
    implements Serializable
{

    public SelezionatoreAction()
    {
    }

    public Forward basicDoBringBack(ActionContext actioncontext)
        throws BusinessProcessException
    {
        AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP)actioncontext.getBusinessProcess();
        HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
        hookforward.addParameter("focusedElement", abstractselezionatorebp.getFocusedElement(actioncontext));
        actioncontext.closeBusinessProcess();
        return hookforward;
    }

    public Forward doBringBack(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP)actioncontext.getBusinessProcess();
            abstractselezionatorebp.setSelection(actioncontext);
            return basicDoBringBack(actioncontext);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doCloseForm(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            Forward forward = actioncontext.findForward("annulla_seleziona");
            if(forward == null)
                forward = actioncontext.findForward("seleziona");
            actioncontext.closeBusinessProcess();
            return forward;
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPrint(ActionContext actioncontext)
    {
        try
        {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
            BulkListPrintBP bulklistprintbp = (BulkListPrintBP)actioncontext.createBusinessProcess(selezionatorelistabp.getPrintbp());
            bulklistprintbp.setColumns(selezionatorelistabp.getColumns());
            bulklistprintbp.setIterator(actioncontext, selezionatorelistabp.getIterator());
            bulklistprintbp.setTitle(selezionatorelistabp.getBulkInfo().getLongDescription());
            return actioncontext.addBusinessProcess(bulklistprintbp);
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(RemoteException remoteexception)
        {
            return handleException(actioncontext, remoteexception);
        }
    }

    public Forward doSelection(ActionContext actioncontext, String s)
        throws BusinessProcessException
    {
        try
        {
            if(!s.startsWith("mainTable"))
            {
                return super.doSelection(actioncontext, s);
            } else
            {
                AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP)actioncontext.getBusinessProcess();
                abstractselezionatorebp.setSelection(actioncontext);
                return doBringBack(actioncontext);
            }
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
}