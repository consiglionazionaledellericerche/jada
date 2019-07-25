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
import java.util.Iterator;
import java.util.Vector;

// Referenced classes of package it.cnr.jada.bulk:
//            SimpleBulkList, BulkCollection, OggettoBulk

public class BulkList<T extends OggettoBulk> extends SimpleBulkList<T>
        implements Serializable, BulkCollection {

    Vector deleteList;

    public BulkList() {
    }

    public BulkList(Collection collection) {
        super(collection);
        if (collection instanceof BulkList)
            addAllDeleted((BulkList) collection);
    }

    public void add(int i, T obj) {
        super.add(i, obj);
        if (deleteList != null)
            deleteList.remove(obj);
    }

    public boolean addAll(int i, Collection collection) {
        if (collection instanceof BulkList)
            deleteList.addAll(((BulkList) collection).deleteList);
        return super.addAll(i, collection);
    }

    public boolean addAll(Collection collection) {
        if (collection instanceof BulkList)
            addAllDeleted((BulkList) collection);
        return super.addAll(collection);
    }

    private void addAllDeleted(BulkList bulklist) {
        if (bulklist.deleteList != null) {
            if (deleteList == null)
                deleteList = new Vector();
            deleteList.addAll(bulklist.deleteList);
        }
    }

    public Iterator deleteIterator() {
        if (deleteList == null)
            return super.deleteIterator();
        else
            return deleteList.iterator();
    }

    public T remove(int i) {
        T obj = super.remove(i);
        if (obj != null) {
            if (deleteList == null)
                deleteList = new Vector();
            deleteList.add(obj);
        }
        return obj;
    }

    public boolean removeByPrimaryKey(OggettoBulk oggettobulk) {
        if (super.removeByPrimaryKey(oggettobulk)) {
            if (deleteList == null)
                deleteList = new Vector();
            deleteList.add(oggettobulk);
            return true;
        } else {
            return false;
        }
    }
}