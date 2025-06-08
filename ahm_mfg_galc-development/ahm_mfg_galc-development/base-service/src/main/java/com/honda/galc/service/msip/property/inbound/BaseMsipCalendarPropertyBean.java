package com.honda.galc.service.msip.property.inbound;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
@PropertyBean()
public interface BaseMsipCalendarPropertyBean extends BaseMsipPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDepartments();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPlanCodes();
}
