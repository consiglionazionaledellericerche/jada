/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.io.Serializable;
import java.text.*;
/**
 * Formattatore con gestione dei null
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class NullableFormat extends Format implements Serializable{

	private static final long serialVersionUID = 1L;
    private final Format format;

	public NullableFormat(Format format1){
        format = format1;
    }
    /**
     * Formats an object to produce a string. Subclasses will implement for particular object, 
     * such as: StringBuffer format (Number obj, StringBuffer toAppendTo) Number parse (String str) 
     * These general routines allow polymorphic parsing and formatting for objects such as the MessageFormat.
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos){
        if(obj == null)
            return null;
        else
            return format.format(obj, toAppendTo, pos);
    }
    /**
     * Recupera il formato
     */
    public Format getFormat(){
        return format;
    }

    public Object parseObject(String source) throws ParseException{
        return format.parseObject(source);
    }
    /**
     * Parses a string to produce an object. Subclasses will typically implement for particular object, such as: 
     * String format (Number obj); String format (long obj); String format (double obj); Number parse (String str);
     */
    public Object parseObject(String source, ParsePosition status){
        if(source == null || source.length() == 0)
            return null;
        else
            return format.parseObject(source, status);
    }
}