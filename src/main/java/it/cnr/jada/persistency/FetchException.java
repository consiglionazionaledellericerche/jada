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

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.persistency:
//            PersistencyException, Persistent

public class FetchException extends PersistencyException
        implements Serializable {

    public FetchException() {
    }

    public FetchException(String s) {
        super(s);
    }

    public FetchException(String s, Persistent persistent) {
        super(s, persistent);
    }

    public FetchException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FetchException(String s, Throwable throwable, Persistent persistent) {
        super(s, throwable, persistent);
    }

    public FetchException(Throwable throwable) {
        super(throwable);
    }

    public FetchException(Throwable throwable, Persistent persistent) {
        super(throwable, persistent);
    }
}