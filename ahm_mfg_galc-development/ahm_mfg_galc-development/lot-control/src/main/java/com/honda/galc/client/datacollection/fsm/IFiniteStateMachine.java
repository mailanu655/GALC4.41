package com.honda.galc.client.datacollection.fsm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.honda.galc.client.datacollection.state.State;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface IFiniteStateMachine {    
	Class<? extends State>[] states();    
	Class<? extends State> initialState();
	Class<? extends State> finalState();    
	//Object as final state flags the machine has no final state    Class<?> finalState() default Object.class;}
}