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
//            CRUDException

public class CRUDReferentialIntegrityException extends CRUDException
        implements Serializable {

    public CRUDReferentialIntegrityException() {
    }

    public CRUDReferentialIntegrityException(String s) {
        super(s);
    }

    public CRUDReferentialIntegrityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CRUDReferentialIntegrityException(String s, Throwable throwable, OggettoBulk oggettobulk) {
        super(s, throwable, oggettobulk);
    }

    public CRUDReferentialIntegrityException(Throwable throwable) {
        super(throwable);
    }

    public CRUDReferentialIntegrityException(Throwable throwable, OggettoBulk oggettobulk) {
        super(throwable, oggettobulk);
    }
}