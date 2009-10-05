/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import it.cnr.jada.bulk.ValidationParseException;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class PositivePercentFormat extends PercentFormat {

	private static final long serialVersionUID = 1L;
	/**
	 * PositivePercentFormat constructor comment.
	 */
	public PositivePercentFormat() {
		super();
	}
	public Object parseObject(String source) throws java.text.ParseException {
		java.math.BigDecimal percent = (java.math.BigDecimal)super.parseObject(source);
		if (percent == null) return null;
		if (percent.signum() < 0)
			throw new ValidationParseException("Sono ammesse solo percentuali positive",0);
		return percent;
	}
}
