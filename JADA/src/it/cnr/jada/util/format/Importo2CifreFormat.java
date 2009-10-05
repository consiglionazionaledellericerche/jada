/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;
/**
 * Formattatore di importi a 2 cifre decimali
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class Importo2CifreFormat extends GenericImportoFormat {

	private static final long serialVersionUID = 1L;

	public Importo2CifreFormat() {
		super();
		setPrecision(2);
	}
}