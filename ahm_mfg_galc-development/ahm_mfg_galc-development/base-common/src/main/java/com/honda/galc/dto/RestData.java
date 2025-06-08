package com.honda.galc.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Subu Kathiresan
 * @date Oct 04, 2019
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RestData {
	
	/**
	 * name used for the REST json key
	 * 
	 * @return String
	 */
	String key() default "";
}