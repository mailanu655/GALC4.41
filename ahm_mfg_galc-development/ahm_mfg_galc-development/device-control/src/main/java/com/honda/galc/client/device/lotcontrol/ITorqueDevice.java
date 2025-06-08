package com.honda.galc.client.device.lotcontrol;

import java.util.List;

import com.honda.galc.client.device.property.TorqueDevicePropertyBean;
import com.honda.galc.device.IDevice;

/**
 * @author Subu Kathiresan
 * Feb 22, 2009
 */
public interface ITorqueDevice extends IDevice
{
	public boolean registerListener(ITorqueDeviceListener listener);

	/**
	 *
	 * @param listener
	 * @return
	 */
	public boolean unregisterListener(ITorqueDeviceListener listener);

	/**
	 *
	 * @return
	 */
	public List<ITorqueDeviceListener> getListeners();
	
	public boolean requestControl(ITorqueDeviceListener listener);
	
	public TorqueDevicePropertyBean getTorqueDevicePropertyBean();

}
