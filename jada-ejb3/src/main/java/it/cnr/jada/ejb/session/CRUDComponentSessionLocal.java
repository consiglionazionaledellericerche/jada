/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

import javax.ejb.Local;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Local
public interface CRUDComponentSessionLocal<T extends OggettoBulk> extends CRUDComponentSession<T>{
}
