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
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;

// Referenced classes of package it.cnr.jada.util:
//            RemoteOrderable, RemoteIterator

public class ListRemoteIterator implements RemoteOrderable, RemoteIterator, Serializable {

    private List list;
    private int position;

    public ListRemoteIterator(List list1) {
        list = list1;
    }

    public void close()
            throws RemoteException {
    }

    public int countElements() {
        return list.size();
    }

    public int getOrderBy(String s)
            throws RemoteException {
        return 0;
    }

    public boolean hasMoreElements() {
        return position < list.size();
    }

    public boolean isOrderableBy(String s)
            throws RemoteException {
        return false;
    }

    public void moveTo(int i) {
        if (i < 0 || i >= list.size()) {
            throw new NoSuchElementException();
        } else {
            position = i;
            return;
        }
    }

    public Object nextElement() {
        if (position >= list.size())
            throw new NoSuchElementException();
        else
            return list.get(position++);
    }

    public void refresh()
            throws RemoteException {
    }

    public void setOrderBy(String s, int i)
            throws RemoteException {
    }

    public void ejbRemove() throws RemoteException {
        // TODO Auto-generated method stub

    }
}