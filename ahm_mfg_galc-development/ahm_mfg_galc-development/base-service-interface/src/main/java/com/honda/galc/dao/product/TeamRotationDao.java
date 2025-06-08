package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.product.TeamRotationId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>TeamRotationDao is the DAO interface for TEAM_ROTATION_TBX</h3>
 * <p>  </p>
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
 * @author Yang Xin<br>
 * Oct 10, 2014
 */
public interface TeamRotationDao extends IDaoService<TeamRotation, TeamRotationId> {
	
	/**
	 * Search team rotations.
	 *
	 * @param selectedLineId the selected line id
	 * @param selectedPlant the selected plant
	 * @param selectedDept the selected dept
	 * @param selectedStart the selected start
	 * @param selectedEnd the selected end
	 * @return the list
	 */
	public List<TeamRotation> searchRotations(String selectedLineId, String selectedPlant, String selectedDept, String selectedStart, String selectedEnd);
	
	/**
	 * Update a team rotation record. <br>
	 * All of the columns of TEAM_ROTATION_TBX are part of the primary key. So we need an existing rotation record to identify the DB record for update.
	 * @param update the updated record to be saved
	 * @param existing the existing the existing record in DB
	 * @return the update row count.
	 */
	public int update(TeamRotation update, TeamRotation existing);

	public TeamRotation findTeamDetails(GpcsDivision gpcsDivision,String shift, Date productionDate);
			
}
