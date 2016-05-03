package it.cnr.jada.firma.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import it.cnr.jada.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.firma.bulk.Doc_firma_digitaleBulk;
import it.cnr.jada.firma.bulk.Doc_firma_digitaleHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class FirmaComponent extends CRUDComponent {

    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk oggettobulk) throws ComponentException {
    	Doc_firma_digitaleBulk doc = (Doc_firma_digitaleBulk)oggettobulk;
		Doc_firma_digitaleHome archiveHome = (Doc_firma_digitaleHome)getHome(userContext,Doc_firma_digitaleBulk.class);
    	File file = doc.getFile();

    	try {
    		java.sql.Timestamp data = archiveHome.getServerTimestamp();
	    	doc.setId_documento(new Long(archiveHome.fetchNextSequenceValue(userContext,"CNRSEQ00_ID_DOC_FIRMA_DIGITALE").intValue()));
	    	doc.setCd_utente(userContext.getUser());
	    	doc.setData(data);
		} catch (PersistencyException e) {
			throw new ComponentException(e);	
		}

    	OggettoBulk newBulk = super.creaConBulk(userContext, oggettobulk);
    	archiviaFileFirmato(userContext, (Doc_firma_digitaleBulk)newBulk, file);
    	return newBulk;
    }
  
    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk oggettobulk) throws ComponentException{
    	File file = ((Doc_firma_digitaleBulk)oggettobulk).getFile();
    	OggettoBulk newBulk = super.modificaConBulk(userContext, oggettobulk);
		if (!(file == null || file.getName().equals(""))) { 
			archiviaFileFirmato(userContext, (Doc_firma_digitaleBulk)newBulk, file);
		}
    	return newBulk;
    }

    private void archiviaFileFirmato(UserContext userContext, Doc_firma_digitaleBulk doc, File file) throws ComponentException{
		Doc_firma_digitaleHome archiveHome = (Doc_firma_digitaleHome)getHome(userContext,Doc_firma_digitaleBulk.class);
		try {
			oracle.sql.BLOB blob = (oracle.sql.BLOB)archiveHome.getSQLBlob(doc,"BDATA");
			java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(file));
			byte[] byteArr = new byte[1024];
			java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
			int len;			
			while ((len = in.read(byteArr))>0){
			   os.write(byteArr,0,len);
			}
			os.close();
			in.close();
		} catch (PersistencyException e) {
			throw new ComponentException(e);	
		} catch (FileNotFoundException e) {
			throw new ComponentException(e);	
		} catch (IOException e) {
			throw new ComponentException(e);	
		} catch (SQLException e) {
			throw new ComponentException(e);	
		}
	}

    protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
    {
    	SQLBuilder sql = (SQLBuilder)super.select( userContext, clauses, bulk );
    	sql.addSQLClause("AND", "CD_UTENTE", sql.EQUALS, userContext.getUser());
    	sql.addOrderBy("CD_UTENTE, DATA DESC");	
    	return sql;
    }
}
