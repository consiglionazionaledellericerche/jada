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

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.UserTransaction;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.TransactionalBulkLoaderIterator;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.RemoteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.NoSuchEJBException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public class UserTransactionalBulkLoaderIterator implements TransactionalBulkLoaderIterator {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(EJBCommonServices.class);
    private UserTransaction userTransaction;
    private RemoteIterator remoteiterator;

    public UserTransactionalBulkLoaderIterator(it.cnr.jada.UserTransaction userTransaction, RemoteIterator remoteiterator) {
        this.userTransaction = userTransaction;
        this.remoteiterator = remoteiterator;
    }

    @Override
    public void close() throws RemoteException {
        try {
            userTransaction.invoke(remoteiterator, "close");
        } catch (jakarta.ejb.ConcurrentAccessTimeoutException _ex) {
            logger.warn("Access timeout to EJB", _ex);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof UserTransactionTimeoutException || e.getTargetException() instanceof NoSuchEJBException)
                logger.warn("Exception while closing EJB", e.getTargetException());
            else
                throw new RuntimeException(e);
        }
    }

    @Override
    public int countElements() throws DetailedRuntimeException, RemoteException {
        try {
            return (int) userTransaction.invoke(remoteiterator, "countElements");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countPages() throws DetailedRuntimeException, RemoteException {
        try {
            return (int) userTransaction.invoke(remoteiterator, "countPages");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrderBy(String s) throws DetailedRuntimeException,
            RemoteException {
        try {
            return (int) userTransaction.invoke(remoteiterator, "getOrderBy", new Object[]{s});
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasMoreElements() throws DetailedRuntimeException,
            RemoteException {
        try {
            return (boolean) userTransaction.invoke(remoteiterator, "hasMoreElements");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasMorePages() throws DetailedRuntimeException,
            RemoteException {
        try {
            return (boolean) userTransaction.invoke(remoteiterator, "hasMorePages");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isOrderableBy(String s) throws DetailedRuntimeException,
            RemoteException {
        try {
            return (boolean) userTransaction.invoke(remoteiterator, "isOrderableBy", new Object[]{s});
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveTo(int i) throws DetailedRuntimeException, RemoteException {
        try {
            userTransaction.invoke(remoteiterator, "moveTo", i);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveToPage(int i) throws DetailedRuntimeException,
            RemoteException {
        try {
            userTransaction.invoke(remoteiterator, "moveToPage", i);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object nextElement() throws DetailedRuntimeException,
            RemoteException {
        try {
            return userTransaction.invoke(remoteiterator, "nextElement");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object[] nextPage() throws DetailedRuntimeException, RemoteException {
        try {
            return (Object[]) userTransaction.invoke(remoteiterator, "nextPage");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void open(UserContext usercontext) throws ComponentException,
            RemoteException, DetailedRuntimeException {
        try {
            userTransaction.invoke(remoteiterator, "open", usercontext);
            userTransaction.addToEjbObjectsToBeRemoved(remoteiterator);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() throws DetailedRuntimeException, RemoteException {
        try {
            userTransaction.invoke(remoteiterator, "refresh");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setOrderBy(String s, int i) throws DetailedRuntimeException,
            RemoteException {
        try {
            userTransaction.invoke(remoteiterator, "setOrderBy", s, i);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPageSize(int i) throws DetailedRuntimeException,
            RemoteException {
        try {
            userTransaction.invoke(remoteiterator, "setPageSize", i);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Query getQuery() throws DetailedRuntimeException, RemoteException {
        try {
            return (Query) userTransaction.invoke(remoteiterator, "getQuery");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
            throws CreateException, ComponentException {
        try {
            userTransaction.invoke(remoteiterator, "ejbCreate", usercontext, query1, class1);
        } catch (InvocationTargetException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ejbCreate(UserContext usercontext, Query query1,
                          Class<?> class1, String s) throws CreateException,
            ComponentException {
        try {
            userTransaction.invoke(remoteiterator, "ejbCreate", usercontext, query1, class1, s);
        } catch (InvocationTargetException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ejbRemove() throws EJBException {
        try {
            userTransaction.invoke(remoteiterator, "ejbRemove");
        } catch (jakarta.ejb.ConcurrentAccessTimeoutException _ex) {
            logger.warn("Access timeout to EJB", _ex);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof UserTransactionTimeoutException || e.getTargetException() instanceof NoSuchEJBException)
                logger.warn("Exception while closing EJB", e.getTargetException());
            else
                throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    public RemoteIterator getRemoteiterator() {
        return remoteiterator;
    }

}
