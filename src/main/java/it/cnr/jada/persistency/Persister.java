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

package it.cnr.jada.persistency;

import it.cnr.jada.UserContext;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, PersistencyListener, Introspector, Persistent

public abstract class Persister
        implements Serializable {

    private Introspector introspector;

    public Persister(Introspector introspector1) {
        introspector = introspector1;
    }

    public void delete(Persistent persistent, UserContext userContext)
            throws PersistencyException {
        if (persistent instanceof PersistencyListener)
            ((PersistencyListener) persistent).deletingUsing(this, userContext);
        doDelete(persistent);
        if (persistent instanceof PersistencyListener)
            ((PersistencyListener) persistent).deletedUsing(this, userContext);
    }

    protected abstract void doDelete(Persistent persistent)
            throws PersistencyException;

    protected abstract void doInsert(Persistent persistent)
            throws PersistencyException;

    protected abstract void doUpdate(Persistent persistent)
            throws PersistencyException;

    public Introspector getIntrospector() {
        return introspector;
    }

    public void setIntrospector(Introspector introspector1) {
        introspector = introspector1;
    }

    public void insert(Persistent persistent, UserContext userContext)
            throws PersistencyException {
        if (persistent instanceof PersistencyListener)
            ((PersistencyListener) persistent).insertingUsing(this, userContext);
        doInsert(persistent);
        if (persistent instanceof PersistencyListener)
            ((PersistencyListener) persistent).insertedUsing(this, userContext);
    }

    public void update(Persistent persistent, UserContext userContext)
            throws PersistencyException {
        if (persistent instanceof PersistencyListener)
            ((PersistencyListener) persistent).updatingUsing(this, userContext);
        doUpdate(persistent);
        if (persistent instanceof PersistencyListener)
            ((PersistencyListener) persistent).updatedUsing(this, userContext);
    }
}