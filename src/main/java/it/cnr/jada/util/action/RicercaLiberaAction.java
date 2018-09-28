package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.ValueTooLargeException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.util.action:
//            FormAction, RicercaLiberaBP, CondizioneRicercaBulk, FormBP, 
//            CondizioneComplessaBulk, SearchProvider, SelezionatoreListaBP, CondizioneSempliceBulk

public class RicercaLiberaAction extends FormAction
    implements Serializable
{

    public RicercaLiberaAction()
    {
    }

    public Forward doApriParentesi(ActionContext actioncontext)
    {
        try
        {
            salvaCondizione(actioncontext);
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            ricercaliberabp.apriParentesi();
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doCancellaCondizione(ActionContext actioncontext)
    {
        try
        {
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            ricercaliberabp.cancellaCondizioneCorrente(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doCancellaCondizioni(ActionContext actioncontext)
    {
        try
        {
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            ricercaliberabp.cancellaCondizioni();
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doChiudiParentesi(ActionContext actioncontext)
    {
        try
        {
            salvaCondizione(actioncontext);
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            if(ricercaliberabp.getCondizioneCorrente().getParent() == null || ricercaliberabp.getCondizioneCorrente().getParent().getParent() == null)
            {
                return actioncontext.findDefaultForward();
            } else
            {
                salvaCondizione(actioncontext);
                ricercaliberabp.chiudiParentesi();
                return actioncontext.findDefaultForward();
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doCloseForm(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            actioncontext.closeBusinessProcess();
            HookForward hookforward = (HookForward)actioncontext.findForward("close");
            if(hookforward != null)
                return hookforward;
            return actioncontext.findForward("seleziona");
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doImpostaAttributo(ActionContext actioncontext)
    {
        try
        {
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            ricercaliberabp.getCondizioneCorrente().fillFromActionContext(actioncontext, null, 4, ricercaliberabp.getFieldValidationMap());
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doIniziaRicerca(ActionContext actioncontext)
    {
        try
        {
            salvaCondizione(actioncontext);
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            if(ricercaliberabp.isNuovaCondizione())
                ricercaliberabp.cancellaCondizioneCorrente(actioncontext);
            if(!ricercaliberabp.isCanPerformSearchWithoutClauses() && ricercaliberabp.getCondizioneRadice().contaCondizioneSemplici() == 0)
            {
                ricercaliberabp.setMessage("E' necessario aggiungere almeno una condizione.");
                return actioncontext.findDefaultForward();
            }
            if(ricercaliberabp.getSearchProvider() == null)
            {
                actioncontext.closeBusinessProcess();
                HookForward hookforward = (HookForward)actioncontext.findForward("filter");
                hookforward.addParameter("filter", (CompoundFindClause)ricercaliberabp.getCondizioneRadice().creaFindClause());
                return hookforward;
            }
            RemoteIterator remoteiterator = EJBCommonServices.openRemoteIterator(actioncontext, ricercaliberabp.getSearchProvider().search(actioncontext, (CompoundFindClause)ricercaliberabp.getCondizioneRadice().creaFindClause(), ricercaliberabp.getPrototype()));
            if(remoteiterator == null || remoteiterator.countElements() == 0)
            {
                ricercaliberabp.setMessage("La ricerca non ha fornito alcun risultato.");
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                return actioncontext.findDefaultForward();
            }
            if(ricercaliberabp.isShowSearchResult())
            {
                SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
                selezionatorelistabp.setMultiSelection(ricercaliberabp.isMultiSelection());
                selezionatorelistabp.setIterator(actioncontext, remoteiterator);
                selezionatorelistabp.setBulkInfo(ricercaliberabp.getPrototype().getBulkInfo());
                selezionatorelistabp.setColumns(ricercaliberabp.getPrototype().getBulkInfo().getColumnFieldPropertyDictionary(ricercaliberabp.getColumnSet()));
                if(actioncontext.findForward("seleziona") != null)
                    actioncontext.addHookForward("seleziona", this, "doSelezionaElemento");
                else
                    selezionatorelistabp.disableSelection();
                return actioncontext.addBusinessProcess(selezionatorelistabp);
            } else
            {
                actioncontext.closeBusinessProcess();
                HookForward hookforward1 = (HookForward)actioncontext.findForward("searchResult");
                hookforward1.addParameter("remoteIterator", remoteiterator);
                hookforward1.addParameter("condizioneRadice", ricercaliberabp.getCondizioneRadice());
                return hookforward1;
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doNuovaCondizione(ActionContext actioncontext)
    {
        try
        {
            salvaCondizione(actioncontext);
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            ricercaliberabp.nuovaCondizione();
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSalvaCondizione(ActionContext actioncontext)
    {
        try
        {
            salvaCondizione(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSelezionaCondizione(ActionContext actioncontext)
    {
        try
        {
            salvaCondizione(actioncontext);
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
            ricercaliberabp.setRigaSelezionata(actioncontext, rigaSelezionata);
            return actioncontext.findDefaultForward();
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSelezionaElemento(ActionContext actioncontext)
    {
        try
        {
            Object obj = ((HookForward)actioncontext.getCaller()).getParameter("focusedElement");
            Object obj1 = ((HookForward)actioncontext.getCaller()).getParameter("selectedElements");
            if(obj == null && obj1 == null)
            {
                return actioncontext.findDefaultForward();
            } else
            {
                actioncontext.closeBusinessProcess();
                HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
                hookforward.addParameter("focusedElement", obj);
                hookforward.addParameter("selectedElements", obj1);
                return hookforward;
            }
        }
        catch(Exception exception)
        {
            return handleException(actioncontext, exception);
        }
    }

    public int getRigaSelezionata()
    {
        return rigaSelezionata;
    }

    public Forward handleException(ActionContext actioncontext, Throwable throwable)
    {
        try
        {
            throw throwable;
        }
        catch(FillException fillexception)
        {
            setErrorMessage(actioncontext, fillexception.getMessage());
            return actioncontext.findDefaultForward();
        }
        catch(ValidationException validationexception)
        {
            ((FormBP)actioncontext.getBusinessProcess()).setErrorMessage(validationexception.getMessage());
            return actioncontext.findDefaultForward();
        }
        catch(ValueTooLargeException _ex)
        {
            ((FormBP)actioncontext.getBusinessProcess()).setErrorMessage("Un valore specificato in qualche clausola \350 troppo grande.");
        }
        catch(Throwable throwable1)
        {
            return super.handleException(actioncontext, throwable1);
        }
        return actioncontext.findDefaultForward();
    }

    private void salvaCondizione(ActionContext actioncontext)
        throws FillException, ValidationException
    {
        RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP)actioncontext.getBusinessProcess();
        if(ricercaliberabp.isNuovaCondizione() && ((CondizioneSempliceBulk)ricercaliberabp.getCondizioneCorrente()).getFindFieldProperty() == null)
            return;
        if((ricercaliberabp.getCondizioneCorrente() instanceof CondizioneSempliceBulk) && ((CondizioneSempliceBulk)ricercaliberabp.getCondizioneCorrente()).getFindFieldProperty() == null)
            throw new ValidationException("E' necessario specificare una condizione");
        CondizioneRicercaBulk condizionericercabulk = ricercaliberabp.getCondizioneCorrente();
        condizionericercabulk.fillFromActionContext(actioncontext, null, 2, ricercaliberabp.getFieldValidationMap());
        if(condizionericercabulk instanceof CondizioneSempliceBulk)
        {
            Object obj = ((CondizioneSempliceBulk)condizionericercabulk).getFindFieldProperty().getValueFromActionContext(ricercaliberabp.getPrototype(), actioncontext, null, ricercaliberabp.getStatus());
            if(obj != FieldProperty.UNDEFINED_VALUE)
                ((CondizioneSempliceBulk)condizionericercabulk).setValue(obj);
        }
        condizionericercabulk.validate();
        ricercaliberabp.setNuovaCondizione(false);
    }

    public void setRigaSelezionata(int i)
    {
        rigaSelezionata = i;
    }

    private int rigaSelezionata;
}