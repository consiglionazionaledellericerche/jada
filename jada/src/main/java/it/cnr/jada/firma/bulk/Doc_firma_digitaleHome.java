/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/04/2009
 */
package it.cnr.jada.firma.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Doc_firma_digitaleHome extends BulkHome {
	public Doc_firma_digitaleHome(Connection conn) {
		super(Doc_firma_digitaleBulk.class, conn);
	}
	public Doc_firma_digitaleHome(Connection conn, PersistentCache persistentCache) {
		super(Doc_firma_digitaleBulk.class, conn, persistentCache);
	}
}