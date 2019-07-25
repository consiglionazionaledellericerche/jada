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

package it.cnr.jada.excel.action;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.ejb.BframeExcelComponentSession;
import it.cnr.jada.util.action.FormAction;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.ejb.EJBException;
import java.rmi.RemoteException;

public class CancellaSchedulazioneExcelAction extends FormAction {
    @Override
    @SuppressWarnings("unused")
    public Forward doDefault(ActionContext actioncontext) throws RemoteException {
        if (((HttpActionContext) actioncontext).getParameter("pg") == null)
            return super.doDefault(actioncontext);
        Long pg = new Long(((HttpActionContext) actioncontext).getParameter("pg").substring(2));
        String indirizzoEMail = ((HttpActionContext) actioncontext).getParameter("indirizzoEMail");
        BusinessProcess bp = actioncontext.getBusinessProcess();
        bp.setResource("pg", String.valueOf(pg));
        bp.setResource("indirizzoEMail", indirizzoEMail);
        UserContext userContext = AdminUserContext.getInstance(actioncontext.getSessionId());
        try {
            Excel_spoolerBulk excelSpooler = getComponent(actioncontext).findExcelSpooler(userContext, pg);
            if (excelSpooler == null) {
                openMessage(actioncontext, "La lista di distribuzione della consultazione, e' stata eliminata!");
                return super.doDefault(actioncontext);
            }
            String msg = "Si conferma la cancellazione dell'indirizzo " + indirizzoEMail + "<BR>dalla lista di distribuzione della consultazione \"" + excelSpooler.getNome_file() + "\"?";
            OptionBP option = openConfirm(actioncontext, msg, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmCancellaSchedulazione");
            return option;
        } catch (ComponentException e) {
            handleException(actioncontext, e);
        } catch (BusinessProcessException e) {
            handleException(actioncontext, e);
        }
        return super.doDefault(actioncontext);
    }

    public Forward doConfirmCancellaSchedulazione(ActionContext actioncontext, it.cnr.jada.util.action.OptionBP option) {
        UserContext userContext = AdminUserContext.getInstance(actioncontext.getSessionId());
        BusinessProcess bp = actioncontext.getBusinessProcess();
        if (option.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
            try {
                getComponent(actioncontext).cancellaSchedulazione(userContext, new Long(bp.getResource("pg")), bp.getResource("indirizzoEMail"));
                openMessage(actioncontext, "La cancellazione e' stata effettuata.");
            } catch (Exception e) {
                handleException(actioncontext, e);
            }
        }
        return actioncontext.findDefaultForward();
    }

    private BframeExcelComponentSession getComponent(ActionContext actioncontext) throws EJBException, RemoteException {
        return (BframeExcelComponentSession) EJBCommonServices.createEJB("BFRAMEEXCEL_EJB_BframeExcelComponentSession", BframeExcelComponentSession.class);
    }
}
