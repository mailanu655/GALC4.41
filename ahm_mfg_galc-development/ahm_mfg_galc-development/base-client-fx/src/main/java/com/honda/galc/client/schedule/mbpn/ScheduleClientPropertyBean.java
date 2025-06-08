package com.honda.galc.client.schedule.mbpn;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * @author Wade Pei <br>
 * @date Aug 26, 2014
 */
@PropertyBean
public interface ScheduleClientPropertyBean extends IProperty {
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.schedule.mbpn.PlasticsMbpnProductProcessor")
	String getProductProcessor();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getPlantCode();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getDepartmentCode();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean getProcessProductWhenClickCompleteMenu();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isPddaPlatformDialogEnabled();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getPddaDeptCode();

}

