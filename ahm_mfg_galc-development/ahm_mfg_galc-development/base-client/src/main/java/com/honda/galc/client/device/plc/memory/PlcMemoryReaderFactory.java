/**
 * 
 */
package com.honda.galc.client.device.plc.memory;

import com.honda.galc.client.device.plc.omron.PlcMemoryReader;

/**
 * @author Subu Kathiresan
 * @date Jan 8, 2013
 */
public class PlcMemoryReaderFactory {

	public static IPlcMemoryReader getReader(String applicationId, String plcDeviceId) {
		//TODO make this a real factory method which can return
		//     other plc memory reader objects as well, when we 
		//     have them available 
		return new PlcMemoryReader(applicationId, plcDeviceId);
	}
}
