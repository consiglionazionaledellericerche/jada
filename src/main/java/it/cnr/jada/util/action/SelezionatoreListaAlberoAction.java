package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Stack;
import java.util.Vector;

// Referenced classes of package it.cnr.jada.util.action:
//            SelezionatoreListaAction, SelezionatoreListaAlberoBP, AbstractSelezionatoreBP, Selection, 
//            SelezionatoreListaBP

public class SelezionatoreListaAlberoAction extends SelezionatoreListaAction
    implements Serializable
{

    public SelezionatoreListaAlberoAction()
    {
    }

    public Forward doBack(ActionContext actioncontext)
    {
        return doGoToLevel(actioncontext, null);
    }

    public Forward doExpand(ActionContext actioncontext)
    {
        try
        {
            SelezionatoreListaAlberoBP selezionatorelistaalberobp = (SelezionatoreListaAlberoBP)actioncontext.getBusinessProcess();
            OggettoBulk oggettobulk = (OggettoBulk)selezionatorelistaalberobp.getFocusedElement(actioncontext);
            selezionatorelistaalberobp.getHistory().push(selezionatorelistaalberobp.getParentElement());
            selezionatorelistaalberobp.setParentElement(oggettobulk);
            selezionatorelistaalberobp.getSelection().clear();
            selezionatorelistaalberobp.setLeafElement(false);
            selezionatorelistaalberobp.setIterator(actioncontext, EJBCommonServices.openRemoteIterator(actioncontext, selezionatorelistaalberobp.getChildren(actioncontext, oggettobulk)));
            return actioncontext.findDefaultForward();
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

    public Forward doGoToLevel(ActionContext actioncontext, String s)
    {
        try
        {
            SelezionatoreListaAlberoBP selezionatorelistaalberobp = (SelezionatoreListaAlberoBP)actioncontext.getBusinessProcess();
            if(s != null)
            {
                for(int i = Integer.parseInt(s) + 1; selezionatorelistaalberobp.getHistory().size() > i; selezionatorelistaalberobp.getHistory().pop());
            }
            if(!selezionatorelistaalberobp.getHistory().isEmpty())
            {
                selezionatorelistaalberobp.setParentElement((OggettoBulk)selezionatorelistaalberobp.getHistory().pop());
                selezionatorelistaalberobp.getSelection().clear();
                selezionatorelistaalberobp.setLeafElement(false);
                selezionatorelistaalberobp.setIterator(actioncontext, EJBCommonServices.openRemoteIterator(actioncontext, selezionatorelistaalberobp.getChildren(actioncontext, selezionatorelistaalberobp.getParentElement())));
            }
            return actioncontext.findDefaultForward();
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
        SelezionatoreListaAlberoBP selezionatorelistaalberobp = (SelezionatoreListaAlberoBP)actioncontext.getBusinessProcess();
        selezionatorelistaalberobp.setFocus(actioncontext);
        selezionatorelistaalberobp.setSelection(actioncontext);
        OggettoBulk oggettobulk = (OggettoBulk)selezionatorelistaalberobp.getFocusedElement(actioncontext);
        selezionatorelistaalberobp.setLeafElement(selezionatorelistaalberobp.isLeaf(actioncontext, oggettobulk));
        return actioncontext.findDefaultForward();
    }
}