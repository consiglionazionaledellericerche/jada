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
public enum OrderConstants{
    ORDER_ASC("ascending"),
    
    UNORDERED("natural"),
    
    ORDER_DESC("descending");
    
    private final String value;

    OrderConstants(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
    
}