/**
 * 
 */
package com.honda.galc.client.metrics.monitors;

/**
 * @author Subu Kathiresan
 * Aug 16, 2011
 */
public interface IMetricsMonitor {

	public String getMonitorName();
	
	public void setMonitorName(String monitorName);
	
	public boolean activate(); 

	public boolean deActivate(); 
	
}
