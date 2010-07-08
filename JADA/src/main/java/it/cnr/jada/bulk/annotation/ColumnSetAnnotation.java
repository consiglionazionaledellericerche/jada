/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Ragruppamento di columnFieldProperty da usare nelle form
 * HTML di ricerca
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ColumnSetAnnotation {
	/** Nome logico del ColumnSet, l'attributo � obbligatorio, ed � univoco all'interno del documento.*/
	public String name() default "default";
	/** Array di ColumnFieldProperty che compongono il ColumnSet.*/
	public FieldPropertyAnnotation[] value();
}
