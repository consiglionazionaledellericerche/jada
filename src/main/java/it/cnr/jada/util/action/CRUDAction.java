package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.ApplicationPersistencyDiscardedException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;

// Referenced classes of package it.cnr.jada.util.action:
//            BulkAction, CRUDBP, BulkBP, FormBP, 
//            SelezionatoreListaBP, RicercaLiberaBP, OptionBP, FormAction

public class CRUDAction extends BulkAction
    implements Serializable
{

    public CRUDAction()
    {
    }

    public Forward doAnnullaRiporta(ActionContext actioncontext)
        throws BusinessProcessException
    {
        CRUDBP crudbp = (CRUDBP)actioncontext.getBusinessProcess();
        OggettoBulk oggettobulk = crudbp.getBulkClone();
        actioncontext.closeBusinessProcess();
        HookForward hookforward = (HookForward)actioncontext.findForward("bringback");
        if(hookforward == null)
            return actioncontext.findDefaultForward();
        if(oggettobulk != null)
            hookforward.addParameter("undoBringBack", oggettobulk);
        return hookforward;
    }
    
    public Forward doLastSearch(ActionContext actioncontext)
            throws RemoteException, InstantiationException, RemoveException
        {
            try
            {
                fillModel(actioncontext);
                CRUDBP crudbp = getBusinessProcess(actioncontext);
                OggettoBulk oggettobulk = crudbp.getModel();
                RemoteIterator remoteiterator = crudbp.lastFind(actioncontext);
                if(remoteiterator == null || remoteiterator.countElements() == 0)
                {
                    EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                    crudbp.setMessage("La ricerca non ha fornito alcun risultato.");
                    return actioncontext.findDefaultForward();
                }
                if(remoteiterator.countElements() == 1)
                {
                    OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
                    EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                    crudbp.setMessage("La ricerca ha fornito un solo risultato.");
                    return doRiportaSelezione(actioncontext, oggettobulk1);
                } else
                {
                    crudbp.setModel(actioncontext, oggettobulk);
                    SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
                    selezionatorelistabp.setIterator(actioncontext, remoteiterator);
                    selezionatorelistabp.setBulkInfo(crudbp.getSearchBulkInfo());
                    selezionatorelistabp.setColumns(getBusinessProcess(actioncontext).getSearchResultColumns());
                    actioncontext.addHookForward("seleziona", this, "doRiportaSelezione");
                    return actioncontext.addBusinessProcess(selezionatorelistabp);
                }
            }
            catch(Throwable throwable)
            {
                return handleException(actioncontext, throwable);
            }
        }

    public Forward doCerca(ActionContext actioncontext)
        throws RemoteException, InstantiationException, RemoveException
    {
        try
        {
            fillModel(actioncontext);
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            OggettoBulk oggettobulk = crudbp.getModel();
            RemoteIterator remoteiterator = crudbp.find(actioncontext, null, oggettobulk);
            if(remoteiterator == null || remoteiterator.countElements() == 0)
            {
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                crudbp.setMessage("La ricerca non ha fornito alcun risultato.");
                return actioncontext.findDefaultForward();
            }
            if(remoteiterator.countElements() == 1)
            {
                OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                crudbp.setMessage("La ricerca ha fornito un solo risultato.");
                return doRiportaSelezione(actioncontext, oggettobulk1);
            } else
            {
                crudbp.setModel(actioncontext, oggettobulk);
                SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
                selezionatorelistabp.setIterator(actioncontext, remoteiterator);
                selezionatorelistabp.setBulkInfo(crudbp.getSearchBulkInfo());
                selezionatorelistabp.setColumns(getBusinessProcess(actioncontext).getSearchResultColumns());
                actioncontext.addHookForward("seleziona", this, "doRiportaSelezione");
                return actioncontext.addBusinessProcess(selezionatorelistabp);
            }
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doCloseForm(ActionContext actioncontext)
        throws BusinessProcessException
    {
        if(getBusinessProcess(actioncontext).isBringBack())
            return doAnnullaRiporta(actioncontext);
        else
            return super.doCloseForm(actioncontext);
    }

    public Forward doConfermaNuovaRicerca(ActionContext actioncontext, int i)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            if(i == 4)
                crudbp.resetForSearch(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfermaNuovo(ActionContext actioncontext, int i)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            if(i == 4)
                crudbp.reset(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfermaRicercaLibera(ActionContext actioncontext, int i)
    {
        try
        {
            if(i == 8)
            {
                return actioncontext.findDefaultForward();
            } else
            {
                CRUDBP crudbp = getBusinessProcess(actioncontext);
                crudbp.resetForSearch(actioncontext);
                RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.createBusinessProcess("RicercaLibera");
                ricercaliberabp.setSearchProvider(crudbp.getSearchProvider());
                ricercaliberabp.setFreeSearchSet(crudbp.getFreeSearchSet());
                ricercaliberabp.setPrototype(crudbp.createEmptyModelForFreeSearch(actioncontext));
                ricercaliberabp.setColumnSet(crudbp.getSearchResultColumnSet());
                actioncontext.addHookForward("seleziona", this, "doRiportaSelezione");
                actioncontext.addHookForward("close", this, "doDefault");
                return actioncontext.addBusinessProcess(ricercaliberabp);
            }
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfermaRiporta(ActionContext actioncontext, int i)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            if(crudbp.isEditOnly())
            {
                if(crudbp.isSaveOnBringBack())
                    crudbp.save(actioncontext);
                else
                    crudbp.validate(actioncontext);
            } else
            {
                if(i == 2)
                    return actioncontext.findDefaultForward();
                if(i == 4)
                {
                    fillModel(actioncontext);
                    crudbp.save(actioncontext);
                } else
                if(i == 8)
                {
                    if(crudbp.isInserting())
                    {
                        actioncontext.closeBusinessProcess();
                        return actioncontext.findDefaultForward();
                    }
                    crudbp.edit(actioncontext, crudbp.getModel());
                }
            }
            OggettoBulk oggettobulk = crudbp.getBringBackModel(actioncontext);
            if(oggettobulk == null)
                throw new MessageToUser("E' necessario selezionare qualcosa", 1);
            else
                return riporta(actioncontext, oggettobulk);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfirmHandleDuplicateKey(ActionContext actioncontext, OptionBP optionbp)
    {
        try
        {
            if(optionbp.getOption() == 8)
            {
                return actioncontext.findDefaultForward();
            } else
            {
                CRUDBP crudbp = getBusinessProcess(actioncontext);
                CRUDDuplicateKeyException crudduplicatekeyexception = (CRUDDuplicateKeyException)optionbp.getAttribute("exception");
                crudbp.edit(actioncontext, crudduplicatekeyexception.getDuplicateBulk());
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

    public Forward doElimina(ActionContext actioncontext)
        throws RemoteException
    {
        try
        {
            fillModel(actioncontext);
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            if(!crudbp.isEditing())
            {
                crudbp.setMessage("Non \350 possibile cancellare in questo momento");
            } else
            {
                crudbp.delete(actioncontext);
                crudbp.reset(actioncontext);
                crudbp.setMessage("Cancellazione effettuata");
            }
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doNuovaRicerca(ActionContext actioncontext)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            try
            {
                fillModel(actioncontext);
            }
            catch(FillException _ex) { }
            if(crudbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfermaNuovaRicerca");
            else
                return doConfermaNuovaRicerca(actioncontext, 4);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doNuovo(ActionContext actioncontext)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            try
            {
                fillModel(actioncontext);
            }
            catch(FillException _ex) { }
            if(crudbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfermaNuovo");
            else
                return doConfermaNuovo(actioncontext, 4);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRicercaLibera(ActionContext actioncontext)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            fillModel(actioncontext);
            if(crudbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfermaRicercaLibera");
            else
                return doConfermaRicercaLibera(actioncontext, 4);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRiporta(ActionContext actioncontext)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            fillModel(actioncontext);
            if(!crudbp.isSaveOnBringBack() && (crudbp.isInserting() || !crudbp.isEditOnly() && crudbp.isDirty()))
                return openConfirm(actioncontext, "Vuoi salvare? Se si sceglie no verrano persi i cambiamenti effettuati.", 3, "doConfermaRiporta");
            else
                return doConfermaRiporta(actioncontext, 8);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRiportaSelezione(ActionContext actioncontext)
        throws RemoteException
    {
        try
        {
            HookForward hookforward = (HookForward)actioncontext.getCaller();
            return doRiportaSelezione(actioncontext, (OggettoBulk)hookforward.getParameter("focusedElement"));
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doRiportaSelezione(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws RemoteException
    {
        try
        {
            if(oggettobulk != null)
                getBusinessProcess(actioncontext).edit(actioncontext, oggettobulk);
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSalva(ActionContext actioncontext)
        throws RemoteException
    {
        try
        {
            fillModel(actioncontext);
            getBusinessProcess(actioncontext).save(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(ValidationException validationexception)
        {
            getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
        return actioncontext.findDefaultForward();
    }

    protected CRUDBP getBusinessProcess(ActionContext actioncontext)
    {
        return (CRUDBP)actioncontext.getBusinessProcess();
    }

    protected Forward handleDuplicateKeyException(ActionContext actioncontext, CRUDDuplicateKeyException crudduplicatekeyexception)
    {
        try
        {
            CRUDBP crudbp = getBusinessProcess(actioncontext);
            if(crudbp.isInserting())
            {
                OptionBP optionbp = openConfirm(actioncontext, "Si sta tentando di creare un oggetto gi\340 esistente in archivio; si vuole aprire tale oggetto in modifica?", 2, "doConfirmHandleDuplicateKey");
                optionbp.addAttribute("exception", crudduplicatekeyexception);
                return optionbp;
            } else
            {
                crudbp.setErrorMessage(crudduplicatekeyexception.getMessage());
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

    protected Forward handleException(ActionContext actioncontext, Throwable throwable)
    {
        try
        {
            throw throwable;
        }
        catch(CRUDConstraintException crudconstraintexception)
        {
            getBusinessProcess(actioncontext).setErrorMessage(crudconstraintexception.getUserMessage());
            return actioncontext.findDefaultForward();
        }
        catch(ApplicationPersistencyDiscardedException applicationPersistencyDiscardedException)
        {
            getBusinessProcess(actioncontext).setErrorMessage(applicationPersistencyDiscardedException.getMessage());
            return actioncontext.findDefaultForward();
        }
        catch(CRUDDuplicateKeyException crudduplicatekeyexception)
        {
            return handleDuplicateKeyException(actioncontext, crudduplicatekeyexception);
        }
        catch(CRUDException crudexception)
        {
            getBusinessProcess(actioncontext).setErrorMessage(crudexception.getMessage());
        }
        catch(Throwable throwable1)
        {
            return super.handleException(actioncontext, throwable1);
        }
        return actioncontext.findDefaultForward();
    }

    protected Forward riporta(ActionContext actioncontext, OggettoBulk oggettobulk)
    {
        try
        {
            actioncontext.closeBusinessProcess();
            HookForward hookforward = (HookForward)actioncontext.findForward("bringback");
            if(hookforward == null)
            {
                return actioncontext.findDefaultForward();
            } else
            {
                hookforward.addParameter("bringback", oggettobulk);
                return hookforward;
            }
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
}