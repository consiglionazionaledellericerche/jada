/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.io.IOException;
/**
 * 
 * @author mspasiano
 *
 * Mar 26, 2007 2:53:53 PM
 */
public interface XmlWriteable{

    public abstract void writeTo(XmlWriter xmlwriter) throws IOException;
}