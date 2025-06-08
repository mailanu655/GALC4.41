package com.honda.galc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "VQSHIP")
public interface VQShipProcessorBean extends IProperty {

	/**
	 * AF_OFF PROCESS_POINT. 
	 */
	@PropertyBeanAttribute(propertyKey = "KEY_SET_PART_NAME")
	public String[] getKeySetPartName();

}

