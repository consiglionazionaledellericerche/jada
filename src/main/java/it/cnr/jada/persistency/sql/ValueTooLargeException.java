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

package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;

import java.io.Serializable;

public class ValueTooLargeException extends PersistencyException
        implements Serializable {

    public ValueTooLargeException() {
    }

    public ValueTooLargeException(String s) {
        super(s);
    }

    public ValueTooLargeException(String s, Persistent persistent) {
        super(s, persistent);
    }

    public ValueTooLargeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ValueTooLargeException(String s, Throwable throwable, Persistent persistent) {
        super(s, throwable, persistent);
    }

    public ValueTooLargeException(Throwable throwable) {
        super(throwable);
    }

    public ValueTooLargeException(Throwable throwable, Persistent persistent) {
        super(throwable, persistent);
    }
}