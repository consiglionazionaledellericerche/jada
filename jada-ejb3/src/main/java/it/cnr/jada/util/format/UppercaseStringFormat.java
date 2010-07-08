/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.io.Serializable;
import java.text.*;
/**
 * Format che converte una stringa in maiuscolo
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class UppercaseStringFormat extends Format implements Serializable{

	private static final long serialVersionUID = 1L;

	public UppercaseStringFormat(){
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos){
        if(obj != null)
            toAppendTo.append(obj.toString().toUpperCase());
        return toAppendTo;
    }

    public Object parseObject(String source, ParsePosition status){
        status.setIndex(source != null ? source.length() : 1);
        if(source == null)
            return null;
        else
            return source.toUpperCase();
    }
}