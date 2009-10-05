/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public final class EmptyEnumeration implements Enumeration<Object>{

    private static final EmptyEnumeration instance = new EmptyEnumeration();

    private EmptyEnumeration(){
    }

    public static final EmptyEnumeration getInstance(){
        return instance;
    }

    public final boolean hasMoreElements(){
        return false;
    }

    public final Object nextElement(){
        throw new NoSuchElementException();
    }
}