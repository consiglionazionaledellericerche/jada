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
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package it.cnr.jada.action:
//            Forward, ActionContext

public class HookForward
        implements Serializable, Forward {

    private Map parameters;
    private Forward forward;
    private String name;

    HookForward(String s, Forward forward1) {
        parameters = new HashMap();
        name = s;
        forward = forward1;
    }

    public void addParameter(String s, Object obj) {
        parameters.put(s, obj);
    }

    public Object getParameter(String s) {
        return parameters.get(s);
    }

    public void perform(ActionContext actioncontext) {
        actioncontext.removeHookForward(name);
        forward.perform(actioncontext);
    }
}