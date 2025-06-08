package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>DataRecoveryPropertyBean</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>Jul 17, 2017</TD>
 * <TD>1.0.0</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */

@PropertyBean
public interface DataRecoveryPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getRecoveryConfig();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getRecoveryPart();
}
