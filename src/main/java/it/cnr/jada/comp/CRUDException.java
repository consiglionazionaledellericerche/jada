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

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            ApplicationException

public class CRUDException extends ApplicationException
        implements Serializable {

    private OggettoBulk bulk;

    public CRUDException() {
    }

    public CRUDException(String s) {
        super(s);
    }

    public CRUDException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CRUDException(String s, Throwable throwable, OggettoBulk oggettobulk) {
        super(s, throwable);
        bulk = oggettobulk;
    }

    public CRUDException(Throwable throwable) {
        super(throwable);
    }

    public CRUDException(Throwable throwable, OggettoBulk oggettobulk) {
        super(throwable);
        bulk = oggettobulk;
    }

    public OggettoBulk getBulk() {
        return bulk;
    }

    public void setBulk(OggettoBulk oggettobulk) {
        bulk = oggettobulk;
    }
}