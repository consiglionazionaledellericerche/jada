/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import java.io.Serializable;
/**
 * Eccezione lanciata per notificare una errore di validazione applicativa di un OggettoBulk; 
 * viene usata nei metodi validate... di OggettoBulk.
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class ValidationException extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;
    private String fieldPropertyName;

	public ValidationException(){
    }

    public ValidationException(String s){
        super(s);
    }

    public ValidationException(String s, String fieldPropertyName){
        super(s);
        this.fieldPropertyName = fieldPropertyName;
    }

    public String getFieldPropertyName(){
        return fieldPropertyName;
    }

    public void setFieldPropertyName(String s){
        fieldPropertyName = s;
    }
}