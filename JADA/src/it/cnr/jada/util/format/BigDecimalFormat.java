/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;
import java.util.Locale;
/**
 * Un Format per i BigDecimal. A differenza di DecimalFormat, parse restituisce 
 * sempre un'istanza di BigDecimal, anche se il numero � piccolo o senza decimali.
 * See Also:Serialized Form
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class BigDecimalFormat extends Format implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final BigDecimalFormat instance = new BigDecimalFormat();
	private static final Format format = new java.text.DecimalFormat("#.####################", new java.text.DecimalFormatSymbols(Locale.getDefault()));
	private static final Format parser = java.text.NumberFormat.getInstance(Locale.getDefault());

	public BigDecimalFormat(){
	}

	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos){
		return format.format(obj, toAppendTo, pos);
	}
    /**
     * Restituisce un'istanza di BigDecimalFormat. Per evitare inutili reistanziazioni viene restituita sempre la stessa istanza
     */
	public static Format getInstance(){
		return instance;
	}

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
				return number;
			if(number instanceof BigInteger)
				return new BigDecimal((BigInteger)number, 0);
			else
				return new BigDecimal(number.toString());
		}catch(ParseException _ex){
			status.setIndex(0);
		}
		return null;
	}
}