package it.cnr.jada.bulk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value=ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface JadaOneToMany {
	@SuppressWarnings("rawtypes")
	public abstract java.lang.Class targetEntity();
	public abstract java.lang.String mappedBy();
	public abstract String[] orderBy() default {};
}
