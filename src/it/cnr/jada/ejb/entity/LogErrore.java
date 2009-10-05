/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.entity;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Entity
public class LogErrore extends OggettoBulk {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer erroreId;
	private String messaggio;
	@Lob
	private String trace;
	
	public LogErrore() {
		// empty constructor
	}
	
	public LogErrore(String messaggio) {
		setMessaggio(messaggio);
	}
	
	public Integer getErroreId() {
		return erroreId;
	}
	public void setErroreId(Integer eventoId) {
		this.erroreId = eventoId;
	}

	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		if (messaggio.length() > 1024)
			this.messaggio = messaggio.substring(0, 1024);
		else
			this.messaggio = messaggio;
	}
	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}	

	@Override
	public Serializable getId() {
		return getErroreId();
	}
}
