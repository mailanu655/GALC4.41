package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * 
 * <h3>ProductResultPropertyBean Class description</h3>
 * <p> ProductResultPropertyBean description </p>
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
 * Mar 19, 2015
 *
 *
 */
@PropertyBean(componentId ="Default_Product_Result")
public interface ProductResultPropertyBean extends IProperty {
	
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isSaveInstalledPartHistory();
	
	/**
	 * Enables/disables build result history record creation for HEAD, BLOCK, CONROD and CRANKSHAFT product types
	 */
	@PropertyBeanAttribute ()
	public Map<String, Boolean> isSaveBuildResultHistoryMap(Class<?> clazz);
	
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isSaveDefectResultHistory();
	
}
