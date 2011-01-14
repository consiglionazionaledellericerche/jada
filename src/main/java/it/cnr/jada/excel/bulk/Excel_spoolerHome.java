/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Excel_spoolerHome extends BulkHome {
	public Excel_spoolerHome(java.sql.Connection conn) {
		super(Excel_spoolerBulk.class, conn);
	}
	public Excel_spoolerHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Excel_spoolerBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		((Excel_spoolerBulk)bulk).setPg_estrazione(new Long(fetchNextSequenceValue(userContext,"CNRSEQ00_PG_ESTRAZIONE_EXCEL").longValue()));
		super.initializePrimaryKeyForInsert(userContext, bulk);
	}
	/**
	 * Ritorna un SQLBuilder con la columnMap del ricevente
	 */
	public SQLBuilder selectJobsToDelete() {

		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("PARAMETRI_ENTE");
		sql.addSQLClause("AND", "PARAMETRI_ENTE.ATTIVO", sql.EQUALS, "Y");
		sql.addSQLClause("AND", "EXCEL_SPOOLER.STATO", sql.EQUALS, "S");	
		sql.addSQLClause("AND", "EXCEL_SPOOLER.DT_PROSSIMA_ESECUZIONE", SQLBuilder.ISNULL, null);
		sql.addSQLClause("AND", "TRUNC(SYSDATE - EXCEL_SPOOLER.DUVA) > Nvl(PARAMETRI_ENTE.CANCELLA_STAMPE,30)");		
		return sql;
	}	
	public void deleteRiga(Excel_spoolerBulk bulk) throws PersistencyException{
		delete(bulk, null);
	}	
}