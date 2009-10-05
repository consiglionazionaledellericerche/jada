/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;


import java.io.Serializable;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class ApplicationException extends ComponentException implements Serializable{

	private static final long serialVersionUID = 1L;
	private Object[] params;
	
    public ApplicationException(){
    }

    public ApplicationException(String s){
        super(s);
    }

    public ApplicationException(String s, Object...params){
        super(s);
        this.params = params;
    }
    
    public ApplicationException(String s, Throwable throwable){
        super(s, throwable);
    }

    public ApplicationException(Throwable throwable){
        super(throwable);
    }

	public Object[] getParams() {
		return params;
	}
}