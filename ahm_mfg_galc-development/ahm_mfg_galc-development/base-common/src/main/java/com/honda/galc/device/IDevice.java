package com.honda.galc.device;


import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.property.DevicePropertyBean;




/**
 * @author Subu Kathiresan
 * Feb 2, 2009
 * 
 * Defines the functionality for an external device
 * that would be managed by the DeviceManager
 */
public interface IDevice 
{
	
	public void setDeviceProperty(DevicePropertyBean devicePropertyBean);
	/**
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 
	 * @return
	 */
	public void setId(String id);
	
	/**
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * 
	 * @return
	 */
	public void setName(String name);
	
	/**
	 * 
	 * @return
	 */
	public String getClientId();

	/**
	 * 
	 * @return
	 */
	public void setClientId(String clientId);
	
	/**
	 * 
	 * @return
	 */
	public String getApplicationId();
	
	/**
	 * 
	 * @return
	 */
	public void setApplicationId(String applicationId);

	/**
	 * 
	 * @return
	 */
	public int getCurrentState();

	/**
	 * 
	 * @param state
	 */
	public void setState(int state);

	/**
	 * 
	 * @return
	 */
	public boolean isActive();
	
	/**
	 * 
	 *
	 */
	public void activate();

	/**
	 * 
	 * @return
	 */
	public boolean isEnabled();
	
	
	public boolean isConnected();

	/**
	 * 
	 *
	 */
	public void deActivate();
	
	/**
	 * 
	 * @return
	 */
	public DeviceType getType();
	
	/**
	 * 
	 * @return
	 */
	public void setType(DeviceType deviceType);
	
	
	public void registerListener(ConnectionStatusListener listener);
	
	public void unregisterListener(ConnectionStatusListener listener);
	
	/**
	 * Get logger for the device
	 * @return
	 */
	public Logger getLogger();
	
	
}
