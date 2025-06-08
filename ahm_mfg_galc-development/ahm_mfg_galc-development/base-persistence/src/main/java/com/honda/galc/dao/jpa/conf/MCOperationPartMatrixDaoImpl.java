package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrixId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCOperationPartMatrixDaoImpl 
	extends BaseDaoImpl<MCOperationPartMatrix, MCOperationPartMatrixId> 
	implements MCOperationPartMatrixDao {
	

	private static String FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID = "SELECT OPPARTMATRIX.* FROM GALADM.MC_OP_PART_MATRIX_TBX OPPARTMATRIX"
			+ " JOIN GALADM.MC_OP_PART_REV_TBX OPPARTREV ON (OPPARTMATRIX.OPERATION_NAME = OPPARTREV.OPERATION_NAME AND OPPARTMATRIX.PART_ID = OPPARTREV.PART_ID AND OPPARTREV.PART_REV = OPPARTMATRIX.PART_REV)"
			+ " JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPPARTREV.OPERATION_NAME = OPREV.OPERATION_NAME AND OPPARTREV.REV_ID = OPREV.REV_ID)"
			+ " JOIN GALADM.MC_PDDA_PLATFORM_TBX PDDA_PLAT ON OPPARTREV.REV_ID=PDDA_PLAT.REV_ID"
			+ " JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREV_PLAT ON (OPREV_PLAT.PDDA_PLATFORM_ID = PDDA_PLAT.PDDA_PLATFORM_ID AND OPREV_PLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPREV.OP_REV)"
			+ " WHERE PDDA_PLAT.PDDA_PLATFORM_ID IN"
			+ " (SELECT P.PDDA_PLATFORM_ID FROM GALADM.MC_PDDA_PLATFORM_TBX P"
			+ " INNER JOIN (SELECT PLANT_LOC_CODE,DEPT_CODE,MODEL_YEAR_DATE,PROD_SCH_QTY,PROD_ASM_LINE_NO,VEHICLE_MODEL_CODE,ASM_PROC_NO FROM GALADM.MC_PDDA_PLATFORM_TBX IP WHERE PDDA_PLATFORM_ID=?1)PF"
			+ " ON P.PLANT_LOC_CODE=PF.PLANT_LOC_CODE AND P.DEPT_CODE=PF.DEPT_CODE AND P.MODEL_YEAR_DATE=PF.MODEL_YEAR_DATE"
			+ " AND P.PROD_SCH_QTY=PF.PROD_SCH_QTY AND P.PROD_ASM_LINE_NO=PF.PROD_ASM_LINE_NO AND P.VEHICLE_MODEL_CODE=PF.VEHICLE_MODEL_CODE"
			+ " AND P.ASM_PROC_NO=PF.ASM_PROC_NO)"
			+ " AND OPPARTREV.APPROVED IS NOT NULL AND OPPARTREV.DEPRECATED IS NULL";
	
	private static String FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID_SPEC_CODE_MASK = "SELECT OPPARTMATRIX.* FROM GALADM.MC_OP_PART_MATRIX_TBX OPPARTMATRIX"
			+ " JOIN GALADM.MC_OP_PART_REV_TBX OPPARTREV ON (OPPARTMATRIX.OPERATION_NAME = OPPARTREV.OPERATION_NAME AND OPPARTMATRIX.PART_ID = OPPARTREV.PART_ID AND OPPARTREV.PART_REV = OPPARTMATRIX.PART_REV)"
			+ " JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPPARTREV.OPERATION_NAME = OPREV.OPERATION_NAME AND OPPARTREV.REV_ID = OPREV.REV_ID)"
			+ " JOIN GALADM.MC_PDDA_PLATFORM_TBX PDDA_PLAT ON OPPARTREV.REV_ID=PDDA_PLAT.REV_ID"
			+ " JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREV_PLAT ON (OPREV_PLAT.PDDA_PLATFORM_ID = PDDA_PLAT.PDDA_PLATFORM_ID AND OPREV_PLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPREV.OP_REV)"
			+ " WHERE PDDA_PLAT.PDDA_PLATFORM_ID IN"
			+ " (SELECT P.PDDA_PLATFORM_ID FROM GALADM.MC_PDDA_PLATFORM_TBX P"
			+ " INNER JOIN (SELECT PLANT_LOC_CODE,DEPT_CODE,MODEL_YEAR_DATE,PROD_SCH_QTY,PROD_ASM_LINE_NO,VEHICLE_MODEL_CODE,ASM_PROC_NO FROM GALADM.MC_PDDA_PLATFORM_TBX IP WHERE PDDA_PLATFORM_ID=?1)PF"
			+ " ON P.PLANT_LOC_CODE=PF.PLANT_LOC_CODE AND P.DEPT_CODE=PF.DEPT_CODE AND P.MODEL_YEAR_DATE=PF.MODEL_YEAR_DATE"
			+ " AND P.PROD_SCH_QTY=PF.PROD_SCH_QTY AND P.PROD_ASM_LINE_NO=PF.PROD_ASM_LINE_NO AND P.VEHICLE_MODEL_CODE=PF.VEHICLE_MODEL_CODE"
			+ " AND P.ASM_PROC_NO=PF.ASM_PROC_NO)"
			+ " AND OPPARTMATRIX.SPEC_CODE_MASK = ?2"
			+ " AND OPPARTREV.APPROVED IS NOT NULL AND OPPARTREV.DEPRECATED IS NULL";
	
	private static String FIND_MATRIX_BY_DEPT_AND_ASM_PROC = "SELECT PM.OPERATION_NAME, PM.PART_ID, PM.PART_REV, PM.SPEC_CODE_TYPE, PM.SPEC_CODE_MASK FROM GALADM.MC_OP_PART_MATRIX_TBX PM "
			+ "JOIN GALADM.MC_OP_PART_REV_TBX PR ON PM.OPERATION_NAME=PR.OPERATION_NAME AND PM.PART_ID=PR.PART_ID AND PM.PART_REV=PR.PART_REV "
			+ "JOIN GALADM.MC_OP_REV_TBX O ON PR.OPERATION_NAME=O.OPERATION_NAME AND PR.REV_ID=O.REV_ID "
			+ "JOIN GALADM.MC_PDDA_PLATFORM_TBX P ON PR.REV_ID=P.REV_ID "
			+ "WHERE P.DEPT_CODE = ?1 AND P.ASM_PROC_NO = ?2 and PR.approved is not null and PR.DEPRECATED is null";
	
	private static String FIND_MATRIX_BY_PLATFORM_AND_ASM_PROC = "SELECT PM.OPERATION_NAME, PM.PART_ID, PM.PART_REV, PM.SPEC_CODE_TYPE, PM.SPEC_CODE_MASK FROM GALADM.MC_OP_PART_MATRIX_TBX PM "
			+ "JOIN GALADM.MC_OP_PART_REV_TBX PR ON PM.OPERATION_NAME=PR.OPERATION_NAME AND PM.PART_ID=PR.PART_ID AND PM.PART_REV=PR.PART_REV "
			+ "JOIN GALADM.MC_OP_REV_TBX O ON PR.OPERATION_NAME=O.OPERATION_NAME AND PR.REV_ID=O.REV_ID "
			+ "JOIN GALADM.MC_OP_REV_PLATFORM_TBX OP ON OP.OP_REV = O.OP_REV and OP.OPERATION_NAME = O.OPERATION_NAME "
			+ "JOIN GALADM.MC_PDDA_PLATFORM_TBX P ON  P.PDDA_PLATFORM_ID = OP.PDDA_PLATFORM_ID "
			+ "WHERE P.ASM_PROC_NO = ?1 and PR.approved is not null and PR.DEPRECATED is null";
	
	private static String FIND_MATRIX_COUNT = "SELECT COUNT(*) FROM GALADM.MC_OP_PART_MATRIX_TBX WHERE " +
							"OPERATION_NAME = ?1 AND PART_ID = ?2 AND PART_REV = ?3 AND SPEC_CODE_TYPE = ?4 AND SPEC_CODE_MASK = ?5";
	
	private static String FIND_ALL_BY_OPERATION_PART_ID_AND_PART_REV = "SELECT * FROM GALADM.MC_OP_PART_MATRIX_TBX WHERE OPERATION_NAME = ?1 AND PART_ID = ?2 AND PART_REV = ?3 AND SPEC_CODE_MASK IN "+
				"(SELECT PM.SPEC_CODE_MASK FROM GALADM.MC_OP_PART_MATRIX_TBX PM WHERE PM.OPERATION_NAME = ?1 AND PM.PART_ID = ?4 AND PM.PART_REV = ?5)";
	
	private static final String GET_MBPN_MASK_QUERY = "select * from galadm.MC_OP_PART_MATRIX_TBX c JOIN galadm.MC_OP_PART_REV_TBX p "
			+ "on c.OPERATION_NAME=p.OPERATION_NAME and c.PART_ID=p.PART_ID and c.PART_REV=p.PART_REV "
			+ "JOIN galadm.MC_OP_REV_TBX o on c.OPERATION_NAME=o.OPERATION_NAME and p.REV_ID=o.REV_ID and o.APPROVED is not null "
			+ "and o.DEPRECATED is null JOIN galadm.MC_OP_REV_PLATFORM_TBX r on o.OPERATION_NAME=r.OPERATION_NAME "
			+ "and o.OP_REV=r.OP_REV JOIN galadm.MC_PDDA_PLATFORM_TBX d on r.PDDA_PLATFORM_ID = d.PDDA_PLATFORM_ID "
			+ "where p.APPROVED is not null and p.DEPRECATED is null ";

	private static final String GET_CHILD_PART_TYPE = "WITH UNIT_PARENT as (select distinct(OPERATION_NAME) as UNIT, trim(TRAILING '*' FROM SPEC_CODE_MASK) as PARENT from MC_OP_PART_MATRIX_TBX where OPERATION_NAME like ?1) " + 
			", PARENT_CHILD as (select trim(TRAILING '*' FROM trim(PRODUCT_SPEC_CODE)) as PARENT, ATTRIBUTE_VALUE as CHILD from gal259tbx where ATTRIBUTE = 'COMPONENT_PARTS' and ATTRIBUTE_VALUE = ?2) " + 
			"select UNIT_PARENT.UNIT, PARENT_CHILD.PARENT, PARENT_CHILD.CHILD from UNIT_PARENT " + 
			"join PARENT_CHILD on substring(PARENT_CHILD.PARENT,1,10) = UNIT_PARENT.PARENT";
	
	public List<MCOperationPartMatrix> findAllSpecCodeForOperationPartIdAndPartRev(
			String operationName, String partId, int partRevision) {
		
		Parameters param = Parameters.with("id.operationName", operationName);
		param.put("id.partId", partId);
		param.put("id.partRevision", partRevision);
		return findAll(param);
	}
	
	public List<MCOperationPartMatrix> findAllMatrixByPDDAPlatformId(int pddaPltformId) {
		Parameters params = Parameters.with("1", pddaPltformId);
		return findAllByNativeQuery(FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID, params);
	}
	
	public List<MCOperationPartMatrix> findAllMatrixByPDDAPlatformIdAndSpecCodeMask(int pddaPltformId, String specCodeMask) {
		Parameters params = Parameters.with("1", pddaPltformId);
		params.put("2", specCodeMask);
		return findAllByNativeQuery(FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID_SPEC_CODE_MASK, params);
	}
	
	@Transactional
	public void deletePartMatrix(String operationName, String partId, int partRevision, String specCodeMask) {
		Parameters param = Parameters.with("id.operationName", operationName);
		param.put("id.partId", partId);
		param.put("id.partRevision", partRevision);
		param.put("id.specCodeMask", specCodeMask);
		delete(param);
	}
	
	public List<MCOperationPartMatrix> findByOperationNamePartIdRevisionAndMask(String operationName, String mfgPartId, int mfgPartRevision, String refPartId, int refPartRevision) {
		Parameters param = Parameters.with("1", operationName).put("2", mfgPartId).put("3", mfgPartRevision)
				.put("4", refPartId).put("5", refPartRevision);
		return findAllByNativeQuery(FIND_ALL_BY_OPERATION_PART_ID_AND_PART_REV, param);
	}
	
	public List<McOperationDataDto> findAllByDeptCodeAndAsmProc(String deptCode, String asmProcNumber, String modelCode) {
		String sql = FIND_MATRIX_BY_DEPT_AND_ASM_PROC + " AND PM.SPEC_CODE_MASK LIKE '" +  modelCode + "%'  ";
		Parameters params = Parameters.with("1", deptCode).put("2", asmProcNumber);
		return findAllByNativeQuery(sql, params, McOperationDataDto.class);
	}
	
	public List<McOperationDataDto> findAllPartMatrixByPlatformAndAsmProc (String viosPlatformId, String asmProcNumber, String modelCode) {
		String sql = FIND_MATRIX_BY_PLATFORM_AND_ASM_PROC + " AND PM.SPEC_CODE_MASK LIKE '" +  modelCode + "%' AND  PM.OPERATION_NAME LIKE '%" +  viosPlatformId + "%'";
		Parameters params = Parameters.with("1", asmProcNumber);
		return findAllByNativeQuery(sql, params, McOperationDataDto.class);
	}
	
	
	public int getMatrixCountBy(String operationName, String partId, int partRev, String specCodeType, String mbpnMask) {
		Parameters params = Parameters.with("1", operationName).put("2", partId).put("3", partRev)
				.put("4", specCodeType).put("5", mbpnMask);
		return findFirstByNativeQuery(FIND_MATRIX_COUNT, params, Integer.class);
	}
	
	@Transactional
	public void deleteMBPNPartMatrixData(MCViosMasterMBPNMatrixData mcmbpnMasterData, String viosPlatformId, boolean isUpload) {
		
		String queryString = GET_MBPN_MASK_QUERY + " AND O.OPERATION_NAME LIKE '%" +  viosPlatformId + "%' ";
		Parameters params = null;
		
		if(!isUpload) {
			queryString = queryString+" and c.SPEC_CODE_MASK = ?1 and d.ASM_PROC_NO = ?2";
			params =Parameters.with("1", mcmbpnMasterData.getId().getMbpnMask());
			params.put("2", mcmbpnMasterData.getId().getAsmProcNo());
		} else {
			queryString = queryString+" and c.SPEC_CODE_MASK not like '"+mcmbpnMasterData.getId().getMtcModel().charAt(0)+"%' ";
		}
		List<MCOperationPartMatrix> oprationPartMatrixData = findAllByNativeQuery(queryString, params);
		
		
		for (MCOperationPartMatrix mcOperationPartMatrix : oprationPartMatrixData) {
			Parameters opMatrixParams = Parameters.with("id.operationName", mcOperationPartMatrix.getId().getOperationName());
			opMatrixParams.put("id.partId", mcOperationPartMatrix.getId().getPartId());
			opMatrixParams.put("id.partRevision", mcOperationPartMatrix.getId().getPartRevision());
			opMatrixParams.put("id.specCodeMask", mcOperationPartMatrix.getId().getSpecCodeMask());
			delete(opMatrixParams);
		}
	} 
	
	public List<Object[]> findByChildPart(String operationName, String productSpecCode) {
		Parameters params = Parameters.with("1", operationName+"%");
		params.put("2", productSpecCode);
		return findAllByNativeQuery(GET_CHILD_PART_TYPE, params, Object[].class);
	}
}
