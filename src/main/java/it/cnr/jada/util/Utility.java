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

package it.cnr.jada.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class Utility {
    /**
     * Salva il file con nome "pathFile" creando tutte le directory non esistenti nel percorso "pathDir"
     * utilizzando i dati in "data"
     *
     * @param pathFile
     * @param pathDir
     * @param data
     * @throws IOException
     */
    public static void saveFile(String pathFile, String pathDir, String data) throws IOException {
        File d = new File(pathDir);
        d.mkdirs();
        File f = new File(pathDir + "/" + pathFile);
        FileWriter fw = new FileWriter(f);
        fw.write(data);
        fw.flush();
        fw.close();
    }

    public static void deleteFile(String pathFile) throws IOException {
        File f = new File(pathFile);
        f.delete();
    }

    /**
     * Salva il file con nome "pathFile" creando tutte le directory non esistenti nel percorso "pathDir"
     * decodificando i dati in "data" da base64 in der
     *
     * @param pathFile
     * @param pathDir
     * @param data
     * @throws IOException
     * @throws CapiException
     */
    public static void decodeAndSaveFile(String pathFile, String pathDir, String data) throws IOException {
        File d = new File(pathDir);
        d.mkdirs();
        try {
            IOUtils.copy(new ByteArrayInputStream(Base64.decodeBase64(data.getBytes())), new FileOutputStream(pathDir + "/" + pathFile));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
