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

package it.cnr.jada.persistency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package it.cnr.jada.persistency:
//            Introspector, IntrospectionException, PersistentInfo, KeyedPersistent

public abstract class AbstractIntrospector
        implements Serializable, Introspector {

    public static final String INTROSPECTOR = "INTROSPECTOR";
    private static final Logger logger = LoggerFactory.getLogger(AbstractIntrospector.class);
    private final Map persistentInfos = new HashMap();
    private final Constructor persistentInfoConstructor;

    protected AbstractIntrospector(Class class1)
            throws IntrospectionException {
        try {
            persistentInfoConstructor = class1.getConstructor(Class.class, Introspector.class);
        } catch (NoSuchMethodException _ex) {
            logger.error(INTROSPECTOR, _ex);
            throw new IntrospectionException("Impossibile creare un'istanza di " + class1 + ",manca un costruttore adeguato");
        }
    }

    protected void cachePersistentInfo(Class class1, PersistentInfo persistentinfo)
            throws IntrospectionException {
        persistentInfos.put(class1, persistentinfo);
    }

    protected PersistentInfo getCachedPersistentInfo(Class class1)
            throws IntrospectionException {
        return (PersistentInfo) persistentInfos.get(class1);
    }

    public PersistentInfo getPersistentInfo(Class class1)
            throws IntrospectionException {

        PersistentInfo persistentinfo = getCachedPersistentInfo(class1);
        if (persistentinfo == null && it.cnr.jada.persistency.Persistent.class.isAssignableFrom(class1))
            synchronized (this) {
                persistentinfo = getCachedPersistentInfo(class1);
                if (persistentinfo == null)
                    try {
                        persistentinfo = (PersistentInfo) persistentInfoConstructor.newInstance(new Object[]{
                                class1, this
                        });
                        cachePersistentInfo(class1, persistentinfo);
                        persistentinfo.initialize();
                    } catch (InstantiationException instantiationexception) {
                        logger.error(INTROSPECTOR, instantiationexception);
                        throw new IntrospectionException("Impossibile creare un'istanza di " + persistentInfoConstructor.getDeclaringClass(), instantiationexception);
                    } catch (IllegalAccessException illegalaccessexception) {
                        logger.error(INTROSPECTOR, illegalaccessexception);
                        throw new IntrospectionException("IllegalAccessException: impossibile creare un'istanza di " + persistentInfoConstructor.getDeclaringClass(), illegalaccessexception);
                    } catch (InvocationTargetException invocationtargetexception) {
                        logger.error(INTROSPECTOR, invocationtargetexception);
                        throw new IntrospectionException("Impossibile creare un'istanza di " + persistentInfoConstructor.getDeclaringClass(), invocationtargetexception.getTargetException());
                    } catch (ClassCastException _ex) {
                        logger.error(INTROSPECTOR, _ex);
                        throw new IntrospectionException(persistentInfoConstructor.getDeclaringClass() + "non implementa PersistentInfo");
                    }
            }
        return persistentinfo;
    }

    public synchronized void resetPersistentInfos() {
        persistentInfos.clear();
    }

    public synchronized void resetPersistentInfos(Class class1) {
        persistentInfos.remove(class1);
    }

    public abstract String getOid(KeyedPersistent keyedpersistent)
            throws IntrospectionException;

    public abstract Class getPropertyType(Class class1, String s)
            throws IntrospectionException;

    public abstract Object getPropertyValue(Object obj, String s)
            throws IntrospectionException;

    public abstract void setPropertyValue(Object obj, String s, Object obj1)
            throws IntrospectionException;
}