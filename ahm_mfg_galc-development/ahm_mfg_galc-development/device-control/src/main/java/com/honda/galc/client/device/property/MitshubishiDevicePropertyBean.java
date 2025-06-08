package com.honda.galc.client.device.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId="Default_Mitshubishi_Device",prefix="plcdevice.mitshubishi")
public interface MitshubishiDevicePropertyBean extends QE71CommandsPropertyBean, PlcDevicePropertyBean {

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isDebugDriver();

}
