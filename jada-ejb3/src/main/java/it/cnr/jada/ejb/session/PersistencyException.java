/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;


import it.cnr.jada.DetailedException;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;
/**
 * Radice delle eccezioni di persistenza
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class PersistencyException extends DetailedException implements Serializable{

	private static final long serialVersionUID = 1L;
    private Object persistent;

	public PersistencyException(){
    }

    public PersistencyException(String s){
        super(s);
    }

    public PersistencyException(String s, OggettoBulk persistent1){
        super(s);
        persistent = persistent1;
    }

    public PersistencyException(String s, Throwable throwable){
        super(s, throwable);
    }

    public PersistencyException(String s, Throwable throwable, OggettoBulk persistent1){
        super(s, throwable);
        persistent = persistent1;
    }

    public PersistencyException(Throwable throwable){
        super(throwable);
    }

    public PersistencyException(Throwable throwable, OggettoBulk persistent1){
        super(throwable);
        persistent = persistent1;
    }

    public OggettoBulk getPersistent(){
        return (OggettoBulk)persistent;
    }

    public void setPersistent(OggettoBulk persistent1){
        persistent = persistent1;
    }
}