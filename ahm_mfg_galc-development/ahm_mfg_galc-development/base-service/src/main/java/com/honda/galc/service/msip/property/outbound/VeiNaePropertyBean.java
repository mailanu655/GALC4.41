package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

public interface VeiNaePropertyBean extends MsipOutboundPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "/BaseWeb/HttpServiceHandler")
	public String getHttpServiceUrlPart();
	
	@PropertyBeanAttribute(defaultValue = "prefix")
	public String getPrefix();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLines();
}

