package it.cnr.jada.blobs.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.io.*;


public class ConfigExcelComponent  extends CRUDComponent
	implements Cloneable, Serializable
{
   
	public ConfigExcelComponent()
	{
	}

	public Excel_blobBulk InsertBlob(UserContext context, Excel_blobBulk excelBlobBulk,java.io.File file) throws ComponentException{	 
		  /*Inserimento nella colonna BLOB*/
		  try 
		  {
			  Excel_blobHome home = (Excel_blobHome)getHome(context,excelBlobBulk);
			  java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(file));
			  byte[] byteArr = new byte[1024];
			  oracle.sql.BLOB blob = (oracle.sql.BLOB)getHome(context,excelBlobBulk).getSQLBlob(excelBlobBulk,"BDATA");
			  java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
			  int len;					
			  while ((len = in.read(byteArr))>0){
				  os.write(byteArr,0,len);
			  }
			  os.close();
			  in.close();
			  excelBlobBulk.setStato(Excel_blobBulk.STATO_ESEGUITO);
			  home.update(excelBlobBulk, context);
		  } catch(Throwable e) {
			  throw  handleException(e);
		  } finally {					
			  file.delete();
		  }		
		  return excelBlobBulk;
	}
	public Excel_blobBulk completaOggetto(UserContext userContext,Excel_blobBulk excelBlobBulk,String fileName)throws ComponentException, PersistencyException{
	 return ((Excel_blobBulk)getHome(userContext,Excel_blobBulk.class).findByPrimaryKey(new Excel_blobBulk(excelBlobBulk.getCd_utente(),Excel_blobBulk.parseFilename(fileName))));
	}
}