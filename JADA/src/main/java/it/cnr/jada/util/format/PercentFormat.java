/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.math.BigDecimal;

/**
 * Formattatore di importi percentuali
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class PercentFormat extends java.text.Format {

	private static final long serialVersionUID = 1L;
	protected static int defaultPrecision = 2;
	private static final java.text.Format defaultFormat = new java.text.DecimalFormat("0.00");
	
	public PercentFormat() {
		super();
	}
	/**
	  * Effettua la formattazione
	  */
	public StringBuffer format(Object obj, StringBuffer toAppendTo, java.text.FieldPosition pos) {
		if (obj != null) {
			getFormat().format(obj,toAppendTo,pos);
			toAppendTo.append('%');
		}
		return toAppendTo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/17/2002 10:31:45 AM)
	 * @return int
	 */
	public java.text.Format getFormat() {
		return defaultFormat;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/17/2002 10:31:45 AM)
	 * @return int
	 */
	public int getPrecision() {
		return defaultPrecision;
	}
	/**
	 * Effettua le operazioni di parse
	 */
	public Object parseObject(String source, java.text.ParsePosition status) {
		if (source == null) {
			status.setIndex(1);
			return null;
		}
		source = source.trim();
		if (source.length() == 0)
			return null;
		if (source.endsWith("%"))
			source = source.substring(0,source.length()-1);
		Number number = (Number)getFormat().parseObject(source,status);
		if (number == null) return null;
		java.math.BigDecimal bd;
		if (number instanceof java.math.BigDecimal)
			bd = (java.math.BigDecimal)number;
		else if (number instanceof java.math.BigInteger)
			bd = new java.math.BigDecimal((java.math.BigInteger)number);
		else if (number instanceof Long)
			bd = java.math.BigDecimal.valueOf(number.longValue());
		else 
			bd = new java.math.BigDecimal(number.doubleValue());
		bd = bd.setScale(getPrecision(),BigDecimal.ROUND_HALF_EVEN);
		return bd;
	}
}
