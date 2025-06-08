package com.honda.galc.client.teamleader.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * The property bean for FIF spec code mask
 */
@PropertyBean(componentId = "Default_FifSpecCodeMask")
public interface FIFSpecCodeMaskPropertyBean extends IProperty {
	
	/**
	 * Checks if is FIF enabled.
	 *
	 * @return true, if is FIF enabled
	 */
	@PropertyBeanAttribute(propertyKey="FIF_CODE_ENABLED", defaultValue="false")
	public boolean isFIFCodeEnabled();
}
