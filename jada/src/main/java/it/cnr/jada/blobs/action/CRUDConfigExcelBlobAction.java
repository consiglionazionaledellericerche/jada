package it.cnr.jada.blobs.action;

import java.io.File;
import java.io.FileInputStream;

import it.cnr.jada.action.*;
import it.cnr.jada.blobs.bp.*;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.blobs.comp.*;
import it.cnr.jada.blobs.ejb.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.upload.UploadedFile;

/**
 * Insert the type's description here.
 * Creation date: (16/05/2003 16.51.29)
 * @author: Spasia
 */
public class CRUDConfigExcelBlobAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDBframeBlobBP constructor comment.
 */
  private static final long LUNGHEZZA_MAX=0x1000000;

public CRUDConfigExcelBlobAction() {
	super();
}
public Forward doSalva(ActionContext context) {
	it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
	CRUDConfigExcelBlobBP bp = (CRUDConfigExcelBlobBP)httpContext.getBusinessProcess();
	Excel_blobBulk excelBlobBulk = (Excel_blobBulk)bp.getModel();
  //Recupero eventuale File Excel da inserire 	
  UploadedFile file =httpContext.getMultipartParameter("main.blob");		
  try{
	  if (file == null || file.getName().equals("")){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");
	  }
	  if (file.length() > LUNGHEZZA_MAX){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: la dimensione del file è superiore alla massima consentita (10 Mb).");
	  }		
	  /* Nome (compreso di Path) del file selezionato*/
	  String fileName = file.getName();	
	  excelBlobBulk.setTipo(Excel_blobBulk.TIPO_MANUALE);	  
      if (excelBlobBulk.getNome_file()==null){
		Excel_blobBulk excel = (Excel_blobBulk)((it.cnr.jada.blobs.ejb.ConfigExcelComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("BFRAMEBLOBS_EJB_ConfigExcelComponentSession", it.cnr.jada.blobs.ejb.ConfigExcelComponentSession.class)).completaOggetto(context.getUserContext(), excelBlobBulk, fileName);
		if(excel != null)
		  throw new it.cnr.jada.comp.ApplicationException("Attenzione: il file '"+Excel_blobBulk.parseFilename(fileName)+"' è già presente in archivio.");
		excelBlobBulk.setNome_file(Excel_blobBulk.parseFilename(fileName));		  		            
      }
	  super.doSalva(context);
	  excelBlobBulk = (Excel_blobBulk)bp.getModel();	  
	  /*Inserimento nella colonna BLOB*/
	  	excelBlobBulk=((it.cnr.jada.blobs.ejb.ConfigExcelComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("BFRAMEBLOBS_EJB_ConfigExcelComponentSession", it.cnr.jada.blobs.ejb.ConfigExcelComponentSession.class)).InsertBlob(context.getUserContext(), excelBlobBulk, file.getFile());
		bp.reset(context, excelBlobBulk);
		bp.setStatus(bp.EDIT);
		  }
	catch(Throwable ex)
	{
		return handleException(context, ex);
	}	 	
	
    return context.findDefaultForward();	
 }	
}
