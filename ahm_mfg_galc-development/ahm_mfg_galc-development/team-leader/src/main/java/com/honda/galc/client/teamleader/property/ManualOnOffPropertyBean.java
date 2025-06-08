package com.honda.galc.client.teamleader.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ManualOnOffPropertyBean</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Oct 9, 2014
 */
public interface ManualOnOffPropertyBean extends IProperty {

	/**
	 * Comma delimited process point ids, ex: ON_PROCESS_POINTS:PP1,PP2,PP3
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getOnProcessPoints();

	/**
	 * Comma delimited process point ids, ex: OFF_PROCESS_POINTS:PP1,PP2,PP3
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getOffProcessPoints();

	/**
	 * Flag indicates If check product Id validation should come from
	 * Build Attributes
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProductIdValidationFromBuildAttributes();

	/**
	 * Flag indicates If check product Id validation should come from
	 * PRODUCT_NUM_DEF_TBX table
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProductIdValidationFromProductIdNumDef();

	/**
	 * Flag indicates If a the On service should be the MBPNProductOnService
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isMbpnProductOnServiceUsed();
	
	/**
	 * Answer a map of <ProcessPointId><Mbpn.mainNo> 
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String,String> getMainNumberMap();
	
	@PropertyBeanAttribute
	public Map<String,String> getMbpnPlanCodeMap();
	
}
