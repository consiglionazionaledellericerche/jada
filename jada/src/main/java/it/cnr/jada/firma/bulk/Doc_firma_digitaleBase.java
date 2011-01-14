/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/04/2009
 */
package it.cnr.jada.firma.bulk;
import it.cnr.jada.persistency.Keyed;
public class Doc_firma_digitaleBase extends Doc_firma_digitaleKey implements Keyed {
//    CD_UTENTE VARCHAR(20) NOT NULL
	private java.lang.String cd_utente;
 
//    DATA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data;
 
//    NOME_FILE VARCHAR(400) NOT NULL
	private java.lang.String nome_file;
 
//    DS_FILE VARCHAR(2000)
	private java.lang.String ds_file;
 
	public Doc_firma_digitaleBase() {
		super();
	}
	public Doc_firma_digitaleBase(java.lang.Long id_documento) {
		super(id_documento);
	}
	public java.lang.String getCd_utente() {
		return cd_utente;
	}
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}
	public java.sql.Timestamp getData() {
		return data;
	}
	public void setData(java.sql.Timestamp data)  {
		this.data=data;
	}
	public java.lang.String getNome_file() {
		return nome_file;
	}
	public void setNome_file(java.lang.String nome_file)  {
		this.nome_file=nome_file;
	}
	public java.lang.String getDs_file() {
		return ds_file;
	}
	public void setDs_file(java.lang.String ds_file)  {
		this.ds_file=ds_file;
	}
}