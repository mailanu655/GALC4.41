package com.honda.galc.dao.oif;

import java.util.Date;
import java.util.List;

import com.honda.galc.entity.oif.MaterialServiceId;
import com.honda.galc.entity.oif.MaterialService;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Class description</h3>
 * Material Service DAO.
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
 * <TD>Daniel Garcia</TD>
 * <TD>Nov. 19, 2014</TD>
 * <TD>1.0</TD>
 * <TD></TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public interface MissionMaterialServiceDao extends IDaoService<MaterialService, MaterialServiceId> {
			
	/**
	 * Execute the query to get all old records in MS_PMX_TBX
	 * @param  dateBefore		-- the date used to get records register before of that date
	 * @return list of all old records
	 */
	public List<MaterialService> selectOldRecordMaterialService (final Date dateBefore);

	/**
	 * Execute the query to get all records that will send to material service with priority plan schedule
	 * @param dateBefore		-- the date used to get records register after of that date
	 * @param processPoint		-- this is a filter to get only the records in that process point
	 * @param plantCode			-- this is the plant code
	 * @return list of all records that will save in MaterialService
	 */
	public List<Object> getTransmissionMaterialServicePriorityPlanSchedule(final Date dateBefore, final String processPoint, final String plantCode);

	/**
	 * Execute the query to get all records that will send to material service with plan in house
	 * @param dateBefore		-- the date used to get records register after of that date
	 * @param processPoint		-- this is a filter to get only the records in that process point
	 * @param plantCode			-- this is the plant code
	 * @return list of all records that will save in MaterialService
	 */
	public List<Object> getTransmissionMaterialServiceInHouseSchedule(final Date dateBefore, final String processPoint, final String plantCode);

}
