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
//            Forward, ActionContext, Action, ActionMapping

public class ActionForward
        implements Serializable, Forward {

    private Action action;
    private String command;
    private ActionMapping actionMapping;

    public ActionForward(Action action1) {
        action = action1;
    }

    public ActionForward(Action action1, ActionMapping actionmapping) {
        action = action1;
        actionMapping = actionmapping;
    }
    public ActionForward(Action action1, ActionMapping actionmapping, String s) {
        action = action1;
        actionMapping = actionmapping;
        command = s;
    }

    public void perform(ActionContext actioncontext) {
        actioncontext.perform(action, actionMapping, command);
    }
}