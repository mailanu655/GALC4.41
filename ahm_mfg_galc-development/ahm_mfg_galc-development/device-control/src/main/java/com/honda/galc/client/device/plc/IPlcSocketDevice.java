/**
 * 
 */
package com.honda.galc.client.device.plc;

import com.honda.galc.device.IDevice;

/**
 * @author Subu Kathiresan
 * @date Nov 2, 2012
 */
public interface IPlcSocketDevice extends IDevice {

    public StringBuilder readMemory(IPlcMemory memory);

    public StringBuilder readMemory(IPlcMemory memory, boolean logTraffic);

    public boolean writeMemory(IPlcMemory memory, StringBuilder data);
    
    public boolean writeMemory(IPlcMemory memory, StringBuilder data, boolean logTraffic);

    public StringBuilder readClock();
    
    public StringBuilder readClock(boolean logTraffic);
    
    public boolean isInitialized();
    
	public String getHostName();
	
	public int getPort();
}
