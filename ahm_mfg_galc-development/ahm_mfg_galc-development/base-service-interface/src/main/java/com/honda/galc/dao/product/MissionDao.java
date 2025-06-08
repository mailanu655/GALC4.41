package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.Mission;

/**
 * <h3>Class description</h3>
 * Mission DAO.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 2, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140902</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public interface MissionDao extends ProductDao<Mission> {
	
	
	/**
	 * Execute the query to get all the necessary data to Warranty System
	 * @param tmPPOff			-- process point for Transmission OFF
	 * @param trackingStatus	-- the Line IDs for the Exceptional or Scrap
	 * @param torquePartName	-- the part name for mission torque case
	 * @param casePartName	 	-- the part name for mission case
	 * @param lastDateFilter
	 * @return
	 */
	public List<Object[]> queryTransmissionWarranty(	final String	tmPPOff,
													final String	trackingStatus,
													final String	torquePartName, 
													final String	casePartName,
													final String		lastDateFilter);

}
