/**
 * 
 */
package com.honda.galc.property;

/**
 * @author Subu Kathiresan
 * @date Oct 28, 2013
 */
@PropertyBean(componentId ="FactoryNews")
public interface FactoryNewsPropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue="Frame")
	String getPlantName();
	
	/**
	 * The flag if the detail link are enabled.
	 *
	 * @return true, if the detail links are enabled
	 */
	@PropertyBeanAttribute(defaultValue="false", propertyKey="DETAIL_LINK_ENABLED")
	boolean isDetailLinkEnabled();
	
	/**
	 * The flag if the detail link are enabled.
	 *
	 * @return true, if the detail links are enabled
	 */
	@PropertyBeanAttribute(defaultValue="true", propertyKey="ENABLE_FACTORY_NEWS")
	boolean isEnableFactoryNews();
}

