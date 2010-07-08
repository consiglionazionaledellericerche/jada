/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.ejb.session.ComponentException;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class BulkNotFoundException extends ComponentException {

	private static final long serialVersionUID = -121990978304680973L;

	public BulkNotFoundException(){
    }

    public BulkNotFoundException(String s){
        super(s);
    }

    public BulkNotFoundException(String s, Throwable throwable){
        super(s, throwable);
    }

    public BulkNotFoundException(Throwable throwable){
        super(throwable);
    }
}
