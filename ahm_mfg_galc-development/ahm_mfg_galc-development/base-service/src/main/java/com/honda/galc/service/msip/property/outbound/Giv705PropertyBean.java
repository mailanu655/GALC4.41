package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public interface Giv705PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String getWeOffResCntPpid();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPaOffResCntPpid();

	@PropertyBeanAttribute(defaultValue = "")
	public String getAfOffResCntPpid();

	@PropertyBeanAttribute(defaultValue = "")
	public String getAeOffResPpid1();

	@PropertyBeanAttribute(defaultValue = "")
	public String getAeOffResPpid2();
}
