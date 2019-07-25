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

import java.io.Serializable;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.util.action:
//            OptionBP

public class OptionAction extends AbstractAction
        implements Serializable {

    public OptionAction() {
    }

    public Forward doCancel(ActionContext actioncontext)
            throws RemoteException, BusinessProcessException {
        return doOption(actioncontext, 2);
    }

    public Forward doClose(ActionContext actioncontext)
            throws RemoteException, BusinessProcessException {
        return doOption(actioncontext, 16);
    }

    public Forward doNo(ActionContext actioncontext)
            throws RemoteException, BusinessProcessException {
        return doOption(actioncontext, 8);
    }

    public Forward doOk(ActionContext actioncontext)
            throws RemoteException, BusinessProcessException {
        return doOption(actioncontext, 1);
    }

    public Forward doOption(ActionContext actioncontext, int i)
            throws RemoteException, BusinessProcessException {
        ((OptionBP) actioncontext.getBusinessProcess()).setOption(i);
        actioncontext.closeBusinessProcess();
        HookForward hookforward = (HookForward) actioncontext.findForward("option");
        hookforward.addParameter("option", new Integer(i));
        return hookforward;
    }

    public Forward doYes(ActionContext actioncontext)
            throws RemoteException, BusinessProcessException {
        return doOption(actioncontext, 4);
    }
}