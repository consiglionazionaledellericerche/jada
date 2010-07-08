/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada;

import java.io.PrintWriter;
import java.io.Serializable;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface UserContext extends Serializable, Cloneable{

    public abstract String getSessionId();

    public abstract String getUser();

    public abstract void writeTo(PrintWriter printwriter);
    
}