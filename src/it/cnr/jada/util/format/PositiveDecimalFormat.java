/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import it.cnr.jada.bulk.ValidationParseException;

/**
 * Formattatore di numeri positivi decimali
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class PositiveDecimalFormat extends java.text.DecimalFormat {

	private static final long serialVersionUID = 1L;

	public PositiveDecimalFormat() {
		super();
	}

	public PositiveDecimalFormat(String pattern) {
		super(pattern);
	}

	public PositiveDecimalFormat(String pattern, java.text.DecimalFormatSymbols symbols) {
		super(pattern, symbols);
	}
    /**
     * Operazioni di parse
     *
     * @exception ParseException if the specified string is invalid.
     */
    public Number parse(String text) throws java.text.ParseException {
	    Number obj = super.parse(text);
		if (obj != null) {
			if (((Number)obj).intValue() < 0)
				throw new ValidationParseException("sono ammessi solo valori positivi!", 0);
		}
		return obj;
    }
    /**
     * Operazioni di parse
     *
     * @exception ParseException if the specified string is invalid.
     */
    public Object parseObject(String text) throws java.text.ParseException {
	    Object obj = super.parseObject(text);
	    if (obj instanceof java.math.BigDecimal) {
		    if (((java.math.BigDecimal)obj).signum() < 0)
				throwValitationException();
		} else if (obj instanceof java.math.BigInteger) {
		    if (((java.math.BigInteger)obj).signum() < 0)
				throwValitationException();
	    } else if (obj instanceof Long) {
			if (((Long)obj).longValue() < 0)
				throwValitationException();
		} else if (obj instanceof Integer) {
			if (((Integer)obj).intValue() < 0)
				throwValitationException();
		} else if (obj instanceof Float) {
			if (((Float)obj).floatValue() < 0)
				throwValitationException();
		} else if (obj instanceof Double) {
			if (((Double)obj).doubleValue() < 0)
				throwValitationException();
		} else if (obj instanceof Byte) {
			if (((Byte)obj).byteValue() < 0)
				throwValitationException();
		} else if (obj instanceof Short) {
			if (((Short)obj).shortValue() < 0)
				throwValitationException();
		}	
		return obj;
    }

    private void throwValitationException() throws ValidationParseException {
	    throw new ValidationParseException("sono ammessi solo valori positivi!", 0);
    }
}