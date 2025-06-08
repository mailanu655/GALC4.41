package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.List;

import com.honda.galc.dao.conf.MCPddaUnitRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.CopyCheckerDetailsDto;
import com.honda.galc.entity.conf.MCPddaUnitRevision;
import com.honda.galc.entity.conf.MCPddaUnitRevisionId;
import com.honda.galc.service.Parameters;

public class MCPddaUnitRevisionDaoImpl extends BaseDaoImpl<MCPddaUnitRevision, MCPddaUnitRevisionId> implements MCPddaUnitRevisionDao{
	private final String SELECT_LATEST_REVISION = "SELECT r.* FROM GALADM.MC_PDDA_UNIT_REV_TBX r WHERE"
			+ " r.OPERATION_NAME = ?1 AND r.PDDA_PLATFORM_ID = ?2 AND r.PDDA_REPORT = ?3"
			+ " AND r.OP_REV = (SELECT MAX(OP_REV) FROM GALADM.MC_PDDA_UNIT_REV_TBX WHERE OPERATION_NAME = ?1 AND PDDA_PLATFORM_ID = ?2 AND PDDA_REPORT = ?3)";
	
	private final String FIND_ALL_OPERATION_NAME_AND_REV = "SELECT DISTINCT U.OPERATION_NAME, U.OP_REV FROM MC_PDDA_UNIT_REV_TBX U " 
			+ " JOIN GALADM.MC_PDDA_PLATFORM_TBX P on P.PDDA_PLATFORM_ID = U.PDDA_PLATFORM_ID "
			+ " JOIN GALADM.MC_OP_REV_TBX O on O.OPERATION_NAME = U.OPERATION_NAME and O.OP_REV = U.OP_REV "
			+ " WHERE U.UNIT_NO = ?1 AND p.PLANT_LOC_CODE = ?2 AND p.DEPT_CODE = ?3 AND p.MODEL_YEAR_DATE = ?4  AND " 
			+ " p.PROD_SCH_QTY = ?5 AND p.PROD_ASM_LINE_NO = ?6 AND p.VEHICLE_MODEL_CODE = ?7 "
			+ " AND O.APPROVED is not null AND O.DEPRECATED is null";
	
	private final String FIND_ALL_OPERATION_NAME_REV_AND_PART_ID = "SELECT DISTINCT U.OPERATION_NAME, U.OP_REV, " 
			+ "PR.PART_ID, pr.PART_NO, pr.PART_SECTION_CODE, pr.PART_ITEM_NO, pr.PART_TYPE FROM MC_PDDA_UNIT_REV_TBX U " 
			+ " JOIN GALADM.MC_PDDA_PLATFORM_TBX P on P.PDDA_PLATFORM_ID = U.PDDA_PLATFORM_ID " 
			+ " join GALADM.MC_OP_REV_TBX O on O.OPERATION_NAME = U.OPERATION_NAME and O.OP_REV = U.OP_REV "
			+ " JOIN GALADM.MC_OP_PART_REV_TBX PR on PR.OPERATION_NAME = O.OPERATION_NAME and O.REV_ID = PR.REV_ID "
			+ " WHERE U.UNIT_NO = ?1 and p.PLANT_LOC_CODE = ?2 and p.DEPT_CODE = ?3 and p.MODEL_YEAR_DATE = ?4  and " 
			+ " p.PROD_SCH_QTY = ?5 and p.PROD_ASM_LINE_NO = ?6 and p.VEHICLE_MODEL_CODE = ?7 "
			+ " and O.APPROVED is not null and O.DEPRECATED is null";
	
	private final String FIND_ALL_OPERATION_NAME_REV_PART_ID_AND_SEQ_NO = "SELECT DISTINCT U.OPERATION_NAME, U.OP_REV, PR.PART_ID, om.OP_MEAS_SEQ_NUM, pr.PART_NO, " 
			+ " pr.PART_SECTION_CODE, pr.PART_ITEM_NO, pr.PART_TYPE, om.OP_MEAS_SEQ_NUM, om.MIN_LIMIT, om.MAX_LIMIT, pr.PART_TYPE FROM MC_PDDA_UNIT_REV_TBX U " 
			+ " JOIN GALADM.MC_PDDA_PLATFORM_TBX P on P.PDDA_PLATFORM_ID = U.PDDA_PLATFORM_ID " 
			+ " join GALADM.MC_OP_REV_TBX O on O.OPERATION_NAME = U.OPERATION_NAME and O.OP_REV = U.OP_REV "
			+ " JOIN GALADM.MC_OP_PART_REV_TBX PR on PR.OPERATION_NAME = O.OPERATION_NAME and O.REV_ID = PR.REV_ID "
			+ " JOIN GALADM.MC_OP_MEAS_TBX om on om.OPERATION_NAME = PR.OPERATION_NAME and om.PART_ID = PR.PART_ID AND om.PART_REV = PR.PART_REV "
			+ " WHERE U.UNIT_NO = ?1 and p.PLANT_LOC_CODE = ?2 and p.DEPT_CODE = ?3 and p.MODEL_YEAR_DATE = ?4  and " 
			+ " p.PROD_SCH_QTY = ?5 and p.PROD_ASM_LINE_NO = ?6 and p.VEHICLE_MODEL_CODE = ?7 "
			+ " and O.APPROVED is not null and O.DEPRECATED is null";
	
	public MCPddaUnitRevision getLatestRevision(String operationName,
			long pddaPlatformId, String pddaReport) {
		if(operationName != null && pddaReport != null) {
			Parameters params = Parameters.with("1", operationName);
			params.put("2", pddaPlatformId);
			params.put("3", pddaReport);
			return findFirstByNativeQuery(SELECT_LATEST_REVISION, params);
		}
		return null;
	}
	
	public List<MCPddaUnitRevision> findAllBy(String operationName, int opRevId, int pddaPltformId) {
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.operationRevision", opRevId);
		params.put("id.pddaPlatformId", pddaPltformId);
		
		return findAll(params);
	}
	
	public MCPddaUnitRevision findBy(String operationName, int opRevId, int pddaPltformId) {
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.operationRevision", opRevId);
		params.put("id.pddaPlatformId", pddaPltformId);
		MCPddaUnitRevision pddaUnitRevision = findFirst(params);
		if(pddaUnitRevision!=null) {
			pddaUnitRevision.getId().setPddaReport(null);
		}
		return pddaUnitRevision;
	}
	
	public MCPddaUnitRevision findBy(String operationName, int opRevId) {
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.operationRevision", opRevId);
		return findFirst(params);
	}
	
	public List<CopyCheckerDetailsDto> findAllByUnitNoAndPddaDetails(String unitNo, String plantLocCode, String deptCode, BigDecimal modelYearDate, 
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode, String checker) {
		Parameters params = Parameters.with("1", unitNo).put("2", plantLocCode).put("3", deptCode)
				.put("4", modelYearDate).put("5", prodSchQty).put("6", prodAsmLineNo).put("7", vehicleModelCode);
	if(checker.equals("OC")) 
		return findAllByNativeQuery(FIND_ALL_OPERATION_NAME_AND_REV, params, CopyCheckerDetailsDto.class);
	else if(checker.equals("PC"))
		return findAllByNativeQuery(FIND_ALL_OPERATION_NAME_REV_AND_PART_ID, params, CopyCheckerDetailsDto.class);
	else
		return findAllByNativeQuery(FIND_ALL_OPERATION_NAME_REV_PART_ID_AND_SEQ_NO, params, CopyCheckerDetailsDto.class);
		
	}
}