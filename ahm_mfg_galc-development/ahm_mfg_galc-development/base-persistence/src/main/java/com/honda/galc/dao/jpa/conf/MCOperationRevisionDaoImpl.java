package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.PartDetailsDto;
import com.honda.galc.dto.McOperationDataDto;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;

import org.springframework.transaction.annotation.Transactional;
/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCOperationRevisionDaoImpl extends BaseDaoImpl<MCOperationRevision, MCOperationRevisionId> 
	implements MCOperationRevisionDao {
	
	private static final String COMMON_NAME ="COMMON_NAME";
	private static String FIND_ALL_OPEARATIONS_FOR_PROCESSPOINT = "SELECT OPREV.OPERATION_NAME AS OPERATION_NAME, OPREV.OP_REV AS OP_REV, OPREV.REV_ID AS REV_ID, OPREV.OP_DESC AS OP_DESC,  " +
								" OPREV.OP_TYPE AS OP_TYPE, OPREV.OP_VIEW AS OP_VIEW, OPREV.OP_PROCESSOR AS OP_PROCESSOR, OPREV.OP_CHECK AS OP_CHECK,  " +
								" OPREV.APPROVED AS APPROVED, OPREV.DEPRECATED_REV_ID AS DEPRECATED_REV_ID, OPREV.DEPRECATED AS DEPRECATED " +
								" FROM GALADM.MC_OP_REV_TBX OPREV " +
								" JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREV_PLAT ON (OPREV_PLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREV_PLAT.OP_REV = OPREV.OP_REV) " +
								" JOIN GALADM.MC_PDDA_PLATFORM_TBX PDDA_PLAT ON (PDDA_PLAT.PDDA_PLATFORM_ID = OPREV_PLAT.PDDA_PLATFORM_ID) " +
								" JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDA_PLAT.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDA_PLAT.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDA_PLAT.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDA_PLAT.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDA_PLAT.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDA_PLAT.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
								" JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDA_PLAT.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
								" JOIN GALADM.GAL214TBX PROCESSPT ON (PROCESSPT.PROCESS_POINT_ID = VIOS_MAST_PROC.PROCESS_POINT_ID) " +
								" WHERE PROCESSPT.PROCESS_POINT_ID = CAST(?1 AS CHARACTER(16))";
	
	//This query uses the above mentioned query i.e. FIND_ALL_OPEARATIONS_FOR_PROCESSPOINT with some added parameters
	private static String FIND_ALL_BY_PROCESS_POINT_AND_PLATFORM = FIND_ALL_OPEARATIONS_FOR_PROCESSPOINT + 
								" AND PDDA_PLAT.PLANT_LOC_CODE = ?2 and PDDA_PLAT.DEPT_CODE = ?3  and PDDA_PLAT.MODEL_YEAR_DATE = ?4 and PDDA_PLAT.PROD_SCH_QTY = ?5 " + 
								" AND PDDA_PLAT.PROD_ASM_LINE_NO = ?6 and PDDA_PLAT.VEHICLE_MODEL_CODE = ?7 ";
	
	private static String GET_MAX_OP_REV_FOR_OPERATION = " SELECT COALESCE(MAX(OP_REV), 0) FROM MC_OP_REV_TBX WHERE OPERATION_NAME=  CAST (?1 AS CHARACTER (32))";

	private static String GET_REV_STATUS = "SELECT REV_STATUS FROM MC_REV_TBX  WHERE REV_ID= ?1 ";
	private static final String SELECT_ALL_EXCEPT_OP_REV = "SELECT r FROM MCOperationRevision r WHERE r.id.operationName = :operationName AND r.id.operationRevision <> :opRev";
	
	private static final String GET_OPERATIONS = "SELECT OPR.* FROM GALADM.MC_OP_REV_PLATFORM_TBX ORP " +
			"JOIN GALADM.MC_OP_REV_TBX OPR ON (ORP.OPERATION_NAME = OPR.OPERATION_NAME AND ORP.OP_REV = OPR.OP_REV) " +
			"WHERE ORP.PDDA_PLATFORM_ID = ?1 AND OPR.REV_ID = ?2 ORDER BY OPR.OP_REV ASC";
	
	private static final String GET_ACTIVE_OPERATIONS = "SELECT OPR.* FROM GALADM.MC_OP_REV_PLATFORM_TBX ORP " +
			"JOIN GALADM.MC_OP_REV_TBX OPR ON (ORP.OPERATION_NAME = OPR.OPERATION_NAME AND ORP.OP_REV = OPR.OP_REV) " +
			"WHERE ORP.PDDA_PLATFORM_ID = ?1 AND OPR.REV_ID = ?2  AND OPR.DEPRECATED IS NULL AND OPR.APPROVED IS NOT NULL ORDER BY OPR.OP_REV ASC";
	
	private static final String GET_OPERATIONS_WITH_NAME = "SELECT OPR.* FROM GALADM.MC_OP_REV_PLATFORM_TBX ORP " +
			"JOIN GALADM.MC_OP_REV_TBX OPR ON (ORP.OPERATION_NAME = OPR.OPERATION_NAME AND ORP.OP_REV = OPR.OP_REV) " +
			"WHERE ORP.PDDA_PLATFORM_ID = ?1 AND OPR.REV_ID = ?2 AND OPR.OPERATION_NAME = CAST (?3 AS CHARACTER (32))";

	private static String GET_MAX_APRVD_OP_REV_FOR_OPERATION = "SELECT COALESCE(MAX(OP_REV), 0) FROM GALADM.MC_OP_REV_TBX WHERE APPROVED IS NOT NULL AND OPERATION_NAME= CAST (?1 AS CHARACTER (32))";
			
	public static String GET_OPERATION_SEQ = "SELECT DISTINCT s.operation_name, s.process_seq_num, op.op_seq_num, op.op_time FROM galadm.mc_structure_tbx s" +
			" JOIN galadm.mc_op_rev_platform_tbx op" +
			" ON (s.operation_name = op.operation_name" +
            " AND s.op_rev = op.op_rev" +
            " AND s.pdda_platform_id = op.pdda_platform_id)" +
            " JOIN galadm.mc_pdda_platform_tbx p" +
            " ON (s.pdda_platform_id = p.pdda_platform_id)" +
            " WHERE s.process_point_id = ?1" +
            " AND s.product_spec_code = ?2" +
            " AND s.structure_rev = ?3" +
            " ORDER BY s.process_seq_num, op.op_seq_num";
	
	public static String GET_APPROVED_OPERATION = "SELECT O.* FROM GALADM.MC_OP_REV_TBX O " +
			"JOIN GALADM.MC_OP_REV_PLATFORM_TBX OP ON O.OPERATION_NAME=OP.OPERATION_NAME AND O.OP_REV=OP.OP_REV " +
			"JOIN GALADM.MC_PDDA_PLATFORM_TBX P ON OP.PDDA_PLATFORM_ID=P.PDDA_PLATFORM_ID " +
			"WHERE " +
			"P.PLANT_LOC_CODE = CAST(?1 AS CHARACTER(1))  " +
			"AND P.DEPT_CODE = CAST(?2 AS CHARACTER(2)) " +
			"AND P.MODEL_YEAR_DATE = CAST(?3 AS DECIMAL(5,1)) " +  
			"AND P.PROD_SCH_QTY = CAST(?4 AS DECIMAL(5,1)) " +  
			"AND P.PROD_ASM_LINE_NO = CAST(?5 AS CHARACTER(1)) " + 
			"AND P.VEHICLE_MODEL_CODE = CAST(?6 AS CHARACTER(1)) " + 
			"AND O.OPERATION_NAME = CAST(?7 AS CHARACTER (32)) " +
			"AND O.APPROVED IS NOT NULL " + 
			"%s " +
            "ORDER BY O.DEPRECATED DESC";
	
	
	public static String GET_DISTINCT_OPERTAION_VIEW_PROCESSOR = "SELECT DISTINCT p.OP_VIEW, p.OP_PROCESSOR from galadm.MC_OP_REV_TBX p";
	
	public static String GET_EFFECTIVE_PART = "SELECT * FROM GALADM.MC_OP_PART_REV_TBX PART JOIN GALADM.MC_STRUCTURE_TBX STRUCTURE on " + 
			"(STRUCTURE.operation_name = PART.operation_name " +
			"AND STRUCTURE.part_id = PART.part_id " +
			"AND STRUCTURE.part_Rev = PART.part_Rev) " +
			"JOIN GALADM.BOM_TBX BOM on (PART.part_no = BOM.part_no " + 
			"and PART.PART_SECTION_CODE = BOM.PART_SECTION_CODE " +
			"and PART.PART_ITEM_NO = BOM.PART_ITEM_NO) " +
			"WHERE STRUCTURE.product_spec_code = ?1 " +
			"AND STRUCTURE.structure_rev = ?2 " +
			"AND STRUCTURE.process_point_id in (?3) " +
			"AND PART.OPERATION_NAME LIKE ?4 " +
			"AND CURRENT_DATE between BOM.EFF_BEG_DATE and BOM.EFF_END_DATE " +
			"AND BOM.MTC_MODEL = SUBSTR (?1 , 1, 4) " +
			"AND BOM.MTC_TYPE = SUBSTR (?1 , 5, 3) " + 
			"AND BOM.PART_NO = ?5 ";
	
	public static String GET_BY_OPERATION = "SELECT DISTINCT TRIM(OPERATION_NAME) FROM MC_OP_REV_TBX WHERE OPERATION_NAME LIKE ?1 FETCH FIRST 20 ROWS ONLY";
	
	public static String GET_ACTIVE_OPERATION_REV = "SELECT OP_REV FROM MC_OP_REV_TBX where OPERATION_NAME = ?1 AND DEPRECATED IS NULL AND APPROVED IS NOT NULL";
	
	private final String GET_ALL_OPERATION_REV_AND_PART_ID = "SELECT DISTINCT O.OP_REV, O.OPERATION_NAME, P.PART_ID "
				+ " FROM GALADM.MC_OP_REV_TBX O "
				+ " JOIN GALADM.MC_OP_PART_REV_TBX P on P.OPERATION_NAME = O.OPERATION_NAME and O.REV_ID=P.REV_ID " 
				+ " where O.OPERATION_NAME = ?1 and P.PART_NO = ?2 and ( P.PART_SECTION_CODE = ?3 OR P.PART_SECTION_CODE IS NULL ) "
				+ " and ( P.PART_ITEM_NO = ?4 OR P.PART_ITEM_NO IS NULL) and P.PART_TYPE = ?5 and o.OP_REV IN ";
	
	private final String FIND_ALL_BY_OPERATION_REV = "select o from MCOperationRevision o where o.id.operationName = :operationName ";
	
	private final String FIND_ALL_APPROVED_REV = "SELECT DISTINCT OPERATION_NAME, OP_TYPE FROM GALADM.MC_OP_REV_TBX " +
			"WHERE APPROVED IS NOT NULL AND DEPRECATED IS NULL AND " +
			"UPPER(OP_TYPE) IN ('GALC_MEAS','GALC_SCAN_WITH_MEAS','GALC_SCAN_WITH_MEAS_MANUAL','GALC_MEAS_MANUAL')";
	
	private static final String FIND_APPROVED_REVISION_BY_OP_NAME = "SELECT * FROM galadm.MC_OP_REV_TBX where OPERATION_NAME=?1 and DEPRECATED IS NULL AND APPROVED IS NOT NULL";

	private static final String FIND_OPERATION_NAME_BY_COMMON_NAME = "SELECT b.OPERATION_NAME FROM galadm.MC_OP_REV_TBX b " + 
			 " where  b.COMMON_NAME=?1 AND b.DEPRECATED IS NULL AND b.APPROVED IS NOT NULL ORDER BY b.APPROVED DESC";
	
	private static final String FIND_OPERATION_NAMES_BY_COMMON_NAMES = "SELECT  b.OPERATION_NAME  FROM galadm.MC_OP_REV_TBX b " + 
			 " where  b.COMMON_NAME IN (@commonNameList@) AND b.DEPRECATED IS NULL AND b.APPROVED IS NOT NULL ORDER BY b.APPROVED DESC";
	
	private static final String FIND_OPERATION_NAMES_BY_COMMON_NAMES_USING_LIKE = "SELECT * FROM galadm.MC_OP_REV_TBX b " + 
			 " where (@commonNameList@) AND b.DEPRECATED IS NULL AND b.APPROVED IS NOT NULL  AND 1 = ?1 ORDER BY b.APPROVED DESC";
	
	
	private static final String FIND_APPROVED_BY_OP_NAME_AND_REV_ID = "SELECT * FROM galadm.MC_OP_REV_TBX where OPERATION_NAME=?1 AND REV_ID=?2 AND APPROVED IS NOT NULL AND DEPRECATED IS NULL";
	
	private static final String FIND_OPERATION_NAMES_BY_COMMON_NAME = "SELECT  b.OPERATION_NAME  FROM galadm.MC_OP_REV_TBX b " + 
			 " where b.COMMON_NAME=?1 AND b.DEPRECATED IS NULL AND b.APPROVED IS NOT NULL ORDER BY b.APPROVED DESC";
	
	private static final String FIND_SELECT_OPERATION = "SELECT O.OPERATION_NAME FROM GALADM.MC_OP_REV_TBX O "+
			"JOIN GALADM.MC_STRUCTURE_TBX S  ON S.OPERATION_NAME=O.OPERATION_NAME AND S.OP_REV=O.OP_REV "+
			"WHERE S.STRUCTURE_REV=?1 AND (O.COMMON_NAME = ?2 OR O.OPERATION_NAME = ?2)";  
	
	public List<Object[]> getOperationSeq(String processPointId, String productSpecCode, long structureRevision) {
	    Parameters params = Parameters.with("1", processPointId);
	    params.put("2", productSpecCode);
	    params.put("3", structureRevision);
		return findAllByNativeQuery(GET_OPERATION_SEQ, params, Object[].class);
	}
	
	public List<Object[]> getEffectivePart(String productSpecCode, long structureRevision , String processPointId , String operationName , String partNo) {
	    Parameters params = Parameters.with("1", productSpecCode);
	    params.put("2", structureRevision);
	    params.put("3", processPointId);
	    params.put("4", operationName);
	    params.put("5", partNo);
	    return findAllByNativeQuery(GET_EFFECTIVE_PART, params, Object[].class);
	}
	
	public List<MCOperationRevision> findAllByOperationName(String operationName) {
		return findAll(Parameters.with("id.operationName", operationName));
	}

	public List<MCOperationRevision> findAllByOperationRevision(int operationRevision) {
		return findAll(Parameters.with("id.operationRevision", operationRevision));
	}

	public List<MCOperationRevision> findAllByOperationNamesForProcessPoint(
			String processPointId) {
		Parameters param = Parameters.with("1", processPointId);
		return findAllByNativeQuery(FIND_ALL_OPEARATIONS_FOR_PROCESSPOINT, param);
	}
	
	public int  getMaxRevisionForOperation(String operation) {
		Parameters params = Parameters.with("1", operation);
		return findFirstByNativeQuery(GET_MAX_OP_REV_FOR_OPERATION, params, Integer.class);

	}
	
	public int  getMaxAprvdRevisionForOperation(String operation) {
		Parameters params = Parameters.with("1", operation);
		return findFirstByNativeQuery(GET_MAX_APRVD_OP_REV_FOR_OPERATION, params, Integer.class);
	}

	public List<MCOperationRevision> findAllByRevision(long revisionId) {
		return findAll(Parameters.with("revisionId", revisionId), new String[]{"id.operationRevision"}, true);
	}
	
	public String  getRevStatusForOperationRev(long revId) {
		Parameters params = Parameters.with("1", revId);
		return findFirstByNativeQuery(GET_REV_STATUS, params, String.class);
	}
	
	public List<MCOperationRevision> findAllExceptOperationRevision(String operationName, int opRev) {
		Parameters params = Parameters.with("operationName", operationName);
		params.put("opRev", opRev);
		return findAllByQuery(SELECT_ALL_EXCEPT_OP_REV, params);
		
	}

	public List<MCOperationRevision> findAllBy(int pddaPlatformId,
			long revisionId) {
		Parameters params = Parameters.with("1", pddaPlatformId);
		params.put("2", revisionId);
		return findAllByNativeQuery(GET_OPERATIONS, params);
	}
	
	public List<MCOperationRevision> findAllActiveBy(int pddaPlatformId,
			long revisionId) {
		String qry = GET_ACTIVE_OPERATIONS;
		Parameters params = Parameters.with("1", pddaPlatformId);
		params.put("2", revisionId);
		return findAllByNativeQuery(qry, params);
	}

	public List<MCOperationRevision> findAllBy(int pddaPlatformId,
			long revisionId, String operationName) {
		Parameters params = Parameters.with("1", pddaPlatformId);
		params.put("2", revisionId);
		params.put("3", operationName);
		return findAllByNativeQuery(GET_OPERATIONS_WITH_NAME, params);
	}
	
	public MCOperationRevision findApprovedOperationBy(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String operationName, int activeDuration) {
		Parameters param = new Parameters();
		param.put("1", plantLocCode);
		param.put("2", deptCode);
		param.put("3", modelYearDate);
		param.put("4", prodSchQty);
		param.put("5", prodAsmLineNo);
		param.put("6", vehicleModelCode);
		param.put("7", operationName);
		
		//Setting up clause for active duration
		String activeDurationClause = "";
		if(activeDuration > -1) {
			activeDurationClause = "AND (O.DEPRECATED IS NULL OR O.DEPRECATED > (CURRENT_TIMESTAMP - "+activeDuration+" DAYS))";
		} 
		return findFirstByNativeQuery(String.format(GET_APPROVED_OPERATION, activeDurationClause), param);
	}
	
	public List<Object[]> findAllOperationViewAndProcessor() {
		return executeNative(GET_DISTINCT_OPERTAION_VIEW_PROCESSOR);
	}

	public List<MCOperationRevision> findAllBy(String operationName, long revisionId) {
		Parameters param = Parameters.with("id.operationName", operationName);
		param.put("revisionId", revisionId);
		return findAll(param);
	}

	public List<String> findByOperationName(String opName) {
		String likeOpName = "%" + opName + "%";
		return findAllByNativeQuery(GET_BY_OPERATION, Parameters.with("1", likeOpName), String.class);
	}
	
	public List<Integer> findAllActiveByOperationName(String opName) {
		return findAllByNativeQuery(GET_ACTIVE_OPERATION_REV, Parameters.with("1", opName), Integer.class);
	}
	
	public List<PartDetailsDto> loadOperationRevisionAndPartIdBy(String operationName, String PartNo, String partSectionCode, 
			String partItemNo, String partType, String revisions, boolean active) {
		String sql = GET_ALL_OPERATION_REV_AND_PART_ID;
		sql = sql.concat("(" + revisions + ")");
		Parameters param = Parameters.with("1", operationName).put("2", PartNo)
				.put("3", partSectionCode).put("4", partItemNo).put("5", partType);
		if(active)
			sql = sql.concat(" and O.DEPRECATED IS NULL AND O.APPROVED IS NOT NULL AND P.DEPRECATED IS NULL AND P.APPROVED IS NOT NULL");
		return findAllByNativeQuery(sql, param, PartDetailsDto.class);
	}
	
	public List<MCOperationRevision> findAllByOperationAndRevisions(String operationName, List<Integer> revisionIds, boolean active) {
		String sql = FIND_ALL_BY_OPERATION_REV;
		Parameters params = Parameters.with("operationName", operationName.trim());
		if(revisionIds != null) {
			sql = sql.concat("  and o.id.operationRevision in (:operationRevision) ");
			params.put("operationRevision", revisionIds);
		}
		if(active)
			sql = sql.concat(" and o.approved is not null and o.deprecated is null ");
		return findAllByQuery(sql, params);
	}
	
	public List<McOperationDataDto> findAllApprovedRevision() {
		return findAllByNativeQuery(FIND_ALL_APPROVED_REV, null, McOperationDataDto.class);
	}

	@Override
	public List<MCOperationRevision> findAllByProcessPointAndPlatform(String processPointId, String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty, String prodAsmLineNo,
			String vehicleModelCode) {
		Parameters params = Parameters.with("1", processPointId)
									.put("2", plantLocCode)
									.put("3", deptCode)
									.put("4", modelYearDate)
									.put("5", prodSchQty)
									.put("6", prodAsmLineNo)
									.put("7", vehicleModelCode);
		return findAllByNativeQuery(FIND_ALL_BY_PROCESS_POINT_AND_PLATFORM, params);
	}

	@Override
	public MCOperationRevision findApprovedOperationBy(String operationName) {
		Parameters params = Parameters.with("1", operationName);
		return findFirstByNativeQuery(FIND_APPROVED_REVISION_BY_OP_NAME, params);
	}

	@Override
	public MCOperationRevision findApprovedByOpNameAndRevId(String operationName, long revId) {
		Parameters params = Parameters.with("1", operationName).put("2", revId);
		return findFirstByNativeQuery(FIND_APPROVED_BY_OP_NAME_AND_REV_ID, params);
	}

	@Override
	public String findOperationNameByCommonName(String commonName) {
		Parameters params = Parameters.with("1", commonName);
		String operationName = findFirstByNativeQuery(FIND_OPERATION_NAME_BY_COMMON_NAME, params, String.class);
		if(StringUtils.isBlank(operationName)) return commonName;
	    return operationName;
	}
	@Override
	public List<String> findOperationsNameByPartNamelist(List<String> commonNameList) {
		List<String> operationNameList = new ArrayList<String>();
		List<String> partNameList = new ArrayList<String>();
		for(String partName : commonNameList) {
			partNameList.add("'"+partName+"'");
		}
		StringBuilder finalQuery = new StringBuilder(StringUtils.replace(FIND_OPERATION_NAMES_BY_COMMON_NAMES, "@commonNameList@", StringUtils.join(partNameList, Delimiter.COMMA)));
		operationNameList = findAllByNativeQuery(finalQuery.toString(), null, String.class);
		if(operationNameList.isEmpty()) return commonNameList;
	    return operationNameList;
	}
	
	@Override
	public List<MCOperationRevision> findOperationsNameByCommonNames(List<String> commonNameList) {
		String finalQuery = FIND_OPERATION_NAMES_BY_COMMON_NAMES_USING_LIKE.replaceAll("@commonNameList@", toSqlLikeString(commonNameList,MCOperationRevisionDaoImpl.COMMON_NAME));
		Parameters params = Parameters.with("1", 1);
		List<MCOperationRevision> operationNameList = findAllByNativeQuery(finalQuery, params);
		return operationNameList; 
	}
	
	
	
	
	private String toSqlLikeString(List<String> params, String columnName) {
		StringBuilder sb = new StringBuilder();
		for(String s : params){
			if(sb.length() > 0) sb.append(" OR ");
			sb.append(columnName + " LIKE ");
			sb.append("\'%");
			sb.append(StringUtils.trim(s));
			sb.append("%\'");
		}

		return sb.toString();
	}

	@Override
	public String findOperationNameByCommonPartName(String commonName) {
		String sql = FIND_OPERATION_NAMES_BY_COMMON_NAMES.replaceAll("@commonNameList@", StringUtil.toSqlInString(commonName));
		List<String> partNames = findAllByNativeQuery(sql, null, String.class);
		StringBuilder partNameAppender = new StringBuilder(); 
		String operationNames = "";
		if(!partNames.isEmpty()) {
		for(String partName : partNames){
			partNameAppender.append(partName);
			partNameAppender.append(Delimiter.COMMA);
		 }
		 operationNames = partNameAppender.toString();
		}
		if(StringUtils.isBlank(operationNames)) return commonName;
	    return operationNames;
	}
	
	@Override
	public List<String> findOperationNamesByCommonPartName(String commonName) {
		List<String> commonNameList = new ArrayList<String>();
		commonNameList.add(commonName);
		Parameters params = Parameters.with("1", commonName);
		List<String> operationNameList = findAllByNativeQuery(FIND_OPERATION_NAMES_BY_COMMON_NAME, params, String.class);
		if(operationNameList.isEmpty()) return commonNameList;
	    return operationNameList; 
	}
	
	@Transactional
	@Override
	public void saveEntity(MCViosMasterOperation masterOp) {
		MCOperationRevision opRev = findApprovedOperationBy(masterOp.getId().getOperationName());
		if (opRev != null) {
			if(StringUtils.isEmpty(masterOp.getView())) {
				String propertyValueView = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class).getOpView();
				opRev.setView(propertyValueView);
			} else {
				opRev.setView(masterOp.getView());
			}
			
			
			if(StringUtils.isEmpty(masterOp.getProcessor())) {
				String propertyValueProcessor = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class).getOpProcessor();
				opRev.setProcessor(propertyValueProcessor);
			} else {
				opRev.setProcessor(masterOp.getProcessor());
			}
			opRev.setCommonName(masterOp.getCommonName());
			opRev.setCheck(masterOp.getOpCheck());
			
			try {
				removeByKey(opRev.getId());
				insert(opRev);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getSelectOperationName(long structureRev, String selectProperty) {
		Parameters params = Parameters.with("1", structureRev).put("2", selectProperty);
		return findFirstByNativeQuery(FIND_SELECT_OPERATION, params, String.class);
	}
	
	public List<String> getAllListPickOperations(long structureRev, String selectProperty) {
		Parameters params = Parameters.with("1", structureRev).put("2", selectProperty);
		return findAllByNativeQuery(FIND_SELECT_OPERATION, params, String.class);
	}
}