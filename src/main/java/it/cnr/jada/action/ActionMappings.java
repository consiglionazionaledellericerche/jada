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
import java.util.Hashtable;

public class ActionMappings implements Serializable {

    private static final long serialVersionUID = 1L;

    private Hashtable<String, ActionMapping> actions;
    private Hashtable<String, BusinessProcessMapping> businessProcesses;
    private Hashtable<String, Forward> forwards;

    public ActionMappings() {
        actions = new Hashtable<String, ActionMapping>();
        businessProcesses = new Hashtable<String, BusinessProcessMapping>();
        forwards = new Hashtable<String, Forward>();
    }

    public void addActionMapping(ActionMapping actionmapping) {
        actionmapping.setMappings(this);
        actions.put(actionmapping.getPath(), actionmapping);
    }

    public void addBusinessProcessMapping(BusinessProcessMapping businessprocessmapping) {
        businessProcesses.put(businessprocessmapping.getName(), businessprocessmapping);
    }

    public void addForward(StaticForward staticforward) {
        forwards.put(staticforward.getName(), staticforward);
    }


    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext)
            throws BusinessProcessException {
        BusinessProcessMapping businessprocessmapping = businessProcesses.get(s);
        if (businessprocessmapping == null)
            return null;
        else
            return businessprocessmapping.getInstance(actioncontext);
    }

    public Boolean isSubclassOf(String s, Class clazz) throws BusinessProcessException {
        if (businessProcesses != null) {
            BusinessProcessMapping businessprocessmapping = businessProcesses.get(s);
            if (businessprocessmapping == null)
                return false;
            else
                return businessprocessmapping.isSubclassOf(clazz);
        } else {
            return false;
        }
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext, Object aobj[])
            throws BusinessProcessException {
        BusinessProcessMapping businessprocessmapping = businessProcesses.get(s);
        if (businessprocessmapping == null)
            return null;
        else
            return businessprocessmapping.getInstance(actioncontext, aobj);
    }

    public Forward findActionForward(String s) {
        ActionMapping actionmapping = actions.get(s);
        if (actionmapping == null)
            return null;
        try {
            return new ActionForward(actionmapping.getActionInstance(), actionmapping);
        } catch (InstantiationException _ex) {
            return null;
        }
    }

    public ActionMapping findActionMapping(String s) {
        return actions.get(s);
    }

    public Forward findForward(String s) {
        return forwards.get(s);
    }

    public BusinessProcess createBusinessProcess(ActionMapping actionmapping, ActionContext actioncontext) throws BusinessProcessException {
        String bpName = getBusinessProcessName(actionmapping, actioncontext);
        return createBusinessProcess(bpName, actioncontext);
    }

    public String getBusinessProcessName(ActionMapping actionmapping, ActionContext actioncontext) throws BusinessProcessException {
        String bpName = null;
        for (String key : businessProcesses.keySet()) {
            BusinessProcessMapping businessProcessMapping = businessProcesses.get(key);
            if (businessProcessMapping.getConfig() != null && actionmapping.getPath().equals("/" + businessProcessMapping.getConfig().getInitParameter("defaultAction"))) {
                bpName = key;
                break;
            }
        }
        return bpName;
    }

    public Hashtable<String, ActionMapping> getActions() {
        return actions;
    }

}