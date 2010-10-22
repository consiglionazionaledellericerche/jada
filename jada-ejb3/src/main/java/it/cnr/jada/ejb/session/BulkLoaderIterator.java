/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.ejb.session;

import java.security.Principal;

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.util.RemoteOrderable;
import it.cnr.jada.util.RemotePagedIterator;
import net.bzdyl.ejb3.criteria.Criteria;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface BulkLoaderIterator extends RemotePagedIterator, RemoteOrderable{

    public abstract void create(Principal principal, Criteria criteria) throws ComponentException;
    
    public abstract void open(Principal principal) throws ComponentException, DetailedRuntimeException;
    
    public abstract void remove();
}