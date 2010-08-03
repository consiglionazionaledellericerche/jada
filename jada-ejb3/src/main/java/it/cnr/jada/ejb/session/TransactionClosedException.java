/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import java.io.Serializable;

import javax.ejb.EJBException;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class TransactionClosedException extends EJBException implements Serializable{

	private static final long serialVersionUID = 1L;

	public TransactionClosedException(){
    }

    public TransactionClosedException(String s){
        super(s);
    }

    public TransactionClosedException(String s, Exception exception){
        super(s, exception);
    }
}