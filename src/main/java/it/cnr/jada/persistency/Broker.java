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

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Optional;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistentCache, FetchAllPolicy, PersistencyException, FetchException, 
//            Introspector, PersistentInfo, PersistentProperty, FetchListener, 
//            FetchPolicy, IntrospectionError, PersistentPropertyNotAvailableException, IntrospectionException, 
//            KeyedPersistent, Keyed, Prefix, Persistent

public abstract class Broker
        implements Serializable {

    private PersistentCache cache;
    private Introspector introspector;
    private FetchPolicy fetchPolicy;

    public Broker(Introspector introspector1) {
        this(introspector1, new PersistentCache());
    }

    public Broker(Introspector introspector1, PersistentCache persistentcache) {
        this(introspector1, persistentcache, FetchAllPolicy.FETCHALL);
    }

    public Broker(Introspector introspector1, PersistentCache persistentcache, FetchPolicy fetchpolicy) {
        cache = new PersistentCache();
        fetchPolicy = FetchAllPolicy.FETCHALL;
        introspector = introspector1;
        cache = persistentcache;
        fetchPolicy = fetchpolicy;
    }

    public void close()
            throws PersistencyException {
    }

    public void fetch(Persistent persistent, String s)
            throws FetchException {
        try {
            for (Iterator iterator = introspector.getPersistentInfo(persistent.getClass()).getNotInOidPersistentProperties().values().iterator(); iterator.hasNext(); )
                try {
                    fetchProperty(persistent, ((PersistentProperty) iterator.next()).getName(), s);
                } catch (PersistentPropertyNotAvailableException _ex) {
                }

            if (persistent instanceof FetchListener)
                ((FetchListener) persistent).fetchedFrom(this);
            cache.removeFromFetchQueue(introspector, persistent, fetchPolicy.addPrefix(s));
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public Persistent fetch(Class class1)
            throws FetchException {
        return fetch(class1, null);
    }

    public Persistent fetch(Class class1, String s)
            throws FetchException {
        return fetch(class1, s, false);
    }

    public Persistent fetch(Class class1, String s, boolean flag)
            throws FetchException {
        return fetch(class1, s, flag, true);
    }

    public Persistent fetch(Class class1, String s, boolean flag, boolean addToFetchQueue)
            throws FetchException {
        try {
            Object obj = null;
            if (it.cnr.jada.persistency.KeyedPersistent.class.isAssignableFrom(class1)) {
                Object obj1 = fetchOid(class1, s);
                if (obj1 == null)
                    return null;
                KeyedPersistent keyedpersistent = getCache().get(obj1);
                if (keyedpersistent == null) {
                    getCache().put(obj1, keyedpersistent = (KeyedPersistent) newInstance(class1));
                    if (keyedpersistent != null)
                        fetchOid(keyedpersistent, s);
                }
                if (flag && addToFetchQueue)
                    getCache().addToFetchQueue(introspector, keyedpersistent, fetchPolicy.addPrefix(s));
                if (!flag)
                    fetch(keyedpersistent, s);
                obj = keyedpersistent;
            } else {
                obj = newInstance(class1);
                fetch(((Persistent) (obj)), s);
            }
            return ((Persistent) (obj));
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public void fetchOid(KeyedPersistent keyedpersistent, String s)
            throws FetchException {
        try {
            for (Iterator iterator = introspector.getPersistentInfo(keyedpersistent.getClass()).getOidPersistentProperties().values().iterator(); iterator.hasNext(); fetchProperty(keyedpersistent, ((PersistentProperty) iterator.next()).getName(), s))
                ;
            if (s == null && (keyedpersistent instanceof Keyed)) {
                Class class1 = introspector.getPersistentInfo(keyedpersistent.getClass()).getKeyClass();
                if (class1 != null) {
                    KeyedPersistent keyedpersistent1 = (KeyedPersistent) newInstance(class1);
                    fetchOid(keyedpersistent1, s);
                    ((Keyed) keyedpersistent).setKey(keyedpersistent1);
                }
            }
        } catch (PersistentPropertyNotAvailableException persistentpropertynotavailableexception) {
            throw new FetchException("Impossibile recuperare la chiave primaria per " + keyedpersistent.getClass(), persistentpropertynotavailableexception);
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public Object fetchOid(Class class1, String s)
            throws FetchException {
        try {
            StringBuffer stringbuffer = new StringBuffer(class1.getName());
            stringbuffer.append('@');
            Object obj = fetchUntypedOid(class1, s);
            if (obj == null) {
                return null;
            } else {
                stringbuffer.append(obj);
                return stringbuffer.toString();
            }
        } catch (PersistentPropertyNotAvailableException persistentpropertynotavailableexception) {
            throw new FetchException("Impossibile recuperare la chiave primaria per " + class1, persistentpropertynotavailableexception);
        }
    }

    private boolean fetchPolicyInclude(String s) {
        return fetchPolicy == null || fetchPolicy.include(s);
    }

    private Object fetchProperty(Persistent persistent, String s, String s1)
            throws FetchException {
        try {
            Class class1 = introspector.getPropertyType(persistent.getClass(), s);
            String s2 = Prefix.prependPrefix(s1, s);
            Object obj = null;
            if (it.cnr.jada.persistency.KeyedPersistent.class.isAssignableFrom(class1))
                obj = fetch(class1, s2, true, fetchPolicyInclude(s2));
            else if (it.cnr.jada.persistency.Persistent.class.isAssignableFrom(class1))
                obj = fetch(class1, s2, false);
            else
                obj = fetchPropertyValue(s2, class1);
            introspector.setPropertyValue(persistent, s, obj);
            return obj;
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public abstract Object fetchPropertyValue(String s, Class class1)
            throws FetchException;

    private Object fetchUntypedOid(Class class1, String s)
            throws FetchException {
        try {
            StringBuffer stringbuffer = new StringBuffer();
            for (Iterator iterator = introspector.getPersistentInfo(class1).getOidPersistentProperties().values().iterator(); iterator.hasNext(); ) {
                PersistentProperty persistentproperty = (PersistentProperty) iterator.next();
                String s1 = Prefix.prependPrefix(s, persistentproperty.getName());
                Class class2 = introspector.getPropertyType(class1, persistentproperty.getName());
                Object obj = null;
                if (it.cnr.jada.persistency.KeyedPersistent.class.isAssignableFrom(class2))
                    obj = fetchUntypedOid(class2, s1);
                else
                    obj = fetchPropertyValue(s1, class2);
                if (obj == null)
                    return null;
                stringbuffer.append(obj);
                if (iterator.hasNext())
                    stringbuffer.append('.');
            }

            return stringbuffer.toString();
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    public PersistentCache getCache() {
        return cache;
    }

    public void setCache(PersistentCache persistentcache) {
        cache = persistentcache;
    }

    public FetchPolicy getFetchPolicy() {
        return fetchPolicy;
    }

    public void setFetchPolicy(FetchPolicy fetchpolicy) {
        fetchPolicy = fetchpolicy;
    }

    public Introspector getIntrospector() {
        return introspector;
    }

    public void setIntrospector(Introspector introspector1) {
        introspector = introspector1;
    }

    public Persistent newInstance(Class class1) throws IntrospectionException {
        return Optional.ofNullable(class1)
                .filter(aClass -> !Modifier.isAbstract(aClass.getModifiers()))
                .map(aClass -> {
                    try {
                        return aClass.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException e) {
                        throw new DetailedRuntimeException("Can't instantiate " + class1.getName());
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new DetailedRuntimeException("Illegal access exception while instantiate " + class1.getName());
                    }
                })
                .filter(Persistent.class::isInstance)
                .map(Persistent.class::cast)
                .orElse(null);
    }

    public abstract boolean next()
            throws FetchException;
}