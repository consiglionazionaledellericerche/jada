/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada;

import java.io.Serializable;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
interface DetailedThrowable extends Serializable, Cloneable{
    public abstract Throwable getDetail();
}