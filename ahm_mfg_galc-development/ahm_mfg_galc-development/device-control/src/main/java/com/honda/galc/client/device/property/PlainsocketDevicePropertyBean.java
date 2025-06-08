package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId="Default_PlainSocket_Device",prefix="device.plainsocket")
public interface PlainsocketDevicePropertyBean extends DevicePropertyBean{
	
	@PropertyBeanAttribute(propertyKey="hostname")
	String getHostName();
	
	@PropertyBeanAttribute(propertyKey="pinginterval")
	long getPingInterval();
	
	@PropertyBeanAttribute(propertyKey="port")
	int getPort();
	
	@PropertyBeanAttribute(propertyKey="protocol")
	String getProtocol();
	
	@PropertyBeanAttribute(propertyKey="type")
	String getType();

	@PropertyBeanAttribute(propertyKey="model")
	String getModel();
		
	@PropertyBeanAttribute(propertyKey="autoRequestInterval", defaultValue = "1000")
	int getAutoRequestInterval();

	@PropertyBeanAttribute(propertyKey="numRetries", defaultValue = "60")
	int getNumRetries();

	/**
	 * retry request
	 */
	@PropertyBeanAttribute(propertyKey="retryRequest", defaultValue = "false")
	public boolean isRetryRequest();
	/**
	 * property to determine if socket should be closed by client code
	 */
	@PropertyBeanAttribute(propertyKey="autoCloseSocket", defaultValue = "false")
	public boolean isAutoCloseSocket();

}
