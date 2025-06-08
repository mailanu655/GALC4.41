package com.honda.galc.client.schedule.property;

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
	
	// Use to property to enable/disable playing sounds on the schedule client
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSoundsEnabled();

}

