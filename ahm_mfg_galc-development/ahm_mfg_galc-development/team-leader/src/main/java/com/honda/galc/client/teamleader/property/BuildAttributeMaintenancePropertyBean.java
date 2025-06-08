package com.honda.galc.client.teamleader.property;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BuildAttributeMaintenancePropertyBean</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jul 22, 2016
 */
@PropertyBean(componentId = "Default_BuildAttributeMaintenance")
public interface BuildAttributeMaintenancePropertyBean extends CommonTlPropertyBean {

	/**
	 * The maximum size of BuildAttribute result set to be returned from db.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "2000")
	public int getBuildAttributeResultsetMaxSize();
}
