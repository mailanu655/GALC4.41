package com.honda.galc.entity;

import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

/**
 * 
 * <h3>EntityBuildPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EntityBuildPropertyBean description </p>
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
 * @author Paul Chou
 * Jun. 9, 2014
 *
 */
@PropertyBean(componentId ="Default_EntityBuild")
public interface EntityBuildPropertyBean extends SystemPropertyBean{
	/**
	 * Defines field in schedule file
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getFieldDefs();
	
	/**
	 * Check if a preproduction lot already processed, if yes, will not insert/update that lot
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCheckProcessedLot();
	
	/**
	 * Interval between sequence number
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getSequenceInterval();
	
	/**
	 * Specify how to build a entity
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getEntityBuildSpecs();

}
