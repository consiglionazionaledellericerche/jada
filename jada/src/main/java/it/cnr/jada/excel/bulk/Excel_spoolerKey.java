/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Excel_spoolerKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_estrazione;
	public Excel_spoolerKey() {
		super();
	}
	public Excel_spoolerKey(java.lang.Long pg_estrazione) {
		super();
		this.pg_estrazione=pg_estrazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Excel_spoolerKey)) return false;
		Excel_spoolerKey k = (Excel_spoolerKey) o;
		if (!compareKey(getPg_estrazione(), k.getPg_estrazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_estrazione());
		return i;
	}
	public void setPg_estrazione(java.lang.Long pg_estrazione)  {
		this.pg_estrazione=pg_estrazione;
	}
	public java.lang.Long getPg_estrazione () {
		return pg_estrazione;
	}
}