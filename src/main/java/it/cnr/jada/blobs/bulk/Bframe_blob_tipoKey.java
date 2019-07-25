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

package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Bframe_blob_tipoKey extends OggettoBulk
        implements KeyedPersistent {

    private String cd_tipo;

    public Bframe_blob_tipoKey() {
    }

    public Bframe_blob_tipoKey(String cd_tipo) {
        this.cd_tipo = cd_tipo;
    }

    public boolean equalsByPrimaryKey(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Bframe_blob_tipoKey))
            return false;
        Bframe_blob_tipoKey k = (Bframe_blob_tipoKey) o;
        return compareKey(getCd_tipo(), k.getCd_tipo());
    }

    public String getCd_tipo() {
        return cd_tipo;
    }

    public void setCd_tipo(String cd_tipo) {
        this.cd_tipo = cd_tipo;
    }

    public int primaryKeyHashCode() {
        return calculateKeyHashCode(getCd_tipo());
    }
}