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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            SelezionatoreListaAction, SelezionatoreListaAlberoBP, AbstractSelezionatoreBP, Selection, 
//            SelezionatoreListaBP

public class SelezionatoreListaAlberoAction extends SelezionatoreListaAction
        implements Serializable {

    public SelezionatoreListaAlberoAction() {
    }

    public Forward doBack(ActionContext actioncontext) {
        return doGoToLevel(actioncontext, null);
    }

    public Forward doExpand(ActionContext actioncontext) {
        try {
            SelezionatoreListaAlberoBP selezionatorelistaalberobp = (SelezionatoreListaAlberoBP) actioncontext.getBusinessProcess();
            OggettoBulk oggettobulk = (OggettoBulk) selezionatorelistaalberobp.getFocusedElement(actioncontext);
            selezionatorelistaalberobp.getHistory().push(selezionatorelistaalberobp.getParentElement());
            selezionatorelistaalberobp.setParentElement(oggettobulk);
            selezionatorelistaalberobp.getSelection().clear();
            selezionatorelistaalberobp.setLeafElement(false);
            RemoteIterator ri = EJBCommonServices.openRemoteIterator(actioncontext, selezionatorelistaalberobp.getChildren(actioncontext, oggettobulk));
            selezionatorelistaalberobp.setIterator(actioncontext, ri);
            return actioncontext.findDefaultForward();
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (RemoteException remoteexception) {
            return handleException(actioncontext, remoteexception);
        }
    }

    public Forward doGoToLevel(ActionContext actioncontext, String s) {
        try {
            SelezionatoreListaAlberoBP selezionatorelistaalberobp = (SelezionatoreListaAlberoBP) actioncontext.getBusinessProcess();
            if (s != null) {
                for (int i = Integer.parseInt(s) + 1; selezionatorelistaalberobp.getHistory().size() > i; selezionatorelistaalberobp.getHistory().pop())
                    ;
            }
            if (!selezionatorelistaalberobp.getHistory().isEmpty()) {
                selezionatorelistaalberobp.setParentElement((OggettoBulk) selezionatorelistaalberobp.getHistory().pop());
                selezionatorelistaalberobp.getSelection().clear();
                selezionatorelistaalberobp.setLeafElement(false);
                RemoteIterator ri = EJBCommonServices.openRemoteIterator(actioncontext, selezionatorelistaalberobp.getChildren(actioncontext, selezionatorelistaalberobp.getParentElement()));
                selezionatorelistaalberobp.setIterator(actioncontext, ri);
            }
            return actioncontext.findDefaultForward();
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (RemoteException remoteexception) {
            return handleException(actioncontext, remoteexception);
        }
    }

    public Forward doSelection(ActionContext actioncontext, String s)
            throws BusinessProcessException {
        SelezionatoreListaAlberoBP selezionatorelistaalberobp = (SelezionatoreListaAlberoBP) actioncontext.getBusinessProcess();
        selezionatorelistaalberobp.setFocus(actioncontext);
        selezionatorelistaalberobp.setSelection(actioncontext);
        OggettoBulk oggettobulk = (OggettoBulk) selezionatorelistaalberobp.getFocusedElement(actioncontext);
        selezionatorelistaalberobp.setLeafElement(selezionatorelistaalberobp.isLeaf(actioncontext, oggettobulk));
        return actioncontext.findDefaultForward();
    }
}