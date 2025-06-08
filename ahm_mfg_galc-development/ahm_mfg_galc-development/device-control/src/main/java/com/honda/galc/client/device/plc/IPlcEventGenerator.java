/**
 * 
 */
package com.honda.galc.client.device.plc;

/**
 * @author Subu Kathiresan
 * @date Nov 1, 2012
 */
public interface IPlcEventGenerator {

	public void setName(String name);

	public String getName();
	
	public boolean isActive();
	
	public void activate();
	
	public void deActivate();
	
	public void setEventClass(String eventClass);
	
	public String getEventClass();
	
	public void setInterval(Integer interval);

	public Integer getInterval();
}
