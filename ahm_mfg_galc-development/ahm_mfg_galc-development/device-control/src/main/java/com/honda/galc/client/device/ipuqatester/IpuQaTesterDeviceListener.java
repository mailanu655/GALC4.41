/**
 * 
 */
package com.honda.galc.client.device.ipuqatester;

/**
 * @author Subu Kathiresan
 * @date Feb 6, 2012
 */
public interface IpuQaTesterDeviceListener {
	
	public String getListenerName();
	
	public void processIpuUnit(StringBuffer unitInTestXmlString);
}
