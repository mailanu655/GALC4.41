package com.honda.galc.client.dc.property;

import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

/**
 * @author Subu Kathiresan
 * @date Apr 19, 2016
 */
@PropertyBean(componentId = "Default_ManufacturingControl")
public interface ManufacturingControlPropertyBean extends 
   AudioPropertyBean, SystemPropertyBean, IProperty {
	
	/**
	 * Flag indicates whether or not to save installed part history
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSavePartHistory();
	
	/**
	 * Flag indicates whether or not to save measurement history
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveMeasurementHistory();
}

