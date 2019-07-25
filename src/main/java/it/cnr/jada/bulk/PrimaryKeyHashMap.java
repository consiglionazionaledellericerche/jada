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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey, PrimaryKeyMapEntrySet, PrimaryKeyMapKeySet

public class PrimaryKeyHashMap extends HashMap
        implements Serializable {

    private transient Set keySet;
    private transient Set entrySet;

    public PrimaryKeyHashMap() {
    }

    public PrimaryKeyHashMap(int i) {
        super(i);
    }

    public PrimaryKeyHashMap(int i, float f) {
        super(i, f);
    }

    public PrimaryKeyHashMap(Map map) {
        super(map);
    }

    public boolean containsKey(Object obj) {
        return super.containsKey(BulkPrimaryKey.getPrimaryKey(obj));
    }

    public Set entrySet() {
        if (entrySet == null)
            return entrySet = new PrimaryKeyMapEntrySet(super.entrySet());
        else
            return entrySet;
    }

    public Object get(Object obj) {
        //Aggiunto il 21/10/2004 da Marco in seguito alla
        //segnalazione di NullPointerException
        Object o = super.get(BulkPrimaryKey.getPrimaryKey(obj));
        if (o == null)
            return super.get(obj);
        else
            return o;
    }

    public Set keySet() {
        if (keySet == null)
            return keySet = new PrimaryKeyMapKeySet(super.keySet());
        else
            return keySet;
    }

    public Object put(Object obj, Object obj1) {
        return super.put(BulkPrimaryKey.getPrimaryKey(obj), obj1);
    }

    public Object remove(Object obj) {
        return super.remove(BulkPrimaryKey.getPrimaryKey(obj));
    }
}