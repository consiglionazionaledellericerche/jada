/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;
/**
 * Un Format che restituisce uno stile CSS da applicare al testo formattato.
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface StyledFormat{
    /**
     * Restituisce lo stile CSS da applicare al testo formattato
     */	
    public abstract String getStyle(Object value);
}