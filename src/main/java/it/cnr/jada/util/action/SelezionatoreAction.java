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
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            BulkAction, AbstractSelezionatoreBP, SelezionatoreListaBP, BulkListPrintBP

public class SelezionatoreAction extends BulkAction
        implements Serializable {

    public SelezionatoreAction() {
    }

    public Forward basicDoBringBack(ActionContext actioncontext)
            throws BusinessProcessException {
        AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP) actioncontext.getBusinessProcess();
        HookForward hookforward = (HookForward) actioncontext.findForward("seleziona");
        hookforward.addParameter("focusedElement", abstractselezionatorebp.getFocusedElement(actioncontext));
        actioncontext.closeBusinessProcess();
        return hookforward;
    }

    public Forward doBringBack(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP) actioncontext.getBusinessProcess();
            abstractselezionatorebp.setSelection(actioncontext);
            return basicDoBringBack(actioncontext);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doCloseForm(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            Forward forward = actioncontext.findForward("annulla_seleziona");
            if (forward == null)
                forward = actioncontext.findForward("seleziona");
            actioncontext.closeBusinessProcess();
            return forward;
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doPrint(ActionContext actioncontext) {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.getBusinessProcess();
            BulkListPrintBP bulklistprintbp = (BulkListPrintBP) actioncontext.createBusinessProcess(selezionatorelistabp.getPrintbp());
            bulklistprintbp.setColumns(selezionatorelistabp.getColumns());
            bulklistprintbp.setIterator(actioncontext, selezionatorelistabp.getIterator());
            bulklistprintbp.setTitle(selezionatorelistabp.getBulkInfo().getLongDescription());
            return actioncontext.addBusinessProcess(bulklistprintbp);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (RemoteException remoteexception) {
            return handleException(actioncontext, remoteexception);
        }
    }

    public Forward doSelection(ActionContext actioncontext, String s)
            throws BusinessProcessException {
        try {
            if (!s.startsWith("mainTable")) {
                return super.doSelection(actioncontext, s);
            } else {
                AbstractSelezionatoreBP abstractselezionatorebp = (AbstractSelezionatoreBP) actioncontext.getBusinessProcess();
                abstractselezionatorebp.setSelection(actioncontext);
                return doBringBack(actioncontext);
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }
}