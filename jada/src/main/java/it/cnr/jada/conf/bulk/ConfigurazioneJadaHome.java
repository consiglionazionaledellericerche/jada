/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2010
 */
package it.cnr.jada.conf.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ConfigurazioneJadaHome extends BulkHome {
	public ConfigurazioneJadaHome(Connection conn) {
		super(ConfigurazioneJadaBulk.class, conn);
	}
	public ConfigurazioneJadaHome(Connection conn, PersistentCache persistentCache) {
		super(ConfigurazioneJadaBulk.class, conn, persistentCache);
	}
}