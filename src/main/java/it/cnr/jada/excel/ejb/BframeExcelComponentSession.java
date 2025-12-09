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

package it.cnr.jada.excel.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;

import jakarta.ejb.Remote;

@Remote
public interface BframeExcelComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
    Excel_spoolerBulk addQueue(it.cnr.jada.UserContext param0, it.cnr.jada.util.OrderedHashtable param1, it.cnr.jada.util.OrderedHashtable param2, String param3, java.util.Dictionary param4, String param5, String param6, it.cnr.jada.persistency.sql.ColumnMap param7, it.cnr.jada.bulk.OggettoBulk param8) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void deleteJobs(it.cnr.jada.UserContext param0, it.cnr.jada.excel.bulk.Excel_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void cancellaSchedulazione(UserContext userContext, Long long1, String resource) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    Excel_spoolerBulk findExcelSpooler(UserContext userContext, Long pg) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void modifyQueue(UserContext userContext, Excel_spoolerBulk bulk) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
}
