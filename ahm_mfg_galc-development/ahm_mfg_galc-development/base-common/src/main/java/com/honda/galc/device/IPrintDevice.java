/**
 * 
 */
package com.honda.galc.device;

import java.util.HashMap;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.events.IPrintDeviceListener;

/**
 * @author Subu Kathiresan
 * Sep 26, 2011
 */
public interface IPrintDevice extends IDevice {
	
	public boolean print(String textToPrint, int printQuantity, String productId);
	
	public String getprintData(DataContainer dc);
	
	public String getHostName();
	
	public Integer getPort();
	
	public boolean registerListener(String applicationId, IPrintDeviceListener listener);

	public boolean unregisterListener(String applicationId, IPrintDeviceListener listener);

	public HashMap<String, IPrintDeviceListener> getListeners();
	
	public boolean requestControl(String applicationId, IPrintDeviceListener listener);

}
 