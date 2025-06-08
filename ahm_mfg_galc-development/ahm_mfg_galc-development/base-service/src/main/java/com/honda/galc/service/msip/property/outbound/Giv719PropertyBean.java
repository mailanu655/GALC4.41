package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public interface Giv719PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getUseNaturalTimeFrame();

	@PropertyBeanAttribute(defaultValue = "")
	public String getLastExecutionDate();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();

	@PropertyBeanAttribute(defaultValue = "")
	public String getInvLocCode();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLines();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLineUrls();

}
