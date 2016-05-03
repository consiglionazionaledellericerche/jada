/*
 * Created on Feb 7, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.blobs.bp;

import it.cnr.jada.blobs.bulk.Excel_blobBulk;

/**
 * @author max
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigExcelBlobBP extends it.cnr.jada.util.action.SimpleCRUDBP
{
	public CRUDConfigExcelBlobBP(){
		super();
	}
	public CRUDConfigExcelBlobBP(String function){
		super(function);
	}	
	public Excel_blobBulk getBlob_path()
	{
		return (Excel_blobBulk)getModel();
	}
	public String getDownloadUrl()
	{
		Excel_blobBulk excel_blob = getBlob_path();
		if(excel_blob == null ||excel_blob.getNome_file() == null ||!excel_blob.isEseguito())
			return "";
		StringBuffer stringbuffer = new StringBuffer("download_excel/fileExcel");
		stringbuffer.append("/");
		stringbuffer.append(excel_blob.getCd_utente());
		stringbuffer.append("/");
		stringbuffer.append(excel_blob.getNome_file());	
		return stringbuffer.toString();
	}
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/

	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {

			openForm(context,action,target,"multipart/form-data");
	
	}	
}
