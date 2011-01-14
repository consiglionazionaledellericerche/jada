/**
 * 
 */
package it.cnr.jada.bulk.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @author mspasiano
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface BulkInfoAnnotation {
	public String shortDescription();
	public String longDescription();
	public FormAnnotation[] form() default @FormAnnotation(value = { @FieldPropertyAnnotation(name = "", type=TypeProperty.FormFieldProperty) });
	public ColumnSetAnnotation[] columnSet() default @ColumnSetAnnotation(value={@FieldPropertyAnnotation(name="", type=TypeProperty.ColumnFieldProperty)});
	public FreeSearchSetAnnotation[] freeSearchSet() default @FreeSearchSetAnnotation(value={@FieldPropertyAnnotation(name="", type=TypeProperty.FindFieldProperty)});
	public PrintFormAnnotation[] printForm() default @PrintFormAnnotation(name="", value = { @FieldPropertyAnnotation(name = "", type=TypeProperty.PrintFieldProperty) });	
}
