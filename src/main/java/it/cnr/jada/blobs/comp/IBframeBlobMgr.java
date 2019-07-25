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

package it.cnr.jada.blobs.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;

public interface IBframeBlobMgr {

    void elimina(UserContext usercontext, Bframe_blob_pathBulk abframe_blob_pathbulk[])
            throws ComponentException;

    RemoteIterator getBlobChildren(UserContext usercontext, Bframe_blob_pathBulk bframe_blob_pathbulk, String s)
            throws ComponentException;

    Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext usercontext)
            throws ComponentException;
}