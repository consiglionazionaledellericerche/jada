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

import java.util.Dictionary;
import java.util.List;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blob_tipoBulk

public class Selezione_blob_tipoVBulk extends OggettoBulk {

    private Bframe_blob_tipoBulk blob_tipo;
    private List blob_tipoList;
    private String ti_visibilita;

    public Selezione_blob_tipoVBulk() {
    }

    public Bframe_blob_tipoBulk getBlob_tipo() {
        return blob_tipo;
    }

    public void setBlob_tipo(Bframe_blob_tipoBulk newBlob_tipo) {
        blob_tipo = newBlob_tipo;
    }

    public List getBlob_tipoList() {
        return blob_tipoList;
    }

    public void setBlob_tipoList(List newBlob_tipoList) {
        blob_tipoList = newBlob_tipoList;
    }

    public String getTi_visibilita() {
        return ti_visibilita;
    }

    public void setTi_visibilita(String newTi_visibilita) {
        ti_visibilita = newTi_visibilita;
    }

    public final Dictionary getTi_visibilitaKeys() {
        return Bframe_blob_tipoBulk.getTi_visibilitaKeys();
    }
}