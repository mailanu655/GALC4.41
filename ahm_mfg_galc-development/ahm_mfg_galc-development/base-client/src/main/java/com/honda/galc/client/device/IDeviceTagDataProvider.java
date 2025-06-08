/**
 * 
 */
package com.honda.galc.client.device;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;

/**
 * @author Subu Kathiresan
 * @date Nov 27, 2012
 */
public interface IDeviceTagDataProvider {
	
	public boolean populateFields(PlcDataCollectionBean bean, String deviceId);
	
	public IPlcDeviceData populateFields(IPlcDeviceData deviceData);
}
