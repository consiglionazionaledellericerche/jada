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

package it.cnr.jada;

import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.transaction.RollbackException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public interface UserTransaction extends Serializable {

    void commit()
            throws RemoteException, EJBException, RollbackException;

    boolean equals(Object obj);

    Object getOwner()
            throws RemoteException;

    Object invoke(String s, String s1, Object aobj[])
            throws InvocationTargetException, RemoteException;

    Object invoke(Object ejbobject, String s, Object... aobj)
            throws InvocationTargetException, RemoteException;

    void remove()
            throws RemoteException, RemoveException;

    void rollback()
            throws RemoteException, EJBException;

    void addToEjbObjectsToBeRemoved(Object ejbobject);
}