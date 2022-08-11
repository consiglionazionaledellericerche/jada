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

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class PersistentCache
        implements Serializable {

    Map<Object, Persistent> cache;
    HashMap<Persistent, FetchPolicy> fetchQueue;
    HashMap<Persistent, FetchPolicy> fetchedQueue;

    public PersistentCache() {
        cache = new HashMap<Object, Persistent>();
        fetchQueue = new HashMap<Persistent, FetchPolicy>();
        fetchedQueue = new HashMap<Persistent, FetchPolicy>();
    }

    public void addToFetchQueue(Introspector introspector, Persistent persistent)
            throws IntrospectionException {
        addToFetchQueue(introspector, persistent, FetchAllPolicy.FETCHALL);
    }

    public void addToFetchQueue(Introspector introspector, Persistent persistent, FetchPolicy fetchpolicy)
            throws IntrospectionException {
        if (fetchpolicy == null)
            return;
        FetchPolicy fetchpolicy1 = fetchedQueue.get(persistent);
        if (FetchAllPolicy.FETCHALL.equals(fetchpolicy1))
            return;
        FetchPolicy fetchpolicy2 = fetchQueue.get(persistent);
        if (fetchpolicy.equals(FetchNonePolicy.FETCHNONE)) {
            if (fetchpolicy1 == null)
                if (fetchpolicy2 == null)
                    fetchQueue.put(persistent, FetchNonePolicy.FETCHNONE);
                else
                    fetchQueue.put(persistent, fetchpolicy2);
        } else {
            fetchQueue.put(persistent, fetchpolicy.addFetchPolicy(fetchpolicy2));
            if (fetchpolicy1 != null) {
                if (!fetchpolicy.equals(fetchpolicy2)) {
                    fetchpolicy = fetchpolicy.addFetchPolicy(fetchpolicy2).removeFetchPolicy(fetchpolicy1);
                    if (fetchpolicy != null) {
                        for (Iterator iterator = introspector.getPersistentInfo(persistent.getClass()).getPersistentProperties().values().iterator(); iterator.hasNext(); ) {
                            PersistentProperty persistentproperty = (PersistentProperty) iterator.next();
                            Object obj = introspector.getPropertyValue(persistent, persistentproperty.getName());
                            if ((obj instanceof KeyedPersistent) && fetchpolicy.include(persistentproperty.getName()))
                                addToFetchQueue(introspector, (Persistent) obj, fetchpolicy.addPrefix(persistentproperty.getName()));
                        }

                    }
                }
                if (fetchpolicy2 == null)
                    fetchQueue.remove(persistent);
                else
                    fetchQueue.put(persistent, fetchpolicy2);
            }
        }
    }

    public void clear() {
        cache.clear();
        fetchQueue.clear();
        fetchedQueue.clear();
    }

    public KeyedPersistent get(Object obj) {
        return (KeyedPersistent) cache.get(obj);
    }

    public Iterator getFetchQueue() {
        return ((HashMap) fetchQueue.clone()).entrySet().iterator();
    }

    public boolean isFetchQueueEmpty() {
        return fetchQueue.isEmpty();
    }

    public void put(Object obj, KeyedPersistent keyedpersistent) {
        cache.put(obj, keyedpersistent);
    }

    public void removeFromFetchQueue(Introspector introspector, Persistent persistent)
            throws IntrospectionException {
        removeFromFetchQueue(introspector, persistent, FetchAllPolicy.FETCHALL);
    }

    private FetchPolicy removeFromFetchQueueByKey(Persistent persistent) {
        FetchPolicy fetchpolicy2 = fetchQueue.remove(persistent);
        if (fetchpolicy2 == null && persistent instanceof OggettoBulk) {
            final Optional<Entry<Persistent, FetchPolicy>> any = fetchQueue
                    .entrySet()
                    .stream()
                    .filter(s -> {
                        if (s.getKey() instanceof OggettoBulk) {
                            return ((OggettoBulk) persistent).equalsByPrimaryKey(s.getKey());
                        }
                        return false;
                    }).findAny();
            if (any.isPresent()) {
                return fetchQueue.remove(any.get().getKey());
            }
        }
        return fetchpolicy2;
    }

    public void removeFromFetchQueue(Introspector introspector, Persistent persistent, FetchPolicy fetchpolicy)
            throws IntrospectionException {
        if (fetchpolicy.equals(FetchAllPolicy.FETCHALL))
            fetchedQueue.put(persistent, fetchpolicy);
        else if (fetchpolicy.equals(FetchNonePolicy.FETCHNONE)) {
            FetchPolicy fetchpolicy1 = fetchedQueue.get(persistent);
            if (fetchpolicy1 == null)
                fetchpolicy1 = fetchpolicy;
            fetchedQueue.put(persistent, fetchpolicy);
        } else {
            fetchedQueue.put(persistent, fetchpolicy.addFetchPolicy(fetchedQueue.get(persistent)));
        }
        FetchPolicy fetchpolicy2 = removeFromFetchQueueByKey(persistent);
        if (fetchpolicy2 == null) {
            for (Iterator<Entry<Persistent, FetchPolicy>> iterator = fetchQueue.entrySet().iterator(); iterator.hasNext(); ) {
                Entry<Persistent, FetchPolicy> persistentKey = iterator.next();
                fetchpolicy2 = persistentKey.getValue();
                if (persistentKey.getKey().equals(persistent)) {
                    if (fetchQueue.size() == 1)
                        fetchQueue.clear();
                    else
                        iterator.remove();
                }
            }
        }
        if (fetchpolicy2 != null) {
            fetchpolicy2 = fetchpolicy2.removeFetchPolicy(fetchpolicy);
            if (fetchpolicy2 != null && !fetchpolicy.equals(FetchNonePolicy.FETCHNONE)) {
                for (Iterator iterator = introspector.getPersistentInfo(persistent.getClass()).getPersistentProperties().values().iterator(); iterator.hasNext(); ) {
                    PersistentProperty persistentproperty = (PersistentProperty) iterator.next();
                    Object obj = introspector.getPropertyValue(persistent, persistentproperty.getName());
                    if ((obj instanceof KeyedPersistent) && fetchpolicy.include(persistentproperty.getName()))
                        addToFetchQueue(introspector, (Persistent) obj, fetchpolicy2.addPrefix(persistentproperty.getName()));
                }

            }
        }
    }
}