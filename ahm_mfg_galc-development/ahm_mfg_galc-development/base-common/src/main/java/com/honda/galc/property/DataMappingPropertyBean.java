package com.honda.galc.property;

/**
 * 
 * <h3>DataMappingPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataMappingPropertyBean description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jun 29, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun 29, 2012
 */

@PropertyBean(componentId ="Default_DataMapping")
public interface DataMappingPropertyBean extends SystemPropertyBean{
	/**
	 * Map data container tag by tag name
	 * values: "", true, false
	 *   true: map by part name. 
	 *         example: PART (partName), 2(second rule) then from container with key: PART_STATUS
	 *   false: map by lot control rule index
	 *         exmaple: PART, 2(second rule) then take from container with key: 2_STATUS
	 *   "": map by tag name only
	 *         example: PART, 2 then take from container with key: STATUS
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "MAP_BY_TAG_NAME")
	public String getPlcDataMapping();
	
	/**
	 * Build result locations for MC/DC head less data collection
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLocations();
	
	
	/**
	 * part name for DC/MC data collection
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getOverallStatusPartName();
	
	/**
	 * Indicate if the process point is configured using Lot Control
	 * Current use DieCast/Machining only 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isLotControl();

	/**
	 * Provide a list of part names for installed part 
	 * Part names are separated by comma
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPartNames();

	/**
	 * Flag for device driven when part name is from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDeviceDriven();

}
