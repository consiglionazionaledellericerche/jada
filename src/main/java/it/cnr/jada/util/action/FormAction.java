/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.OptionRequestException;
import it.cnr.jada.util.Introspector;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.ParseException;

/**
 * Action da usare in combinazione con un FormBP. Gestisce i seguenti comandi:
 * doChiudiForm Chiude il BusinessProccess correne
 * doOption Gestisce un HookForward da un OptionBP
 * doAggiungiPreferiti Richiama il BusinessProccess apposito
 * doTab Gestisce un cambiamento di pagina su un controllo tabbed it.cnr.jada.util.jsp.JSPUtils.tabbed
 */
public class FormAction extends AbstractAction implements Serializable {

    public FormAction() {
    }

    public OptionBP createOptionBP(ActionContext actioncontext, String message, int icon, int type, String action) {
        try {
            OptionBP optionbp = (OptionBP) actioncontext.createBusinessProcess("OptionBP");
            optionbp.setMessage(icon, message);
            optionbp.setType(type);
            HookForward hookforward = actioncontext.addHookForward("option", this, "doOption");
            hookforward.addParameter("bp", optionbp);
            if (action != null)
                hookforward.addParameter("action", action);
            return optionbp;
        } catch (BusinessProcessException businessprocessexception) {
            throw new ActionPerformingError(businessprocessexception);
        }
    }

    /**
     * Gestisce il comando "doCloseForm". Chiude il BusinessProcess corrente e restituisce il default forward.
     */
    public Forward doCloseForm(ActionContext actioncontext) throws BusinessProcessException {
        actioncontext.closeBusinessProcess();
        HookForward hookforward = (HookForward) actioncontext.findForward("close");
        if (hookforward != null)
            return hookforward;
        else
            return actioncontext.findDefaultForward();
    }

    /**
     * Gestisce il comando "doAggiungiPreferiti".
     */
    public Forward doAggiungiPreferiti(ActionContext actioncontext) throws BusinessProcessException {
        String descrizione = null;
        BusinessProcess bp = actioncontext.getBusinessProcess();
        if (bp instanceof BulkBP) {
            BulkBP bulkBP = (BulkBP) bp;
            descrizione = bulkBP.getBulkInfo().getLongDescription();
        }
        BusinessProcess bpPreferiti = actioncontext.createBusinessProcess("CRUDAggiungiPreferitiBP", new Object[]{bp.getName(), bp.getFunction() == null ? 'C' : bp.getFunction(), descrizione});
        return actioncontext.addBusinessProcess(bpPreferiti);
    }

    /**
     * Gestisce un HookForward al ritorno da un OptionBP
     */
    public final Forward doOption(ActionContext actioncontext) throws RemoteException {
        try {
            HookForward hookforward = (HookForward) actioncontext.getCaller();
            String s = (String) hookforward.getParameter("action");
            if (s == null)
                return doDefault(actioncontext);
            try {
                return (Forward) Introspector.invoke(this, s, actioncontext, hookforward.getParameter("bp"));
            } catch (NoSuchMethodException _ex) {
                return (Forward) Introspector.invoke(this, s, actioncontext, hookforward.getParameter("option"));
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    /**
     * Gestisce un cambiamento di pagina su un controllo tabbed it.cnr.jada.util.jsp.JSPUtils.tabbed
     */
    public Forward doTab(ActionContext actioncontext, String tabName, String pageName) throws RemoteException {
        try {
            FormBP formbp = (FormBP) actioncontext.getBusinessProcess();
            formbp.setTab(tabName, pageName);
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    /**
     * Description copied from class:
     * Gestisce in maniera standard le eccezioni generate da un perform di una action.
     * La gestione standard prevede: Se l'eccezione  una RemoteException ne viene estratto il detail e
     * passato reinvocato ricorsivamente allo stesso metodo.
     * Se l'eccezione  org.omg.CORBA.COMM_FAILURE viene cercato e restituito un forward a "serviceUnavailable";
     * Per ogni altra eccezione viene impostato viene generata un ActionPerformingError che ha
     * come detail l'eccezione originale.
     */
    protected Forward handleException(ActionContext actioncontext, Throwable throwable) {
        try {
            throw throwable;
        } catch (OptionRequestException optionrequestexception) {
            try {
                return openConfirm(actioncontext, optionrequestexception.getMessage(), optionrequestexception.getType(), Introspector.buildMetodName("do", optionrequestexception.getName()));
            } catch (BusinessProcessException businessprocessexception) {
                return handleException(actioncontext, businessprocessexception);
            }
        } catch (ApplicationException | ApplicationRuntimeException applicationexception) {
            setErrorMessage(actioncontext, applicationexception.getMessage());
            if (applicationexception.getDetail() != null)
                actioncontext.traceException(applicationexception.getDetail());
            return actioncontext.findDefaultForward();
        } catch (ComponentException componentexception) {
            if (componentexception.getDetail() == null)
                throw new ActionPerformingError(componentexception);
            else
                return handleException(actioncontext, componentexception.getDetail());
        } catch (MessageToUser messagetouser) {
            setMessage(actioncontext, messagetouser.getStatus(), messagetouser.getMessage());
            return actioncontext.findDefaultForward();
        } catch (ParseException parseexception) {
            setErrorMessage(actioncontext, parseexception.getMessage());
        } catch (Throwable throwable1) {
            return super.handleException(actioncontext, throwable1);
        }
        return actioncontext.findDefaultForward();
    }

    /**
     * Crea un OptionBP con i parametri specificati
     */
    public OptionBP openConfirm(ActionContext actioncontext, String message, int type, String action) throws BusinessProcessException {
        OptionBP optionbp = createOptionBP(actioncontext, message, FormBP.QUESTION_MESSAGE, type, action);
        return (OptionBP) actioncontext.addBusinessProcess(optionbp);
    }

    /**
     * Crea un OptionBP con i parametri specificati
     */
    public OptionBP openConfirm(ActionContext actioncontext, String message, String action) throws BusinessProcessException {
        return openConfirm(actioncontext, message, OptionBP.OK_BUTTON, action);
    }

    public OptionBP openContinuePrompt(ActionContext actioncontext, String action) throws BusinessProcessException {
        return openConfirm(actioncontext, "L'operazione richiesta comporta la perdita delle modifiche non salvate. Vuoi continuare?", 2, action);
    }

    public Forward openMessage(ActionContext actioncontext, String action) throws BusinessProcessException {
        return actioncontext.addBusinessProcess(createOptionBP(actioncontext, action, FormBP.WARNING_MESSAGE, 0, null));
    }

    public OptionBP openSavePrompt(ActionContext actioncontext, String action) throws BusinessProcessException {
        return openConfirm(actioncontext, "Vuoi tornare indietro e salvare?", FormBP.WARNING_MESSAGE, action);
    }

    protected void setErrorMessage(ActionContext actioncontext, String message) {
        ((FormBP) actioncontext.getBusinessProcess()).setErrorMessage(message);
    }

    protected void setMessage(ActionContext actioncontext, int status, String message) {
        ((FormBP) actioncontext.getBusinessProcess()).setMessage(status, message);
    }
}