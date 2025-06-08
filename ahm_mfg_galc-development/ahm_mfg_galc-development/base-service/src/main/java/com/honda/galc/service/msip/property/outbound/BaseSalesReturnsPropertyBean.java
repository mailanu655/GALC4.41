package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jun 21, 2017
 */
public interface BaseSalesReturnsPropertyBean extends MsipOutboundPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "")
	String getPlantName();

	@PropertyBeanAttribute(defaultValue = "")
	String getProcessPoint();
}
