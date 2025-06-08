package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Anusha Gopalan
 * @date Aug 28, 2017
 */
public interface XmRadioPropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingStatus();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPpvqship();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPpafoff();

	@PropertyBeanAttribute(defaultValue = "")
	public String getAcuraModelCodes();

	@PropertyBeanAttribute(defaultValue = "")
	public String getCountryCode();

	@PropertyBeanAttribute(defaultValue = "")
	public String getGroupNames();

	@PropertyBeanAttribute(defaultValue = "")
	public String getActiveLineUrls();
}
