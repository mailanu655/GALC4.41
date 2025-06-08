package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean()
public interface Gpp103PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointOn();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointOff();

}
