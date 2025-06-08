package com.honda.galc.property;

@PropertyBean(componentId ="DEFAULT_KICKOUT")
public interface KickoutPropertyBean  extends IProperty {

	@PropertyBeanAttribute(defaultValue = "FALSE")
	public boolean isMultipleKickout();
	
	@PropertyBeanAttribute(defaultValue = "TRUE")
	public boolean isKickoutReasonRequired();
	
	@PropertyBeanAttribute(defaultValue = "FALSE")
	public boolean isFilterKickoutProcess();
}
