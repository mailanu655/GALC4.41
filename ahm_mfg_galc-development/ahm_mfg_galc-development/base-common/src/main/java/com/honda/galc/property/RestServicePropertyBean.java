package com.honda.galc.property;

/**
 * @author Subu Kathiresan
 * @date Sep 29, 2017
 */
@PropertyBean
public interface RestServicePropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUserAuthenticationEnabled();
}
