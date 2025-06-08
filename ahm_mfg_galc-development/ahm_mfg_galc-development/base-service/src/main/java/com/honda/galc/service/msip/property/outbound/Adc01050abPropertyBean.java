package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean()
public interface Adc01050abPropertyBean extends MsipOutboundPropertyBean{	
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getPartName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getSendLocation();

	@PropertyBeanAttribute(defaultValue = "")
	public String getAdcProcessCode();

	@PropertyBeanAttribute(defaultValue = "")
	public String getTranType();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPartInstalled();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getKeyNoPartName();

	@PropertyBeanAttribute(defaultValue = "true")
	public Boolean getVinJustify();

	@PropertyBeanAttribute(defaultValue = "")
	public String getAfOffProcessPoint();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLines();
	
}

