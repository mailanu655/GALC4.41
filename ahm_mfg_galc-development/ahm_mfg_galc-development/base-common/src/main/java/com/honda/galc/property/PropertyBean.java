package com.honda.galc.property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PropertyBean is annotation to override implicit<br/>
 * component ID, deriving from Property Bean class name
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PropertyBean {
	public String DEF_UNNASSIGNED = "DEF_UNNASSIGNED";

	/**
	 * @return explicit component ID
	 */
	String  componentId() default DEF_UNNASSIGNED;
	
	/**
	 * 
	 * @return
	 */
	String prefix() default "";
	
	/**
	 * @return default value for all bean methods
	 */
	String  defaultValue() default DEF_UNNASSIGNED;

}
