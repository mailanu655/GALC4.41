package com.honda.galc.property;

import java.util.Map;


/**
 * 
 * <h3>FiringTestPropertyBean Class description</h3>
 * <p> FiringTestPropertyBean description </p>
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
 * Apr 19, 2012
 *
 *
 */

@PropertyBean
public interface FiringTestPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getInstalledParts();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getInstalledSealants();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductCheckTypes();
	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getDictionary();
}
