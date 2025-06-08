package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.MeasurementCheckerDto;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public class MCMeasurementCheckerDaoImpl extends BaseDaoImpl<MCMeasurementChecker, MCMeasurementCheckerId> implements MCMeasurementCheckerDao {



	private static String GET_MEASUREMENT_CHECKER = "SELECT DISTINCT MC.OPERATION_NAME, MC.PART_ID, MC.OP_REV, MC.CHECK_POINT, MC.CHECK_SEQ, MC.CHECK_NAME, MC.CHECKER, MC.REACTION_TYPE, MC.OP_MEAS_SEQ_NUM, M.MIN_LIMIT, M.MAX_LIMIT, "
				+ " P.PART_NO, P.PART_SECTION_CODE, P.PART_ITEM_NO, P.PART_TYPE, O.APPROVED, O.DEPRECATED " 
				+ " FROM GALADM.MC_OP_MEAS_CHECKER_TBX MC "
				+ " LEFT OUTER JOIN GALADM.MC_OP_REV_TBX O ON "
				//Check for underscore
				+ " (CASE WHEN SUBSTR(MC.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(MC.OPERATION_NAME, 2) ELSE MC.OPERATION_NAME END) = O.OPERATION_NAME "
				+ " AND MC.OP_REV = O.OP_REV "
				+ " LEFT OUTER JOIN GALADM.MC_OP_PART_REV_TBX P ON "
				//Check for underscore
				+ " (CASE WHEN SUBSTR(MC.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(MC.OPERATION_NAME, 2) ELSE MC.OPERATION_NAME END) = P.OPERATION_NAME "
				+ " AND MC.PART_ID = P.PART_ID AND O.REV_ID = P.REV_ID "
				+ " LEFT OUTER JOIN GALADM.MC_OP_MEAS_TBX M ON P.OPERATION_NAME = M.OPERATION_NAME AND P.PART_ID = M.PART_ID AND P.PART_REV = M.PART_REV AND MC.OP_MEAS_SEQ_NUM = M.OP_MEAS_SEQ_NUM "
				+ " LEFT OUTER JOIN galadm.MC_OP_REV_PLATFORM_TBX orp on orp.OPERATION_NAME = O.OPERATION_NAME and orp.OP_REV = O.OP_REV "
				+ " LEFT OUTER JOIN galadm.MC_PDDA_PLATFORM_TBX ppt on ppt.PDDA_PLATFORM_ID = orp.PDDA_PLATFORM_ID "
				+ " LEFT OUTER JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON ppt.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND ppt.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND ppt.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND ppt.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND ppt.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND ppt.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE "
				+ " LEFT OUTER JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND ppt.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO "
				+ " LEFT OUTER JOIN galadm.GAL214TBX pp on pp.PROCESS_POINT_ID = VIOS_MAST_PROC.PROCESS_POINT_ID "
				+ " WHERE ";
	
	public static String GET_ALL_OPERATIONS = "SELECT DISTINCT TRIM(OPERATION_NAME) FROM GALADM.MC_OP_MEAS_TBX WHERE OPERATION_NAME LIKE ?1 FETCH FIRST 20 ROWS ONLY";
	
	private static String GET_ALL_MEASUREMENT_BY_PDDA_PLATFORM = "SELECT DISTINCT mc.OPERATION_NAME, mc.CHECK_POINT, mc.CHECK_SEQ, mc.CHECK_NAME, mc.CHECKER, mc.REACTION_TYPE, r.OP_REV, pr.PART_NO, pr.PART_SECTION_CODE, "
					+ "pr.PART_ITEM_NO, pr.PART_TYPE, om.OP_MEAS_SEQ_NUM, om.MIN_LIMIT, om.MAX_LIMIT FROM GALADM.MC_OP_MEAS_CHECKER_TBX mc "
					+ "JOIN GALADM.MC_OP_REV_PLATFORM_TBX orp on orp.OPERATION_NAME = mc.OPERATION_NAME and orp.OP_REV = mc.OP_REV " 
					+ "JOIN GALADM.MC_PDDA_PLATFORM_TBX p on p.PDDA_PLATFORM_ID = orp.PDDA_PLATFORM_ID " 
					+ "JOIN GALADM.MC_OP_REV_TBX r on mc.OPERATION_NAME = r.OPERATION_NAME and mc.OP_REV = r.OP_REV "
					+ "JOIN GALADM.MC_OP_PART_REV_TBX PR on PR.OPERATION_NAME = mc.OPERATION_NAME and PR.REV_ID = r.REV_ID and pr.PART_ID = mc.PART_ID "
					+ "JOIN GALADM.MC_OP_MEAS_TBX om on om.OPERATION_NAME = mc.OPERATION_NAME and om.PART_ID = PR.PART_ID "
					+ "AND om.PART_REV = PR.PART_REV and mc.OP_MEAS_SEQ_NUM = om.OP_MEAS_SEQ_NUM "
					+ "where p.PLANT_LOC_CODE = ?1 and p.DEPT_CODE = ?2 and p.MODEL_YEAR_DATE = ?3 and p.PROD_SCH_QTY = ?4 and p.PROD_ASM_LINE_NO = ?5 and p.VEHICLE_MODEL_CODE = ?6 "
					+ "and r.APPROVED is not null and r.DEPRECATED is null and PR.APPROVED is not null and PR.DEPRECATED is null";
	
	private static String GET_ALL_CHECKERS = "SELECT DISTINCT MC.* FROM GALADM.MC_OP_MEAS_CHECKER_TBX MC "
				+ " LEFT OUTER JOIN GALADM.MC_OP_REV_TBX O ON "
				//Check for underscore
				+ " (CASE WHEN SUBSTR(MC.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(MC.OPERATION_NAME, 2) ELSE MC.OPERATION_NAME END) = O.OPERATION_NAME "
				+ " AND MC.OP_REV = O.OP_REV " 
				+ " LEFT OUTER JOIN GALADM.MC_OP_PART_REV_TBX P ON "
				//Check for underscore
				+ " (CASE WHEN SUBSTR(MC.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(MC.OPERATION_NAME, 2) ELSE MC.OPERATION_NAME END) = P.OPERATION_NAME "
				+ " AND MC.PART_ID = P.PART_ID AND O.REV_ID = P.REV_ID " 
				+ " LEFT OUTER JOIN GALADM.MC_OP_MEAS_TBX M ON P.OPERATION_NAME = M.OPERATION_NAME AND P.PART_ID = M.PART_ID AND P.PART_REV = M.PART_REV AND MC.OP_MEAS_SEQ_NUM = M.OP_MEAS_SEQ_NUM "
				+ " WHERE MC.OPERATION_NAME = ?1 AND MC.OP_MEAS_SEQ_NUM = ?2 AND MC.CHECK_NAME = ?3 AND " 
				+ " MC.CHECK_POINT = ?4 AND MC.CHECK_SEQ = ?5 AND MC.CHECKER = ?6 AND MC.REACTION_TYPE = ?7";
	
	private static String FIND_ALL_BY_FILTER = "select e from MCMeasurementChecker e where e.id.operationName=:opName and e.id.partId=:partId "
			+ "and e.id.measurementSeqNumber=:measSeqNo and e.id.checkPoint=:checkPt and e.id.checkSeq=:checkSeq and e.id.checkName=:checkName";
	
private static String DELETE_MEAS_CHECKER = "DELETE FROM galadm.MC_OP_MEAS_CHECKER_TBX oc where oc.OPERATION_NAME=?1 and oc.OP_REV in (select o.OP_REV from galadm.MC_OP_REV_TBX o where o.APPROVED is not null and o.DEPRECATED is null and o.OPERATION_NAME=?1)";

private static String DELETE_MEAS_CHECKER_ON_SAVE = "DELETE FROM galadm.MC_OP_MEAS_CHECKER_TBX oc where oc.OPERATION_NAME=?1 and OP_MEAS_SEQ_NUM = ?2 and oc.OP_REV in (select o.OP_REV from galadm.MC_OP_REV_TBX o where o.APPROVED is not null and o.DEPRECATED is null and o.OPERATION_NAME=?1)";



	public List<MCMeasurementChecker> findAllByOperation(String operationName, String partId, int opRevision, String checkPoint) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.partId", partId)
				.put("id.operationRevision", opRevision)
				.put("id.checkPoint", checkPoint);
		return findAll(params);
	}

	public List<MCMeasurementChecker> findAllBy(String operationName,
			String partId, int opRevision) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.partId", partId)
				.put("id.operationRevision", opRevision);
		return findAll(params);
	}

	public List<MCMeasurementChecker> findAllBy(String operationName,
			int opRevision) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.operationRevision", opRevision);
		return findAll(params);
	}
	
	public List<MeasurementCheckerDto> findAllByOperationNamePartNoAndChecker(String operationName, String basePartNo, String checker, String divisionId){
		Parameters params = new Parameters();
		boolean whereClause = false;
		String sql = GET_MEASUREMENT_CHECKER;
		if(!StringUtils.isBlank(operationName)) {
			sql = sql + " MC.OPERATION_NAME = ?1 ";
			params.put("1", operationName);
			whereClause = true;
		}
		if(!StringUtils.isBlank(checker)) {
			sql = sql.concat(whereClause  ? " and MC.CHECKER = ?2  " : " MC.CHECKER = ?1 ");
			params.put(whereClause ? "2" : "1", checker);
			whereClause = true;
		}
		if(!StringUtils.isBlank(basePartNo)) {
			sql = sql.concat(whereClause  ? " and P.PART_NO LIKE '"+ basePartNo + "%' " : " P.PART_NO LIKE '"+ basePartNo + "%' ");
			whereClause = true;
		}
		if(!StringUtils.isBlank(divisionId)) {
			sql = sql.concat(whereClause  ? " and pp.DIVISION_ID = ?3 " : " pp.DIVISION_ID = ?1 ");
			params.put(whereClause ? "3" : "1", divisionId);
			whereClause = true;
		}
		if(!whereClause)
			sql = sql.replace("WHERE", "");
		sql = sql.concat(" ORDER BY OPERATION_NAME ");
		return findAllByNativeQuery(sql, params, MeasurementCheckerDto.class);
	}
	
	public List<MCMeasurementChecker> findAllBy(MeasurementCheckerDto measCheckerDto, List<Integer> opRevList) {
		StringBuilder queryString = new StringBuilder(FIND_ALL_BY_FILTER);
		Parameters params = Parameters.with("opName", measCheckerDto.getOperationName())
				.put("partId", measCheckerDto.getPartId())
				.put("measSeqNo", measCheckerDto.getMeasurementSeqNum())
				.put("checkPt", measCheckerDto.getCheckPoint())
				.put("checkSeq", measCheckerDto.getCheckSeq())
				.put("checkName", measCheckerDto.getCheckName());
		
		if(!opRevList.isEmpty()) {
			queryString.append(" and e.id.operationRevision in (:opRevLst)");
			params.put("opRevLst", opRevList);
		}
		return findAllByQuery(queryString.toString(), params);
	}
	
	public List<String> loadAllMeasurementCheckerOpName(String operationName) {
		String likeOpName = "%" + operationName + "%";
		return findAllByNativeQuery(GET_ALL_OPERATIONS, Parameters.with("1", likeOpName), String.class);
	}
	
	public List<MeasurementCheckerDto> loadMeasurementCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode).put("2", deptCode).put("3", modelYearDate)
				.put("4", prodSchQty).put("5", prodAsmLineNo).put("6", vehicleModelCode);
		return findAllByNativeQuery(GET_ALL_MEASUREMENT_BY_PDDA_PLATFORM, params, MeasurementCheckerDto.class);
	}
	
	public List<MCMeasurementChecker> findAllOpRevBy(String operationName, String partId, String checkPoint, int checkSeq, 
			int measurementSeqNumber, String checkName) {
		Parameters params = Parameters.with("id.operationName", operationName).put("id.partId", partId)
				.put("id.checkPoint", checkPoint).put("id.checkSeq", checkSeq)
				.put("id.measurementSeqNumber", measurementSeqNumber).put("id.checkName", checkName);
		return findAll(params);
	}
	
	public List<MCMeasurementChecker> findAllCheckerBy(MeasurementCheckerDto measCheckerDto) {
		StringBuffer sqlBuffer = new StringBuffer(GET_ALL_CHECKERS);
		Parameters params = Parameters.with("1", measCheckerDto.getOperationName())
				.put("2", measCheckerDto.getMeasurementSeqNum()).put("3", measCheckerDto.getCheckName())
				.put("4", measCheckerDto.getCheckPoint()).put("5", measCheckerDto.getCheckSeq())
				.put("6", measCheckerDto.getChecker()).put("7", measCheckerDto.getReactionType());
		
		if(StringUtils.isNotBlank(measCheckerDto.getPartNo())){
			sqlBuffer.append(" AND P.PART_NO = ?8 ");
			params.put("8", measCheckerDto.getPartNo());
		} else {
			sqlBuffer.append(" AND P.PART_NO IS NULL ");
		}
		if(StringUtils.isNotBlank(measCheckerDto.getPartItemNo())){
			sqlBuffer.append(" AND P.PART_ITEM_NO = ?9");
			params.put("9", measCheckerDto.getPartItemNo());
		}
		if(StringUtils.isNotBlank(measCheckerDto.getPartSectionCode())){
			sqlBuffer.append(" AND P.PART_SECTION_CODE = ?10 ");
			params.put("10", measCheckerDto.getPartSectionCode());
		}
		return findAllByNativeQuery(sqlBuffer.toString(), params);
	}
	
	@Transactional
	@Override
	public void removeAndInsertAll(List<MCMeasurementChecker> insertMeasCheckersList, List<MCMeasurementChecker> removeMeasCheckersList) {
		removeAll(removeMeasCheckersList);
		entityManager.flush();
		insertAll(insertMeasCheckersList);
	}

	@Override
	public List<MCMeasurementChecker> findAllBy(String operationName, String partId, int opRevision,
			int measurementSeqNumber) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.partId", partId)
				.put("id.operationRevision", opRevision)
				.put("id.measurementSeqNumber", measurementSeqNumber);
		return findAll(params);
	}
	@Transactional
	@Override
	public void deleteAndInsertAllMeasurementCheckerRevision(String viosPlatformId, String unitNo,int measSeq, List<MCViosMasterOperationMeasurementChecker> meansCheckerList){
		deleteAllMeasurementCheckerRevision(viosPlatformId, unitNo, measSeq);
		for (MCViosMasterOperationMeasurementChecker mcViosMasterOperationMeasurementChecker : meansCheckerList) {
			saveEntity(mcViosMasterOperationMeasurementChecker);
		}
	}
	

	
	@Transactional
	@Override
	public void deleteAllRevOperation(String viosPlatform, String unitNo) {
		Parameters params =  Parameters.with("1", unitNo+"_"+viosPlatform);
		executeNativeUpdate(DELETE_MEAS_CHECKER, params);
	}
	
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationMeasurementChecker mcViosmOpsMeasChecker) {
		
		List<MCOperationPartRevision> partRevisions = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.findAllActivePartsByOperationNameForMFG(mcViosmOpsMeasChecker.getOperationName());
		for (MCOperationPartRevision operationPartRevision : partRevisions) {
			MCOperationRevision opRev = ServiceFactory.getDao(MCOperationRevisionDao.class)
					.findApprovedByOpNameAndRevId(operationPartRevision.getId().getOperationName(), operationPartRevision.getRevisionId());
			if (opRev != null) {
						MCMeasurementCheckerId id = new MCMeasurementCheckerId(mcViosmOpsMeasChecker.getOperationName(),
								operationPartRevision.getId().getPartId(), opRev.getId().getOperationRevision(),
								mcViosmOpsMeasChecker.getId().getMeasurementSeqNum(),
								mcViosmOpsMeasChecker.getId().getCheckPoint(), mcViosmOpsMeasChecker.getId().getCheckSeq(),
								mcViosmOpsMeasChecker.getId().getCheckName());
						
						MCMeasurementChecker measChecker = new MCMeasurementChecker();
						measChecker.setId(id);
						measChecker.setChecker(mcViosmOpsMeasChecker.getChecker());
						measChecker.setReactionType(mcViosmOpsMeasChecker.getReactionType());
						insert(measChecker);
		     }
		}
		
	}
	
	@Override
	public void deleteAllMeasurementRevision(String viosPlatformId, String unitNo,int measSeq){
		ServiceFactory.getDao(MCOperationMeasurementDao.class).deleteMeasurementRevision(measSeq, unitNo, viosPlatformId);
		deleteAllMeasurementCheckerRevision(viosPlatformId, unitNo, measSeq);
	}
	
	private void deleteAllMeasurementCheckerRevision(String viosPlatformId, String unitNo,int measSeq){
		Parameters params =  Parameters.with("1", unitNo+"_"+viosPlatformId).put("2", measSeq);
		executeNativeUpdate(DELETE_MEAS_CHECKER_ON_SAVE, params);

	}
}
