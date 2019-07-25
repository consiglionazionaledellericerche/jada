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

import it.cnr.jada.persistency.Keyed;

// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blob_tipoKey

public class Bframe_blob_tipoBase extends Bframe_blob_tipoKey
        implements Keyed {

    private String ds_tipo;
    private Boolean fl_binario;
    private String root;

    public Bframe_blob_tipoBase() {
    }

    public Bframe_blob_tipoBase(String cd_tipo) {
        super(cd_tipo);
    }

    public String getDs_tipo() {
        return ds_tipo;
    }

    public void setDs_tipo(String ds_tipo) {
        this.ds_tipo = ds_tipo;
    }

    public Boolean getFl_binario() {
        return fl_binario;
    }

    public void setFl_binario(Boolean fl_binario) {
        this.fl_binario = fl_binario;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}