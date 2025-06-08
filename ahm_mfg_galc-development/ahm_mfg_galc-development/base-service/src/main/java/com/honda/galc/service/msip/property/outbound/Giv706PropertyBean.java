package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public interface Giv706PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "true")
	public Boolean getIncludeActiveLine();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getIsTransmissionPlant();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getJapanVinLeftJustified();	
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLineUrls();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPoint();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingStatus();
}
