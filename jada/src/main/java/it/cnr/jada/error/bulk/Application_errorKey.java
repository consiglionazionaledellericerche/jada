/*
* Created by Generator 1.0
* Date 02/09/2005
*/
package it.cnr.jada.error.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Application_errorKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long progressivo;
	public Application_errorKey() {
		super();
	}
	public Application_errorKey(java.lang.Long progressivo) {
		super();
		this.progressivo=progressivo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Application_errorKey)) return false;
		Application_errorKey k = (Application_errorKey) o;
		if (!compareKey(getProgressivo(), k.getProgressivo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getProgressivo());
		return i;
	}
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	public java.lang.Long getProgressivo () {
		return progressivo;
	}
}