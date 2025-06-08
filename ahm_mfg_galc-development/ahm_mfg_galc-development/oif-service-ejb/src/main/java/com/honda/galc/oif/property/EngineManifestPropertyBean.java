package com.honda.galc.oif.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "OIF_ENGINE_MANIFEST")
public interface EngineManifestPropertyBean extends IProperty {
	/**
	 * Specifies interfaceId for the task
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInterfaceId();
	
	/**
	 * Specifies length of each line in incoming message for the task
	 */
	@PropertyBeanAttribute(defaultValue = "130")
	public int getMessageLineLength();
	
	/**
	 * Specifies parsing information for incoming message
	 */
	@PropertyBeanAttribute(defaultValue = "OIF_EM_DEFS")
	public String getParseLineDefs();
	
	/**
	 * Specifies process point for tracking service
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getShippingRecvPpid();
	
	/**
	 * Specifies value for FiredFlag when 
	 */
	@PropertyBeanAttribute(defaultValue = "1")
	public String getGalcFiredFlag();
	
	/**
	 * Specifies value for NotFiredFlag
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public String getGalcNotFiredFlag();
	
	/**
	 * Specifies value for FiredFlag
	 */
	@PropertyBeanAttribute(defaultValue = "F")
	public String getAepFiredFlag();
	
	/**
	 * Specifies engine plant
	 */
	@PropertyBeanAttribute(defaultValue = "AEP")
	public String getPlantCode();

	/**
	 * Specifies if records should be designated to specific line 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckLine();
	/**
	 * Specifies if the engine number is auto assigned..True for PMC
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoAssign();
	
	/**
	 * Specifies certain tracking status (scrap or exceptional out)
	 */
	@PropertyBeanAttribute(defaultValue = "LINE57,LINE58")
	public String[] getNotSellableTrackingStatus();
	
	
}
