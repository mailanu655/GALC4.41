package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMatrixId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCOperationMatrixDaoImpl 
	extends BaseDaoImpl<MCOperationMatrix, MCOperationMatrixId> 
	implements MCOperationMatrixDao {

	private static String FIND_ALL_FOR_OPER_AND_PROCESS_POINT = "SELECT OPERMATRIX.OPERATION_NAME, OPERMATRIX.OP_REV, OPERMATRIX.PDDA_PLATFORM_ID,  " +
							" OPERMATRIX.SPEC_CODE_TYPE, OPERMATRIX.SPEC_CODE_MASK FROM GALADM.MC_OP_MATRIX_TBX OPERMATRIX " +
							" JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREV_PLAT ON ( OPERMATRIX.PDDA_PLATFORM_ID = OPREV_PLAT.PDDA_PLATFORM_ID AND OPREV_PLAT.OPERATION_NAME = OPERMATRIX.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPERMATRIX.OP_REV) " +
							" JOIN GALADM.MC_PDDA_PLATFORM_TBX PDDA_PLAT ON (PDDA_PLAT.PDDA_PLATFORM_ID = OPREV_PLAT.PDDA_PLATFORM_ID) " +
							" JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDA_PLAT.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDA_PLAT.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDA_PLAT.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDA_PLAT.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDA_PLAT.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDA_PLAT.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
							" JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDA_PLAT.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
							" JOIN GALADM.GAL214TBX PROCESSPT ON (PROCESSPT.PROCESS_POINT_ID = VIOS_MAST_PROC.PROCESS_POINT_ID) " +
							" WHERE PROCESSPT.PROCESS_POINT_ID = CAST(?1 AS CHARACTER(16)) AND OPREV_PLAT.OPERATION_NAME = CAST(?2 AS CHARACTER(32)) AND OPREV_PLAT.OP_REV = CAST(?3 AS INTEGER)";
	
	private static String GET_ALL_OPE_MATRIX_FOR_REVID = "SELECT OPMAT.OPERATION_NAME, OPMAT.OP_REV, OPMAT.PDDA_PLATFORM_ID, OPMAT.SPEC_CODE_TYPE, OPMAT.SPEC_CODE_MASK FROM GALADM.MC_OP_MATRIX_TBX OPMAT WHERE OPMAT.OPERATION_NAME = ?1 AND OPMAT.OP_REV = ?2 ORDER BY OPMAT.SPEC_CODE_TYPE, OPMAT.SPEC_CODE_MASK";
	
	private static String FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID = "SELECT OPMATRIX.* FROM GALADM.MC_OP_MATRIX_TBX OPMATRIX"
			+ " JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREV_PLAT ON (OPREV_PLAT.OPERATION_NAME = OPMATRIX.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPMATRIX.OP_REV AND OPREV_PLAT.PDDA_PLATFORM_ID = OPMATRIX.PDDA_PLATFORM_ID)"
			+ " JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPREV_PLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPREV.OP_REV)"
			+ " JOIN GALADM.MC_PDDA_PLATFORM_TBX PDDA_PLAT ON (PDDA_PLAT.PDDA_PLATFORM_ID = OPREV_PLAT.PDDA_PLATFORM_ID)"  
			+ " WHERE PDDA_PLAT.PDDA_PLATFORM_ID IN"
			+ " (SELECT P.PDDA_PLATFORM_ID FROM GALADM.MC_PDDA_PLATFORM_TBX P"
			+ " INNER JOIN (SELECT PLANT_LOC_CODE,DEPT_CODE,MODEL_YEAR_DATE,PROD_SCH_QTY,PROD_ASM_LINE_NO,VEHICLE_MODEL_CODE,ASM_PROC_NO FROM GALADM.MC_PDDA_PLATFORM_TBX IP WHERE PDDA_PLATFORM_ID=?1)PF"
			+ " ON P.PLANT_LOC_CODE=PF.PLANT_LOC_CODE AND P.DEPT_CODE=PF.DEPT_CODE AND P.MODEL_YEAR_DATE=PF.MODEL_YEAR_DATE"
			+ " AND P.PROD_SCH_QTY=PF.PROD_SCH_QTY AND P.PROD_ASM_LINE_NO=PF.PROD_ASM_LINE_NO AND P.VEHICLE_MODEL_CODE=PF.VEHICLE_MODEL_CODE"
			+ " AND P.ASM_PROC_NO=PF.ASM_PROC_NO)"
			+ " AND OPREV.APPROVED IS NOT NULL AND OPREV.DEPRECATED IS NULL";
	
	private static String FIND_MATRIX_BY_DEPT_AND_ASM_PROC = "SELECT OM.OPERATION_NAME, OM.OP_REV, OM.PDDA_PLATFORM_ID, OM.SPEC_CODE_TYPE, OM.SPEC_CODE_MASK FROM GALADM.MC_OP_MATRIX_TBX OM "
			+ "JOIN GALADM.MC_PDDA_PLATFORM_TBX P ON OM.PDDA_PLATFORM_ID=P.PDDA_PLATFORM_ID "
			+ "JOIN GALADM.MC_OP_REV_TBX O ON OM.OPERATION_NAME=O.OPERATION_NAME AND OM.OP_REV=O.OP_REV AND P.REV_ID=O.REV_ID "
			+ "JOIN GALADM.MC_OP_REV_PLATFORM_TBX OP ON O.OPERATION_NAME=OP.OPERATION_NAME AND O.OP_REV=OP.OP_REV AND P.PDDA_PLATFORM_ID=OP.PDDA_PLATFORM_ID "
			+ "WHERE P.DEPT_CODE = ?1 AND P.ASM_PROC_NO = ?2 and o.approved is not null and o.DEPRECATED is null";
	
	private static String FIND_MATRIX_BY_PLATFORM_AND_ASM_PROC = "SELECT OM.OPERATION_NAME, OM.OP_REV, OM.PDDA_PLATFORM_ID, OM.SPEC_CODE_TYPE, OM.SPEC_CODE_MASK FROM GALADM.MC_OP_MATRIX_TBX OM "
			+ "JOIN GALADM.MC_PDDA_PLATFORM_TBX P ON OM.PDDA_PLATFORM_ID=P.PDDA_PLATFORM_ID "
			+ "JOIN GALADM.MC_OP_REV_TBX O ON OM.OPERATION_NAME=O.OPERATION_NAME AND OM.OP_REV=O.OP_REV AND P.REV_ID=O.REV_ID "
			+ "JOIN GALADM.MC_OP_REV_PLATFORM_TBX OP ON O.OPERATION_NAME=OP.OPERATION_NAME AND O.OP_REV=OP.OP_REV AND P.PDDA_PLATFORM_ID=OP.PDDA_PLATFORM_ID "
			+ "WHERE P.ASM_PROC_NO = ?1 and o.approved is not null and o.DEPRECATED is null";
	
	private static String FIND_MATRIX_COUNT = "SELECT COUNT(*) FROM GALADM.MC_OP_MATRIX_TBX WHERE OPERATION_NAME = ?1 AND OP_REV = ?2" +
							"AND PDDA_PLATFORM_ID = ?3 AND SPEC_CODE_TYPE = ?4 AND SPEC_CODE_MASK = ?5";
	
	private static String GET_MBPN_MASK_QUERY = "select * from galadm.MC_OP_MATRIX_TBX c JOIN galadm.MC_OP_REV_TBX o "
			+ "on c.OPERATION_NAME=o.OPERATION_NAME and c.OP_REV=o.OP_REV "
			+ "JOIN galadm.MC_PDDA_PLATFORM_TBX d on c.PDDA_PLATFORM_ID = d.PDDA_PLATFORM_ID "
			+ "where o.APPROVED is not null and o.DEPRECATED is null";
	
	
	
	public List<MCOperationMatrix> findAllMatrixForOperationAndRevId(
			String operationName, Integer opRevId) {
		
		Parameters params = Parameters.with("1", operationName);
		params.put("2", opRevId);
		
		return findAllByNativeQuery(GET_ALL_OPE_MATRIX_FOR_REVID,params);
	}

	public List<MCOperationMatrix> findAllMatrixForOperationAndProcessPoint(
			String ProcessPoint, String operationName, Integer revisionId) {
		
		Parameters params = Parameters.with("1", ProcessPoint);
		params.put("2", operationName);
		params.put("3", revisionId);
		
		return  findAllByNativeQuery(FIND_ALL_FOR_OPER_AND_PROCESS_POINT, params);
	}
	
	public List<MCOperationMatrix> findAllBy(
			String operationName, int opRevId, int pddaPltformId) {
		
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.operationRevision", opRevId);
		params.put("id.pddaPlatformId", pddaPltformId);
		
		return findAll(params);
	}
	
	@Transactional
	public int deleteExcludedOperations(String operationName, Integer opRevId, Integer pddaPltformId){
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.operationRevision", opRevId);
		params.put("id.pddaPlatformId", pddaPltformId);
		params.put("id.specCodeType", "EXCLUDE");
		
		return delete(params);
	}
	
	public List<MCOperationMatrix> findAllMatrixByPDDAPlatformId(int pddaPltformId) {
		Parameters params = Parameters.with("1", pddaPltformId);
		return findAllByNativeQuery(FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID, params);
	}

	public List<MCOperationMatrix> findAllMatrixByPDDAPlatformIdAndSpecCodeMask(
			int pddaPltformId, String specCodeMask) {
		Parameters params = Parameters.with("1", pddaPltformId);
		params.put("2", specCodeMask);
		return findAllByNativeQuery(FIND_ALL_MATRIX_BY_PDDA_PLATFORM_ID+" AND OPMATRIX.SPEC_CODE_MASK = ?2", params);
	}
	
	public List<McOperationDataDto> findAllByDeptCodeAndAsmProc(String deptCode, String asmProcNumber, String model_code) {
		String sql = FIND_MATRIX_BY_DEPT_AND_ASM_PROC + " AND OM.SPEC_CODE_MASK LIKE '" +  model_code + "%'  ";
		Parameters params = Parameters.with("1", deptCode).put("2", asmProcNumber);
		return findAllByNativeQuery(sql, params, McOperationDataDto.class);
	}
	
	public List<McOperationDataDto> findAllByPlatformAndAsmProc(String platform, String asmProcNumber, String model_code) {
		String sql = FIND_MATRIX_BY_PLATFORM_AND_ASM_PROC + " AND OM.SPEC_CODE_MASK LIKE '" +  model_code + "%' AND O.OPERATION_NAME LIKE '%" +  platform + "%' ";
		Parameters params = Parameters.with("1", asmProcNumber);
		return findAllByNativeQuery(sql, params, McOperationDataDto.class);
	}
	
	public int getMatrixCountBy(String operationName, int opRev, int pddaPltformId, String specCodeType, String mbpnMask) {
		Parameters params = Parameters.with("1", operationName).put("2", opRev).put("3", pddaPltformId)
				.put("4", specCodeType).put("5", mbpnMask);
		return findFirstByNativeQuery(FIND_MATRIX_COUNT, params, Integer.class);
	}
	
	@Transactional
	public void deleteMBPNOpMatrixData(MCViosMasterMBPNMatrixData mcmbpnMasterData, String viosPlatformId, boolean isUpload) {
		
		String queryString = GET_MBPN_MASK_QUERY + " AND O.OPERATION_NAME LIKE '%" +  viosPlatformId + "%' ";

		Parameters params = null;
		if(!isUpload)
		{
			queryString = queryString+" and c.SPEC_CODE_MASK = ?1 and d.ASM_PROC_NO = ?2";
			params = Parameters.with("1", mcmbpnMasterData.getId().getMbpnMask());
			params.put("2", mcmbpnMasterData.getId().getAsmProcNo());
		}  else {
			queryString = queryString+" and c.SPEC_CODE_MASK not like '"+mcmbpnMasterData.getId().getMtcModel().charAt(0)+"%' ";
		}
		
		List<MCOperationMatrix> oprationMatrixData = findAllByNativeQuery(queryString, params);
		
		for (MCOperationMatrix mcOperationMatrix : oprationMatrixData) {
			Parameters opMatrixParams = Parameters.with("id.operationName", mcOperationMatrix.getId().getOperationName());
			opMatrixParams.put("id.pddaPlatformId", mcOperationMatrix.getId().getPddaPlatformId());
			opMatrixParams.put("id.operationRevision", mcOperationMatrix.getId().getOperationRevision());
			opMatrixParams.put("id.specCodeMask", mcOperationMatrix.getId().getSpecCodeMask());
			delete(opMatrixParams);
		}
		
	}
}
