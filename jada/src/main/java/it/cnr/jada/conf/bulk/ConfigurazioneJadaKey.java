/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2010
 */
package it.cnr.jada.conf.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ConfigurazioneJadaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdChiave;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFIGURAZIONE_JADA
	 **/
	public ConfigurazioneJadaKey() {
		super();
	}
	public ConfigurazioneJadaKey(java.lang.String cdChiave) {
		super();
		this.cdChiave=cdChiave;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ConfigurazioneJadaKey)) return false;
		ConfigurazioneJadaKey k = (ConfigurazioneJadaKey) o;
		if (!compareKey(getCdChiave(), k.getCdChiave())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdChiave());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdChiave]
	 **/
	public void setCdChiave(java.lang.String cdChiave)  {
		this.cdChiave=cdChiave;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdChiave]
	 **/
	public java.lang.String getCdChiave() {
		return cdChiave;
	}
}