/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.io.Serializable;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface RemoteIterator extends Serializable{

    public abstract void close();

    /**
     * Restituisce la dimensione della collezione di elementi.
     */
    public abstract int countElements();

    /**
     * Restituisce false se il cursore � arrivato oltre l'ultimo elemento della collezione
     */
    public abstract boolean hasMoreElements();

    /**
     * Muove il cursore alla posizione specificata.
     */
    public abstract void moveTo(int index);

    /**
     * Restituisce il prossimo elemento della collezione.
     */
    public abstract java.lang.Object nextElement();

    public abstract void refresh();
}