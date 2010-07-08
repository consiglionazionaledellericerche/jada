/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.util.Prefix;

import java.io.Serializable;
import java.util.*;
/**
 * Un dizionario che mantiene la collezione degli errori di validazione di una form. 
 * Ogni campo di una form viene associato ad una FieldValidationException che descriver la natura dell'errore. 
 * Ogni campo � identificato da un nome e da un prefisso opzionale che indica la posizione nella gerarchia 
 * dei controller della form
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class FieldValidationMap implements Serializable{

	private static final long serialVersionUID = 1L;
    private Map<String, FieldValidationException> fieldValidationExceptions;

    public FieldValidationMap(){
        fieldValidationExceptions = new HashMap<String, FieldValidationException>();
    }
    /**
     * Elimina da questo FieldValidationMap gli errori di validazione di un campo
     */
    public void clear(String prefix, String fieldName){
        fieldValidationExceptions.remove(Prefix.prependPrefix(prefix, fieldName));
    }
    /**
     * Elimina da questo FieldValidationMap gli errori di validazione di pi� campi
     */
    public void clearAll(String prefix){
        for(Iterator<String> iterator = fieldValidationExceptions.keySet().iterator(); iterator.hasNext();)
            if(((String)iterator.next()).startsWith(prefix))
                iterator.remove();
    }
    /**
     * Cerca la FieldValidationException associata ad un campo
     */
    public FieldValidationException get(String prefix, String fieldName){
        return (FieldValidationException)fieldValidationExceptions.get(Prefix.prependPrefix(prefix, fieldName));
    }
    /**
     * Associa una FieldValidationException ad un campo
     */
    public FieldValidationException put(String prefix, String fieldName, FieldValidationException fieldvalidationexception){
        fieldValidationExceptions.put(Prefix.prependPrefix(prefix, fieldName), fieldvalidationexception);
        return fieldvalidationexception;
    }
}