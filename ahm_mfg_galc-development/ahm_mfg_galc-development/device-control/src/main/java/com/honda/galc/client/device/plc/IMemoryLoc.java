/**
 * 
 */
package com.honda.galc.client.device.plc;

/**
 * @author Subu Kathiresan
 * @date Feb 13, 2012
 */
public interface IMemoryLoc {

	public String name();
	public int code();
	public IMemoryLoc getBank(int code);
}
