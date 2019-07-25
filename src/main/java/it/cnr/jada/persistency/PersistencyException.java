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

import it.cnr.jada.DetailedException;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            Persistent

public class PersistencyException extends DetailedException
        implements Serializable {

    private Object persistent;

    public PersistencyException() {
    }

    public PersistencyException(String s) {
        super(s);
    }

    public PersistencyException(String s, Persistent persistent1) {
        super(s);
        persistent = persistent1;
    }

    public PersistencyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PersistencyException(String s, Throwable throwable, Persistent persistent1) {
        super(s, throwable);
        persistent = persistent1;
    }

    public PersistencyException(Throwable throwable) {
        super(throwable);
    }

    public PersistencyException(Throwable throwable, Persistent persistent1) {
        super(throwable);
        persistent = persistent1;
    }

    public Persistent getPersistent() {
        return (Persistent) persistent;
    }

    public void setPersistent(Persistent persistent1) {
        persistent = persistent1;
    }
}