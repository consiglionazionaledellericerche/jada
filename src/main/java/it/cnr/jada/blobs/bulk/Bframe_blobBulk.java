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
//            Bframe_blobBase

public class Bframe_blobBulk extends Bframe_blobBase {

    public static final String STATO_IN_CODA = "C";
    public static final String STATO_IN_ESECUZIONE = "X";
    public static final String STATO_ERRORE = "E";
    public static final String STATO_ESEGUITA = "S";
    private static final Dictionary statoKeys;

    static {
        statoKeys = new OrderedHashtable();
        statoKeys.put("C", "In coda");
        statoKeys.put("X", "In esecuzione");
        statoKeys.put("E", "Errore");
        statoKeys.put("S", "Eseguita");
    }
    public Bframe_blobBulk() {
    }
    public Bframe_blobBulk(String cd_tipo, String filename, String path) {
        super(cd_tipo, filename, path);
    }

    public static final Dictionary getStatoKeys() {
        return statoKeys;
    }
}