/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.util.Enumeration;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public final class ArrayEnumeration implements Enumeration<Object>{

    private Object array[];
    int index;

    public ArrayEnumeration(Object aobj[]){
        array = aobj;
    }

    public final boolean hasMoreElements(){
        return index < array.length;
    }

    public final Object nextElement(){
        return array[index++];
    }
}