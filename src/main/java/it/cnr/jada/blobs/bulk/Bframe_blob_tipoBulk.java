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

import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blob_tipoBase, Bframe_blob_tipoKey

public class Bframe_blob_tipoBulk extends Bframe_blob_tipoBase {

    public static final String TI_VISIBILITA_PUBLICO = "P";
    public static final String TI_VISIBILITA_UTENTE = "U";
    public static final String TI_VISIBILITA_CDR = "C";
    public static final String TI_VISIBILITA_UNITA_ORGANIZZATIVA = "O";
    public static final String TI_VISIBILITA_CDS = "S";
    public static final String TI_VISIBILITA_CNR = "N";
    private static final Dictionary ti_visibilitaKeys;

    static {
        ti_visibilitaKeys = new OrderedHashtable();
        ti_visibilitaKeys.put("U", "Utente");
        ti_visibilitaKeys.put("P", "Pubblico");
    }
    public Bframe_blob_tipoBulk() {
    }
    public Bframe_blob_tipoBulk(String cd_tipo) {
        super(cd_tipo);
    }

    public static final Dictionary getTi_visibilitaKeys() {
        return ti_visibilitaKeys;
    }

    public String getCd_ds_tipo() {
        return getCd_tipo() + " - " + getDs_tipo();
    }
}