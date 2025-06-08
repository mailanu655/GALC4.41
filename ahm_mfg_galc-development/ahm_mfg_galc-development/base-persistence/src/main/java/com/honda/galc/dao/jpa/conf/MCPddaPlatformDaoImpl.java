package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.PddaDetailDto;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.service.Parameters;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public class MCPddaPlatformDaoImpl extends BaseDaoImpl<MCPddaPlatform, Integer>
		implements MCPddaPlatformDao {

	private final String SELECT_ACTIVE_PLATFORM = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE PF.DEPRECATED IS NULL";
	
	private final String SELECT_APRD_PDDA_PLATFORM_WITHOUT_PARAMS = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE PF.DEPRECATED IS NULL "
			+ "AND PF.APPROVED IS NOT NULL AND (PF.APPROVER_NO IS NOT NULL AND TRIM(PF.APPROVER_NO) <> '') ";
	
	private final String SELECT_APRD_PDDA_PLATFORM_WITH_REV_DESC = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE PF.DEPRECATED IS NULL "
			+ "AND PF.APPROVED IS NOT NULL AND (PF.APPROVER_NO IS NOT NULL AND TRIM(PF.APPROVER_NO) <> '') "
			+"AND REV_ID=CAST(?1 AS BIGINT)"
			+"ORDER BY PF.APPROVED desc ";

	
	private final String SELECT_APRD_PDDA_PLATFORM = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE PF.DEPRECATED IS NULL "
			+ "AND PF.APPROVED IS NOT NULL AND (PF.APPROVER_NO IS NOT NULL AND TRIM(PF.APPROVER_NO) <> '') "
			+ "AND PF.ASM_PROC_NO = CAST(?1 AS VARCHAR(5)) "
			+ " AND PF.PLANT_LOC_CODE = CAST(?2 AS CHARACTER(1)) "
			+ " AND PF.DEPT_CODE = CAST(?3 AS CHARACTER(2)) "
			+ " AND PF.MODEL_YEAR_DATE = CAST(?4 AS DECIMAL(5,1)) "
			+ " AND PF.PROD_SCH_QTY = CAST(?5 AS DECIMAL(5,1)) "
			+ " AND PF.PROD_ASM_LINE_NO = CAST(?6 AS CHARACTER(1)) "
			+ " AND PF.VEHICLE_MODEL_CODE = CAST(?7 AS CHARACTER(1)) "
			+ " AND PF.REV_ID <> CAST(?8 AS BIGINT)";
	
	private final String SELECT_ALL_APRD_PDDA_PLATFORM = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE "
			+ "PF.APPROVED IS NOT NULL AND (PF.APPROVER_NO IS NOT NULL AND TRIM(PF.APPROVER_NO) <> '') "
			+ "AND PF.ASM_PROC_NO = CAST(?1 AS VARCHAR(5)) "
			+ " AND PF.PLANT_LOC_CODE = CAST(?2 AS CHARACTER(1)) "
			+ " AND PF.DEPT_CODE = CAST(?3 AS CHARACTER(2)) "
			+ " AND PF.MODEL_YEAR_DATE = CAST(?4 AS DECIMAL(5,1)) "
			+ " AND PF.PROD_SCH_QTY = CAST(?5 AS DECIMAL(5,1)) "
			+ " AND PF.PROD_ASM_LINE_NO = CAST(?6 AS CHARACTER(1)) "
			+ " AND PF.VEHICLE_MODEL_CODE = CAST(?7 AS CHARACTER(1)) "
			+ " AND PF.REV_ID <> CAST(?8 AS BIGINT)";
	
	private final String SELECT_LATEST_APRVD_PLATFORM = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE PF.DEPRECATED IS NULL "
			+ "AND PF.APPROVED IS NOT NULL AND (PF.APPROVER_NO IS NOT NULL AND TRIM(PF.APPROVER_NO) <> '') "
			+ "AND PF.ASM_PROC_NO = CAST(?1 AS VARCHAR(5)) "
			+ " AND PF.PLANT_LOC_CODE = CAST(?2 AS CHARACTER(1)) "
			+ " AND PF.DEPT_CODE = CAST(?3 AS CHARACTER(2)) "
			+ " AND PF.MODEL_YEAR_DATE = CAST(?4 AS DECIMAL(5,1)) "
			+ " AND PF.PROD_SCH_QTY = CAST(?5 AS DECIMAL(5,1)) "
			+ " AND PF.PROD_ASM_LINE_NO = CAST(?6 AS CHARACTER(1)) "
			+ " AND PF.VEHICLE_MODEL_CODE = CAST(?7 AS CHARACTER(1)) "
			+ " ORDER BY PF.APPROVED DESC";
	
	private static final String FIND_UNAPPROVED_REV = "SELECT DISTINCT REV.REV_ID FROM GALADM.MC_PDDA_PLATFORM_TBX P "
			+ "JOIN GALADM.MC_REV_TBX REV ON P.REV_ID=REV.REV_ID "
			+ "WHERE "
			+ "P.REV_ID < CAST(?1 AS BIGINT) AND "
			+ "REV.REV_STATUS <> '"+RevisionStatus.APPROVED.getRevStatus()+"' AND "
			+ "P.PLANT_LOC_CODE=?2 AND P.DEPT_CODE=?3 AND P.MODEL_YEAR_DATE=?4 AND P.PROD_SCH_QTY=?5 AND P.PROD_ASM_LINE_NO=?6 AND P.VEHICLE_MODEL_CODE=?7 AND "
			+ "P.ASM_PROC_NO IN (SELECT P1.ASM_PROC_NO FROM GALADM.MC_PDDA_PLATFORM_TBX P1 WHERE P1.REV_ID=CAST(?1 AS BIGINT)) "
			+ "ORDER BY REV.REV_ID";
			

	private static final String FIND_PLATFORM_BY_REVISION = "SELECT DISTINCT p.PLANT_LOC_CODE, p.DEPT_CODE, p.MODEL_YEAR_DATE, p.PROD_SCH_QTY, p.PROD_ASM_LINE_NO, p.VEHICLE_MODEL_CODE FROM MC_STRUCTURE_TBX s"
				+ " join MC_PDDA_PLATFORM_TBX p on s.PDDA_PLATFORM_ID = p.PDDA_PLATFORM_ID "
				+ " where s.STRUCTURE_REV = ?1";
	
	private static final String FIND_ALL_PLANT_CODE = "SELECT DISTINCT PLANT_LOC_CODE FROM GALADM.MC_PDDA_PLATFORM_TBX" ;
	
	private static final String FIND_ALL_DEPT_CODE = "SELECT DISTINCT DEPT_CODE FROM GALADM.MC_PDDA_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1";
	
	private static final String FIND_ALL_MODEL_YEAR = "SELECT DISTINCT MODEL_YEAR_DATE FROM GALADM.MC_PDDA_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2";
	
	private static final String FIND_ALL_PROD_QTY = "SELECT DISTINCT PROD_SCH_QTY FROM GALADM.MC_PDDA_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 AND MODEL_YEAR_DATE = ?3";
	
	private static final String FIND_ALL_LINE_NO = "SELECT DISTINCT PROD_ASM_LINE_NO FROM GALADM.MC_PDDA_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 AND MODEL_YEAR_DATE = ?3 AND PROD_SCH_QTY = ?4";
	
	private static final String FIND_ALL_VMC = "SELECT DISTINCT VEHICLE_MODEL_CODE FROM GALADM.MC_PDDA_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 AND MODEL_YEAR_DATE = ?3 AND PROD_SCH_QTY = ?4 AND PROD_ASM_LINE_NO = ?5";
	
	private static final String FIND_ALL_PLATFORMS_BY_PLANT_LOC_CODE_AND_DEPT_CODE = "SELECT DISTINCT p.MODEL_YEAR_DATE, p.PROD_SCH_QTY, p.PROD_ASM_LINE_NO, p.VEHICLE_MODEL_CODE "
			+ "FROM galadm.MC_PDDA_PLATFORM_TBX p WHERE p.PLANT_LOC_CODE=?1 AND p.DEPT_CODE=?2 ORDER BY p.MODEL_YEAR_DATE DESC, p.PROD_SCH_QTY DESC";
	
	private final String FIND_ALL_BY_PDDA_PLATFORM = "SELECT PF.* FROM GALADM.MC_PDDA_PLATFORM_TBX PF WHERE PF.DEPRECATED IS NULL "
			+ "AND PF.APPROVED IS NOT NULL AND (PF.APPROVER_NO IS NOT NULL AND TRIM(PF.APPROVER_NO) <> '') "
			+ "AND PF.PLANT_LOC_CODE = ?1 AND PF.DEPT_CODE = ?2  AND PF.MODEL_YEAR_DATE = ?3 AND PF.PROD_SCH_QTY = ?4 AND "
			+ "PF.PROD_ASM_LINE_NO = ?5 AND PF.VEHICLE_MODEL_CODE = ?6";
	
	public List<MCPddaPlatform> getPDDAPlatformForOperation(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo) {
		
		Parameters param = new Parameters();
		param.put("asmProcNo", asmProcNo);
		param.put("plantLocCode", plantLocCode);
		param.put("deptCode", deptCode);
		param.put("modelYearDate", modelYearDate);
		param.put("prodSchQty", prodSchQty);
		param.put("prodAsmLineNo", prodAsmLineNo);
		param.put("vehicleModelCode", vehicleModelCode);
		return findAll(param);
	}

	public List<MCPddaPlatform> getAllActivePlatforms() {
		return findAllByNativeQuery(SELECT_ACTIVE_PLATFORM, null);
	}
	
	public List<MCPddaPlatform> getAprvdPDDAPlatforms() {
		return findAllByNativeQuery(SELECT_APRD_PDDA_PLATFORM_WITHOUT_PARAMS, null);
	}
	
	public MCPddaPlatform getAprvdPDDAPlatformExceptRev(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo, long revId) {
		Parameters param = new Parameters();
		param.put("1", asmProcNo);
		param.put("2", plantLocCode);
		param.put("3", deptCode);
		param.put("4", modelYearDate);
		param.put("5", prodSchQty);
		param.put("6", prodAsmLineNo);
		param.put("7", vehicleModelCode);
		param.put("8", revId);
		return findFirstByNativeQuery(SELECT_APRD_PDDA_PLATFORM, param);
	}
	
	public List<MCPddaPlatform> getAllAprvdPDDAPlatformExceptRev(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo, long revId) {
		Parameters param = new Parameters();
		param.put("1", asmProcNo);
		param.put("2", plantLocCode);
		param.put("3", deptCode);
		param.put("4", modelYearDate);
		param.put("5", prodSchQty);
		param.put("6", prodAsmLineNo);
		param.put("7", vehicleModelCode);
		param.put("8", revId);
		return findAllByNativeQuery(SELECT_ALL_APRD_PDDA_PLATFORM, param);
	}

	public MCPddaPlatform getPDDAPlatformForOperation(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo, long revId) {
		
		Parameters param = new Parameters();
		param.put("asmProcNo", asmProcNo);
		param.put("plantLocCode", plantLocCode);
		param.put("deptCode", deptCode);
		param.put("modelYearDate", modelYearDate);
		param.put("prodSchQty", prodSchQty);
		param.put("prodAsmLineNo", prodAsmLineNo);
		param.put("vehicleModelCode", vehicleModelCode);
		param.put("revId", revId);
		MCPddaPlatform platform = findFirst(param);
		return platform;

	}
	
	public MCPddaPlatform getLatestAprvdPDDAPlatform(String plantLocCode,
			String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, String asmProcNo) {
		Parameters param = new Parameters();
		param.put("1", asmProcNo);
		param.put("2", plantLocCode);
		param.put("3", deptCode);
		param.put("4", modelYearDate);
		param.put("5", prodSchQty);
		param.put("6", prodAsmLineNo);
		param.put("7", vehicleModelCode);
		return findFirstByNativeQuery(SELECT_LATEST_APRVD_PLATFORM, param);
	}
	
	public List<MCPddaPlatform> getPlatformsForRevision(long revId) {
		return findAll(Parameters.with("revId", revId));
	}

	public List<MCPddaPlatform> getAprvdPDDAPlatformsforMatrix(long revId) {
		Parameters param = new Parameters();
		param.put("1", revId);
		return findAllByNativeQuery(SELECT_APRD_PDDA_PLATFORM_WITH_REV_DESC, param);
	}
	
	public List<Long> getUnapprovedOldRevisions(long revisionId) {
		List<Long> revisionList = new ArrayList<Long>();
		MCPddaPlatform pddaPlatform = findFirst(Parameters.with("revId", revisionId));
		if(pddaPlatform!=null) {
			Parameters params = Parameters.with("1", revisionId);
			params.put("2", pddaPlatform.getPlantLocCode());
			params.put("3", pddaPlatform.getDeptCode());
			params.put("4", pddaPlatform.getModelYearDate());
			params.put("5", pddaPlatform.getProductScheduleQty());
			params.put("6", pddaPlatform.getProductAsmLineNo());
			params.put("7", pddaPlatform.getVehicleModelCode());
			revisionList= findAllByNativeQuery(FIND_UNAPPROVED_REV, params, Long.class);
		}
		return revisionList;
	}

	public List<PddaDetailDto> findPlatformsByStructureRevision(long structureRev) {
		return findAllByNativeQuery(FIND_PLATFORM_BY_REVISION, Parameters.with("1", structureRev), PddaDetailDto.class);
	}
	
	public List<String> findAllPlants() {
		return findAllByNativeQuery(FIND_ALL_PLANT_CODE, null, String.class);
	}
	
	public List<String> findAllDeptBy(String plantCode) {
		Parameters param = new Parameters();
		param.put("1", plantCode);
		return findAllByNativeQuery(FIND_ALL_DEPT_CODE, param, String.class);
	}
		
	public List<Long> findAllModelYearBy(String plantCode, String dept){
		Parameters param = new Parameters();
		param.put("1", plantCode).put("2", dept);
		return findAllByNativeQuery(FIND_ALL_MODEL_YEAR, param, Long.class);
	}
	
	public List<Long> findAllProdQtyBy(String plantCode, String dept, BigDecimal modelYearDate) {
		Parameters param = new Parameters();
		param.put("1", plantCode).put("2", dept).put("3", modelYearDate);
		return findAllByNativeQuery(FIND_ALL_PROD_QTY, param, Long.class);
	}
	
	public List<String> findAllLineNo(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty) {
		Parameters param = new Parameters();
		param.put("1", plantCode).put("2", dept).put("3", modelYearDate).put("4", prodSchQty);
		return findAllByNativeQuery(FIND_ALL_LINE_NO, param, String.class);
	}
	
	public List<String> findAllVMC(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty, String lineNo) {
		Parameters param = new Parameters();
		param.put("1", plantCode).put("2", dept).put("3", modelYearDate).put("4", prodSchQty).put("5", lineNo);
		return findAllByNativeQuery(FIND_ALL_VMC, param, String.class);
	}

	@Override
	public List<PddaPlatformDto> findAllPlatformsByPlantLocCodeAndDeptCode(String plantLocCode, String deptCode) {
		Parameters params = Parameters.with("1", plantLocCode).put("2", deptCode);
		return findAllByNativeQuery(FIND_ALL_PLATFORMS_BY_PLANT_LOC_CODE_AND_DEPT_CODE, params, PddaPlatformDto.class);
	}

	@Override
	public List<MCPddaPlatform> findAllByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate,
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode)
									.put("2", deptCode)
									.put("3", modelYearDate)
									.put("4", prodSchQty)
									.put("5", prodAsmLineNo)
									.put("6", vehicleModelCode);
		return findAllByNativeQuery(FIND_ALL_BY_PDDA_PLATFORM, params, MCPddaPlatform.class);
	}
	
}
