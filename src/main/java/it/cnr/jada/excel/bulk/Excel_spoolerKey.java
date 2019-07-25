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
 * Date 23/01/2006
 */
package it.cnr.jada.excel.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Excel_spoolerKey extends OggettoBulk implements KeyedPersistent {
    private java.lang.Long pg_estrazione;

    public Excel_spoolerKey() {
        super();
    }

    public Excel_spoolerKey(java.lang.Long pg_estrazione) {
        super();
        this.pg_estrazione = pg_estrazione;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o) return true;
        if (!(o instanceof Excel_spoolerKey)) return false;
        Excel_spoolerKey k = (Excel_spoolerKey) o;
        return compareKey(getPg_estrazione(), k.getPg_estrazione());
    }

    public int primaryKeyHashCode() {
        int i = 0;
        i = i + calculateKeyHashCode(getPg_estrazione());
        return i;
    }

    public java.lang.Long getPg_estrazione() {
        return pg_estrazione;
    }

    public void setPg_estrazione(java.lang.Long pg_estrazione) {
        this.pg_estrazione = pg_estrazione;
    }
}