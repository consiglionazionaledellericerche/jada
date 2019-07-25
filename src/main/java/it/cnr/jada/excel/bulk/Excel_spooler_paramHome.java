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

/*
 * Created by Generator 1.0
 * Date 23/01/2006
 */
package it.cnr.jada.excel.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Excel_spooler_paramHome extends BulkHome {
    public Excel_spooler_paramHome(java.sql.Connection conn) {
        super(Excel_spooler_paramBulk.class, conn);
    }

    public Excel_spooler_paramHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Excel_spooler_paramBulk.class, conn, persistentCache);
    }

    public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
        try {
            ((Excel_spooler_paramBulk) oggettobulk).setPg_column(
                    new Long(
                            ((Long) findAndLockMax(oggettobulk, "pg_column", new Long(0))).longValue() + 1
                    )
            );
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw new PersistencyException(e);
        }
        super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
    }

}