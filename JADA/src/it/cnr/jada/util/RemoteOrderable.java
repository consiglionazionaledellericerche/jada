/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import it.cnr.jada.DetailedRuntimeException;

/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface RemoteOrderable{
    public abstract void setOrderBy(java.lang.String feature, OrderConstants sortDirection) throws DetailedRuntimeException;
}