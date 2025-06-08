package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.product.TeamRotationId;
import com.honda.galc.service.Parameters;

/**
 * <h3>TeamRotationDaoImpl is the DAO implementation class for TEAM_ROTATION_TBX</h3>
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
 * </TABLE>.
 *
 * @author Yang Xin<br>
 * Oct 10, 2014
 */
public class TeamRotationDaoImpl extends BaseDaoImpl<TeamRotation, TeamRotationId> implements TeamRotationDao {
	
	/** The SQL for  searchRotations. */
	private static final String SEARCH_ROTATIONS = "select tr.* from TEAM_ROTATION_TBX tr " 
													+ "where tr.LINE_NO=?1 and tr.PLANT_CODE=?2 and tr.PROCESS_LOCATION=?3 "
													+ "and tr.PRODUCTION_DATE between ?4 and  ?5 "
													+ "order by tr.LINE_NO, tr.PLANT_CODE, tr.PROCESS_LOCATION, tr.PRODUCTION_DATE, tr.SHIFT";
	
	/** The SQL for update(). */
	private static final String UPDATE_ROTATIONS = "update TEAM_ROTATION_TBX tr " 
			+ "set tr.PRODUCTION_DATE=?1,tr.PLANT_CODE=?2,tr.LINE_NO=?3,tr.PROCESS_LOCATION=?4,tr.SHIFT=?5,tr.team=?6"
			+ "where tr.PRODUCTION_DATE=?7 and tr.PLANT_CODE=?8 and tr.LINE_NO=?9 and tr.PROCESS_LOCATION=?10 "
			+ "and tr.SHIFT=?11 and tr.team=?12";

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.TeamRotationDao#searchRotations(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<TeamRotation> searchRotations(String selectedLineId,
			String selectedPlant, String selectedDept, String selectedStart,
			String selectedEnd) {
		Parameters params = Parameters.with("1", selectedLineId);
		params.put("2", selectedPlant);
		params.put("3", selectedDept);
		params.put("4", selectedStart);
		params.put("5", selectedEnd);
		return findAllByNativeQuery(SEARCH_ROTATIONS, params);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.dao.product.TeamRotationDao#update(com.honda.galc.entity.product.TeamRotation, com.honda.galc.entity.product.TeamRotation)
	 */
	@Transactional
	public int update(TeamRotation update, TeamRotation existing) {
		Parameters params = Parameters.with("1", update.getId().getProductionDate());
		params.put("2", update.getId().getPlantCode());
		params.put("3", update.getId().getLineNo());
		params.put("4", update.getId().getProcessLocation());
		params.put("5", update.getId().getShift());
		params.put("6", update.getId().getTeam());
		
		params.put("7", existing.getId().getProductionDate());
		params.put("8", existing.getId().getPlantCode());
		params.put("9", existing.getId().getLineNo());
		params.put("10", existing.getId().getProcessLocation());
		params.put("11", existing.getId().getShift());
		params.put("12", existing.getId().getTeam());
		
		return executeNative(UPDATE_ROTATIONS, params);
		
	}

	public TeamRotation findTeamDetails(GpcsDivision gpcsDivision,
			String shift, Date productionDate) {
		
		return findFirst(Parameters.with("id.lineNo", gpcsDivision.getGpcsLineNo()).put("id.plantCode", gpcsDivision.getGpcsPlantCode())
				.put("id.processLocation", gpcsDivision.getGpcsProcessLocation()).put("id.shift", shift)
				.put("id.productionDate", productionDate));
	}
	
	

}
