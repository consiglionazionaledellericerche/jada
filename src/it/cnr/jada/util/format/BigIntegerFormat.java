/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;
import java.util.Locale;
/**
 * Un Format per i BigInteger. A differenza di DecimalFormat, parse restituisce sempre 
 * un'istanza di BigInteger, anche se il numero � piccolo. Genera un'eccezione se il numero ha decimali
 * See Also:Serialized Form
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class BigIntegerFormat extends Format implements Serializable{
	private static final long serialVersionUID = 1L;
    private static final BigIntegerFormat instance = new BigIntegerFormat();
    private static final Format format = new DecimalFormat("#", new DecimalFormatSymbols(Locale.getDefault()));
    private static final Format parser = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.getDefault()));
	
	public BigIntegerFormat(){
    }
    /**
     * Formats an object to produce a string. Subclasses will implement for particular object, 
     * such as: StringBuffer format (Number obj, StringBuffer toAppendTo) Number parse (String str) 
     * These general routines allow polymorphic parsing and formatting for objects such as the MessageFormat.
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition fieldposition){
        return format.format(obj, toAppendTo, fieldposition);
    }
    /**
     * Restituisce il valore della propriet� 'instance'
     */
    public static Format getInstance(){
        return instance;
    }
    /**
     * Parses a string to produce an object. Subclasses will typically implement for particular object, 
     * such as: String format (Number obj); String format (long obj); String format (double obj); 
     * Number parse (String str);
     */
    public Object parseObject(String source, ParsePosition status){
        status.setIndex(source != null ? source.length() + 1 : 1);
        if(source == null)
            return null;
        source = source.trim();
        if(source.length() == 0)
            return null;
        try{
            Number number = (Number)parser.parseObject(source);
            if(number instanceof BigDecimal)
                throw new ParseException("Sono ammessi solo numeri interi", source.length());
            if(number instanceof BigInteger)
                return number;
            else
                return BigInteger.valueOf(number.longValue());
        }catch(ParseException _ex){
            status.setIndex(0);
        }
        return null;
    }
}