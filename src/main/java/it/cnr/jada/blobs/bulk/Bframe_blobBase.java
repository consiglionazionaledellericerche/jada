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
//            Bframe_blobKey

public class Bframe_blobBase extends Bframe_blobKey
        implements Keyed {

    private String ds_file;
    private String ds_utente;
    private String stato;
    private String ti_visibilita;

    public Bframe_blobBase() {
    }

    public Bframe_blobBase(String cd_tipo, String filename, String path) {
        super(cd_tipo, filename, path);
    }

    public String getDs_file() {
        return ds_file;
    }

    public void setDs_file(String ds_file) {
        this.ds_file = ds_file;
    }

    public String getDs_utente() {
        return ds_utente;
    }

    public void setDs_utente(String ds_utente) {
        this.ds_utente = ds_utente;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getTi_visibilita() {
        return ti_visibilita;
    }

    public void setTi_visibilita(String ti_visibilita) {
        this.ti_visibilita = ti_visibilita;
    }
}