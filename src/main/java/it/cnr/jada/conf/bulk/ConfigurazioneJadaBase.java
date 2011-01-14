/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2010
 */
package it.cnr.jada.conf.bulk;
import it.cnr.jada.persistency.Keyed;
public class ConfigurazioneJadaBase extends ConfigurazioneJadaKey implements Keyed {
//    VAL01 VARCHAR(100)
	private java.lang.String val01;
 
//    VAL02 VARCHAR(100)
	private java.lang.String val02;
 
//    IM01 DECIMAL(6,0)
	private java.lang.Integer im01;
 
//    IM02 DECIMAL(20,6)
	private java.math.BigDecimal im02;
 
//    DT01 TIMESTAMP(7)
	private java.sql.Timestamp dt01;
 
//    FL01 CHAR(1)
	private java.lang.String fl01;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFIGURAZIONE_JADA
	 **/
	public ConfigurazioneJadaBase() {
		super();
	}
	public ConfigurazioneJadaBase(java.lang.String cdChiave) {
		super(cdChiave);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [val01]
	 **/
	public java.lang.String getVal01() {
		return val01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [val01]
	 **/
	public void setVal01(java.lang.String val01)  {
		this.val01=val01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [val02]
	 **/
	public java.lang.String getVal02() {
		return val02;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [val02]
	 **/
	public void setVal02(java.lang.String val02)  {
		this.val02=val02;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im01]
	 **/
	public java.lang.Integer getIm01() {
		return im01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im01]
	 **/
	public void setIm01(java.lang.Integer im01)  {
		this.im01=im01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im02]
	 **/
	public java.math.BigDecimal getIm02() {
		return im02;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im02]
	 **/
	public void setIm02(java.math.BigDecimal im02)  {
		this.im02=im02;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dt01]
	 **/
	public java.sql.Timestamp getDt01() {
		return dt01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dt01]
	 **/
	public void setDt01(java.sql.Timestamp dt01)  {
		this.dt01=dt01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fl01]
	 **/
	public java.lang.String getFl01() {
		return fl01;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fl01]
	 **/
	public void setFl01(java.lang.String fl01)  {
		this.fl01=fl01;
	}
}