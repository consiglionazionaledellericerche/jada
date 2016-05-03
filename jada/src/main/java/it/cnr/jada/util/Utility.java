package it.cnr.jada.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import it.actalis.ellips.capi.CapiException;
import it.actalis.ellips.capi.Util;

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
			Util.saveToFile(Util.base64Decode(Util.getBytes(data), true), pathDir+"/"+pathFile);
		} catch (CapiException e) {
			throw new IOException(e.getMessage());
		}
	}
}
