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
public interface BulkLoaderIterator extends RemotePagedIterator, RemoteOrderable {

    void close()
            throws RemoteException;

    /**
     * Restituisce la dimensione della collezione di elementi.
     */
    int countElements()
            throws DetailedRuntimeException, RemoteException;

    /**
     * Restituisce il numero di pagine che l'iterator   in grado di scorrere.
     */
    int countPages()
            throws DetailedRuntimeException, RemoteException;

    int getOrderBy(String s)
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Restituisce false se il cursore   arrivato oltre l'ultimo elemento della collezione
     */
    boolean hasMoreElements()
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Interroga il ricevente se possiede ancora pagine da scorrere partendo dalla posizione corrente del cursore
     */
    boolean hasMorePages()
            throws DetailedRuntimeException, RemoteException;

    boolean isOrderableBy(String s)
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Muove il cursore alla posizione specificata.
     */
    void moveTo(int i)
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Muove il cursore del ricevente all'inizio di una pagina
     */
    void moveToPage(int i)
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Restituisce il prossimo elemento della collezione.
     */
    Object nextElement()
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Legge l'intero contenuto della pagina corrente; all'uscita il cursore del ricevente   posizionato all'inizio della pagina successiva
     */
    Object[] nextPage()
            throws DetailedRuntimeException, RemoteException;

    void open(UserContext usercontext)
            throws ComponentException, RemoteException, DetailedRuntimeException;

    void refresh()
            throws DetailedRuntimeException, RemoteException;

    void setOrderBy(String s, int i)
            throws DetailedRuntimeException, RemoteException;

    /**
     * Description copied from interface:
     * Imposta la dimensione della paginatura.
     */
    void setPageSize(int i)
            throws DetailedRuntimeException, RemoteException;

    Query getQuery()
            throws DetailedRuntimeException, RemoteException;

    void ejbCreate(UserContext usercontext, Query query1, Class<?> class1)
            throws CreateException, ComponentException;

    void ejbCreate(UserContext usercontext, Query query1, Class<?> class1, String fetchPolicyName)
            throws CreateException, ComponentException;

    void ejbRemove()
            throws EJBException;
}