/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk.annotation;

import java.lang.annotation.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public @interface HomeClass {
	public Class<?> name();
}
