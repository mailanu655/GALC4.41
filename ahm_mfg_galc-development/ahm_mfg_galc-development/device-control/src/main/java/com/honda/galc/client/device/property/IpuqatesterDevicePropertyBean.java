/**
 * 
 */
package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Feb 6, 2012
 */

@PropertyBean(componentId="Default_IpuQaTester_Device",prefix="device.ipuqatester")
public interface IpuqatesterDevicePropertyBean extends DevicePropertyBean {

	@PropertyBeanAttribute(propertyKey="port")
	int getPort();
}

