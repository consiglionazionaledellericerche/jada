/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/04/2009
 */
package it.cnr.jada.firma.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Doc_firma_digitaleKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long id_documento;
	public Doc_firma_digitaleKey() {
		super();
	}
	public Doc_firma_digitaleKey(java.lang.Long id_documento) {
		super();
		this.id_documento=id_documento;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Doc_firma_digitaleKey)) return false;
		Doc_firma_digitaleKey k = (Doc_firma_digitaleKey) o;
		if (!compareKey(getId_documento(), k.getId_documento())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_documento());
		return i;
	}
	public void setId_documento(java.lang.Long id_documento)  {
		this.id_documento=id_documento;
	}
	public java.lang.Long getId_documento() {
		return id_documento;
	}
}