package com.honda.galc.client.dc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "MARKET_QUALITY")
public interface MarketQualityBroadcastPropertyBean extends IProperty {

	/**
	 * Template name used to generate Market Quality message 
	 */
	@PropertyBeanAttribute(propertyKey = "TEMPLATE_NAME", defaultValue = "MARKET_QUALITY_JMS_TEMPLATE")
	public String getTemplateName();
	
	/**
	 * JNDI Name for MQ resource 
	 */
	@PropertyBeanAttribute(propertyKey = "MQ_JNDI_NAME", defaultValue = "jms/LotControlMQQueue")
	public String getMqJndiName();
	
	/**
	 * Text id for Market Quality
	 */
	@PropertyBeanAttribute(propertyKey = "TEXT_ID", defaultValue = "C410")
	public String getTextId();
}
