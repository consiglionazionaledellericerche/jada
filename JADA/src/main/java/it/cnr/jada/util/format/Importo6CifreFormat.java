/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;
/**
 * Formattatore di importi a 6 cifre decimali
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class Importo6CifreFormat extends GenericImportoFormat {

	private static final long serialVersionUID = 1L;
	public final static java.text.Format format = new java.text.DecimalFormat("#,##0.000000");

	public Importo6CifreFormat() {
		super();
		setPrecision(6);
	}
	
	public java.text.Format getFormat() {
		return format;
	}
}