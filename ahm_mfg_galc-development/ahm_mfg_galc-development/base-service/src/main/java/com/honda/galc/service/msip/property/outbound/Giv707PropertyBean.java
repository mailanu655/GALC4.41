package com.honda.galc.service.msip.property.outbound;

import java.util.Map;

import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Anusha Gopalan
 * @date Sep 1 2017
 */
public interface Giv707PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDepartments();

	@PropertyBeanAttribute(defaultValue = "")
	public String getInitialDate();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointAmOn();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPointAmOff();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getActiveLines();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getIsTransmissionPlant();

	@PropertyBeanAttribute
	public Map<String, String> getProcessPointOn();

	@PropertyBeanAttribute
	public Map<String, String> getProcessPointOff();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getUseSeqToBuildSeq();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getAllowDbUpdate();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getExcludeListedPlanCode();

	@PropertyBeanAttribute(defaultValue = "4")
	public Integer getSequenceNumberScale();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPlanCodesToExclude();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getTrackingStatus();

}
