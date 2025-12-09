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

package it.cnr.jada.ejb;

import jakarta.ejb.Remote;
import jakarta.transaction.RollbackException;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

@Remote
public interface UserTransactionWrapper {
    int HIGH_TIME = 10000000;

    void begin()
            throws RemoteException;

    void begin(int i)
            throws RemoteException;

    void commit()
            throws RemoteException, RollbackException;

    Object invoke(String s, String s1, Object aobj[])
            throws RemoteException, InvocationTargetException;

    Object invoke(Object ejbobject, String s, Object aobj[])
            throws RemoteException, InvocationTargetException;

    void ping()
            throws RemoteException;

    void rollback()
            throws RemoteException;

    void ejbRemove()
            throws RemoteException;

    void addToEjbObjectsToBeRemoved(Object ejbobject);
}
