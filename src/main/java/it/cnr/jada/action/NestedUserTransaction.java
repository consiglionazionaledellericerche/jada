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

package it.cnr.jada.action;

import it.cnr.jada.UserTransaction;

import jakarta.ejb.EJBException;
import jakarta.ejb.RemoveException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.jada.action:
//            BusinessProcessException

class NestedUserTransaction
        implements Serializable, UserTransaction {
    private final UserTransaction parent;

    private NestedUserTransaction(UserTransaction usertransaction) {
        parent = usertransaction;
    }

    public static UserTransaction createNestedUserTransaction(UserTransaction usertransaction)
            throws BusinessProcessException {
        if (usertransaction == null || (usertransaction instanceof NestedUserTransaction))
            return usertransaction;
        else
            return new NestedUserTransaction(usertransaction);
    }

    public void commit()
            throws EJBException, RemoteException {
    }

    public Object getOwner()
            throws RemoteException {
        return parent.getOwner();
    }

    public Object invoke(String s, String s1, Object aobj[])
            throws InvocationTargetException, RemoteException {
        return parent.invoke(s, s1, aobj);
    }

    public Object invoke(Object ejbobject, String s, Object aobj[])
            throws InvocationTargetException, RemoteException {
        return parent.invoke(ejbobject, s, aobj);
    }

    public void remove()
            throws RemoteException, RemoveException {
    }

    public void rollback()
            throws EJBException, RemoteException {
    }

    @Override
    public void addToEjbObjectsToBeRemoved(Object ejbobject) {
        parent.addToEjbObjectsToBeRemoved(ejbobject);
    }
}