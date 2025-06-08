package com.honda.galc.client.datacollection.observer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.honda.galc.client.datacollection.state.Action;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ProcessPartState{
	Action[] actions();
}
