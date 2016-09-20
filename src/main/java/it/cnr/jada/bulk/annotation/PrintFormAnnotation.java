package it.cnr.jada.bulk.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Ragruppamento di printFieldProperty da usare per la stampa
 * @author mspasiano
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface PrintFormAnnotation {
	/** Nome del report da eseguire, l'attributo e' obbligatorio, ed e' univoco all'interno del documento.*/
	public String name();
	/** Array di PrintFieldProperty che compongono il PrintForm.*/
	public FieldPropertyAnnotation[] value();
}
