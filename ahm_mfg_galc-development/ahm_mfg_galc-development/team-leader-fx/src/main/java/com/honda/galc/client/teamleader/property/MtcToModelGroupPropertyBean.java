package com.honda.galc.client.teamleader.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="Default_MtcToModelGroup")
public interface MtcToModelGroupPropertyBean extends IProperty {

	/**
	 * The values of the MODEL_GROUPING_TBX's SYSTEM column under which build sheet configuration is being done.
	 */
	@PropertyBeanAttribute
	public String[] getMtcToModelGroupProductTypes();

	/**
	 * The values of the MODEL_GROUPING_TBX's SYSTEM column under which build sheet configuration is being done.
	 */
	@PropertyBeanAttribute
	public String[] getMtcToModelGroupSystems();

	/**
	 * Flag indicates that a model can only be assigned to one group in a given system.
	 */
	@PropertyBeanAttribute(propertyKey = "ONE_GROUP_PER_MODEL", defaultValue = "false")
	public boolean isOneGroupPerModel();
}
