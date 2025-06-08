package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCOperationCheckerDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationCheckerId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public class MCOperationCheckerDaoImpl extends BaseDaoImpl<MCOperationChecker, MCOperationCheckerId> implements MCOperationCheckerDao {

	private static String GET_OPERATION_CHECKER = "SELECT DISTINCT oct.OPERATION_NAME, oct.OP_REV, oct.CHECK_POINT, oct.CHECK_SEQ, oct.CHECK_NAME, oct.CHECKER, oct.REACTION_TYPE, ort.APPROVED, ort.DEPRECATED "
			+ " FROM MC_OP_CHECKER_TBX oct "
			+ " LEFT OUTER JOIN MC_OP_REV_TBX ort on ort.OPERATION_NAME = oct.OPERATION_NAME and ort.OP_REV = oct.OP_REV "
			+ " LEFT OUTER JOIN MC_OP_REV_PLATFORM_TBX orp on orp.OPERATION_NAME = ort.OPERATION_NAME and orp.OP_REV = ort.OP_REV "
			+ " LEFT OUTER JOIN MC_PDDA_PLATFORM_TBX ppt on ppt.PDDA_PLATFORM_ID = orp.PDDA_PLATFORM_ID "
			+ " LEFT OUTER JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON ppt.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND ppt.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND ppt.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND ppt.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND ppt.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND ppt.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE "
			+ " LEFT OUTER JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND ppt.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO "
			+ " LEFT OUTER JOIN GAL214TBX pp on pp.PROCESS_POINT_ID = VIOS_MAST_PROC.PROCESS_POINT_ID "; 
												
			
	private static String GET_ALL_OPERATION_BY_PDDA_PLATFORM = "SELECT o.* FROM GALADM.MC_OP_CHECKER_TBX o "
							+ " join GALADM.MC_OP_REV_PLATFORM_TBX orp on orp.OPERATION_NAME = o.OPERATION_NAME and orp.OP_REV = o.OP_REV "
							+ " join GALADM.MC_PDDA_PLATFORM_TBX p on p.PDDA_PLATFORM_ID = orp.PDDA_PLATFORM_ID "
							+ " join GALADM.MC_OP_REV_TBX r on o.OPERATION_NAME = r.OPERATION_NAME and o.OP_REV = r.OP_REV "
							+ " where p.PLANT_LOC_CODE = ?1 and p.DEPT_CODE = ?2  and p.MODEL_YEAR_DATE = ?3  and p.PROD_SCH_QTY = ?4 and " 
							+ " p.PROD_ASM_LINE_NO = ?5 and p.VEHICLE_MODEL_CODE = ?6 and r.APPROVED is not null and r.DEPRECATED is null";
	
	public static String GET_ALL_OPERATIONS = "SELECT DISTINCT TRIM(OPERATION_NAME) FROM galadm.MC_OP_CHECKER_TBX WHERE OPERATION_NAME LIKE ?1 FETCH FIRST 20 ROWS ONLY";
	
	private static String FIND_ALL_BY_FILTER = "select e from MCOperationChecker e where e.id.operationName=:opName and e.id.checkPoint=:checkPt and e.id.checkSeq=:checkSeq";
	
	private static String DELETE_OPERATION = "DELETE from galadm.MC_OP_CHECKER_TBX oc where oc.OPERATION_NAME=?1 and oc.OP_REV in (select o.OP_REV from galadm.MC_OP_REV_TBX o where o.APPROVED is not null and o.DEPRECATED is null and o.OPERATION_NAME=?1)";
	public List<MCOperationChecker> findAllBy(String operationName,
			int operationRevision) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.operationRevision", operationRevision);
		return findAll(params);
	}
	
	public List<OperationCheckerDto> findAllByOperationNameAndChecker(String opName, String checker, String divisionId) {
		Parameters params = new Parameters();
		boolean whereClause = false;
		String sql = GET_OPERATION_CHECKER;
		if(!StringUtils.isBlank(opName)) {
			sql = sql + " where oct.OPERATION_NAME = ?1 ";
			params.put("1", opName);
			whereClause = true;
		}
		if(!StringUtils.isBlank(checker)) {
			sql = sql.concat(whereClause  ? " and oct.CHECKER = ?2  " : " where oct.CHECKER = ?1 ");
			params.put(whereClause ? "2" : "1", checker);
			whereClause = true;
		}
		if(!StringUtils.isBlank(divisionId)) {
			sql = sql.concat(whereClause  ? " and pp.DIVISION_ID = ?3  " : " where pp.DIVISION_ID = ?1 ");
			params.put(whereClause ? "3" : "1", divisionId);
		}
		return findAllByNativeQuery(sql, params, OperationCheckerDto.class);
	}
	
	public List<MCOperationChecker> findAllBy(String operationName, String checkPoint, int checkSeq, List<Integer> opRevList) {
		StringBuilder queryString = new StringBuilder(FIND_ALL_BY_FILTER);
		Parameters params = Parameters.with("opName", operationName)
										.put("checkPt", checkPoint)
										.put("checkSeq", checkSeq);
		if(!opRevList.isEmpty()) {
			queryString.append(" and e.id.operationRevision in (:opRevLst)");
			params.put(":opRevLst", opRevList);
		}
		return findAllByQuery(queryString.toString(), params);
	}
	
	public List<MCOperationChecker> loadOperationCheckerByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode).put("2", deptCode)
				.put("3", modelYearDate).put("4", prodSchQty)
				.put("5", prodAsmLineNo).put("6", vehicleModelCode);
		return findAllByNativeQuery(GET_ALL_OPERATION_BY_PDDA_PLATFORM, params, MCOperationChecker.class);
	}
	
	public List<String> findByOperationName(String opName) {
		String likeOpName = "%" + opName + "%";
		return findAllByNativeQuery(GET_ALL_OPERATIONS, Parameters.with("1", likeOpName), String.class);
	}
	
	@Transactional
	public void deleteCheckerByOpNameCheckPtAndSeq(String operationName, String checkPoint, int checkSeq) {
		Parameters params = Parameters.with("id.operationName", operationName)
				.put("id.checkPoint", checkPoint).put("id.checkSeq", checkSeq);
		delete(params);
	}
	
	@Transactional
	@Override
	public void removeAndInsertAll(List<MCOperationChecker> insertOpCheckerList, List<MCOperationChecker> removeOpCheckerList) {
		removeAll(removeOpCheckerList);
		entityManager.flush();
		insertAll(insertOpCheckerList);
	}
	
	@Transactional
	@Override
	public void deleteAllRevOperation(String viosPlatform, String unitNo) {
		Parameters params = Parameters.with("1", unitNo+"_"+viosPlatform);
		executeNativeUpdate(DELETE_OPERATION, params);
	}
	
	@Transactional
	@Override
	public void deleteAndInsertAllOperatioRevCheckers(String viosPlatformId, String unitNo, List<MCViosMasterOperationChecker> masterOperationCheckerList) {
		deleteAllRevOperation(viosPlatformId, unitNo);
		
		for (MCViosMasterOperationChecker mcViosMasterOperationChecker : masterOperationCheckerList) {
			saveEntity(mcViosMasterOperationChecker);
		}
	}
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperationChecker masterOpChecker) {
		try {
			String operationName = masterOpChecker.getOperationName();
			@SuppressWarnings("unchecked")
			MCOperationRevisionDao revDao = ServiceFactory.getDao(MCOperationRevisionDao.class);
			MCOperationRevision opRev = revDao.findApprovedOperationBy(operationName);
			if (opRev != null) {
				MCOperationCheckerId id = new MCOperationCheckerId(opRev.getId().getOperationName(),
						opRev.getId().getOperationRevision(), masterOpChecker.getId().getCheckPoint(),
						masterOpChecker.getId().getCheckSeq());

				MCOperationChecker opChecker = new MCOperationChecker();
				opChecker.setId(id);
				opChecker.setChecker(masterOpChecker.getChecker());
				opChecker.setCheckName(masterOpChecker.getCheckName());
				opChecker.setReactionType(masterOpChecker.getReactionType());
				insert(opChecker);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
