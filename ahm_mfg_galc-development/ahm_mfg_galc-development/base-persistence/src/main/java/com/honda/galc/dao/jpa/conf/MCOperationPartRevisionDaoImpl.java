package com.honda.galc.dao.jpa.conf;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCOperationPartRevisionDaoImpl 
	extends BaseDaoImpl<MCOperationPartRevision, MCOperationPartRevisionId> 
	implements MCOperationPartRevisionDao {

	private static String FIND_ALL_PARTS_FOR_OPERATION = "SELECT PARTREV.OPERATION_NAME AS OPERATION_NAME, PARTREV.PART_ID AS PART_ID, PARTREV.PART_REV AS PART_REV, PARTREV.REV_ID AS REV_ID,  " +
					" PARTREV.PART_DESC AS PART_DESC, PARTREV.PART_TYPE AS PART_TYPE, PARTREV.PART_VIEW AS PART_VIEW, PARTREV.PART_PROCESSOR AS PART_PROCESSOR,  " +
					" PARTREV.DEVICE_ID AS DEVICE_ID, PARTREV.DEVICE_MSG AS DEVICE_MSG, PARTREV.PART_NO AS PART_NO, PARTREV.PART_SECTION_CODE AS PART_SECTION_CODE,  " +
					" PARTREV.PART_ITEM_NO AS PART_ITEM_NO, PARTREV.PART_MASK AS PART_MASK, PARTREV.PART_MARK AS PART_MARK, PARTREV.PART_MAX_ATTEMPTS AS PART_MAX_ATTEMPTS, " + 
					" PARTREV.MEASUREMENT_COUNT AS MEASUREMENT_COUNT, PARTREV.PART_TIME AS PART_TIME, PARTREV.PART_CHECK AS PART_CHECK, PARTREV.APPROVED AS APPROVED,  " +
					" PARTREV.DEPRECATED_REV_ID AS DEPRECATED_REV_ID, PARTREV.DEPRECATED AS DEPRECATED FROM GALADM.MC_OP_PART_REV_TBX PARTREV " +
					" JOIN GALADM.MC_OP_REV_TBX OPREV ON (PARTREV.OPERATION_NAME = OPREV.OPERATION_NAME AND PARTREV.REV_ID = OPREV.REV_ID) " +
					" JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREV_PLAT ON (OPREV_PLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPREV.OP_REV) " +
					" JOIN GALADM.MC_PDDA_PLATFORM_TBX PDDA_PLAT ON (PDDA_PLAT.PDDA_PLATFORM_ID = OPREV_PLAT.PDDA_PLATFORM_ID) " +
					" JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDA_PLAT.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDA_PLAT.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDA_PLAT.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDA_PLAT.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDA_PLAT.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDA_PLAT.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
					" JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDA_PLAT.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
					" JOIN GALADM.GAL214TBX PROCESSPT ON (PROCESSPT.PROCESS_POINT_ID = VIOS_MAST_PROC.PROCESS_POINT_ID) " +
					" WHERE PROCESSPT.PROCESS_POINT_ID = CAST(?1 AS CHARACTER(16)) AND OPREV.OPERATION_NAME = CAST(?2 AS CHARACTER(32)) AND OPREV.OP_REV = CAST(?3 AS INTEGER)";
	
	private static String GET_MAX_OP_REV_FOR_OPERATION_PART = " SELECT COALESCE(MAX(PART_REV), 0) FROM MC_OP_PART_REV_TBX WHERE OPERATION_NAME = CAST(?1 AS CHARACTER (32))";
	
	private static final String SELECT_ALL_EXCEPT_PART_REV = "SELECT r FROM MCOperationPartRevision r WHERE r.id.operationName = :operationName AND r.id.partId = :partId AND r.id.partRevision <> :partRev";
	
	private static String GET_MAX_APRVD_OP_REV_FOR_OPERATION_PART = " SELECT COALESCE(MAX(PART_REV), 0) FROM MC_OP_PART_REV_TBX WHERE APPROVED IS NOT NULL AND OPERATION_NAME = CAST(?1 AS CHARACTER (32)) AND PART_ID = CAST(?2 AS CHARACTER (5))";
	
	private static final String SELECT_ALL_ACTIVE = "SELECT P.* FROM MC_OP_PART_REV_TBX P" +
			 		" WHERE P.DEPRECATED IS NULL AND P.APPROVED IS NOT NULL" +
			 		" AND P.OPERATION_NAME = CAST(?1 AS CHARACTER (32)) AND P.REV_ID = CAST(?2 AS BIGINT)";
	
	private static final String SELECT_LATEST_APPROVED = "SELECT P.* FROM GALADM.MC_OP_PART_REV_TBX P WHERE " +
	 		" P.APPROVED IS NOT NULL AND P.PART_TYPE = '"+ PartType.MFG.name() + "'" + 
	 		" AND P.OPERATION_NAME = CAST(?1 AS CHARACTER (32)) AND P.REV_ID = CAST(?2 AS BIGINT)" +
	 		" AND P.PART_REV = " + 
	 		" (SELECT MIN(SP.PART_REV) FROM GALADM.MC_OP_PART_REV_TBX SP WHERE" + 
	 		" SP.APPROVED IS NOT NULL AND SP.PART_TYPE = '"+ PartType.MFG.name() + "'" + 
	 		" AND SP.OPERATION_NAME = CAST(?1 AS CHARACTER (32)) AND SP.REV_ID = CAST(?2 AS BIGINT))";
	 		

	private static String GET_ALL_PARTS = "SELECT part FROM MCOperationPartRevision part LEFT JOIN FETCH part.measurements WHERE EXISTS" +
					" (SELECT structure FROM MCStructure structure" +
					" WHERE structure.id.productSpecCode = :productSpecCode" +
					" AND structure.id.revision = :structureRevision" +
					" AND structure.id.processPointId = :processPointId" +
					" AND structure.id.operationName = part.id.operationName" +
					" AND structure.id.partId = part.id.partId" +
					" AND structure.id.partRevision = part.id.partRevision)";
	
	private static String GET_ALL_EFFECTIVE_PARTS = "SELECT part FROM MCOperationPartRevision part LEFT JOIN FETCH part.measurements WHERE EXISTS" +
			" (SELECT structure FROM MCStructure structure" +
			" WHERE structure.id.productSpecCode = :productSpecCode" +
			" AND structure.id.revision = :structureRevision" +
			" AND structure.id.processPointId = :processPointId" +
			" AND structure.id.operationName = part.id.operationName" +
			" AND structure.id.partId = part.id.partId" +
			" AND structure.id.partRevision = part.id.partRevision)" +
			" AND part.partNo in (SELECT bom.id.partNo FROM Bom bom WHERE bom.effEndDate > CURRENT_DATE" + 
            " AND bom.id.mtcModel = SUBSTRING(:productSpecCode , 1 , 4) AND bom.id.mtcType = SUBSTRING(:productSpecCode , 5 , 3))";

	private static final String MAX_PART_ID = "SELECT MAX(PART_ID) FROM MC_OP_PART_REV_TBX where OPERATION_NAME = ?1 and PART_REV = ?2";
	
	private static final String MIN_PART_ID = "SELECT MIN(PART_ID) FROM MC_OP_PART_REV_TBX where OPERATION_NAME = ?1 and PART_REV = ?2";
	
	private static final String MFG_PART_ID = "SELECT PART_TYPE FROM MC_OP_PART_REV_TBX where OPERATION_NAME = ?1 and PART_ID = ?2";
	
	private static String GET_ALL_PARTS_PP = "SELECT part FROM MCOperationPartRevision part LEFT JOIN FETCH part.measurements WHERE EXISTS" +
			" (SELECT structure FROM MCStructure structure" +
			" WHERE structure.id.productSpecCode = :productSpecCode" +
			" AND structure.id.revision = :structureRevision" +
			" AND structure.id.processPointId in(:processPointId)" +
			" AND structure.id.operationName = part.id.operationName" +
			" AND structure.id.partId = part.id.partId" +
			" AND structure.id.partRevision = part.id.partRevision)";
	
	public static String GET_BY_OPERATION = "SELECT DISTINCT TRIM(OPERATION_NAME) FROM GALADM.MC_OP_PART_REV_TBX WHERE OPERATION_NAME LIKE ?1 FETCH FIRST 50 ROWS ONLY";
	
	public static String GET_BY_OPERATION_AND_PART = "SELECT PART_NO, PART_ITEM_NO, PART_SECTION_CODE, PART_TYPE FROM MC_OP_PART_REV_TBX where OPERATION_NAME = ?1";
	
	public static String GET_BY_OPERATION_AND_PART_MEAS = "SELECT P.PART_NO, P.PART_ITEM_NO, P.PART_SECTION_CODE, P.PART_TYPE, M.OP_MEAS_SEQ_NUM, M.MIN_LIMIT, M.MAX_LIMIT "
						+ " FROM GALADM.MC_OP_PART_REV_TBX P "
						+ " JOIN GALADM.MC_OP_MEAS_TBX M ON P.OPERATION_NAME = M.OPERATION_NAME AND P.PART_ID = M.PART_ID AND P.PART_REV = M.PART_REV "
						+ " WHERE P.OPERATION_NAME = ?1 ";
	
	public static String GET_ALL_PART_NO = "SELECT DISTINCT PART_NO FROM MC_OP_PART_REV_TBX ";
	
	private static String GET_MAX_PART_ID = "select SUBSTR(max(p.PART_ID),2) from galadm.MC_OP_PART_TBX p where p.OPERATION_NAME = ?1";
	
	private static String GET_ALL_ACTIVE_BY_OPERATION_NAME = "SELECT P.PART_ID, P.PART_REV FROM GALADM.MC_OP_PART_REV_TBX P "
			+ "JOIN GALADM.MC_OP_REV_TBX O ON P.OPERATION_NAME=O.OPERATION_NAME AND P.REV_ID=O.REV_ID AND O.APPROVED IS NOT NULL AND O.DEPRECATED IS NULL AND UPPER(O.OP_TYPE) IN ('GALC_MEAS','GALC_SCAN_WITH_MEAS','GALC_SCAN_WITH_MEAS_MANUAL','GALC_MEAS_MANUAL') "
			+ "WHERE P.APPROVED IS NOT NULL AND P.DEPRECATED IS NULL AND P.PART_TYPE='MFG' AND P.OPERATION_NAME =  ?1";
	
	private static String GET_ALL_ACTIVE_PARTS_BY_OPERATION_NAME = "select p.* from galadm.MC_OP_PART_REV_TBX p where p.OPERATION_NAME = ?1 and p.DEPRECATED IS NULL and p.APPROVED IS NOT NULL";
	private static String GET_ALL_ACTIVE_PARTS_FOR_MFG = "select p.* from galadm.MC_OP_PART_REV_TBX p where p.OPERATION_NAME = ?1 and p.DEPRECATED IS NULL and p.APPROVED IS NOT NULL and P.PART_TYPE='MFG'";
	
	private static final String FIND_APPROVED_BY_OPERATION_AND_PART = "SELECT * FROM galadm.MC_OP_PART_REV_TBX "
			+ " WHERE OPERATION_NAME=?1 AND %1$s=?2  AND PART_TYPE=?3 "
			+ " AND APPROVED IS NOT NULL AND DEPRECATED IS NULL "
			+ " ORDER BY CREATE_TIMESTAMP DESC";
	
	public List<MCOperationPartRevision> findAllParts(String productSpecCode, long structureRevision, String processPointId) {
	    return findAllParts(productSpecCode, structureRevision, processPointId, false);
	}
	
	public List<MCOperationPartRevision> findAllParts(String productSpecCode, long structureRevision, String processPointId, boolean isEffectivePartQuery) {
	    Parameters params = Parameters.with("productSpecCode", productSpecCode);
	    params.put("structureRevision", structureRevision);
	    params.put("processPointId", processPointId);
	    if(isEffectivePartQuery){
	    	return findAllByQuery(GET_ALL_EFFECTIVE_PARTS, params);
	    }else{
	    	return findAllByQuery(GET_ALL_PARTS, params);
	    }
	}
	
	public List<MCOperationPartRevision> findAllPartForOperationAndOpRevision(String operationName, int revision) {
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.operationRevision", revision);
		return findAll(params);
	}

	public List<MCOperationPartRevision> findAllPartsForOperationAndProcessPoint(String ProcessPoint, String operationName, Integer revision) {
		Parameters params = Parameters.with("1", ProcessPoint);
		params.put("2", operationName);
		params.put("3", revision);
		return findAllByNativeQuery(FIND_ALL_PARTS_FOR_OPERATION, params);
	}

	public List<MCOperationPartRevision> findAllPartsForOperation(
			String operationName) {
		Parameters params = Parameters.with("id.operationName", operationName);
		return findAll(params);
	}
	
	public int  getMaxRevisionForOperationPart(String operationPart) {
		Parameters params = Parameters.with("1", operationPart);
		return findFirstByNativeQuery(GET_MAX_OP_REV_FOR_OPERATION_PART, params, Integer.class);
	}
	
	public int  getMaxAprvdRevisionForOperationPart(String operationPart, String partId) {
		Parameters params = Parameters.with("1", operationPart);
		params.put("2", partId);
		return findFirstByNativeQuery(GET_MAX_APRVD_OP_REV_FOR_OPERATION_PART, params, Integer.class);
	}
	
	public List<MCOperationPartRevision> findAllByRevision(long revisionId) {
		return findAll(Parameters.with("revisionId", revisionId), new String[]{"id.partRevision"}, true);
	}
	
	public List<MCOperationPartRevision> findAllExceptPartRevision(String operationName, String partId, int partRev) {
		Parameters params = Parameters.with("operationName", operationName);
		params.put("partId", partId);
		params.put("partRev", partRev);
		return findAllByQuery(SELECT_ALL_EXCEPT_PART_REV, params);
	}
	
	public List<MCOperationPartRevision> findAllBy(String operationName, long revisionId) {
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("revisionId", revisionId);
		String[] orderBy = {"id.partRevision"};
		return findAll(params, orderBy, true);
	}
	
	public List<MCOperationPartRevision> findAllActiveBy(String operationName, long revisionId) {
		Parameters params = Parameters.with("1", operationName);
		params.put("2", revisionId);
		return findAllByNativeQuery(SELECT_ALL_ACTIVE, params);
	}
	
	public List<MCOperationPartRevision> findLatestApvdMfgPartsBy(String operationName, long revisionId) {
		Parameters params = Parameters.with("1", operationName);
		params.put("2", revisionId);
		return findAllByNativeQuery(SELECT_LATEST_APPROVED, params);
	}
	
	public String findMaxPartId(String operationName, int partRev) {
		
		/*Parameters params = Parameters.with("1", operationName);
		params.put("2", partRev);
		
		return max("PART_ID", String.class, params);*/
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.partRevision", partRev);
		
		return max("id.partId", String.class, params);
		//return findFirstByNativeQuery(MAX_PART_ID, params, String.class);
		
	}
	
	public String findMinPartId(String operationName, int partRev) {
		//Parameters params = Parameters.with("1", operationName);
/*		params.put("2", partRev);
*/		//return min("PART_ID", String.class, params);
		Parameters params = Parameters.with("id.operationName", operationName);
		params.put("id.partRevision", partRev);
		
		return min("id.partId", String.class, params);
		//return findFirstByNativeQuery(MIN_PART_ID, params, String.class);
		
	}
	
	public boolean getMFGPartType(String operationName, String partId) {
		Parameters params = Parameters.with("1", operationName);
		params.put("2", partId);
		return findFirstByNativeQuery(MFG_PART_ID, params, String.class).equals(PartType.MFG.name()) ? true : false;
	}
	
	public List<MCOperationPartRevision> findAllPartsPP(String productSpecCode, long structureRevision, String[] processPointIds) {
		List<String> processPointIdsList = Arrays.asList(processPointIds); 
	    Parameters params = Parameters.with("productSpecCode", productSpecCode);
	    params.put("structureRevision", structureRevision);
	    params.put("processPointId", processPointIdsList);
		return findAllByQuery(GET_ALL_PARTS_PP, params);
	}
	
	public List<String> findAllByOperationName(String operationName) {
		String likeOpName = "%" + operationName + "%";
		return findAllByNativeQuery(GET_BY_OPERATION, Parameters.with("1", likeOpName), String.class);
	}
	
	public List<PartDetailsDto> findAllByOperationPartAndPartType(String operationName, String partNo, String partType) {
		String sql = GET_BY_OPERATION_AND_PART;
		 Parameters params = Parameters.with("1", operationName);
		 if(!StringUtils.isBlank(partNo)) {
			 sql =  sql.concat(" AND PART_NO like '" + partNo + "%'");
		 }
		 if(!StringUtils.isBlank(partType) && !StringUtils.equals(partType, "ALL")) {
			 sql = sql.concat(" AND PART_TYPE = ?2 ");
			 params.put("2", partType);
		 }
		 sql = sql.concat(" GROUP BY PART_NO, PART_ITEM_NO, PART_SECTION_CODE, PART_TYPE ");
		 return findAllByNativeQuery(sql, params, PartDetailsDto.class);
	}
	
	public List<PartDetailsDto> findAllByPartByOpPartTypeAndMeasurement(String operationName, String partNo, String partType) {
		String sql = GET_BY_OPERATION_AND_PART_MEAS;
		 Parameters params = Parameters.with("1", operationName);
		 if(StringUtils.isNotBlank(partNo)) {
			 sql =  sql.concat(" AND PART_NO like '" + partNo + "%'");
		 }
		 if(StringUtils.isNotBlank(partType) && !StringUtils.equals(partType, "ALL")) {
			 sql = sql.concat(" AND PART_TYPE = ?2 ");
			 params.put("2", partType);
		 }
		 sql = sql.concat(" GROUP BY P.PART_NO, P.PART_ITEM_NO, P.PART_SECTION_CODE, P.PART_TYPE, M.OP_MEAS_SEQ_NUM, M.MIN_LIMIT, M.MAX_LIMIT ");
		 return findAllByNativeQuery(sql, params, PartDetailsDto.class);
	}
	
	public List<String> findAllPartNoByOperationName(String partNo, String operationName) {
		String sql = GET_ALL_PART_NO; 
		sql = sql.concat(" WHERE PART_NO LIKE '" + partNo + "%' AND OPERATION_NAME = ?1 ORDER BY PART_NO FETCH FIRST 20 ROWS ONLY");
		return findAllByNativeQuery(sql, Parameters.with("1", operationName), String.class);
	}
	
	public MCOperationPartRevision findAllByPartNoSecCodeItemNoAndType(String opName, String partNo, String partItemNo, String partSectionCode, PartType partType) {
		Parameters params = Parameters.with("id.operationName", opName).put("partNo", partNo)
				.put("partType", partType);
		if(StringUtils.isNotBlank(partItemNo))
			params.put("partItemNo", partItemNo);
		if(StringUtils.isNotBlank(partSectionCode))
			params.put("partSectionCode", partSectionCode);
		return findFirst(params);
	}
	
	public String getMaxPartId(String operationName) {
		return findFirstByNativeQuery(GET_MAX_PART_ID, Parameters.with("1", operationName), String.class);
	}
	
	public List<McOperationDataDto> findAllActiveByOperationName(String operationName) {
		Parameters params = Parameters.with("1", operationName);
		return findAllByNativeQuery(GET_ALL_ACTIVE_BY_OPERATION_NAME, params, McOperationDataDto.class);
	}
	
	public List<MCOperationPartRevision> findAllActivePartsByOperationName(String operationName) {
		Parameters params = Parameters.with("1", operationName);
		return findAllByNativeQuery(GET_ALL_ACTIVE_PARTS_BY_OPERATION_NAME, params);
	}
	
	public List<MCOperationPartRevision> findAllActivePartsByOperationNameForMFG(String operationName){
		Parameters params = Parameters.with("1", operationName);
		return findAllByNativeQuery(GET_ALL_ACTIVE_PARTS_FOR_MFG, params);
	}

	@Override
	public MCOperationPartRevision findApprovedByPartNoAndType(String opName, String partNo,String partType) {
		String partParam = partNo;
		String partClause = "COALESCE(PART_NO,'')";
		if(StringUtils.isBlank(partNo) && partType.equalsIgnoreCase(PartType.MFG.name())) {
			//This is default MFG part
			partParam = ApplicationConstants.DEFAULT_MFG_PART_ID;
			partClause = "PART_ID";
		}
		Parameters params = Parameters.with("1", opName)
				.put("2", partParam)
				.put("3", partType);
		
		return findFirstByNativeQuery(String.format(FIND_APPROVED_BY_OPERATION_AND_PART, partClause), params);
	}

	@Override
	public List<MCOperationPartRevision> findAllApprovedByPartNoAndType(String operationName, String partNo,
			String partType) {
		String partParam = partNo;
		String partClause = "COALESCE(PART_NO,'')";
		if(StringUtils.isBlank(partNo) && partType.equalsIgnoreCase(PartType.MFG.name())) {
			//This is default MFG part
			partParam = ApplicationConstants.DEFAULT_MFG_PART_ID;
			partClause = "PART_ID";
		}
		Parameters params = Parameters.with("1", operationName)
				.put("2", partParam)
				.put("3", partType);
		
		return findAllByNativeQuery(String.format(FIND_APPROVED_BY_OPERATION_AND_PART, partClause), params);
	}
	
	
}
