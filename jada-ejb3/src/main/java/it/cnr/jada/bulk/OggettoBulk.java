/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.bulk.annotation.BulkInfoAnnotation;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormatName;
import it.cnr.jada.bulk.annotation.HomeClass;
import it.cnr.jada.bulk.annotation.InputType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
@BulkInfoAnnotation(shortDescription="", longDescription="")
@HomeClass(name=BulkHome.class)
@MappedSuperclass
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public abstract class OggettoBulk implements Cloneable, Serializable{
	/**
	* Classe astratta che rappresenta un contenitore di informazioni da utilizzare come veicolo tra Servlet, JSP e EJB. 
	* Un OggettoBulk viene utilizzato nei seguenti flussi di informazione:
	* 
	*     * Tra Servlet ed EJB per rendere pi� leggero ed efficiente lo scambio di informazioni.
	*     * Tra Servlet e JSP per rappresentare un oggetto di business da utilizzare per riempire una FORM HTML; 
	* 
	* L'intelligenza applicativa non deve essere messa negli oggetti bulk, perch� questi rappresentano 
	* solamente un veicolo di informazioni. OggettoBulk definisce 3 attributi 
	* sistemistici per il supporto di controlli di consistenza transazionali:
	* 
	*     * dacr (Timestamp) - Data di creazione;
	*     * utcr (String) - Utente di creazione;
	*     * duva (Timestamp) - Data di ultima variazione;
	*     * utuv (String) - Utente di ultima variazione;
	*     * pg_ver_rec (Long) - Versione del record (viene incrementato ad ogni update) 
	* 
	* L'attributo crudStatus � utilizzato per comunicare lo stato di persistenza richiesto ad una CRUDComponent 
	* e pu� assumere i seguenti valori:
	* 
	* UNDEFINED
	*     L'OggettoBulk ha uno stato di persistenza indefinito 
	* NORMAL
	*     E' lo stato di persistenza che viene associato agli oggetti bulk letti dalla base dati o in seguito 
	*     ad una operazione di creazione o aggiornamento andata a buon fine. 
	* TO_BE_CREATED
	*     E' lo stato di persistenza associato agli oggetti da rendere persistenti mediante una insert 
	* TO_BE_UPDATED
	*     E' lo stato di persistenza associato agli oggetti da rendere persistenti mediante un update 
	* TO_BE_DELETED
	*     E' lo stato di persistenza associato agli oggetti da rendere persistenti mediante una delete; 
	*     se l'operazione va a buon fine lo stato di persistenza torna ad UNDEFINED 
	* TO_BE_CHECKED
	*     E' lo stato di persistenza associato agli oggetti per cui � necessario un controllo di 
	*     consistenza (controllo di versione) 
	* 
	* Per modificare lo stato di persistenza usare uno dei seguenti metodi:
	* 
	*       setToBeChecked setToBeCreated setToBeUpdated setToBeDeleted 
	*/
	@FieldPropertyAnnotation(name="dacr",
			inputType=InputType.ROTEXT,
			formatName=FormatName.timestamp,
			nullable=false,
			label="dacr")
	@Column(name="dacr", nullable=false)
	private Date dacr;
	@FieldPropertyAnnotation(name="utcr",
			inputType=InputType.ROTEXT,
			inputSize=20,
			maxLength=20,
			nullable=false,
			label="utcr")
	@Column(name="utcr", nullable=false)
	private String utcr;
	@FieldPropertyAnnotation(name="duva",
			inputType=InputType.ROTEXT,
			formatName=FormatName.timestamp,
			nullable=false,
			label="duva")
	@Column(name="duva", nullable=false)
	private Date duva;
	@FieldPropertyAnnotation(name="utuv",
			inputType=InputType.ROTEXT,
			inputSize=20,
			maxLength=20,
			nullable=false,
			label="utuv")
	@Column(name="utuv", nullable=false)
	private String utuv;
	@FieldPropertyAnnotation(name="pg_ver_rec",
			inputType=InputType.ROTEXT,
			inputSize=20,
			maxLength=20,
			nullable=false,
			label="pg_ver_rec")	
    @Version
    private Long pg_ver_rec;

    @Transient
    protected String user;
    @Transient
    protected boolean operabile;
    @Transient
    private int crudStatus;
    public static final int UNDEFINED = 0;
    public static final int TO_BE_CREATED = 1;
    public static final int TO_BE_UPDATED = 2;
    public static final int TO_BE_DELETED = 3;
    public static final int TO_BE_CHECKED = 4;
    public static final int NORMAL = 5;
    private static final long serialVersionUID = 0x93cc4a6eb3975ff8L;

    
    public OggettoBulk() {
		super();
		setCrudStatus(UNDEFINED);
	}
	/**
     * Restituisce lo stato di persistenza del ricevente. Lo stato di persistenza pu� assumere i seguenti valori:
     * Per modificare lo stato di persistenza usare uno dei seguenti metodi:
     */
    public int getCrudStatus(){
        return crudStatus;
    }
    /**
     * Restituisce la data di creazione.
     */
	public Date getDacr() {
		return dacr;
	}
    /**
     * Imposta la data di creazione.
     */
	public void setDacr(Date dacr) {
		this.dacr = dacr;
	}
    /**
     * Restituisce l'utente di creazione.
     */
	public String getUtcr() {
		return utcr;
	}
    /**
     * Imposta l'utente di creazione.
     */
	public void setUtcr(String utcr) {
		this.utcr = utcr;
	}
    /**
     * Restituisce la data di ultima variazione.
     */
	public Date getDuva() {
		return duva;
	}
    /**
     * Imposta la data di ultima variazione.
     */
	public void setDuva(Date duva) {
		this.duva = duva;
	}
    /**
     * Restituisce l'utente di ultima variazione.
     */
	public String getUtuv() {
		return utuv;
	}
    /**
     * Imposta l'utente di ultima variazione.
     */
	public void setUtuv(String utuv) {
		this.utuv = utuv;
	}
    /**
     * Restituisce il numero di versione dell'OggettoBulk.
     */
	public Long getPg_ver_rec() {
		return pg_ver_rec;
	}
    /**
     * Imposta il numero di versione dell'OggettoBulk.
     */
	public void setPg_ver_rec(Long pg_ver_rec) {
		this.pg_ver_rec = pg_ver_rec;
	}
    /**
     * Restituisce l'utente che ha modificato o creato questo OggettoBulk. 
     * Valido solo per oggetti non ancora resi persistenti
     */
    public String getUser(){
        return user;
    }
    /**
     * Equivale a isToBeCreated: restituisce true se il ricevente ha un crudStatus uguale a TO_BE_CREATED.
     */
    public boolean isNew(){
        return crudStatus == TO_BE_CREATED;
    }
    /**
     * E' il contrario di isToBeCreated: restituisce true se il ricevente ha un crudStatus diverso da TO_BE_CREATED.
     */
    public boolean isNotNew(){
        return !isNew();
    }

    public static boolean isNullOrEmpty(String s){
        return s == null || s.length() == 0;
    }
    /**
     * Restituisce true se il crudStatus del ricevente � TO_BE_CHECKED
     */
    public boolean isToBeChecked(){
        return crudStatus == TO_BE_CHECKED;
    }
    /**
     * Restituisce true se il crudStatus del ricevente � TO_BE_CREATED
     */
    public boolean isToBeCreated(){
        return crudStatus == TO_BE_CREATED;
    }
    /**
     * Restituisce true se il crudStatus del ricevente � TO_BE_DELETED
     */
    public boolean isToBeDeleted(){
        return crudStatus == TO_BE_DELETED;
    }
    /**
     * Restituisce true se il crudStatus del ricevente � TO_BE_UPDATED
     */
    public boolean isToBeUpdated(){
        return crudStatus == TO_BE_UPDATED;
    }
    /**
     * Imposta il crudStatus del ricevente. Utilizzare con cautela; 
     * � preferible usare uno dei metodi setToBe... perch� effettuano dei controlli di 
     * consistenza sul crudStatus attuale.
     */
    public void setCrudStatus(int newCRUDStatus){
        crudStatus = newCRUDStatus;
    }
    /**
     * Imposta il valore dell'attributo operabile.
     */
    public void setOperabile(boolean flag){
        operabile = flag;
    }
    /**
     * Imposta il crudStatus del ricevente a TO_CHECKED (solo se il crudStatus attuale � NORMAL).
     */
    public void setToBeChecked(){
        if(crudStatus == NORMAL)
            setCrudStatus(TO_BE_CHECKED);
    }
    /**
     * Se il crudStatus del ricevente � TO_BE_DELETE lo imposta a NORMAL, 
     * se � uguale a UNDEFINED lo imposta a TO_BE_CREATED, altrimenti lo lascia inalterato.
     */
    public void setToBeCreated(){
        if(crudStatus == TO_BE_DELETED)
            setCrudStatus(NORMAL);
        else if(crudStatus == UNDEFINED)
            setCrudStatus(TO_BE_CREATED);
    }
    /**
     * Se il crudStatus del ricevente � TO_BE_CREATED lo imposta a UNDEFINED, 
     * se � diverso da UNDEFINED lo imposta a TO_BE_DELETED
     */
    public void setToBeDeleted(){
        if(crudStatus == TO_BE_CREATED)
            setCrudStatus(UNDEFINED);
        else if(crudStatus == UNDEFINED || crudStatus == NORMAL || crudStatus == TO_BE_UPDATED)
            setCrudStatus(TO_BE_DELETED);
    }
    /**
     * Imposta il crudStatus del ricevente a TO_BE_UPDATED (solo se il crudStatus attuale � uguale a NORMAL)
     * se invece il crudStatus � UNDEFINED lo imposta a TO_BE_CREATED
     */
    public void setToBeUpdated(){
        if(crudStatus == NORMAL ||crudStatus == UNDEFINED)
            setCrudStatus(TO_BE_UPDATED);
        else if(crudStatus == UNDEFINED)
            setCrudStatus(TO_BE_CREATED);
    }

    public void setUser(String newUser){
        user = newUser;
    }
    /**
     * Inizializza il ricevente per la visualizzazione in un FormController. Questo metodo viene invocato da
     * ,
     * ,
     * e
     * e pu� contenere inizializzazioni comuni ai 4 stati del FormController
     */
    protected OggettoBulk initialize(){
        return this;
    }
    /**
     * Inizializza il ricevente per la visualizzazione in un FormController in stato EDIT. 
     * Questo metodo viene invocato automaticamente da un CRUDBP 
     * quando viene inizializzato per la modifica di un OggettoBulk.
     */
    public OggettoBulk initializeForEdit(){
        return this;
    }
    /**
     * Inizializza il ricevente per la visualizzazione in un FormController in stato FREESEARCH. 
     * Questo metodo viene invocato automaticamente da un CRUDBP 
     * quando viene inizializzato per la ricerca libera di un OggettoBulk.
     */
    public OggettoBulk initializeForFreeSearch(){
        return initializeForSearch();
    }
    /**
     * Inizializza il ricevente per la visualizzazione in un FormController in stato INSERT. 
     * Questo metodo viene invocato automaticamente da un CRUDBP 
     * quando viene inizializzato per l'inserimento di un OggettoBulk.
     */
    public OggettoBulk initializeForInsert(){
        return initialize();
    }
    /**
     * Inizializza il ricevente per la visualizzazione in un FormController in stato SEARCH. 
     * Questo metodo viene invocato automaticamente da un CRUDBP 
     * quando viene inizializzato per la ricerca di un OggettoBulk.
     */
    public OggettoBulk initializeForSearch(){
        return initialize();
    }

    protected boolean compareKey(Object obj, Object obj1){
        if(obj != null)
            return obj.equals(obj1);
        return obj1 == null;
    }

    public boolean equalsByPrimaryKey(Object bulk){
        if(this == bulk)
            return true;
        if(!(bulk instanceof OggettoBulk))
            return false;
        OggettoBulk controlkey = (OggettoBulk)bulk;
        return compareKey(getId(), controlkey.getId());
    }

    @Override
    public boolean equals(Object obj) {    	
    	return equalsByPrimaryKey(obj);
    }
    public abstract Serializable getId();
	
}
