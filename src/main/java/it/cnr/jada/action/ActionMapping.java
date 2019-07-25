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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;

// Referenced classes of package it.cnr.jada.action:
//            Config, StaticForward, InitParameter, BusinessProcessException, 
//            ActionMappings, Action, Forward, ActionContext, 
//            BusinessProcess

public class ActionMapping
        implements Serializable {

    private String path;
    private Class actionClass;
    private Class formClass;
    private Hashtable forwards;
    private Config config;
    private ActionMappings mappings;
    private Action instance;
    private boolean needExistingSession;
    private boolean createMultipleInstances;

    public ActionMapping() {
        forwards = new Hashtable();
        config = new Config();
        needExistingSession = true;
    }

    public void addForward(StaticForward staticforward) {
        forwards.put(staticforward.getName(), staticforward);
    }

    public void addInitParameter(InitParameter initparameter) {
        config.setInitParameter(initparameter.getName(), initparameter.getValue());
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext)
            throws BusinessProcessException {
        return mappings.createBusinessProcess(s, actioncontext);
    }

    public BusinessProcess createBusinessProcess(String s, ActionContext actioncontext, Object aobj[])
            throws BusinessProcessException {
        return mappings.createBusinessProcess(s, actioncontext, aobj);
    }

    public Action createNewInstance()
            throws InstantiationException {
        try {
            instance = (Action) actionClass.newInstance();
            instance.init(config);
            return instance;
        } catch (IllegalAccessException illegalaccessexception) {
            throw new InstantiationException(illegalaccessexception.getMessage());
        }
    }

    public Forward findActionForward(String s) {
        return mappings.findActionForward(s);
    }

    public Forward findDefaultForward() {
        return findForward("default");
    }

    public Forward findForward(String s) {
        Forward forward = (Forward) forwards.get(s);
        if (forward != null)
            return forward;
        else
            return mappings.findForward(s);
    }

    public Action getActionInstance()
            throws InstantiationException {
        if (createMultipleInstances && instance == null)
            return instance = createNewInstance();
        else
            return createNewInstance();
    }

    public ActionMappings getMappings() {
        return mappings;
    }

    public void setMappings(ActionMappings actionmappings) {
        mappings = actionmappings;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String s) {
        path = s;
    }

    public boolean isCreateMultipleInstances() {
        return createMultipleInstances;
    }

    public void setCreateMultipleInstances(boolean flag) {
        createMultipleInstances = flag;
    }

    public boolean needExistingSession() {
        return needExistingSession;
    }

    public void setActionClass(String s) {
        try {
            actionClass = getClass().getClassLoader().loadClass(s);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setFormClass(String s) {
        try {
            formClass = Class.forName(s);
            createMultipleInstances = testClassForMultipleInstances(formClass);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setNeedExistingSession(String s) {
        needExistingSession = "true".equals(s);
    }

    private boolean testClassForMultipleInstances(Class class1) {
        for (; class1 != null; class1 = class1.getSuperclass()) {
            Field[] afield = class1.getDeclaredFields();
            for (int i = 0; i < afield.length; i++) {
                if (!Modifier.isStatic(afield[i].getModifiers()))
                    return true;
            }
        }

        return false;
    }
}