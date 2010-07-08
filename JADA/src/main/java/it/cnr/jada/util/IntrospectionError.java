/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;
/**
 * Eccezione di introspezione non recuperabile
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class IntrospectionError extends DetailedRuntimeException implements Serializable{

	private static final long serialVersionUID = 1L;

	public IntrospectionError(){
    }

    public IntrospectionError(Exception exception){
        super(exception);
    }

    public IntrospectionError(String s){
        super(s);
    }
}