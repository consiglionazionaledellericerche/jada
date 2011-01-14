/*
* Created by Generator 1.0
* Date 02/09/2005
*/
package it.cnr.jada.error.bulk;
import it.cnr.jada.persistency.Keyed;
public class Application_errorBase extends Application_errorKey implements Keyed {
//    CD_UTENTE VARCHAR(20)
	private java.lang.String cd_utente;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;
 
	public Application_errorBase() {
		super();
	}
	public Application_errorBase(java.lang.Long progressivo) {
		super(progressivo);
	}
	public java.lang.String getCd_utente () {
		return cd_utente;
	}
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
}