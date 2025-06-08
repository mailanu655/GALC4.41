package com.honda.galc.service.msip.property.inbound;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Subu Kathiresan
 * @date May 3, 2017
 */
@PropertyBean()
public interface Gpp305PropertyBean extends BaseMsipPropertyBean {

	@PropertyBeanAttribute(defaultValue = "MP")
	public String getNotHoldDemandTypes();
}
