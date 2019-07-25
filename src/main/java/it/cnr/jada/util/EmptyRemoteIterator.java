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

// Referenced classes of package it.cnr.jada.util:
//            RemoteIterator, RemoteOrderable

public class EmptyRemoteIterator
        implements Serializable, RemoteIterator, RemoteOrderable {

    public EmptyRemoteIterator() {
    }

    public void close()
            throws RemoteException {
    }

    public int countElements()
            throws RemoteException {
        return 0;
    }

    public int getOrderBy(String s)
            throws RemoteException {
        return 0;
    }

    public boolean hasMoreElements()
            throws RemoteException {
        return false;
    }

    public boolean isOrderableBy(String s)
            throws RemoteException {
        return false;
    }

    public void moveTo(int i)
            throws RemoteException {
    }

    public Object nextElement()
            throws RemoteException {
        return null;
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