package com.honda.galc.property;

@PropertyBean
public interface PrintAttributeFormatPropertyBean extends IProperty {
	/**
	 * Specify the list of Form IDs that are visible
	 */
	@PropertyBeanAttribute(propertyKey = "FILTER_BY_FORM_IDS", defaultValue = "")
	public String getFilterByFormIds();
}
