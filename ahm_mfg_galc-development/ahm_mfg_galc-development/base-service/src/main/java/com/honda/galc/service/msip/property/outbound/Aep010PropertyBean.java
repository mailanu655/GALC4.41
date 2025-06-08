package com.honda.galc.service.msip.property.outbound;

import java.util.Map;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Anusha Gopalan
 * @date Aug 28 2017
 */
public interface Aep010PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getJapanVinLeftJustified();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getRecordTypes();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLines();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();

	@PropertyBeanAttribute(defaultValue = "3")
	public int getDeleteOldRecordsByCalDays();

	@PropertyBeanAttribute(defaultValue = "14")
	public int getInsertLatestRecordsByCalDays();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getPart();
}
