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

package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Bframe_blob_tipoHome extends BulkHome {

    public Bframe_blob_tipoHome(Connection conn) {
        super(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class, conn);
    }

    public Bframe_blob_tipoHome(Connection conn, PersistentCache persistentCache) {
        super(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class, conn, persistentCache);
    }
}