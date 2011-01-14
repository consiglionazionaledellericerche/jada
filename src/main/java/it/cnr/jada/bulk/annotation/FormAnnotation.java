package it.cnr.jada.bulk.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Ragruppamento di formFieldProperty da usare nelle form HTML
 * @author mspasiano
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface FormAnnotation {
	/** Nome logico del Form, l'attributo è obbligatorio, ed è univoco all'interno del documento.*/
	public String name() default "default";
	/** Array di FormFieldProperty che compongono il Form.*/
	public FieldPropertyAnnotation[] value();
}
