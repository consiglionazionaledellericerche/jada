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

import javax.servlet.ServletInputStream;
import java.io.*;

public class FilePart extends Part {

    private String fileName;
    private String filePath;
    private String contentType;
    private PartInputStream partInput;

    FilePart(String name, ServletInputStream in, String boundary, String contentType, String fileName, String filePath)
            throws IOException {
        super(name);
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        partInput = new PartInputStream(in, boundary);
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public InputStream getInputStream() {
        return partInput;
    }

    public boolean isFile() {
        return true;
    }

    long write(OutputStream out)
            throws IOException {
        if (contentType.equals("application/x-macbinary"))
            out = new MacBinaryDecoderOutputStream(out);
        long size = 0L;
        byte[] buf = new byte[8192];
        int read;
        while ((read = partInput.read(buf)) != -1) {
            out.write(buf, 0, read);
            size += read;
        }
        return size;
    }

    public long writeTo(File fileOrDirectory)
            throws IOException {
        long written = 0L;
        OutputStream fileOut = null;
        try {
            if (fileName != null) {
                File file;
                if (fileOrDirectory.isDirectory())
                    file = new File(fileOrDirectory, fileName);
                else
                    file = fileOrDirectory;
                fileOut = new BufferedOutputStream(new FileOutputStream(file));
                written = write(fileOut);
            }
        } finally {
            if (fileOut != null)
                fileOut.close();
        }
        return written;
    }

    public long writeTo(OutputStream out)
            throws IOException {
        long size = 0L;
        if (fileName != null)
            size = write(out);
        return size;
    }
}