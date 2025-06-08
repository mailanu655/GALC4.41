package com.honda.galc.client.datacollection.fsm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.honda.galc.client.datacollection.state.Action;
@Retention(value=RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Notification {

	Action action();

	String condition() default "N/A";

	String postAction() default "N/A";
	
	String preAction() default "N/A";

	FsmType type() default FsmType.DEFAULT;

}
