package it.cnr.jada.blobs.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Bframe_blob_pathBulk;
import it.cnr.jada.blobs.bulk.Excel_blobBulk;
import it.cnr.jada.blobs.bulk.Selezione_blob_tipoVBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.RicercaComponentSession;
import it.cnr.jada.util.RemoteIterator;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Dictionary;
import javax.ejb.Remote;
@Remote
public interface BframeBlobComponentSession	extends RicercaComponentSession{
	public abstract void elimina(UserContext usercontext, Bframe_blob_pathBulk abframe_blob_pathbulk[])
		throws ComponentException, RemoteException;

	public abstract RemoteIterator getBlobChildren(UserContext usercontext, Bframe_blob_pathBulk bframe_blob_pathbulk, String s)
		throws ComponentException, RemoteException;

	public abstract Selezione_blob_tipoVBulk getSelezione_blob_tipo(UserContext usercontext)
		throws ComponentException, RemoteException;
        
	public abstract java.io.File estraiFileExcel(UserContext param0, String param1)
		throws ComponentException, RemoteException,it.cnr.jada.persistency.PersistencyException;
		
	public abstract void caricaFileExcel(UserContext param0, String param1,Dictionary param2,File param3,RemoteIterator param4,String param5)	
		throws ComponentException, RemoteException,it.cnr.jada.persistency.PersistencyException;
	void InsertBlob(UserContext context) throws ComponentException,  Exception;
	void DeleteAllExcelBlob(UserContext context,Excel_blobBulk[] array)throws ComponentException,RemoteException;
}