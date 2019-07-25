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


// Referenced classes of package it.cnr.jada.blobs.bulk:
//            Bframe_blobBulk, Bframe_blobKey

public class Bframe_blob_pathBulk extends Bframe_blobBulk {

    private String relativepath;
    private Boolean fl_dir;

    public Bframe_blob_pathBulk() {
    }

    public Bframe_blob_pathBulk(String cd_tipo, String filename, String path) {
        super(cd_tipo, filename, path);
    }

    public Boolean getFl_dir() {
        return fl_dir;
    }

    public void setFl_dir(Boolean newFl_dir) {
        fl_dir = newFl_dir;
    }

    public String getItemname() {
        if (getFl_dir() == null || !getFl_dir().booleanValue())
            return getFilename();
        else
            return "[" + getFilename() + "]";
    }

    public String getRelativepath() {
        return relativepath;
    }

    public void setRelativepath(String newRelativepath) {
        relativepath = newRelativepath;
    }

    public boolean isDirectory() {
        return getFl_dir() != null && getFl_dir().booleanValue();
    }
}