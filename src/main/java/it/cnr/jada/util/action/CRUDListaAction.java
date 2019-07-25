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

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            SelezionatoreListaAction, CRUDListaBP, RicercaLiberaBP, SelezionatoreListaBP, 
//            FormBP, FormAction

public class CRUDListaAction extends SelezionatoreListaAction
        implements Serializable {

    public CRUDListaAction() {
    }

    public Forward doConfermaRicercaLibera(ActionContext actioncontext, int i) {
        try {
            CRUDListaBP crudlistabp = (CRUDListaBP) actioncontext.getBusinessProcess();
            if (i == 8) {
                return actioncontext.findDefaultForward();
            } else {
                RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP) actioncontext.createBusinessProcess("RicercaLibera");
                ricercaliberabp.setPrototype(crudlistabp.createEmptyModelForFreeSearch(actioncontext));
                ricercaliberabp.setCanPerformSearchWithoutClauses(true);
                actioncontext.addHookForward("filter", this, "doRiportaRicerca");
                return actioncontext.addBusinessProcess(ricercaliberabp);
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doElimina(ActionContext actioncontext)
            throws RemoteException {
        try {
            CRUDListaBP crudlistabp = (CRUDListaBP) actioncontext.getBusinessProcess();
            crudlistabp.setSelection(actioncontext);
            crudlistabp.delete(actioncontext);
            crudlistabp.refresh(actioncontext);
            crudlistabp.setMessage("Cancellazione effettuata");
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRicercaLibera(ActionContext actioncontext) {
        try {
            CRUDListaBP crudlistabp = (CRUDListaBP) actioncontext.getBusinessProcess();
            crudlistabp.fillModels(actioncontext);
            if (crudlistabp.isDirty())
                return openContinuePrompt(actioncontext, "doConfermaRicercaLibera");
            else
                return doConfermaRicercaLibera(actioncontext, 4);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRiportaRicerca(ActionContext actioncontext) {
        try {
            CRUDListaBP crudlistabp = (CRUDListaBP) actioncontext.getBusinessProcess();
            HookForward hookforward = (HookForward) actioncontext.getCaller();
            crudlistabp.setIterator(actioncontext, crudlistabp.find(actioncontext, (CompoundFindClause) hookforward.getParameter("filter"), crudlistabp.createEmptyModelForSearch(actioncontext)));
            return actioncontext.findDefaultForward();
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSalva(ActionContext actioncontext)
            throws RemoteException {
        CRUDListaBP crudlistabp = (CRUDListaBP) actioncontext.getBusinessProcess();
        try {
            crudlistabp.fillModels(actioncontext);
            crudlistabp.save(actioncontext);
            crudlistabp.refetchPage(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (ValidationException validationexception) {
            crudlistabp.setErrorMessage(validationexception.getMessage());
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
        return actioncontext.findDefaultForward();
    }

    public Forward doSelection(ActionContext actioncontext, String s) {
        try {
            CRUDListaBP crudlistabp = (CRUDListaBP) actioncontext.getBusinessProcess();
            crudlistabp.fillModels(actioncontext);
            crudlistabp.setSelection(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (FillException fillexception) {
            return handleException(actioncontext, fillexception);
        }
    }
}