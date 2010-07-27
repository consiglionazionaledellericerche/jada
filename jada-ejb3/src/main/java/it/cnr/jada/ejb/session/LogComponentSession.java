/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;
import javax.ejb.Remote;

/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Remote
public interface LogComponentSession {
	public void elaboraErrore(Throwable t) throws ComponentException;
}
