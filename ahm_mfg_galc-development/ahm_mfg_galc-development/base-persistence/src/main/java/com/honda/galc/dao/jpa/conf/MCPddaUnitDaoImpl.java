package com.honda.galc.dao.jpa.conf;

import java.util.List;

import com.honda.galc.dao.conf.MCPddaUnitDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCPddaUnit;
import com.honda.galc.entity.conf.MCPddaUnitId;
import com.honda.galc.service.Parameters;

public class MCPddaUnitDaoImpl extends BaseDaoImpl<MCPddaUnit, MCPddaUnitId> 
			implements MCPddaUnitDao{
	
	private final String SELECT_ACTIVE_PDDA_UNIT = "SELECT PU.* FROM GALADM.MC_PDDA_UNIT_TBX PU WHERE PU.DEPRECATED IS NULL "
			+ "AND PU.APPROVED IS NOT NULL AND (PU.APPROVER_NO IS NOT NULL AND TRIM(PU.APPROVER_NO) <> '') "
			+ "AND PU.PDDA_PLATFORM_ID = CAST(?1 AS INTEGER) AND PU.REV_ID = CAST(?2 AS BIGINT)";
	
	private final static String FIND_ALL_BY_PDDA_PLATFROM_ID_SPEC_CODE_MASK = "Select PDDA_UNIT.* from GALADM.MC_PDDA_UNIT_TBX PDDA_UNIT"
			+ " JOIN GALADM.MC_OP_REV_TBX OPREV ON (PDDA_UNIT.OPERATION_NAME = OPREV.OPERATION_NAME AND PDDA_UNIT.REV_ID = OPREV.REV_ID)"
			+ " JOIN GALADM.MC_OP_MATRIX_TBX OPMATRIX ON (OPMATRIX.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREV.OP_REV = OPMATRIX.OP_REV AND PDDA_UNIT.PDDA_PLATFORM_ID = OPMATRIX.PDDA_PLATFORM_ID)"
			+ " WHERE PDDA_UNIT.PDDA_PLATFORM_ID IN"
			+ " (SELECT P.PDDA_PLATFORM_ID FROM GALADM.MC_PDDA_PLATFORM_TBX P"
			+ " INNER JOIN (SELECT PLANT_LOC_CODE,DEPT_CODE,MODEL_YEAR_DATE,PROD_SCH_QTY,PROD_ASM_LINE_NO,VEHICLE_MODEL_CODE,ASM_PROC_NO FROM GALADM.MC_PDDA_PLATFORM_TBX IP WHERE PDDA_PLATFORM_ID=?1)PF"
			+ " ON P.PLANT_LOC_CODE=PF.PLANT_LOC_CODE AND P.DEPT_CODE=PF.DEPT_CODE AND P.MODEL_YEAR_DATE=PF.MODEL_YEAR_DATE"
			+ " AND P.PROD_SCH_QTY=PF.PROD_SCH_QTY AND P.PROD_ASM_LINE_NO=PF.PROD_ASM_LINE_NO AND P.VEHICLE_MODEL_CODE=PF.VEHICLE_MODEL_CODE"
			+ " AND P.ASM_PROC_NO=PF.ASM_PROC_NO)"
			+ " AND OPMATRIX.SPEC_CODE_MASK = ?2 AND PDDA_UNIT.DEPRECATED IS NULL"
			+ " AND PDDA_UNIT.APPROVED IS NOT NULL AND (PDDA_UNIT.APPROVER_NO IS NOT NULL AND TRIM(PDDA_UNIT.APPROVER_NO) <> '')";
	
	public List<MCPddaUnit> findBy(int pddaPlatformId, long revId) {
		return findAll(Parameters.with("id.pddaPlatformId", pddaPlatformId).put("id.revId", revId));
	}
	
	public List<MCPddaUnit> findBy(int pddaPlatformId, long revId,
			String operationName) {
		return findAll(Parameters.with("id.pddaPlatformId", pddaPlatformId).put("id.revId", revId).put("id.operationName", operationName));
	}
	
	public List<MCPddaUnit> findActivePddaUnits(int pddaPlatformId, long revId) {
		Parameters param = new Parameters();
		param.put("1", pddaPlatformId);
		param.put("2", revId);
		return findAllByNativeQuery(SELECT_ACTIVE_PDDA_UNIT, param);
	}
	
	public List<MCPddaUnit> findAllByPDDAPlatformIdSpecCodeMask(int pddaPlatformId, String specCodeMask) {
		Parameters param = Parameters.with("1", pddaPlatformId);
		param.put("2", specCodeMask);
		return findAllByNativeQuery(FIND_ALL_BY_PDDA_PLATFROM_ID_SPEC_CODE_MASK, param);
	}
}