package com.honda.galc.service.msip.property.outbound;

import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jun 13, 2017
 */
@PropertyBean()
public interface Gpp102PropertyBean extends MsipOutboundPropertyBean {
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDepartments();

	@PropertyBeanAttribute
	public Map<String, String> getProcessPointOn();

	@PropertyBeanAttribute
	public Map<String, String> getProcessPointOff();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getTrackingStatus();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getUseSeqToBuildSeq();

	@PropertyBeanAttribute(defaultValue = "4")
	public int getSequenceNumberScale();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getExcludeListedPlanCodes();

	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean getAllowDbUpdate();

	@PropertyBeanAttribute
	public String[] getPlanCodesToExclude();
}
