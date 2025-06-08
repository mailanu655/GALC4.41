package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public interface Gpp104PropertyBean extends MsipOutboundPropertyBean {

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessLocation();

	@PropertyBeanAttribute(defaultValue = "1990-01-01 00:00:00")
	public String getDate();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointAmOn();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointAmOff();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();
}
