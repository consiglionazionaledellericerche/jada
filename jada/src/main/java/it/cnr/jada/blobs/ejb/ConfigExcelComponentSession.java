package it.cnr.jada.blobs.ejb;
import it.cnr.jada.UserContext;
import it.cnr.jada.blobs.bulk.Excel_blobBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import javax.ejb.Remote;

@Remote
public interface ConfigExcelComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
Excel_blobBulk InsertBlob(UserContext userContext,Excel_blobBulk param1,java.io.File file)throws ComponentException,java.rmi.RemoteException;
Excel_blobBulk completaOggetto(UserContext userContext,Excel_blobBulk excelBlobBulk,String fileName)throws ComponentException, PersistencyException,java.rmi.RemoteException;
}
