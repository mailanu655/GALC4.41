package com.honda.galc.service.msip.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Mar 27, 2017
 */
@PropertyBean(componentId ="DEFAULT_MSIP")
public interface BaseMsipPropertyBean extends IProperty {

	/**
	 * Specifies the Valid Check Digits
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public String[] getValidCheckDigits();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProcessLocations();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getOffProcessPoint();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getOffLineId();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getSiteName();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getLineNo();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPlanCodes();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getAssemblyLineId();
}
