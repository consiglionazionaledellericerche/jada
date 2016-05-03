/*
* Created by Generator 1.0
* Date 23/01/2006
*/
package it.cnr.jada.excel.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Excel_spooler_param_columnKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_estrazione;
	private java.lang.Long pg_column;
	private java.lang.String id_key;
	public Excel_spooler_param_columnKey() {
		super();
	}
	public Excel_spooler_param_columnKey(java.lang.Long pg_estrazione, java.lang.Long pg_column,java.lang.String id_key) {
		super();
		this.pg_estrazione=pg_estrazione;
		this.pg_column=pg_column;
		this.id_key=id_key;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Excel_spooler_param_columnKey)) return false;
		Excel_spooler_param_columnKey k = (Excel_spooler_param_columnKey) o;
		if (!compareKey(getPg_estrazione(), k.getPg_estrazione())) return false;
		if (!compareKey(getPg_column(), k.getPg_column())) return false;
		if (!compareKey(getId_key(), k.getId_key())) return false;		
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_estrazione());
		i = i + calculateKeyHashCode(getPg_column());
		i = i + calculateKeyHashCode(getId_key());		
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
	public java.lang.String getId_key() {
		return id_key;
	}
	public void setId_key(java.lang.String string) {
		id_key = string;
	}

}