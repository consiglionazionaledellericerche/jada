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
import java.util.Comparator;

public class CollectComparator
        implements Serializable, Comparator {

    private final String property;
    private final Comparator comparator;

    public CollectComparator(String s, Comparator comparator1) {
        property = s;
        comparator = comparator1;
    }

    public int compare(Object obj, Object obj1) {
        return 0;
    }

    public final Comparator getComparator() {
        return comparator;
    }

    public final String getProperty() {
        return property;
    }
}