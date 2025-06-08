package com.honda.galc.property;

import java.util.List;
import java.util.Map;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiMultiLinePropertyBean</code> is ... .
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
 * @author Vivek Bettada
 * @created March 15, 2018
 */
public interface QiMultiLinePropertyBean extends IProperty {

	/**
	 IS_MULTI_LINE
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "FALSE")
	public boolean isMultiLineStation();
	
	//define origin_line to QicsStation mapping
	//specified as line_pp[qics_station_pp]
	@PropertyBeanAttribute    
	public Map<String, String> getAltStationLineOffMap();
	
	//associates a origin line_off process point for current qics station
	@PropertyBeanAttribute(defaultValue = "")
	public String getLineOff();


}
