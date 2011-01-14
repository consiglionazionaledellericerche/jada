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
public class Excel_spooler_param_columnHome extends BulkHome {
	public Excel_spooler_param_columnHome(java.sql.Connection conn) {
		super(Excel_spooler_param_columnBulk.class, conn);
	}
	public Excel_spooler_param_columnHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Excel_spooler_param_columnBulk.class, conn, persistentCache);
	}
}