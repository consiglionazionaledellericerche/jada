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

/*
 * Created by Generator 1.0
 * Date 02/09/2005
 */
package it.cnr.jada.error.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Application_errorKey extends OggettoBulk implements KeyedPersistent {
    private java.lang.Long progressivo;

    public Application_errorKey() {
        super();
    }

    public Application_errorKey(java.lang.Long progressivo) {
        super();
        this.progressivo = progressivo;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application_errorKey)) return false;
        Application_errorKey k = (Application_errorKey) o;
        return compareKey(getProgressivo(), k.getProgressivo());
    }

    public int primaryKeyHashCode() {
        int i = 0;
        i = i + calculateKeyHashCode(getProgressivo());
        return i;
    }

    public java.lang.Long getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(java.lang.Long progressivo) {
        this.progressivo = progressivo;
    }
}