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

package it.cnr.jada.bulk;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey

class PrimaryKeyMapEntrySet extends AbstractSet
        implements Serializable {
    private final Set set;

    public PrimaryKeyMapEntrySet(Set set1) {
        set = set1;
    }

    public void clear() {
        set.clear();
    }

    public boolean contains(Object obj) {
        if (!(obj instanceof java.util.Map.Entry))
            return false;
        else
            return set.contains(new PrimaryKeyMapEntry((java.util.Map.Entry) obj));
    }

    public Iterator iterator() {
        return new PrimaryKeySetIterator(set.iterator());
    }

    public boolean remove(Object obj) {
        return set.remove(obj);
    }

    public int size() {
        return set.size();
    }

    class PrimaryKeySetIterator
            implements Iterator, java.util.Map.Entry, Serializable {

        private Iterator iterator;
        private java.util.Map.Entry entry;

        public PrimaryKeySetIterator(Iterator iterator1) {
            iterator = iterator1;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            entry = (java.util.Map.Entry) iterator.next();
            return this;
        }

        public void remove() {
            iterator.remove();
        }

        public Object getKey() {
            if (entry.getKey() instanceof BulkPrimaryKey)
                return entry.getKey();
            return BulkPrimaryKey.getBulk(entry.getKey());
        }

        public Object getValue() {
            return entry.getValue();
        }

        public Object setValue(Object obj) {
            return entry.setValue(obj);
        }

        public int hashCode() {
            return entry.hashCode();
        }
    }

    class PrimaryKeyMapEntry
            implements java.util.Map.Entry, Serializable {

        Object key;
        Object value;

        public PrimaryKeyMapEntry(java.util.Map.Entry entry) {
            key = BulkPrimaryKey.getPrimaryKey(entry.getKey());
            value = entry.getValue();
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object obj) {
            return obj;
        }
    }
}