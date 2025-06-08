package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * Property for setting up the Ubisense device
 * 
 * @author Bernard Leong
 * @date Oct 23, 2017
 */
@PropertyBean(componentId="Default_Ubisense_Device",prefix="device.ubisense")
public interface UbisenseDevicePropertyBean extends DevicePropertyBean {

	@PropertyBeanAttribute(propertyKey="hostname")
	String getHostName();
	
	@PropertyBeanAttribute(propertyKey="port")
	int getPort();

}
