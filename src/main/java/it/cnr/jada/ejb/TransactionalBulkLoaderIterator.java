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

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.RemoteOrderable;
import it.cnr.jada.util.RemotePagedIterator;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import java.rmi.RemoteException;

@Remote
public interface TransactionalBulkLoaderIterator extends RemotePagedIterator, RemoteOrderable {

    void close()
            throws RemoteException;

    int countElements()
            throws DetailedRuntimeException, RemoteException;

    int countPages()
            throws DetailedRuntimeException, RemoteException;

    int getOrderBy(String s)
            throws DetailedRuntimeException, RemoteException;

    boolean hasMoreElements()
            throws DetailedRuntimeException, RemoteException;

    boolean hasMorePages()
            throws DetailedRuntimeException, RemoteException;

    boolean isOrderableBy(String s)
            throws DetailedRuntimeException, RemoteException;

    void moveTo(int i)
            throws DetailedRuntimeException, RemoteException;

    void moveToPage(int i)
            throws DetailedRuntimeException, RemoteException;

    Object nextElement()
            throws DetailedRuntimeException, RemoteException;

    Object[] nextPage()
            throws DetailedRuntimeException, RemoteException;

    void open(UserContext usercontext)
            throws ComponentException, RemoteException, DetailedRuntimeException;

    void refresh()
            throws DetailedRuntimeException, RemoteException;

    void setOrderBy(String s, int i)
            throws DetailedRuntimeException, RemoteException;

    void setPageSize(int i)
            throws DetailedRuntimeException, RemoteException;

    Query getQuery()
            throws DetailedRuntimeException, RemoteException;

    void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
            throws CreateException, ComponentException;

    void ejbCreate(UserContext usercontext, Query query1, Class<?> class1, String s)
            throws CreateException, ComponentException;

    void ejbRemove()
            throws EJBException;
}