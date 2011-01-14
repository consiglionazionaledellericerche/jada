/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Excel_spooler_paramKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_estrazione;
	private java.lang.Long pg_column;
	public Excel_spooler_paramKey() {
		super();
	}
	public Excel_spooler_paramKey(java.lang.Long pg_estrazione, java.lang.Long pg_column) {
		super();
		this.pg_estrazione=pg_estrazione;
		this.pg_column=pg_column;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Excel_spooler_paramKey)) return false;
		Excel_spooler_paramKey k = (Excel_spooler_paramKey) o;
		if (!compareKey(getPg_estrazione(), k.getPg_estrazione())) return false;
		if (!compareKey(getPg_column(), k.getPg_column())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_estrazione());
		i = i + calculateKeyHashCode(getPg_column());
		return i;
	}
	public void setPg_estrazione(java.lang.Long pg_estrazione)  {
		this.pg_estrazione=pg_estrazione;
	}
	public java.lang.Long getPg_estrazione () {
		return pg_estrazione;
	}
	public void setPg_column(java.lang.Long pg_column)  {
		this.pg_column=pg_column;
	}
	public java.lang.Long getPg_column () {
		return pg_column;
	}
}