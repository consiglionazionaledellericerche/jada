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

package it.cnr.jada.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

// Referenced classes of package it.cnr.jada.comp:
//            ICRUDMgr, ComponentException

public interface ICRUDDetailMgr
        extends ICRUDMgr {

    RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, Class class1, OggettoBulk oggettobulk, String s)
            throws ComponentException;

    OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws ComponentException;

    void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[], OggettoBulk oggettobulk, String s)
            throws ComponentException;

    void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk, String s)
            throws ComponentException;

    OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws ComponentException;

    OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws ComponentException;

    OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
            throws ComponentException;
}