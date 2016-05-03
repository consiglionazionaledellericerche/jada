package it.cnr.jada.blobs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Excel_blobHome extends BulkHome {
public Excel_blobHome(java.sql.Connection conn) {
	super(Excel_blobBulk.class,conn);
}
public Excel_blobHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Excel_blobBulk.class,conn,persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder selectJobsToDelete() {

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("PARAMETRI_ENTE");
	sql.addSQLClause("AND", "PARAMETRI_ENTE.ATTIVO", sql.EQUALS, "Y");
	sql.addSQLClause("AND", "EXCEL_BLOB.STATO", sql.EQUALS, "S");	
	sql.addSQLClause("AND", "TRUNC(SYSDATE - EXCEL_BLOB.DUVA) > Nvl(PARAMETRI_ENTE.CANCELLA_STAMPE,30)");		
	return sql;
}
public void deleteRiga(Excel_blobBulk bulk) throws PersistencyException{
	delete(bulk, null);
}
}