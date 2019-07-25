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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkPrimaryKey, PrimaryKeyIterator

public class PrimaryKeyHashSet extends HashSet
        implements Serializable {

    public PrimaryKeyHashSet() {
    }

    public PrimaryKeyHashSet(int i) {
        super(i);
    }

    public PrimaryKeyHashSet(int i, float f) {
        super(i, f);
    }

    public PrimaryKeyHashSet(Collection collection) {
        super(collection);
    }

    public boolean add(Object obj) {
        return super.add(BulkPrimaryKey.getPrimaryKey(obj));
    }

    public boolean contains(Object obj) {
        return super.contains(BulkPrimaryKey.getPrimaryKey(obj));
    }

    public Iterator iterator() {
        return new PrimaryKeyIterator(super.iterator());
    }

    public boolean remove(Object obj) {
        return super.remove(BulkPrimaryKey.getPrimaryKey(obj));
    }
}