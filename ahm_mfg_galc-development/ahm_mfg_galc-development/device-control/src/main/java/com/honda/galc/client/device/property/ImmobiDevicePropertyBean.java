package com.honda.galc.client.device.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
@PropertyBean(componentId="Default_Immobi_Device", prefix="device.immobi")
public interface ImmobiDevicePropertyBean extends SerialDevicePropertyBean {
	
	/**
	 *   Specifies the eoichar for immobi device
	 */ 
	@PropertyBeanAttribute(propertyKey="eoichar")
	int getEoichar();
}

