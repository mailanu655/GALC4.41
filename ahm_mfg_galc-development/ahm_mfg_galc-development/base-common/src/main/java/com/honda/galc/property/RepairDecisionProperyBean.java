package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * 
 * <h3>RepairDecisionProperyBean Class description</h3>
 * <p> RepairDecisionProperyBean description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jul 13, 2015
 *
 *
 */
public interface RepairDecisionProperyBean extends IProperty{

	public Map<String, String> getLocationProcessPointIdMapping();
	
	public String[] getProductCheckTypes();
	
	@PropertyBeanAttribute(propertyKey="USE_GTS_SERVICE",defaultValue = "FALSE")
	public boolean useGTSService();
	
}
