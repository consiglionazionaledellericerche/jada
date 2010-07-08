/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.io.Serializable;
/**
 * Classe di utilit� per la gestione di prefissi
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public final class Prefix implements Serializable{

	private static final long serialVersionUID = 1L;

	private Prefix(){
    }

    public static String prependPrefix(String prefix, String name){
        if(prefix == null)
            return name;
        else
            return prefix + "." + name;
    }
}