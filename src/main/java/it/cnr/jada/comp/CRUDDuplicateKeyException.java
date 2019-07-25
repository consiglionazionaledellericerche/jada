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

public class CRUDDuplicateKeyException extends CRUDException
        implements Serializable {

    private OggettoBulk duplicateBulk;

    public CRUDDuplicateKeyException() {
    }

    public CRUDDuplicateKeyException(String s) {
        super(s);
    }

    public CRUDDuplicateKeyException(String s, OggettoBulk oggettobulk, OggettoBulk oggettobulk1) {
        super(s, null, oggettobulk);
        duplicateBulk = oggettobulk1;
    }

    public CRUDDuplicateKeyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CRUDDuplicateKeyException(String s, Throwable throwable, OggettoBulk oggettobulk, OggettoBulk oggettobulk1) {
        super(s, throwable, oggettobulk);
        duplicateBulk = oggettobulk1;
    }

    public CRUDDuplicateKeyException(Throwable throwable) {
        super(throwable);
    }

    public CRUDDuplicateKeyException(Throwable throwable, OggettoBulk oggettobulk, OggettoBulk oggettobulk1) {
        super(throwable, oggettobulk);
        duplicateBulk = oggettobulk1;
    }

    public OggettoBulk getDuplicateBulk() {
        return duplicateBulk;
    }

    public void setDuplicateBulk(OggettoBulk oggettobulk) {
        duplicateBulk = oggettobulk;
    }
}