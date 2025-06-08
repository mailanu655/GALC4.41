package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId="Default_Torque_Device",prefix="device.torque")
public interface TorqueDevicePropertyBean extends DevicePropertyBean{

	@PropertyBeanAttribute(propertyKey="hostname")
	String getHostName();
	
	@PropertyBeanAttribute(propertyKey="model")
	String getModel();
	
	@PropertyBeanAttribute(propertyKey="pinginterval")
	long getPingInterval();
	
	@PropertyBeanAttribute(propertyKey="port")
	int getPort();
	
	@PropertyBeanAttribute(propertyKey="protocol")
	String getProtocol();
	
	@PropertyBeanAttribute(propertyKey="type")
	String getType();

	@PropertyBeanAttribute(propertyKey="multispindle", defaultValue="false")
	boolean isMultiSpindle();

	@PropertyBeanAttribute(propertyKey="spindleid", defaultValue="")
	String getSpindleId();
	
	@PropertyBeanAttribute(propertyKey="delaySubscribe", defaultValue="false")
	boolean getDelaySubscribe();

	@PropertyBeanAttribute(propertyKey="delayInMillis", defaultValue="500")
	long getDelayInMillis();
	
	@PropertyBeanAttribute(propertyKey="maxTries", defaultValue="10")
	int getMaxTries();
	
	@PropertyBeanAttribute(propertyKey="socketTimeout", defaultValue="2000")
	int getSocketTimeOut();
	
	@PropertyBeanAttribute(propertyKey="delayToSendAbortJob", defaultValue = "false")
	public Boolean isDelayToSendAbortJob();
	
	@PropertyBeanAttribute(propertyKey="delayTimeToSendAbortJob", defaultValue = "0")
	public int getDelayTimeToSendAbortJob();
}
