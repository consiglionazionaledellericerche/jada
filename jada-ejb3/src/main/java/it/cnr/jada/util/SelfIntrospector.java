/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
interface SelfIntrospector{

    public abstract Object getPropertyValue(String s);

    public abstract void setPropertyValue(String s, Object obj);
}