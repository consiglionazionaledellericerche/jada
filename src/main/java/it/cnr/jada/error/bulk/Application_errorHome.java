/*
* Created by Generator 1.0
* Date 02/09/2005
*/
package it.cnr.jada.error.bulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Application_errorHome extends BulkHome {
	public Application_errorHome(java.sql.Connection conn) {
		super(Application_errorBulk.class, conn);
	}
	public Application_errorHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Application_errorBulk.class, conn, persistentCache);
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.bulk.BulkHome#initializePrimaryKeyForInsert(it.cnr.jada.UserContext, it.cnr.jada.bulk.OggettoBulk)
	 */
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			((Application_errorBulk)oggettobulk).setProgressivo(
				new Long(
					((Long)findAndLockMax( oggettobulk, "progressivo", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}		
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}

}