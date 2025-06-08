package com.honda.galc.property;

import java.util.Map;

@PropertyBean(componentId ="Default_Product_Hold")
public interface ProductHoldPropertyBean extends IProperty{
	
	@PropertyBeanAttribute(defaultValue = "GEN_HOLD")
	public String getDefaultHoldAccessType();
	
	/**
	 * Maps hold type value (HOLD_NOW, HOLD_AT_SHIPPING, KICKOUT) to hold access type ID from HOLD_ACCESS_TYPE_TBX.
	 * E.g., DEFAULT_ACCESS_TYPE_BY_HOLD{HOLD_AT_SHIPPING} = SHP_HOLD_ACCESS_TYPE
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getDefaultAccessTypeByHoldMap(Class<?> clazz);
}
