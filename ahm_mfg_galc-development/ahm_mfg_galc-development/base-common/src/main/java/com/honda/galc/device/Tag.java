package com.honda.galc.device;


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
@Target(ElementType.FIELD)
public @interface Tag {

	/**
	 * @return explicit tag id
	 */
	String  name() ;
	/**
	 * @return alternative tag id
	 */
	String alt() default "";
	
	boolean optional() default false;
	

}
