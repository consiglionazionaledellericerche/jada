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

package it.cnr.jada.util;

import java.io.Serializable;

class OrderedHashMapEntry
        implements Serializable, Cloneable, java.util.Map.Entry {

    private Object key;
    private Object value;

    public OrderedHashMapEntry(Object obj, Object obj1) {
        key = obj;
        value = obj1;
    }

    protected Object clone() {
        return new OrderedHashMapEntry(key, value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof java.util.Map.Entry))
            return false;
        java.util.Map.Entry entry = (java.util.Map.Entry) obj;
        return (key != null ? key.equals(entry.getKey()) : entry.getKey() == null) && (value != null ? value.equals(entry.getValue()) : entry.getValue() == null);
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public Object setValue(Object obj) {
        Object obj1 = value;
        value = obj;
        return obj1;
    }
}