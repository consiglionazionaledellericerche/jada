/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.io.Serializable;
import java.util.Enumeration;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
class OrderedHashtableEnumerator implements Serializable, Enumeration{

	private static final long serialVersionUID = 1L;
    private Enumeration keysEnum;
    private OrderedHashtable oht;
	
	public OrderedHashtableEnumerator(OrderedHashtable orderedhashtable){
        oht = orderedhashtable;
        keysEnum = orderedhashtable.keys();
    }

    public boolean hasMoreElements(){
        return keysEnum.hasMoreElements();
    }

    public Object nextElement(){
        return oht.get(keysEnum.nextElement());
    }
}