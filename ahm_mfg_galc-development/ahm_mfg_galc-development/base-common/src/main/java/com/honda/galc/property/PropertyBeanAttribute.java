package com.honda.galc.property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PropertyBeanAttribute is annotation to override implicit<br/>
 * property key, deriving from Property Bean method name
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PropertyBeanAttribute {
	
	public String DEF_UNNASSIGNED = "DEF_UNNASSIGNED";

	/**
	 * @return explicit property key
	 */
	String  propertyKey() default DEF_UNNASSIGNED;
	
	/**
	 * @return default value for bean method
	 */
	String  defaultValue() default DEF_UNNASSIGNED;
}
