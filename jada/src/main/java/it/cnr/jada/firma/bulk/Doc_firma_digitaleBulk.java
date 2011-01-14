/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/04/2009
 */
package it.cnr.jada.firma.bulk;
import java.io.File;
import java.util.StringTokenizer;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Doc_firma_digitaleBulk extends Doc_firma_digitaleBase {
	private java.lang.String blob;
	private File file;
	public Doc_firma_digitaleBulk() {
		super();
	}
	public Doc_firma_digitaleBulk(java.lang.Long id_documento) {
		super(id_documento);
	}
	public boolean isROAttivaFileBlob() {
		return this == null;
	}
	public java.lang.String getBlob () {
		return blob;
	}
	public void setBlob(java.lang.String blob)  {
		this.blob=blob;
	}
	public String getDownloadUrl()
	{
		if(this == null ||this.getNome_file() == null)
			return null;
		StringBuffer stringbuffer = new StringBuffer("download_file/download_doc_firma_digitale");
		stringbuffer.append("/");
		stringbuffer.append(this.getId_documento());
		stringbuffer.append("/");
		stringbuffer.append(this.getNome_file());	
		return stringbuffer.toString();
	}
	public static String parseFilename(String file) {
		StringTokenizer fileName = new StringTokenizer(file,"\\",false);
		String newFileName = null;
		
		while (fileName.hasMoreTokens()){
			newFileName = fileName.nextToken();   	
		}

		if (newFileName != null)
			return newFileName;
		
		return file;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}