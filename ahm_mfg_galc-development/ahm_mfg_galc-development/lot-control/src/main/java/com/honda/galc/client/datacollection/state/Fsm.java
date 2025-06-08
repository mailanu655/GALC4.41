package com.honda.galc.client.datacollection.state;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.honda.galc.client.datacollection.fsm.FsmType;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface Fsm {

	FsmType type() default FsmType.DEFAULT;

}
