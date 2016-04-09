package it.cnr.jada.blobs.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.blobs.bulk.*;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persister;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.RemoteIteratorEnumeration;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.ejb.HttpEJBCleaner;

import java.io.*;
import java.math.BigDecimal;
import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import javax.ejb.Handle;
import javax.jms.*;
import javax.naming.*;

import org.apache.poi.hssf.usermodel.*;

// Referenced classes of package it.cnr.jada.blobs.comp:
//			  IBframeBlobMgr

public class BframeBlobComponent extends RicercaComponent
	implements IBframeBlobMgr, Cloneable, Serializable, Component
{
    private static final Integer NUMERO_MAX_RIGHE = new Integer(25000);
	public BframeBlobComponent()
	{
	}

	public void elimina(UserContext userContext, Bframe_blob_pathBulk blob_path[])
		throws ComponentException
	{
		try
		{
			Bframe_blobHome home = (Bframe_blobHome)getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blobBulk.class);
			for(int i = 0; i < blob_path.length; i++)
			{
				if(blob_path[i].isDirectory())
					throw new ApplicationException("Non \350 possibile eliminare le cartelle.");
				home.delete(new Bframe_blobKey(blob_path[i].getCd_tipo(), blob_path[i].getFilename(), blob_path[i].getRelativepath()), userContext);
			}

		}
		catch(Throwable e)
		{
			throw handleException(e);
		}
	}
	public RemoteIterator getBlobChildren(UserContext userContext, Bframe_blob_pathBulk blob_path, String ti_visibilita)
		throws ComponentException
	{
		getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class);
		String cd_tipo = blob_path.getCd_tipo();
		String abspath = blob_path.getPath() + blob_path.getFilename();
		String relativepath = blob_path.getFilename() != null ? blob_path.getRelativepath() != null ? blob_path.getRelativepath() + blob_path.getFilename() + "/" : blob_path.getFilename() + "/" : "";
		abspath.length();
		int relativepath_length = relativepath.length();
		SQLBuilder sql_dirs = getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, "V_BFRAME_BLOB_DIR").createSQLBuilder();
		if(relativepath_length > 0)
			sql_dirs.addClause("AND", "relativepath", 40968, relativepath + "_");
		sql_dirs.addClause("AND", "cd_tipo", 8192, cd_tipo);
		sql_dirs.setDistinctClause(true);
		SQLBuilder sql_files = getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, "V_BFRAME_BLOB_FILE").createSQLBuilder();
		if(relativepath_length == 0)
			sql_files.addClause("AND", "relativepath", 8201, null);
		else
			sql_files.addClause("AND", "relativepath", 8192, relativepath);
		if("U".equals(ti_visibilita))
		{
			sql_files.addClause("AND", "ti_visibilita", 8192, ti_visibilita);
			sql_files.addClause("AND", "utcr", 8192, userContext.getUser());
		} else
		if("P".equals(ti_visibilita))
		{
			sql_files.openParenthesis("AND");
			sql_files.addClause("AND", "ti_visibilita", 8192, ti_visibilita);
			sql_files.addClause("OR", "ti_visibilita", 8201, null);
			sql_files.closeParenthesis();
		}
		sql_files.addClause("AND", "cd_tipo", 8192, cd_tipo);
		sql_files.resetColumns();
		sql_dirs.resetColumns();
		ColumnMapping mapping;
		for(Iterator i = getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, "V_BFRAME_BLOB_FILE").getColumnMap().getColumnMappings().iterator(); i.hasNext(); sql_files.addColumn(mapping.getColumnName()))
		{
			mapping = (ColumnMapping)i.next();
			if(mapping.getPropertyName().equals("relativepath"))
				sql_dirs.addColumn("'" + relativepath + "'", mapping.getColumnName());
			else
			if(mapping.getPropertyName().equals("path"))
				sql_dirs.addColumn("'" + abspath + "'", mapping.getColumnName());
			else
			if(mapping.getPropertyName().equals("filename"))
				sql_dirs.addColumn("substr(relativepath," + (relativepath_length + 1) + ",instr(relativepath,'/'," + (relativepath_length + 1) + ")-" + (relativepath_length + 1) + ")", mapping.getColumnName());
			else
				sql_dirs.addColumn(mapping.getColumnName());
		}

		SQLQuery sql = sql_dirs.union(sql_files, false);
		return iterator(userContext, sql, it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk.class, null);
	}

	public Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext userContext)
		throws ComponentException
	{
		Selezione_blob_tipoVBulk selezione = new Selezione_blob_tipoVBulk();
		try
		{
			selezione.setBlob_tipo((Bframe_blob_tipoBulk)getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findByPrimaryKey(new Bframe_blob_tipoKey("tipo1")));
			selezione.setBlob_tipoList(getHome(userContext, it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findAll());
			selezione.setTi_visibilita("U");
		}
		catch(PersistencyException e)
		{
			throw handleException(e);
		}
		return selezione;
	}
	protected it.cnr.jada.persistency.sql.Query select(
		UserContext userContext,
		CompoundFindClause clauses,
		OggettoBulk bulk)
		throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	    
		it.cnr.jada.persistency.sql.SQLBuilder sql = 
			(it.cnr.jada.persistency.sql.SQLBuilder)super.select(userContext, clauses, bulk);
		if(bulk instanceof Excel_blobBulk){	
		  sql.addSQLClause("AND", "CD_UTENTE", sql.EQUALS, userContext.getUser());
		  sql.addOrderBy("DUVA DESC");
		}		  
		return sql;
	}	    
	public java.io.File estraiFileExcel(UserContext userContext, String longDescription)
	   throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		File excelFile;
		try{
			 excelFile = File.createTempFile(longDescription,".xls");
			 //Creo inizialmente la riga in Excel_blob
			 Excel_blobBulk excelBlobBulk = new Excel_blobBulk(userContext.getUser(),excelFile.getName());
			 BulkHome home = getHome(userContext, Excel_blobBulk.class);  
			 excelBlobBulk.setStato(Excel_blobBulk.STATO_IN_ESECUZIONE);
			 excelBlobBulk.setTipo(Excel_blobBulk.TIPO_AUTOMATICO);
			 excelBlobBulk.setUser(userContext.getUser());
			 excelBlobBulk.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			 makeBulkPersistent(userContext,excelBlobBulk);
			 return excelFile;
		}
		catch (FileNotFoundException e)
		{
			throw new ApplicationException("File non trovato");
		}
		catch (IOException e)
		{
			throw new ComponentException(e);
		}
	   	
		
	}	
	public void caricaFileExcel(UserContext param0, String longDescription,Dictionary columns,File excelFile,RemoteIterator remoteIterator,String user)	
	throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		new Thread(new ExcelThread(longDescription,columns,excelFile,remoteIterator,user)).start();
		/*try{	
		  java.sql.Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
		  conn.setAutoCommit(false);
		  it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
		  Excel_blobBulk excelBlobBulk = (Excel_blobBulk)homeCache.getHome(Excel_blobBulk.class).findByPrimaryKey(new Excel_blobBulk(user,excelFile.getName()));  
		  BulkHome home = (BulkHome)homeCache.getHome(excelBlobBulk);		  
		  try{
			oracle.sql.BLOB blob = (oracle.sql.BLOB)home.getSQLBlob(excelBlobBulk,"BDATA");
			FileOutputStream excelOutput = new FileOutputStream(excelFile);
            if (remoteIterator.countElements() < NUMERO_MAX_RIGHE.intValue()){				 			   	 
				HSSFWorkbook wb = new HSSFWorkbook(); // Istanzio la classe workbook
				HSSFSheet s = wb.createSheet(); // creo un foglio
				HSSFRow r = null; // dichiaro r di tipo riga
				HSSFCell c = null; // dichiaro c di tipo cella
				s.setDefaultColumnWidth((short)20);
				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				cellStyle.setFont(font);
				wb.setSheetName(0, longDescription,HSSFWorkbook.ENCODING_COMPRESSED_UNICODE );
				short cellnum = (short) -1;
				r = s.createRow(0); //creo la prima riga
				for(Enumeration enumeration1 = columns.elements(); enumeration1.hasMoreElements();)//comincio col creare l'intestazione delle colonne
				{
					ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
					cellnum++;
					c = r.createCell(cellnum);
					c.setCellValue(columnfieldproperty.getLabel());
					c.setCellStyle(cellStyle);
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				int j = 0;
				for(Enumeration enumeration2 =  new RemoteIteratorEnumeration(remoteIterator); enumeration2.hasMoreElements(); j++)
				{
					Object obj = enumeration2.nextElement();
					r = s.createRow(j+1);
					cellnum = (short) -1;
					for(Enumeration enumeration3 = columns.elements(); enumeration3.hasMoreElements();)
					{
							cellnum++;
							ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
							c = r.createCell((short)cellnum);
							Object obj2 = Introspector.getPropertyValue(obj,columnfieldproperty1.getProperty());
							String valoreStringa = columnfieldproperty1.getStringValueFrom(obj,Introspector.getPropertyValue(obj, columnfieldproperty1.getProperty()));
							if(obj2 != null){									
							  if (obj2 instanceof String){								   
								c.setCellType(HSSFCell.CELL_TYPE_STRING);
								c.setCellValue(valoreStringa);
							  }else if (obj2 instanceof Number){								   
								c.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								c.setCellValue(new BigDecimal(obj2.toString()).doubleValue());
							  }else if (obj2 instanceof Timestamp){								   
								c.setCellType(HSSFCell.CELL_TYPE_STRING);
								c.setCellValue(valoreStringa);
							  }else{								   
								c.setCellType(HSSFCell.CELL_TYPE_STRING);
								c.setCellValue(valoreStringa);
							  }    
							}  
					}
				}	
				wb.write(excelOutput);// assegno lo stream al FileOutputStream
			}else{
				excelBlobBulk.setDs_file("Il file è stato estratto in formato testo, poichè le righe superano il limite consentito.("+NUMERO_MAX_RIGHE+").");
				excelOutput.write("<table>".getBytes());
				excelOutput.write("<tr>".getBytes());
				for(Enumeration enumeration1 = columns.elements(); enumeration1.hasMoreElements();)//comincio col creare l'intestazione delle colonne
				{
					ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
				    excelOutput.write(("<td><H4>"+columnfieldproperty.getLabel()+"</H4></td>").getBytes());
				}
				excelOutput.write("</tr>".getBytes());
				int j = 0;
				for(Enumeration enumeration2 =  new RemoteIteratorEnumeration(remoteIterator); enumeration2.hasMoreElements(); j++)
				{
				    excelOutput.write("<tr>".getBytes());
					Object obj = enumeration2.nextElement();
					for(Enumeration enumeration3 = columns.elements(); enumeration3.hasMoreElements();)
					{
						ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
						Object obj2 = Introspector.getPropertyValue(obj,columnfieldproperty1.getProperty());
						String valoreStringa = columnfieldproperty1.getStringValueFrom(obj,Introspector.getPropertyValue(obj, columnfieldproperty1.getProperty()));
						excelOutput.write("<td>".getBytes());
						if(obj2 != null){
					      excelOutput.write(valoreStringa.getBytes());
						}
						excelOutput.write("</td>".getBytes());
					}
				    excelOutput.write("</tr>".getBytes());
				}
				excelOutput.write("</table>".getBytes());
			}
			excelOutput.close(); // chiudo il file
			   	 
			java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(excelFile));
			byte[] byteArr = new byte[1024];
			java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
			int len;			
			while ((len = in.read(byteArr))>0){
			   os.write(byteArr,0,len);
			}
			os.close();
			in.close();
			excelBlobBulk.setStato(Excel_blobBulk.STATO_ESEGUITO);
			excelBlobBulk.setDuva(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());			
		  }
		  catch(Throwable throwable){
			excelBlobBulk.setStato(Excel_blobBulk.STATO_ERRORE);
			excelBlobBulk.setDuva(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
			throw new ComponentException(throwable);
		  }		  
		  finally {
			  home.update(excelBlobBulk);
			  conn.close();
			  excelFile.delete();
			  try{
				Object obj = remoteIterator;
				if(obj instanceof BulkLoaderIterator){
				 ((BulkLoaderIterator)obj).close();
				 ((BulkLoaderIterator)obj).remove();			  
				}
				else if(obj instanceof TransactionalBulkLoaderIterator){
					((TransactionalBulkLoaderIterator)obj).remove();
				}	
			  }
			  catch(NoSuchObjectException ex){	
			  }
			  catch(Throwable _ex) {
				//Inserire nella lista per la rimozione	
			  }
		  }		  
		}
		catch(Throwable throwable)
		{
			throw new ComponentException(throwable);
		}*/
		   			
	   }
	class ExcelThread
	  implements Runnable
	{
	  private String longDescription;
	  private Dictionary columns;
	  private File excelFile;
	  private RemoteIterator remoteIterator;
	  private String user;
	  private Excel_blobBulk excelBlobBulk;
	  private java.sql.Connection conn;
	  private BulkHome home;			  
	  public void run()
	  {
	  try{	
		java.sql.Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
		conn.setAutoCommit(false);
		it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
		Excel_blobBulk excelBlobBulk = (Excel_blobBulk)homeCache.getHome(Excel_blobBulk.class).findByPrimaryKey(new Excel_blobBulk(user,excelFile.getName()));  
		BulkHome home = (BulkHome)homeCache.getHome(excelBlobBulk);		  
		try{
		  oracle.sql.BLOB blob = (oracle.sql.BLOB)home.getSQLBlob(excelBlobBulk,"BDATA");
		  FileOutputStream excelOutput = new FileOutputStream(excelFile);
		  if (remoteIterator.countElements() < NUMERO_MAX_RIGHE.intValue()){				 			   	 
			  HSSFWorkbook wb = new HSSFWorkbook(); // Istanzio la classe workbook
			  HSSFSheet s = wb.createSheet(); // creo un foglio
			  HSSFRow r = null; // dichiaro r di tipo riga
			  HSSFCell c = null; // dichiaro c di tipo cella
			  s.setDefaultColumnWidth((short)20);
			  HSSFCellStyle cellStyle = wb.createCellStyle();
			  cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
			  HSSFFont font = wb.createFont();
			  font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  cellStyle.setFont(font);
			  wb.setSheetName(0, longDescription,HSSFWorkbook.ENCODING_COMPRESSED_UNICODE );
			  short cellnum = (short) -1;
			  r = s.createRow(0); //creo la prima riga
			  for(Enumeration enumeration1 = columns.elements(); enumeration1.hasMoreElements();)//comincio col creare l'intestazione delle colonne
			  {
				  ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
				  cellnum++;
				  c = r.createCell(cellnum);
				  c.setCellValue(columnfieldproperty.getLabel());
				  c.setCellStyle(cellStyle);
				  c.setCellType(HSSFCell.CELL_TYPE_STRING);
			  }
			  int j = 0;
			  for(Enumeration enumeration2 =  new RemoteIteratorEnumeration(remoteIterator); enumeration2.hasMoreElements(); j++)
			  {
				  Object obj = enumeration2.nextElement();
				  r = s.createRow(j+1);
				  cellnum = (short) -1;
				  for(Enumeration enumeration3 = columns.elements(); enumeration3.hasMoreElements();)
				  {
						  cellnum++;
						  ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
						  c = r.createCell((short)cellnum);
						  Object obj2 = Introspector.getPropertyValue(obj,columnfieldproperty1.getProperty());
						  String valoreStringa = columnfieldproperty1.getStringValueFrom(obj,Introspector.getPropertyValue(obj, columnfieldproperty1.getProperty()));
						  if(obj2 != null){									
							if (obj2 instanceof String){								   
							  c.setCellType(HSSFCell.CELL_TYPE_STRING);
							  c.setCellValue(valoreStringa);
							}else if (obj2 instanceof Number){								   
							  c.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							  c.setCellValue(new BigDecimal(obj2.toString()).doubleValue());
							}else if (obj2 instanceof Timestamp){								   
							  c.setCellType(HSSFCell.CELL_TYPE_STRING);
							  c.setCellValue(valoreStringa);
							}else{								   
							  c.setCellType(HSSFCell.CELL_TYPE_STRING);
							  c.setCellValue(valoreStringa);
							}    
						  }  
				  }
			  }	
			  wb.write(excelOutput);// assegno lo stream al FileOutputStream
		  }else{
			  excelBlobBulk.setDs_file("Il file è stato estratto in formato testo, poichè le righe superano il limite consentito.("+NUMERO_MAX_RIGHE+").");
			  excelOutput.write("<table>".getBytes());
			  excelOutput.write("<tr>".getBytes());
			  for(Enumeration enumeration1 = columns.elements(); enumeration1.hasMoreElements();)//comincio col creare l'intestazione delle colonne
			  {
				  ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration1.nextElement();
				  excelOutput.write(("<td><H4>"+columnfieldproperty.getLabel()+"</H4></td>").getBytes());
			  }
			  excelOutput.write("</tr>".getBytes());
			  int j = 0;
			  for(Enumeration enumeration2 =  new RemoteIteratorEnumeration(remoteIterator); enumeration2.hasMoreElements(); j++)
			  {
				  excelOutput.write("<tr>".getBytes());
				  Object obj = enumeration2.nextElement();
				  for(Enumeration enumeration3 = columns.elements(); enumeration3.hasMoreElements();)
				  {
					  ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration3.nextElement();
					  Object obj2 = Introspector.getPropertyValue(obj,columnfieldproperty1.getProperty());
					  String valoreStringa = columnfieldproperty1.getStringValueFrom(obj,Introspector.getPropertyValue(obj, columnfieldproperty1.getProperty()));
					  excelOutput.write("<td>".getBytes());
					  if(obj2 != null){
						excelOutput.write(valoreStringa.getBytes());
					  }
					  excelOutput.write("</td>".getBytes());
				  }
				  excelOutput.write("</tr>".getBytes());
			  }
			  excelOutput.write("</table>".getBytes());
		  }
		  excelOutput.close(); // chiudo il file
			   	 
		  java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(excelFile));
		  byte[] byteArr = new byte[1024];
		  java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
		  int len;			
		  while ((len = in.read(byteArr))>0){
			 os.write(byteArr,0,len);
		  }
		  os.close();
		  in.close();
		  excelBlobBulk.setStato(Excel_blobBulk.STATO_ESEGUITO);
		  excelBlobBulk.setDuva(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());			
		}
		catch(Throwable throwable){
		  excelBlobBulk.setStato(Excel_blobBulk.STATO_ERRORE);
		  excelBlobBulk.setDuva(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		  throw new ComponentException(throwable);
		}		  
		finally {
			home.update(excelBlobBulk, null);
			conn.commit();
			conn.close();
			excelFile.delete();
			try{
			  Object obj = remoteIterator;
			  if(obj instanceof BulkLoaderIterator){
			   ((BulkLoaderIterator)obj).close();
			   ((BulkLoaderIterator)obj).ejbRemove();			  
			  }
			  else if(obj instanceof TransactionalBulkLoaderIterator){
				  ((TransactionalBulkLoaderIterator)obj).ejbRemove();
			  }	
			}
			catch(NoSuchObjectException ex){	
			}
			catch(Throwable _ex) {
			}
		}		  
	  }
	  catch(Throwable throwable)
	  {
	  	  System.out.println(throwable.getMessage());
		  //throw new ComponentException(throwable);
	  }				   			
    }			
    ExcelThread(String plongDescription,Dictionary pcolumns,File pexcelFile,RemoteIterator premoteIterator,String puser)
    {
	  longDescription = plongDescription;
	  columns = pcolumns;
	  excelFile = pexcelFile;
	  remoteIterator = premoteIterator;
	  user = puser;
    }
  }
 public void InsertBlob(UserContext context) throws ComponentException,  Exception{	 
		Bframe_blobBulk bframe_blob = new Bframe_blobBulk("tipo","filename","percorso");
		BulkHome home = (BulkHome)getHome(context,Bframe_blobBulk.class);
		bframe_blob.setUser(context.getUser());
		home.insert(bframe_blob, context);
		oracle.sql.BLOB blob = (oracle.sql.BLOB)getHome(context,Bframe_blobBulk.class).getSQLBlob(bframe_blob,"BDATA");
		java.io.OutputStream os = blob.getBinaryOutputStream();
		os.write("PROVA PROVA".getBytes());
		os.close();
 }
 public void DeleteAllExcelBlob(UserContext context,Excel_blobBulk[] array)throws ComponentException
 {
	 try {
			for (int i = 0;i < array.length;i++) {
				if (array[i] == null)
					throw new it.cnr.jada.comp.ApplicationException("Uno o più file sono stati cancellati.");
				if (Excel_blobBulk.STATO_IN_ESECUZIONE.equals(array[i].getStato()))
					throw new it.cnr.jada.comp.ApplicationException("Uno o più file sono attualmente in esecuzione e non possono essere cancellati.");						
				BulkHome home = (BulkHome)getHome(context,array[i]);
				home.delete(array[i], context);
			}
		} catch(PersistencyException e) {
			throw handleException(e);
		}
 }
}