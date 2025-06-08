package com.honda.galc.oif.task.ahm;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "OIF_AHM_SHIPPING_TRANSACTION")
public interface ShippingTransactionPropertyBean extends IProperty {
	/**
	 * Specifies interfaceId for the task
	 */
	@PropertyBeanAttribute(defaultValue = "D--GMG#HMAGAL#AHM020")
	public String getInterfaceId();
	
	/**
	 * Specifies length of each line in incoming message for the task
	 */
	@PropertyBeanAttribute(defaultValue = "80")
	public int getMessageLineLength();
	
}
