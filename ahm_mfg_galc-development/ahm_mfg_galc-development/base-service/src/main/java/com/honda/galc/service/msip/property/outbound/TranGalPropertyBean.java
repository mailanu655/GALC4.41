package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

public interface TranGalPropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "1950-01-01 00:00:00")
	public String getTmWarLastDateFilter();

	@PropertyBeanAttribute(defaultValue = "")
	public String getTmWarTmppOff();

	@PropertyBeanAttribute(defaultValue = "")
	public String getTmScrapExceptional();

	@PropertyBeanAttribute(defaultValue = "")
	public String getTmWarTorquePartName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getTmWarCasePartName();
}