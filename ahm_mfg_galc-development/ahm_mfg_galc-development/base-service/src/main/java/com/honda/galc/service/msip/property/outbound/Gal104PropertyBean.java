package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Anusha Gopalan
 * @date Aug 28, 2017
 */
public interface Gal104PropertyBean extends MsipOutboundPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getShipperId();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getVersionNumber();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingStatus();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getJapanVinLeftJustified();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLineUrls();
}
