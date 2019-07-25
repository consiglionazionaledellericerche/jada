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

package it.cnr.jada.util.ejb;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.ejb.UserTransactionWrapper;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public abstract class TransactionalStatefulSessionImpl
        implements EJBObject, Serializable {

    protected EJBObject ejbObject;
    private UserTransaction userTransaction;
    private UserTransactionWrapper userTransactionWrapper;

    public TransactionalStatefulSessionImpl() {
    }

    public EJBHome getEJBHome()
            throws RemoteException {
        return ejbObject.getEJBHome();
    }

    public EJBObject getEjbObject() {
        return ejbObject;
    }

    public void setEjbObject(EJBObject ejbobject) {
        ejbObject = ejbobject;
    }

    public Handle getHandle()
            throws RemoteException {
        return ejbObject.getHandle();
    }

    public Object getPrimaryKey()
            throws RemoteException {
        return ejbObject.getPrimaryKey();
    }

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    public void setUserTransaction(UserTransaction usertransaction) {
        userTransaction = usertransaction;
    }

    public UserTransactionWrapper getUserTransactionWrapper() {
        return userTransactionWrapper;
    }

    public void setUserTransactionWrapper(UserTransactionWrapper usertransactionwrapper) {
        userTransactionWrapper = usertransactionwrapper;
    }

    public Object invoke(String s, Object aobj[])
            throws InvocationTargetException, RemoteException {
        return userTransaction.invoke(this, s, aobj);
    }

    public boolean isIdentical(EJBObject ejbobject)
            throws RemoteException {
        return ejbObject.isIdentical(ejbobject);
    }

    public void remove()
            throws RemoteException, RemoveException {
    }
}