package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

public interface Aaa112PropertyBean extends MsipOutboundPropertyBean {

	@PropertyBeanAttribute(defaultValue = "")
	public String getHttpServiceUrlPart();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyProcessPoint();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyActiveLinesUrls();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyLineNo();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyPlantCode();

	@PropertyBeanAttribute(defaultValue = "")
	public boolean getPropertyIsCustomizedTime();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyCustomizedStartTime();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyCustomizedEndTime();

	@PropertyBeanAttribute(defaultValue = "")
	public String PropertyProcessLocation();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getPropertyScrapProcessPoint();
}
