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
//            PrimaryKeyIterator

class PrimaryKeyMapKeySet extends AbstractSet
        implements Serializable {

    private final Set set;

    public PrimaryKeyMapKeySet(Set set1) {
        set = set1;
    }

    public void clear() {
        set.clear();
    }

    public boolean contains(Object obj) {
        return set.contains(obj);
    }

    public Iterator iterator() {
        return new PrimaryKeyIterator(set.iterator());
    }

    public boolean remove(Object obj) {
        return set.remove(obj);
    }

    public int size() {
        return set.size();
    }
}