package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

public interface GalPrgPropertyBean extends MsipOutboundPropertyBean {

	@PropertyBeanAttribute(defaultValue = "0")
	public int getDaysToRunBefore();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointOn();

	@PropertyBeanAttribute(defaultValue = "")
	public String getFormatDate();

	@PropertyBeanAttribute(defaultValue = "")
	public String getFormatTime();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();

	@PropertyBeanAttribute(defaultValue = "")
	public String getLocations();
}
