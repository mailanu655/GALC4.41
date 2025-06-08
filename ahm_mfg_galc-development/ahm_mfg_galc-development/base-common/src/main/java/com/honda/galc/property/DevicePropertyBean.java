package com.honda.galc.property;

public interface DevicePropertyBean extends IProperty {
	
	@PropertyBeanAttribute(propertyKey="classname", defaultValue="")
	String getClassName();
	
	@PropertyBeanAttribute(propertyKey="deviceId", defaultValue="")
	String getDeviceId();
	
	@PropertyBeanAttribute(propertyKey="enabled", defaultValue="true")
	boolean isEnabled();
	
	@PropertyBeanAttribute(propertyKey="connected", defaultValue="false")
	boolean isConnected();
}
