package com.honda.galc.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

@PropertyBean(componentId = "Default_MfgControlMaintenance")
public interface MfgControlMaintenancePropertyBean extends SystemPropertyBean {
	
	@PropertyBeanAttribute(defaultValue="")
	public String getPlanCodes();
	
	@PropertyBeanAttribute(defaultValue="false")
	public boolean isMatrixMaintenanceTabDisplayed();
	
	@PropertyBeanAttribute(defaultValue="0")
	public int getOpCheck();
	
	@PropertyBeanAttribute(defaultValue="")
	public String getOpView();
	
	@PropertyBeanAttribute(defaultValue="")
	public String getOpProcessor();
	
	@PropertyBeanAttribute(defaultValue="")
	public String getPartMask();
	
	@PropertyBeanAttribute(defaultValue="true")
	public boolean isCheckChronologicalOrder();
	
	@PropertyBeanAttribute(defaultValue="-1")
	public int getActiveDurationInDays();
	
	@PropertyBeanAttribute(defaultValue="true")
	public boolean isValidationRequired();
}
