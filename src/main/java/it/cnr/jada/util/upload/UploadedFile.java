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

package it.cnr.jada.util.upload;

import java.io.File;

public class UploadedFile {

    private final String dir;
    private final String filename;
    private final String type;
    private final String filePath;

    UploadedFile(String dir, String filename, String filePath, String type) {
        this.dir = dir;
        this.filename = filename;
        this.type = type;
        this.filePath = filePath;
    }

    public String getContentType() {
        return type;
    }

    public File getFile() {
        if (dir == null || filename == null)
            return null;
        else
            return new File(dir + File.separator + filename);
    }

    public String getFilesystemName() {
        return filename;
    }

    public String getName() {
        if (getFile() == null)
            return "";
        return getFile().getName();
    }

    public long length() {
        if (getFile() == null)
            return 0;
        return getFile().length();
    }

    public String getFilePath() {
        return filePath;
    }
}