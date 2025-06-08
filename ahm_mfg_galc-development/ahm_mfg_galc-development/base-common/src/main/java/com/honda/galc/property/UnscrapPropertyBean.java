package com.honda.galc.property;

@PropertyBean(componentId="DEFAULT_UNSCRAP")
public interface UnscrapPropertyBean extends ApplicationPropertyBean {
	
	/**
	 * Integer value to set the max number of products that can be
	 * unscrapped at a time.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	Integer getMaxUnscrapCount();
	
	/**
	 * flag to determine if a reason for unscrap must be provided by the user
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isUnscrapReasonRequired();
 	/**
	 * A list of exceptional line ids that need to avoid when changing last tracking status
	 * 
	 * @returnto 
	 */
	@PropertyBeanAttribute(defaultValue = "LINE57")
	public String[] getScrapLines();
}
