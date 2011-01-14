/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Excel_spooler_paramHome extends BulkHome {
	public Excel_spooler_paramHome(java.sql.Connection conn) {
		super(Excel_spooler_paramBulk.class, conn);
	}
	public Excel_spooler_paramHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Excel_spooler_paramBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			((Excel_spooler_paramBulk)oggettobulk).setPg_column(
				new Long(
					((Long)findAndLockMax( oggettobulk, "pg_column", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}		
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}

}