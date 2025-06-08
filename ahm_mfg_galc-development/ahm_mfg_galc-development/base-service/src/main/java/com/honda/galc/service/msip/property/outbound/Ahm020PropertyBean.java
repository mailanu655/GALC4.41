package com.honda.galc.service.msip.property.outbound;

import java.util.Map;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Anusha Gopalan
 * @date Aug 28, 2017
 */
public interface Ahm020PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getJapanVinLeftJustified();

	@PropertyBeanAttribute(defaultValue = "ENGINE")
	public String getEngProductType();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLineUrls();

	@PropertyBeanAttribute(defaultValue = "LOCAL")
	public String getEngineUrls();

	@PropertyBeanAttribute(defaultValue = "")
	public String getCustomStartDate();

	@PropertyBeanAttribute(defaultValue = "")
	public String getCustomEndDate();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getUseCustomProductionDate();

	@PropertyBeanAttribute(defaultValue = "true")
	public Boolean getDebug();

	@PropertyBeanAttribute
	public Map<String, String> getNameMapping();
}
