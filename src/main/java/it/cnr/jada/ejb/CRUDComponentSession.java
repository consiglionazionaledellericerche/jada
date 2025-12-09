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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import jakarta.ejb.Remote;
import java.rmi.RemoteException;

@Remote
public interface CRUDComponentSession extends RicercaComponentSession {

    OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk[] oggettobulk)
            throws ComponentException, RemoteException;

    void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    void eliminaConBulk(UserContext usercontext, OggettoBulk[] oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk[] oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk[] oggettobulk)
            throws ComponentException, RemoteException;

    OggettoBulk initializeKeysAndOptionsInto(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;
    boolean isLockedBulk(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException, RemoteException;
}
