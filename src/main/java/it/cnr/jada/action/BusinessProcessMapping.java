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

import it.cnr.jada.util.Introspector;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

// Referenced classes of package it.cnr.jada.action:
//            Config, StaticForward, InitParameter, Forward, 
//            BusinessProcessException, BusinessProcess, ActionContext

public class BusinessProcessMapping
        implements Serializable {

    private String name;
    private String className;
    private Hashtable forwards;
    private Config config;

    public BusinessProcessMapping() {
        forwards = new Hashtable();
        config = new Config();
    }

    public void addForward(StaticForward staticforward) {
        forwards.put(staticforward.getName(), staticforward);
    }

    public void addInitParameter(InitParameter initparameter) {
        config.setInitParameter(initparameter.getName(), initparameter.getValue());
    }

    public Forward findForward(String s) {
        return (Forward) forwards.get(s);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String s) {
        className = s;
    }

    public Config getConfig() {
        return config;
    }

    public BusinessProcess getInstance(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            BusinessProcess businessprocess = (BusinessProcess) Class.forName(className).newInstance();
            businessprocess.initializeUserTransaction(actioncontext);
            businessprocess.setMapping(this, actioncontext);
            return businessprocess;
        } catch (InstantiationException instantiationexception) {
            throw new BusinessProcessException(instantiationexception);
        } catch (ClassNotFoundException _ex) {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": " + className + " not found.");
        } catch (IllegalAccessException _ex) {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": can't access counstructor.");
        }
    }

    public BusinessProcess getInstance(ActionContext actioncontext, Object aobj[])
            throws BusinessProcessException {
        try {
            BusinessProcess businessprocess = (BusinessProcess) Introspector.newInstance(Class.forName(className), aobj);
            businessprocess.initializeUserTransaction(actioncontext);
            businessprocess.setMapping(this, actioncontext);
            return businessprocess;
        } catch (InstantiationException instantiationexception) {
            throw new BusinessProcessException(instantiationexception);
        } catch (InvocationTargetException invocationtargetexception) {
            throw new BusinessProcessException(invocationtargetexception);
        } catch (NoSuchMethodException nosuchmethodexception) {
            throw new BusinessProcessException(nosuchmethodexception);
        } catch (ClassNotFoundException _ex) {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": " + className + " not found.");
        } catch (IllegalAccessException _ex) {
            throw new BusinessProcessException("Error creating BusinessProcess \"" + getName() + "\": can't access counstructor.");
        }
    }

    public Boolean isSubclassOf(Class clazzIn) throws BusinessProcessException {
        try {
            Class clazz = Class.forName(className);
            return clazzIn.isAssignableFrom(clazz);
        } catch (ClassNotFoundException _ex) {
            throw new BusinessProcessException("Error creating Class \"" + className);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }
}