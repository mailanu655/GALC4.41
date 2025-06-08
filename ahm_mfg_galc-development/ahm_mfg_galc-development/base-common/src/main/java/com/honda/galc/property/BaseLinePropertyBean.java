package com.honda.galc.property;

import java.util.Map;

public interface BaseLinePropertyBean extends SystemPropertyBean {
	/**
	 * Scrap line id.
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getScrapLineId();
	
	
	/**
	 * Valid lines to perform scrap from
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getValidLinesToScrapFrom();
	
	
	/**
	 * Valid lines to perform exceptional outgoing from
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getValidLinesToExceptionalOutgoingFrom();

	/**
	 * Return a map of names used by lot control rules to install products, including engine, mission, block, head and etc. 
	 * 
	 * Key: Product type name 
	 * Value: Common separated part names for the product type (See following example)
	 * 
	 *  PROPERTY_KEY								PROPERTY_VALUE
	 *  INSTALLED_PRODUCT_NAME_MAP{BLOCK}			BLOCK MC NUMBER
	 *  INSTALLED_PRODUCT_NAME_MAP{CONROD}			CONROD LC 1,CONROD LC 2,CONROD LC 3,CONROD LC 4,CONROD LC 5,CONROD LC 6
	 */
	@PropertyBeanAttribute
	public Map<String, String> getInstalledProductNameMap();
}
