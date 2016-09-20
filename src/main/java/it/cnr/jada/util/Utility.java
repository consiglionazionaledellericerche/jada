package it.cnr.jada.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

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
		File f = new File(pathDir+"/"+pathFile);
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
			IOUtils.copy(new ByteArrayInputStream(Base64.decodeBase64(data.getBytes())), new FileOutputStream(pathDir+"/"+pathFile));
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}
}
