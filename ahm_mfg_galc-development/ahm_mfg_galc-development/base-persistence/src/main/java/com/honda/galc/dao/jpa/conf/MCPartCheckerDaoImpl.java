package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.PartCheckerDto;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public class MCPartCheckerDaoImpl extends BaseDaoImpl<MCPartChecker, MCPartCheckerId> implements MCPartCheckerDao {

	private static String GET_PART_CHECKER = "SELECT DISTINCT opc.OPERATION_NAME, opc.PART_ID, opc.OP_REV, opc.CHECK_POINT, opc.CHECK_SEQ, opc.CHECK_NAME, opc.CHECKER, "
					+ " opr.PART_NO, opr.PART_SECTION_CODE, opr.PART_ITEM_NO, opc.REACTION_TYPE, opr.PART_TYPE, opr.APPROVED, " 
					+ " opr.DEPRECATED  FROM galadm.MC_OP_PART_CHECKER_TBX opc "
					+ " LEFT OUTER JOIN galadm.MC_OP_REV_TBX ort ON "
					//Check for underscore
					+ " ort.OPERATION_NAME = (CASE WHEN SUBSTR(opc.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(opc.OPERATION_NAME, 2) ELSE opc.OPERATION_NAME END) "
					+ " AND ort.OP_REV = opc.OP_REV "
					+ " LEFT OUTER JOIN galadm.MC_OP_PART_REV_TBX opr ON "
					//Check for underscore
					+ " opr.OPERATION_NAME = (CASE WHEN SUBSTR(opc.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(opc.OPERATION_NAME, 2) ELSE opc.OPERATION_NAME END) "
					+ " AND opr.PART_ID = opc.PART_ID AND opr.REV_ID = ort.REV_ID " 
					+ " LEFT OUTER JOIN galadm.MC_OP_REV_PLATFORM_TBX orp on orp.OPERATION_NAME = ort.OPERATION_NAME and orp.OP_REV = ort.OP_REV "
					+ " LEFT OUTER JOIN galadm.MC_PDDA_PLATFORM_TBX ppt on ppt.PDDA_PLATFORM_ID = orp.PDDA_PLATFORM_ID "
					+ " LEFT OUTER JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON ppt.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND ppt.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND ppt.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND ppt.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND ppt.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND ppt.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE "
					+ " LEFT OUTER JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND ppt.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO "
					+ " LEFT OUTER JOIN galadm.GAL214TBX pp on pp.PROCESS_POINT_ID = VIOS_MAST_PROC.PROCESS_POINT_ID WHERE ";
	
	public static String GET_ALL_OPERATIONS = "SELECT DISTINCT TRIM(OPERATION_NAME) FROM galadm.MC_OP_PART_CHECKER_TBX WHERE OPERATION_NAME LIKE ?1 FETCH FIRST 20 ROWS ONLY";
	
	private static String GET_ALL_PART_BY_PDDA_PLATFORM = "SELECT DISTINCT PC.*, pr.PART_NO, pr.PART_SECTION_CODE, pr.PART_ITEM_NO, pr.PART_TYPE FROM MC_OP_PART_CHECKER_TBX pc "
				+ " join GALADM.MC_OP_REV_PLATFORM_TBX orp on orp.OPERATION_NAME = pc.OPERATION_NAME and orp.OP_REV = pc.OP_REV "
				+ " join GALADM.MC_PDDA_PLATFORM_TBX p on p.PDDA_PLATFORM_ID = orp.PDDA_PLATFORM_ID "
				+ " join GALADM.MC_OP_REV_TBX r on pc.OPERATION_NAME = r.OPERATION_NAME and pc.OP_REV = r.OP_REV "
				+ " JOIN GALADM.MC_OP_PART_REV_TBX PR on PR.OPERATION_NAME = r.OPERATION_NAME and PR.REV_ID = r.REV_ID and pr.PART_ID = pc.PART_ID "
				+ " where p.PLANT_LOC_CODE = ?1 and p.DEPT_CODE = ?2 and p.MODEL_YEAR_DATE = ?3  and p.PROD_SCH_QTY = ?4 and p.PROD_ASM_LINE_NO = ?5 and p.VEHICLE_MODEL_CODE = ?6 "
				+ " and r.APPROVED is not null and r.DEPRECATED is null and PR.APPROVED is not null and PR.DEPRECATED is null";
	
	private static String GET_ALL_CHECKERS = "SELECT DISTINCT opc.* FROM MC_OP_PART_CHECKER_TBX opc "
				+ " LEFT OUTER JOIN MC_OP_REV_TBX ort ON "
				//Check for underscore
				+ " ort.OPERATION_NAME = (CASE WHEN SUBSTR(opc.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(opc.OPERATION_NAME, 2) ELSE opc.OPERATION_NAME END) "
				+ " AND ort.OP_REV = opc.OP_REV "
				+ " LEFT OUTER JOIN MC_OP_PART_REV_TBX opr ON "
				//Check for underscore
				+ " opr.OPERATION_NAME = (CASE WHEN SUBSTR(opc.OPERATION_NAME, 1, 1) = '_' THEN SUBSTR(opc.OPERATION_NAME, 2) ELSE opc.OPERATION_NAME END) "
				+ " AND opr.PART_ID = opc.PART_ID AND opr.REV_ID = ort.REV_ID "	
				+ " WHERE opc.OPERATION_NAME = ?1 AND opc.CHECK_POINT = ?2 AND opc.CHECK_NAME = ?3 AND opc.CHECK_SEQ = ?4";
	
	private static String FIND_ALL_BY_FILTER = "select e from MCPartChecker e where e.id.operationName=:opName and e.id.partId=:partId and e.id.checkPoint=:checkPt and e.id.checkSeq=:checkSeq";
	
	public List<MCPartChecker> findAllByOperation(String operationName, String partId, int opRevision, String checkPoint) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.partId", partId)
				.put("id.operationRevision", opRevision)
				.put("id.checkPoint", checkPoint);
		return findAll(params);
	}

	public List<MCPartChecker> findAllBy(String operationName, String partId,
			int opRevision) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.partId", partId)
				.put("id.operationRevision", opRevision);
		return findAll(params);
	}

	public List<MCPartChecker> findAllBy(String operationName, int opRevision) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.operationRevision", opRevision);
		return findAll(params);
	}
	
	public List<PartCheckerDto> findAllByOperationNamePartNoAndChecker(String operationName, String basePartNo, String checker, String divisionId){
		Parameters params = new Parameters();
		boolean whereClause = false;
		String sql = GET_PART_CHECKER;
		if(StringUtils.isNotBlank(operationName)) {
			sql = sql + " opc.OPERATION_NAME = ?1 ";
			params.put("1", operationName);
			whereClause = true;
		}
		if(StringUtils.isNotBlank(checker)) {
			sql = sql.concat(whereClause  ? " and opc.CHECKER = ?2  " : " opc.CHECKER = ?1 ");
			params.put(whereClause ? "2" : "1", checker);
			whereClause = true;
		}
		if(StringUtils.isNotBlank(basePartNo)) {
			sql = sql.concat(whereClause  ? " and opr.PART_NO LIKE '"+ basePartNo + "%' " : " opr.PART_NO LIKE '"+ basePartNo + "%' ");
			whereClause = true;
		}
		if(StringUtils.isNotBlank(divisionId)) {
			sql = sql.concat(whereClause  ? " and pp.DIVISION_ID = ?3  " : " pp.DIVISION_ID = ?1 ");
			params.put(whereClause ? "3" : "1", divisionId);
			whereClause = true;
		}
		if(!whereClause)
			sql = sql.replace("WHERE", "");
		return findAllByNativeQuery(sql, params, PartCheckerDto.class);
	}
	
	public List<MCPartChecker> findAllOpRevBy(String operationName, String partId, String checkPoint, int checkSeq, List<Integer> opRevList) {
		StringBuilder queryString = new StringBuilder(FIND_ALL_BY_FILTER);
		Parameters params = Parameters.with("opName", operationName)
										.put("partId", partId)
										.put("checkPt", checkPoint)
										.put("checkSeq", checkSeq);
		if(!opRevList.isEmpty()) {
			queryString.append(" and e.id.operationRevision in (:opRevLst)");
			params.put("opRevLst", opRevList);
		}
		return findAllByQuery(queryString.toString(), params);
	}
	
	public List<String> loadAllPartCheckerOpName(String operationName) {
		String likeOpName = "%" + operationName + "%";
		return findAllByNativeQuery(GET_ALL_OPERATIONS, Parameters.with("1", likeOpName), String.class);
	}
	
	public List<PartCheckerDto> loadPartCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode).put("2", deptCode).put("3", modelYearDate)
				.put("4", prodSchQty).put("5", prodAsmLineNo).put("6", vehicleModelCode);
		return findAllByNativeQuery(GET_ALL_PART_BY_PDDA_PLATFORM, params, PartCheckerDto.class);
	}
	
	public List<MCPartChecker> findAllPartCheckerBy(PartCheckerDto partCheckerDto) {
		StringBuffer sqlBuffer = new StringBuffer(GET_ALL_CHECKERS);
		Parameters params = Parameters.with("1", partCheckerDto.getOperationName()).put("2", partCheckerDto.getCheckPoint())
				.put("3", partCheckerDto.getCheckName()).put("4", partCheckerDto.getCheckSeq());
		
		if(StringUtils.isNotBlank(partCheckerDto.getPartNo())){
			sqlBuffer.append(" AND opr.PART_NO = ?5 ");
			params.put("5", partCheckerDto.getPartNo());
		} else {
			sqlBuffer.append(" AND opr.PART_NO IS NULL ");
		}
		if(StringUtils.isNotBlank(partCheckerDto.getPartItemNo())){
			sqlBuffer.append(" AND opr.PART_ITEM_NO = ?6 ");
			params.put("6", partCheckerDto.getPartItemNo());
		}
		if(StringUtils.isNotBlank(partCheckerDto.getPartSectionCode())){
			sqlBuffer.append(" AND opr.PART_SECTION_CODE = ?7 ");
			params.put("7", partCheckerDto.getPartSectionCode());
		}
		return findAllByNativeQuery(sqlBuffer.toString(), params);
	}
	
	@Transactional
	@Override
	public void removeAndInsertAll(List<MCPartChecker> insertPartCheckerList, List<MCPartChecker> removePartCheckerList) {
		removeAll(removePartCheckerList);
		entityManager.flush();
		insertAll(insertPartCheckerList);
	}
	
	@Transactional
	@Override
	public void deleteAndInsertAllPartRevisionCheckers(String viosPlatformId, String unitNo,String partNo, List<MCViosMasterOperationPartChecker> partCheckerList) {
		deleteAllPartRev(viosPlatformId, unitNo, partNo);
		for (MCViosMasterOperationPartChecker mcViosMasterOperationPartChecker : partCheckerList) {
			saveEntity(mcViosMasterOperationPartChecker);
		}
	}
	@Transactional
	@Override
	public void deleteAllPartRev(String viosPlatform, String unitNo, String partNo) {
		List<MCOperationPartRevision> opPartRev = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.findAllApprovedByPartNoAndType(
						unitNo + "_"+viosPlatform,partNo, "MFG");
		for (MCOperationPartRevision mcOperationPartRevision : opPartRev) {
			MCOperationRevision opRev = ServiceFactory.getDao(MCOperationRevisionDao.class)
					.findApprovedByOpNameAndRevId(mcOperationPartRevision.getId().getOperationName(), mcOperationPartRevision.getRevisionId());
			if (opRev != null) {
				Parameters params = Parameters.with("id.operationName", unitNo + "_"+viosPlatform)
						.put("id.partId", mcOperationPartRevision.getId().getPartId())
						.put("id.operationRevision", opRev.getId().getOperationRevision());
				delete(params);
			}
		}
			
	}
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationPartChecker mcViosMasterOpPartChecker) {
		
	try {
		
		List<MCOperationPartRevision> opPartRev = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.findAllApprovedByPartNoAndType(
						mcViosMasterOpPartChecker.getId().getUnitNo() + "_"
								+ mcViosMasterOpPartChecker.getId().getViosPlatformId(),
						mcViosMasterOpPartChecker.getId().getPartNo(), "MFG");
		for (MCOperationPartRevision mcOperationPartRevision : opPartRev) {
			MCOperationRevision opRev = ServiceFactory.getDao(MCOperationRevisionDao.class)
					.findApprovedByOpNameAndRevId(mcOperationPartRevision.getId().getOperationName(), mcOperationPartRevision.getRevisionId());
			if (opRev != null) {
				String operationName = mcViosMasterOpPartChecker.getOperationName();
				String partId = mcOperationPartRevision.getId().getPartId();
				String checkpoint = mcViosMasterOpPartChecker.getId().getCheckPoint();
				int checkSeq = mcViosMasterOpPartChecker.getId().getCheckSeq();
				int opRevNo = opRev.getId().getOperationRevision();
				MCPartCheckerId mcPartCheckerId = new MCPartCheckerId(operationName, partId, opRevNo, checkpoint,
						checkSeq);
				MCPartChecker partChecker = new MCPartChecker();
				partChecker.setId(mcPartCheckerId);
				partChecker.setChecker(mcViosMasterOpPartChecker.getChecker());
				partChecker.setCheckName(mcViosMasterOpPartChecker.getCheckName());
				partChecker.setReactionType(mcViosMasterOpPartChecker.getReactionType());
				insert(partChecker);
			}
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<MCPartChecker> findAllByDetails(String operationName, String checkPointName, int opRevision) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.operationRevision", opRevision)
				.put("id.checkPoint", checkPointName);
		return findAll(params);
	}
}
