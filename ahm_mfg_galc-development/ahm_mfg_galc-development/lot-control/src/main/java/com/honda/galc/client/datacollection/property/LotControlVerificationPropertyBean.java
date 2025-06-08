package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * properties for Lot Control verification
 * <h4>Usage and Example</h4>
 *
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2013.12.12</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>application properties for Lot Control Verification</TD>
 * </TR>
 * </TABLE>
 * @see
 * @ver 0.1
 * @author YX
 */
@PropertyBean(componentId ="LotControl_Verification")
public interface LotControlVerificationPropertyBean extends IProperty {
	//Part Name of EIN Lot Control Rule (case-sensitive)
	@PropertyBeanAttribute(defaultValue = "EIN")
	String getPartEin();
	
	//Part Name of Mission Lot Control Rule (case-sensitive)
	@PropertyBeanAttribute(defaultValue = "MISSION")
	String getPartMission();
}
