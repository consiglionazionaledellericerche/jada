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

import it.cnr.jada.UserContext;

import jakarta.ejb.Remote;
import java.rmi.RemoteException;

@Remote
public interface AdminSession extends GenericComponentSession {

    void addSQLTraceUser(String s)
            throws RemoteException;

    long getActiveBulkLoaderIteratorCounter()
            throws RemoteException;

    long getActiveComponentCounter()
            throws RemoteException;

    long getActiveUserTransactionCounter()
            throws RemoteException;

    String[] getTraceUsers()
            throws RemoteException;

    byte[] getZippedTrace(UserContext usercontext)
            throws RemoteException;

    boolean isDumpStackTraceEnabled()
            throws RemoteException;

    void setDumpStackTraceEnabled(boolean flag)
            throws RemoteException;

    void removeSQLTraceUser(String s)
            throws RemoteException;

    void loadBulkInfos(Class class1)
            throws RemoteException;

    void loadPersistentInfos(Class class1)
            throws RemoteException;

    void resetPersistentInfos()
            throws RemoteException;
}
