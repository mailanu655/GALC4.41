package com.honda.galc.oif.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "OIF_MISSION_MASTER_SPEC")
public interface MissionMasterSpecPropertyBean extends IProperty {
	/**
	 * Specifies interfaceId for the task
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInterfaceId();
	
	/**
	 * Specifies length of each line in incoming message for the task
	 */
	@PropertyBeanAttribute(defaultValue = "400")
	public int getMessageLineLength();
	
	/**
	 * Specifies parsing information for incoming message
	 */
	@PropertyBeanAttribute(defaultValue = "OIF_MMS_DEFS")
	public String getParseLineDefs();
	
}
