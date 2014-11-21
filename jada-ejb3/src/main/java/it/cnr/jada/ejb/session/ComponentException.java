/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import it.cnr.jada.DetailedRuntimeException;

import java.io.Serializable;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class ComponentException extends DetailedRuntimeException implements Serializable{

	private static final long serialVersionUID = 1L;

	public ComponentException(){
    }

    public ComponentException(String s){
        super(s);
    }

    public ComponentException(String s, Throwable throwable){
        super(s, throwable);
    }

    public ComponentException(Throwable throwable){
        super(throwable);
    }
}