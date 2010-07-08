/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;
/**
 * Formattatore di importi
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public abstract class ImportoFormat extends java.text.Format implements StyledFormat {

	private static final long serialVersionUID = 1L;
	protected static final java.text.Format format = new java.text.DecimalFormat("#,##0.00");
	private int precision = 0;
	private int round = java.math.BigDecimal.ROUND_HALF_EVEN;

	public ImportoFormat() {
		super();
	}

	/**
	  * Effettua la formattazione
	  */
	public StringBuffer format(Object obj, StringBuffer toAppendTo, java.text.FieldPosition pos) {
		if (obj != null) {
			getFormat().format(obj,toAppendTo,pos);
			String symbol = getSymbol();
			if (symbol != null) {
				toAppendTo.append(' ');
				toAppendTo.append(symbol);
			}
		}
		return toAppendTo;
	}

	/**
	 * Restituisce il valore della propriet� 'format'
	 *
	 * @return Il valore della propriet� 'format'
	 */
	public java.text.Format getFormat() {
		return format;
	}

	/**
	 * Restituisce il valore della propriet� 'precision'
	 *
	 * @return Il valore della propriet� 'precision'
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Restituisce il valore della propriet� 'round'
	 *
	 * @return Il valore della propriet� 'round'
	 */
	public int getRound() {
		return round;
	}

	/**
	 * Restituisce "text-align: right"
	 *
	 * @return String
	 */
	public String getStyle(Object value) {
		return "text-align: right";
	}

	/**
	 * Restituisce il valore della propriet� 'symbol'
	 *
	 * @return Il valore della propriet� 'symbol'
	 */
	public String getSymbol() {
		return null;
	}

	/**
	  * Effettua le operazioni di parse
	  */
	public Object parseObject(String source, java.text.ParsePosition status) {
		status.setIndex(source == null ? 1 : source.length());
		if (source == null || source.equals("")) return null;
		source = source.trim();
		if (source.length() == 0) return null;
		if (source.endsWith(String.valueOf(getSymbol()))) {
			source = source.substring(0,source.length()-1);
			source = source.trim();
		}
		Number number;
		try {
			number = (Number)getFormat().parseObject(source);
		} catch(java.text.ParseException pe) {
			status.setIndex(0);
			return null;
		} catch(java.lang.NumberFormatException pe) {
		    status.setIndex(0);
		    return null;
	    }
		if(number == null) return null;
		java.math.BigDecimal bd;
		if (number instanceof java.math.BigDecimal)
			bd = (java.math.BigDecimal)number;
		else if (number instanceof java.math.BigInteger)
			bd = new java.math.BigDecimal((java.math.BigInteger)number);
		else if (number instanceof Long)
			bd = java.math.BigDecimal.valueOf(number.longValue());
		else 
			bd = new java.math.BigDecimal(number.doubleValue());
		bd = bd.setScale(getPrecision(),getRound());
		return bd;
	}
	/**
	 * Imposta il valore della propriet� 'precision'
	 *
	 * @param newPrecision	Il valore da assegnare a 'precision'
	 */
	public void setPrecision(int newPrecision) {
		precision = newPrecision;
	}

	/**
	 * Imposta il valore della propriet� 'round'
	 *
	 * @param newRound	Il valore da assegnare a 'round'
	 */
	public void setRound(int newRound) {
		round = newRound;
	}
}