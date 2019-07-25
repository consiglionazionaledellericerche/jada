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
import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Referenced classes of package it.cnr.jada.util:
//            Sortable, Introspector, IntrospectionError

public class Collect extends AbstractList
        implements Serializable, Sortable {
    protected List list;
    protected String selectProperty;

    public Collect(List list1, String s) {
        list = list1;
        selectProperty = s;
    }

    public Object get(int i) {
        return selectElement(list.get(i));
    }

    public Object remove(int i) {
        return selectElement(list.remove(i));
    }

    private Object selectElement(Object obj) {
        if (obj == null)
            return null;
        try {
            return Introspector.getPropertyValue(obj, selectProperty);
        } catch (Exception exception) {
            throw new IntrospectionError(exception);
        }
    }

    public void setList(List list1) {
        list = list1;
    }

    public int size() {
        return list.size();
    }

    public void sort(Comparator comparator) {
        Collections.sort(list, new CollectComparator(comparator, null));
    }

    class CollectComparator
            implements Serializable, Comparator {

        private final Comparator comparator;

        private CollectComparator(Comparator comparator1) {
            comparator = comparator1;
        }

        CollectComparator(Comparator comparator1, CollectComparator collectcomparator) {
            this(comparator1);
        }

        public int compare(Object obj, Object obj1) {
            return comparator.compare(selectElement(obj), selectElement(obj1));
        }
    }

}