package com.honda.galc.property;

import java.util.Map;

import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="prop_BadgeInfo")
public interface BadgeInfoPropertyBean extends ApplicationPropertyBean {

	/**
	 * Property to set the rest serivice url 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey = "REST_URL", defaultValue = "http://hnadxlmsipjb01.amerhonda.com:8181")
	public String getRestUrl();
		
	/**
	 * flag to decide insert badge info to table if not present 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isAllowInsert();
	
	
	/**
	 * Header value for the Rest call
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getRequestHeader();
}
