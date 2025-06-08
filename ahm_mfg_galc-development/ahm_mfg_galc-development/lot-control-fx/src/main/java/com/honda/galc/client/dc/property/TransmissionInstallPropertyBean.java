package com.honda.galc.client.dc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean
public interface TransmissionInstallPropertyBean extends IProperty {

	/**
	 * transmission install operation name
	 */
	@PropertyBeanAttribute(defaultValue = "Mission and Type")
	public String getMissionAndTypeName();
	
	/**
	 * is check product previous line 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isPreviousLineCheckEnabled();
}
