package com.honda.galc.datacollection;

/**
 * 
 * <h3>HeadlessDataMapping</h3>
 * <h3> Class description</h3>
 * <h4> Added to support the new dynamically data mapping </h4>
 * <p> HeadlessDataMapping description </p>
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
 * <TD>Mar 12, 2012</TD>
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
 * @since Mar 12, 2012
 */

public enum HeadlessDataMapping {

	/**
	 * Map by part Name
	 * Example: PART (partName), 2(second rule) then from container with key: PART.STATUS
	 */
	MAP_BY_PART_NAME,
	
	/**
	 * Map by tag name
	 * Example: example: PART, 2 then take from container with key: STATUS
	 */
	MAP_BY_TAG_NAME,
	
	/**
	 * Map dynamically 
	 * Refer to Head less data collection Plc data mapping document
	 */
	MAP_BY_RULE,
	
	/**
	 * Map by lot control rule index
	 * Example: PART, 2(second rule) then take from container with key: 2.STATUS
	 */
	MAP_BY_RULE_INDEX
}
