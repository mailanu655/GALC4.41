package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public interface Giv730PropertyBean extends MsipOutboundPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLines();
}
