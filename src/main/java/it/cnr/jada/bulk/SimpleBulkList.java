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
import java.util.*;

// Referenced classes of package it.cnr.jada.bulk:
//            BulkCollection, OggettoBulk

public class SimpleBulkList<T extends OggettoBulk> extends AbstractList<T>
        implements Serializable, BulkCollection {

    Vector<T> list;

    public SimpleBulkList() {
        list = new Vector();
    }

    public SimpleBulkList(Collection collection) {
        list = new Vector(collection);
    }

    public void add(int i, T obj) {
        list.add(i, obj);
    }

    public boolean containsByPrimaryKey(OggettoBulk oggettobulk) {
        Iterator iterator = iterator();
        if (oggettobulk == null) {
            while (iterator.hasNext())
                if (iterator.next() == null)
                    return true;
        } else {
            while (iterator.hasNext())
                if (oggettobulk.equalsByPrimaryKey(iterator.next()))
                    return true;
        }
        return false;
    }

    public Iterator deleteIterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    public T get(int i) {
        return list.get(i);
    }

    public int indexOfByPrimaryKey(OggettoBulk oggettobulk) {
        ListIterator listiterator = list.listIterator();
        if (oggettobulk == null) {
            while (listiterator.hasNext())
                if (listiterator.next() == null)
                    return listiterator.previousIndex();
        } else {
            while (listiterator.hasNext())
                if (oggettobulk.equalsByPrimaryKey(listiterator.next()))
                    return listiterator.previousIndex();
        }
        return -1;
    }

    public int lastIndexOfByPrimaryKey(OggettoBulk oggettobulk) {
        ListIterator listiterator = list.listIterator(list.size());
        if (oggettobulk == null) {
            while (listiterator.hasPrevious())
                if (listiterator.previous() == null)
                    return listiterator.nextIndex();
        } else {
            while (listiterator.hasPrevious())
                if (oggettobulk.equalsByPrimaryKey(listiterator.previous()))
                    return listiterator.nextIndex();
        }
        return -1;
    }

    public T remove(int i) {
        T obj = list.remove(i);
        return obj;
    }

    public boolean removeByPrimaryKey(OggettoBulk oggettobulk) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            if (oggettobulk.equalsByPrimaryKey(obj)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    public T set(int i, T obj) {
        return list.set(i, obj);
    }

    public int size() {
        return list.size();
    }
}