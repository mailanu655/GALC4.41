/**
 * 
 */
package com.honda.galc.client.datacollection.observer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.honda.galc.client.datacollection.state.Action;

/**
 * @author Subu Kathiresan
 * @Date May 1, 2012
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ProcessRefreshState {
	Action[] actions();
}
