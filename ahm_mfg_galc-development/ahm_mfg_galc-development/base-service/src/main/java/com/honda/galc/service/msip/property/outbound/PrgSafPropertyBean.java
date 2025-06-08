package com.honda.galc.service.msip.property.outbound;

import java.util.List;

import com.honda.galc.property.PropertyBeanAttribute;

public interface PrgSafPropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDepartments();

	@PropertyBeanAttribute
	public List<String> getSubAssemblyList();
}
