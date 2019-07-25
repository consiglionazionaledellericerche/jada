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

package it.cnr.jada.action;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.action:
//            AbstractAction, ActionContext, BusinessProcess, BusinessProcessException, 
//            Forward

public class StartBusinessProcessAction extends AbstractAction
        implements Serializable {

    private String businessProcessName;

    public StartBusinessProcessAction() {
    }

    public Forward doDefault(ActionContext actioncontext) {
        try {
            BusinessProcess businessprocess = actioncontext.createBusinessProcess(businessProcessName);
            actioncontext.getBusinessProcess().closeAllChildren();
            return actioncontext.addBusinessProcess(businessprocess);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        }
    }

    public String getBusinessProcessName() {
        return businessProcessName;
    }

    public void setBusinessProcessName(String s) {
        businessProcessName = s;
    }
}