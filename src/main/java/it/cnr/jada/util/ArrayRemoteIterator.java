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
import java.util.NoSuchElementException;

public class ArrayRemoteIterator implements RemoteOrderable, RemoteIterator, Serializable {

    private Object[] array;
    private int position;

    public ArrayRemoteIterator(Object aobj[]) {
        array = aobj;
    }

    public void close()
            throws RemoteException {
    }

    public int countElements() {
        return array.length;
    }

    public int getOrderBy(String s)
            throws RemoteException {
        return 0;
    }

    public boolean hasMoreElements() {
        return position < array.length;
    }

    public boolean isOrderableBy(String s)
            throws RemoteException {
        return false;
    }

    public void moveTo(int i) {
        if (i < 0 || i >= array.length) {
            throw new NoSuchElementException();
        } else {
            position = i;
            return;
        }
    }

    public Object nextElement() {
        if (position >= array.length)
            throw new NoSuchElementException();
        else
            return array[position++];
    }

    public void refresh()
            throws RemoteException {
    }

    public void setOrderBy(String s, int i)
            throws RemoteException {
    }

    public void ejbRemove() throws RemoteException {

    }

}