package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Apr 21, 2017
 */
@PropertyBean(componentId="Default_LecLaser_Device", prefix="device.lecLaser")
public interface LecLaserDevicePropertyBean extends DevicePropertyBean {

	@PropertyBeanAttribute(propertyKey="hostname")
	String getHostName();

	@PropertyBeanAttribute(propertyKey="model")
	String getModel();

	@PropertyBeanAttribute(propertyKey="pinginterval", defaultValue = "10000")
	long getPingInterval();

	@PropertyBeanAttribute(propertyKey="port", defaultValue = "12500")
	int getPort();

	@PropertyBeanAttribute(propertyKey="protocol")
	String getProtocol();
	
	@PropertyBeanAttribute(propertyKey="formid", defaultValue = "LEC_LASER_FORM")
	String getFormId();

	@PropertyBeanAttribute(propertyKey="maxtries", defaultValue = "10")
	int getMaxTries();
	
	@PropertyBeanAttribute(propertyKey="timeoutinsecs", defaultValue = "60")
	int getTimeoutInSecs();
}
