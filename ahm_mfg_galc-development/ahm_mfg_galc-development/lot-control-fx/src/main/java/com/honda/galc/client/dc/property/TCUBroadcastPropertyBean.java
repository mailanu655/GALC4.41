package com.honda.galc.client.dc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "TCU")
public interface TCUBroadcastPropertyBean extends IProperty {

	/**
	 * Broadcast Destination. 
	 */
	@PropertyBeanAttribute(propertyKey = "DESTINATION", defaultValue = "")
	public String getDestination();

	/**
	 * Value of AUTO_MFR_PLANT_ID in TCU message
	 */
	@PropertyBeanAttribute(propertyKey = "AUTO_MFR_PLANT_ID", defaultValue = "")
	public String getAutoMfrPlantId();

	/**
	 * Template name used to generate TCU message 
	 */
	@PropertyBeanAttribute(propertyKey = "TEMPLATE_NAME", defaultValue = "")
	public String getTemplateName();
	
	/**
	 * JNDI Name for MQ resource 
	 */
	@PropertyBeanAttribute(propertyKey = "MQ_JNDI_NAME", defaultValue = "")
	public String getMqJndiName();	
}

