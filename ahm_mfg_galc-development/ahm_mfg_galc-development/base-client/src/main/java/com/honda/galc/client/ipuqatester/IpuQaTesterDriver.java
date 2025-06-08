/**
 * 
 */
package com.honda.galc.client.ipuqatester;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @Date Apr 24, 2012
 *
 */
public interface IpuQaTesterDriver {

	public void getProductId();
	
	public void getInstalledpart(String partName);
	
	public void getMeasurements(String partName);
}
