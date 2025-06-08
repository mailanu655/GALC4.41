/**
 * 
 */
package com.honda.galc.client.datacollection.state;

import java.io.Serializable;

/**
 * @author Subu Kathiresan
 * @date Jan 4, 2013
 */
public abstract class State implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract String getApplicationId();
	
	public abstract void setApplicationId(String applicationId);
}
