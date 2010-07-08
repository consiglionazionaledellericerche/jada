/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import java.io.Serializable;
import java.text.ParseException;
/**
 * Una ParseException che ha anche un messaggio utente che descrive l'errore di formattazione.
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class ValidationParseException extends ParseException implements Serializable{

	private static final long serialVersionUID = 1L;
    private String errorMessage;

	public ValidationParseException(String errorMessage, int errorOffset){
        super(errorMessage, errorOffset);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}