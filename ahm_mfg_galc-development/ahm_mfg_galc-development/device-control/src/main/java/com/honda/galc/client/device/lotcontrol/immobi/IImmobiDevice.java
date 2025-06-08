package com.honda.galc.client.device.lotcontrol.immobi;

import java.util.Hashtable;

import com.honda.galc.device.IDevice;



/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public interface IImmobiDevice extends IDevice
{
	public boolean registerListener(IImmobiDeviceListener listener);

	/**
	 *
	 * @param listener
	 * @return
	 */
	public boolean unregisterListener(IImmobiDeviceListener listener);

	/**
	 *
	 * @return
	 */
	public Hashtable<String, IImmobiDeviceListener> getListeners();
}
