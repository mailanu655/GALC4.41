package com.honda.galc.client.device.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId="Default_Laseretcher_Device", prefix="device.laseretcher")
public interface LaseretcherDevicePropertyBean extends PrinterDevicePropertyBean {
	

	@PropertyBeanAttribute(propertyKey="baudrate")
	int getBaudrate();
	
	@PropertyBeanAttribute(propertyKey="databits")
	int getDatabits();
	
	@PropertyBeanAttribute(propertyKey="eocmessage")
	String getEocmessage();
	
	@PropertyBeanAttribute(propertyKey="parity")
	int getParity();
	
	@PropertyBeanAttribute(propertyKey="portname")
	String getPortname();
	
	@PropertyBeanAttribute(propertyKey="stopbits")
	int getStopbits();
	
	@PropertyBeanAttribute(propertyKey="timeout")
	int getTimeout();
}

