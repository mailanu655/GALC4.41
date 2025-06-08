/**
 * 
 */
package com.honda.galc.client.device.plc.memory;

/**
 * @author Subu Kathiresan
 * @date Jan 8, 2013
 */
public interface IPlcMemoryReader {
	
	public StringBuilder readPlcMemory(String memoryMapItemName);
}
