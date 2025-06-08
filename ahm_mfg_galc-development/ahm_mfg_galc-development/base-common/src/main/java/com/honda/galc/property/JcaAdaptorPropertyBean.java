/**
 * 
 */
package com.honda.galc.property;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2013
 */
@PropertyBean(componentId ="JcaAdaptor")
public interface JcaAdaptorPropertyBean extends IProperty {
	
	@PropertyBeanAttribute(defaultValue="ISO-8859-1")
	String getEncoding();
	
	@PropertyBeanAttribute(defaultValue="20")
	int getMaxConnections();
	
	@PropertyBeanAttribute(defaultValue="9999")
	int getConnectionTimoutInMilliSeconds();
}
