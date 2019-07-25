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

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

// Referenced classes of package it.cnr.jada.util:
//            Introspector, IntrospectionError

public class MultipleComparator
        implements Serializable, Comparator {

    private final Map orderByClauses;

    public MultipleComparator(Map map) {
        orderByClauses = map;
    }

    public int compare(Object obj, Object obj1) {
        try {
            for (Iterator iterator = orderByClauses.entrySet().iterator(); iterator.hasNext(); ) {
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
                String s = (String) entry.getKey();
                int i = ((Integer) entry.getValue()).intValue();
                if (i != 0) {
                    Comparable comparable = (Comparable) Introspector.getPropertyValue(obj, s);
                    Comparable comparable1 = (Comparable) Introspector.getPropertyValue(obj1, s);
                    if (comparable == null)
                        return comparable1 == null ? 0 : 1;
                    if (comparable1 == null)
                        return -1;
                    int j = comparable.compareTo(comparable1);
                    if (j != 0)
                        return i != 1 ? -j : j;
                }
            }

            return 0;
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        } catch (InvocationTargetException invocationtargetexception) {
            throw new IntrospectionError(invocationtargetexception);
        }
    }
}