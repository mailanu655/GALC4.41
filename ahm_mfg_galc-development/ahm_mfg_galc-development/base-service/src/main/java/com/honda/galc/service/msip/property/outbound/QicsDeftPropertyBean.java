package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

public interface QicsDeftPropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantName();

	@PropertyBeanAttribute(defaultValue = "")
	public Boolean getJapanVINLeftJustified();

	@PropertyBeanAttribute(defaultValue = "")
	public String getHttpServiceUrlPart();

	@PropertyBeanAttribute(defaultValue = "")
	public String getActiveLinesUrls();
}
