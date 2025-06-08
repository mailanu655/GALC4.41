package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.entity.pdda.ChangeFormUnitId;
import com.honda.galc.service.Parameters;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class ChangeFormUnitDaoImpl extends
		BaseDaoImpl<ChangeFormUnit, ChangeFormUnitId> implements
		ChangeFormUnitDao {

	private static final String ORDER_BY_CHANGE_FORM_ID = " ORDER BY A.CHANGE_FORM_ID ";
	
	private static final String SELECT_NEW_CHANGE_FORM_PROCESS = " SELECT B.* FROM  VIOS.PVCFR1 A  , VIOS.PVCFU1 B WHERE A.CHANGE_FORM_ID = B.CHANGE_FORM_ID AND "
			+ "  B.ASM_PROC_NO  NOT IN (SELECT C.ASM_PROC_NO FROM  MC_PDDA_PLATFORM_TBX C ) AND A.CHANGE_FORM_ID IN (SELECT CHANGE_FORM_ID FROM  MC_PDDA_CHG_TBX) ";
	
	private static final String GET_NEW_CHANGE_FORM_PROCESS = SELECT_NEW_CHANGE_FORM_PROCESS
			+ ORDER_BY_CHANGE_FORM_ID;

	private static final String GET_NEW_CHANGE_FORM_PROCESS_COUNT = "SELECT COUNT(*) "
			+ "FROM  VIOS.PVCFU1 A  "
			+ "WHERE A.ASM_PROC_NO NOT IN (SELECT B.ASM_PROC_NO FROM  MC_PDDA_PROCESS_TBX B  )";

	private static final String SELECT_NEW_CHANGE_FORM_UNIT = " SELECT A.* "
			+ " FROM  VIOS.PVCFU1 A , VIOS.PVCFR1 B WHERE A.CHANGE_FORM_ID=B.CHANGE_FORM_ID "
			+ " AND  A.UNIT_NO NOT IN (SELECT B.UNIT_NO FROM  MC_PDDA_UNIT_TBX B  ) AND A.CHANGE_FORM_ID IN (SELECT CHANGE_FORM_ID FROM  MC_PDDA_CHG_TBX) ";
			
	private static final String GET_NEW_CHANGE_FORM_UNIT = SELECT_NEW_CHANGE_FORM_UNIT 
			+ ORDER_BY_CHANGE_FORM_ID;

	private static final String GET_NEW_CHANGE_FORM_UNIT_COUNT = "SELECT COUNT(*) "
			+ "FROM  VIOS.PVCFU1 A  "
			+ "WHERE A.UNIT_NO NOT IN (SELECT B.UNIT_NO FROM  MC_PDDA_UNIT_TBX B  )";

	private static final String GET_UNMAPPED_UNIT_COUNT_FOR_REV = "SELECT COUNT(*) FROM  VIOS.PVCFU1 A  JOIN VIOS.PVCFR1 B "
			+ " ON A.CHANGE_FORM_ID=B.CHANGE_FORM_ID JOIN MC_PDDA_CHG_TBX  C ON B.CHANGE_FORM_ID=C.CHANGE_FORM_ID "
			+ " WHERE A.UNIT_NO NOT IN (SELECT B.UNIT_NO FROM  MC_PDDA_UNIT_TBX B  ) AND C.REV_ID= CAST(?1 AS BIGINT)";

	private static final String GET_UNMAPPED_PROCESS_COUNT_FOR_REV = "SELECT COUNT(DISTINCT PC.REV_ID) FROM GALADM.MC_PDDA_CHG_TBX PC "
			+ "JOIN VIOS.PVCFU1 CFU ON PC.CHANGE_FORM_ID = CFU.CHANGE_FORM_ID "
			+ "JOIN VIOS.PVPMX1 PR ON CFU.APVD_PROC_MAINT_ID = PR.MAINTENANCE_ID "
			+ "WHERE NOT EXISTS (SELECT * FROM GALADM.MC_PDDA_PLATFORM_TBX PL WHERE PC.REV_ID = PL.REV_ID AND PR.ASM_PROC_NO = PL.ASM_PROC_NO) "
			+ "AND PC.REV_ID = CAST(?1 AS BIGINT)";

	private static String FIND_ALL_CHANGE_FORM_ID_FOR_REV = "SELECT CHGFRM.CHANGE_FORM_ID AS CHANGE_FORM_ID, CHGFRM.PLANT_LOC_CODE AS PLANT_LOC_CODE, CHGFRM.DEPT_CODE AS DEPT_CODE, CHGFRM.APVD_UNIT_MAINT_ID AS APVD_UNIT_MAINT_ID,  "
			+ " CHGFRM.APVD_PROC_MAINT_ID AS APVD_PROC_MAINT_ID, CHGFRM.ISSUED_DATE AS ISSUED_DATE,  "
			+ " CHGFRM.ISSR_LOGON_ID AS ISSR_LOGON_ID, CHGFRM.ASM_PROC_NO AS ASM_PROC_NO, CHGFRM.CHANGE_DESC AS CHANGE_DESC, CHGFRM.UNIT_NO AS UNIT_NO, CHGFRM.PCF_IN AS PCF_IN, "
			+ " CHGFRM.USR_IN AS USR_IN, CHGFRM.PPS_IN AS PPS_IN, CHGFRM.CU_IN AS CU_IN, CHGFRM.PS_IN AS PS_IN,  "
			+ " CHGFRM.SS_IN AS SS_IN FROM VIOS.PVCFU1 CHGFRM "
			+ " JOIN VIOS.PVUMX1 UNIT ON (CHGFRM.APVD_UNIT_MAINT_ID = UNIT.MAINTENANCE_ID) "
			+ " JOIN GALADM.MC_PDDA_CHG_TBX PDDACHG ON (CHGFRM.CHANGE_FORM_ID = PDDACHG.CHANGE_FORM_ID) "
			+ " JOIN GALADM.MC_REV_TBX MCREV  ON (MCREV.REV_ID = PDDACHG.REV_ID) "
			+ " WHERE MCREV.REV_ID = CAST(?1 AS BIGINT) "
			+ " ORDER BY UNIT.MAINT_DATE DESC";

	private final String FIND_MAINTENANCE_ID_FOR_REPORTTYPE = "select c.* from vios.pvcfu1 c join mc_pdda_unit_rev_tbx  d  "
			+ " on c.unit_no=d.unit_no where d.op_rev= ( select max(a.op_rev) from mc_pdda_unit_rev_tbx a join vios.pvcfu1 b "
			+ " on a.unit_no=b.unit_no where b.pps_in='Y' and b.unit_no= ?1 and a.pdda_report=?2 "
			+ " and a.pdda_platform_id=?3) and c.unit_no=?1 and d.pdda_report=?2 "
			+ " and   d.pdda_platform_id=?3";
	
	private static final String FIND_ALL_PROCESSES_BY_REV_ID = "SELECT DISTINCT e.PROCESS_POINT_ID, a.ASM_PROC_NO, e.PROCESS_SEQ_NUM FROM vios.PVCFU1 a " 
			+ "JOIN galadm.MC_PDDA_CHG_TBX b on a.CHANGE_FORM_ID = b.CHANGE_FORM_ID "
			+ "JOIN vios.PVCFR1 c on a.CHANGE_FORM_ID = c.CHANGE_FORM_ID "
			+ "JOIN galadm.MC_VIOS_MASTER_PLATFORM_TBX d on c.PLANT_LOC_CODE = d.PLANT_LOC_CODE AND c.DEPT_CODE = d.DEPT_CODE AND c.MODEL_YEAR_DATE = d.MODEL_YEAR_DATE " 
			+ "AND c.PROD_SCH_QTY = d.PROD_SCH_QTY AND c.PROD_ASM_LINE_NO = d.PROD_ASM_LINE_NO AND c.VEHICLE_MODEL_CODE = d.VEHICLE_MODEL_CODE "
			+ "LEFT JOIN galadm.MC_VIOS_MASTER_ASM_PROC_TBX e on e.VIOS_PLATFORM_ID = d.VIOS_PLATFORM_ID AND e.ASM_PROC_NO = a.ASM_PROC_NO ";
	
	private static final String FIND_ALL_UNMAPPED_PDDA_PROCESS = "SELECT DISTINCT a.ASM_PROC_NO FROM vios.PVCFU1 a "
			+ "JOIN vios.PVCFR1 b on a.CHANGE_FORM_ID = b.CHANGE_FORM_ID "
			+ "JOIN galadm.MC_PDDA_CHG_TBX c on a.CHANGE_FORM_ID = c.CHANGE_FORM_ID "
			+ "JOIN galadm.MC_VIOS_MASTER_PLATFORM_TBX d on b.PLANT_LOC_CODE = d.PLANT_LOC_CODE AND b.DEPT_CODE = d.DEPT_CODE AND b.MODEL_YEAR_DATE = d.MODEL_YEAR_DATE " 
			+ "AND b.PROD_SCH_QTY = d.PROD_SCH_QTY AND b.PROD_ASM_LINE_NO = d.PROD_ASM_LINE_NO AND b.VEHICLE_MODEL_CODE = d.VEHICLE_MODEL_CODE "
			+ "JOIN galadm.MC_VIOS_MASTER_ASM_PROC_TBX e ON d.VIOS_PLATFORM_ID = e.VIOS_PLATFORM_ID AND a.ASM_PROC_NO = e.ASM_PROC_NO "
			+ "LEFT JOIN galadm.MC_PDDA_PLATFORM_TBX f ON f.PLANT_LOC_CODE = b.PLANT_LOC_CODE AND f.DEPT_CODE = b.DEPT_CODE AND f.MODEL_YEAR_DATE = b.MODEL_YEAR_DATE " 
			+ "AND f.PROD_SCH_QTY = b.PROD_SCH_QTY AND f.PROD_ASM_LINE_NO = b.PROD_ASM_LINE_NO AND f.VEHICLE_MODEL_CODE = b.VEHICLE_MODEL_CODE "
			+ "AND f.ASM_PROC_NO = a.ASM_PROC_NO "
			+ "AND f.REV_ID = c.REV_ID "
			+ "WHERE c.REV_ID = ?1 AND f.PDDA_PLATFORM_ID IS NULL";
	
	private static final String FIND_ALL_PROCESS_BY_PLATFORM = "SELECT DISTINCT a.ASM_PROC_NO FROM vios.PVCFU1 a "
			+ "JOIN vios.PVCFR1 b on a.CHANGE_FORM_ID = b.CHANGE_FORM_ID "
			+ "WHERE b.PLANT_LOC_CODE=?1 AND b.DEPT_CODE=?2 AND b.MODEL_YEAR_DATE=?3 AND b.PROD_SCH_QTY=?4 AND b.PROD_ASM_LINE_NO=?5 AND b.VEHICLE_MODEL_CODE=?6 "
			+ "ORDER BY a.ASM_PROC_NO";
	
	private static final String FIND_ALL_UNMAPPED_PROCESS_BY_PLATFORM = "SELECT DISTINCT d.PROCESS_POINT_ID, a.ASM_PROC_NO, d.PROCESS_SEQ_NUM FROM vios.PVCFU1 a "
			+ "JOIN vios.PVCFR1 b on a.CHANGE_FORM_ID = b.CHANGE_FORM_ID " 
			+ "JOIN galadm.MC_VIOS_MASTER_PLATFORM_TBX c on b.PLANT_LOC_CODE = c.PLANT_LOC_CODE AND b.DEPT_CODE = c.DEPT_CODE AND b.MODEL_YEAR_DATE = c.MODEL_YEAR_DATE "  
			+ "AND b.PROD_SCH_QTY = c.PROD_SCH_QTY AND b.PROD_ASM_LINE_NO = c.PROD_ASM_LINE_NO AND b.VEHICLE_MODEL_CODE = c.VEHICLE_MODEL_CODE " 
			+ "LEFT JOIN galadm.MC_VIOS_MASTER_ASM_PROC_TBX d on d.VIOS_PLATFORM_ID = c.VIOS_PLATFORM_ID AND d.ASM_PROC_NO = a.ASM_PROC_NO "
			+ "WHERE d.VIOS_PLATFORM_ID IS NULL "
			+ "AND c.PLANT_LOC_CODE=?1 AND c.DEPT_CODE=?2 AND c.MODEL_YEAR_DATE=?3 AND c.PROD_SCH_QTY=?4 AND c.PROD_ASM_LINE_NO=?5 AND c.VEHICLE_MODEL_CODE=?6 "
			+ "ORDER BY a.ASM_PROC_NO";

	private static final String FIND_ALL_UNIT_NO_BY_PLATFORM = "SELECT DISTINCT a.UNIT_NO FROM vios.PVCFU1 a "
			+ "join vios.PVCFR1 b on a.CHANGE_FORM_ID = b.CHANGE_FORM_ID "
			+ "WHERE b.PLANT_LOC_CODE=?1 AND b.DEPT_CODE=?2 AND b.MODEL_YEAR_DATE=?3 AND b.PROD_SCH_QTY=?4 AND b.PROD_ASM_LINE_NO=?5 AND b.VEHICLE_MODEL_CODE=?6 "
			+ "ORDER BY a.UNIT_NO";

	public int getUnmappedChangeFormProcessCount() {
		Integer count = findFirstByNativeQuery(
				GET_NEW_CHANGE_FORM_PROCESS_COUNT, null, Integer.class);
		return count.intValue();
	}

	@Transactional(readOnly = true)
	public List<ChangeFormUnit> getAllUnmappedChangeFormProcesses() {
		List<ChangeFormUnit> changeFormUnitList = findAllByNativeQuery(
				GET_NEW_CHANGE_FORM_PROCESS, null);
		return changeFormUnitList;
	}
	
	@Transactional(readOnly = true)
	public List<ChangeFormUnit> getAllUnmappedChangeFormProcesses(String inClause) {
		String qry = SELECT_NEW_CHANGE_FORM_PROCESS;
		if(StringUtils.isNotEmpty(inClause)) {
			qry += " AND A.CHANGE_FORM_ID IN ("+inClause+") ";
		}
		qry += ORDER_BY_CHANGE_FORM_ID;
		List<ChangeFormUnit> changeFormUnitList = findAllByNativeQuery(
				qry , null);
		return changeFormUnitList;
	}

	public int getUnmappedChangeFormUnitCount() {
		Integer count = findFirstByNativeQuery(GET_NEW_CHANGE_FORM_UNIT_COUNT,
				null, Integer.class);
		return count.intValue();
	}

	public int getUnmappedChangeFormUnitCountForRev(int revId) {
		Parameters param = Parameters.with("1", revId);
		Integer count = findFirstByNativeQuery(GET_UNMAPPED_UNIT_COUNT_FOR_REV,
				param, Integer.class);
		return count.intValue();
	}

	public int getUnmappedChangeFormProcessCountForRev(long revId) {
		Parameters param = Parameters.with("1", revId);
		Integer count = findFirstByNativeQuery(
				GET_UNMAPPED_PROCESS_COUNT_FOR_REV, param, Integer.class);
		return count.intValue();
	}

	public List<ChangeFormUnit> getAllUnmappedChangeFormUnits() {
		List<ChangeFormUnit> changeFormUnitList = findAllByNativeQuery(
				GET_NEW_CHANGE_FORM_UNIT, null);
		return changeFormUnitList;
	}
	
	public List<ChangeFormUnit> getAllUnmappedChangeFormUnits(String inClause) {
		String qry = SELECT_NEW_CHANGE_FORM_UNIT;
		if(StringUtils.isNotEmpty(inClause)) {
			qry += " AND A.CHANGE_FORM_ID IN ("+inClause+") ";
		}
		qry += ORDER_BY_CHANGE_FORM_ID;
		List<ChangeFormUnit> changeFormUnitList = findAllByNativeQuery(
				qry, null);
		return changeFormUnitList;
	}

	public List<ChangeFormUnit> findAllChangeFormUnitIdForRev(long revId) {
		Parameters param = Parameters.with("1", revId);
		return findAllByNativeQuery(FIND_ALL_CHANGE_FORM_ID_FOR_REV, param);
	}

	public ChangeFormUnit findMaintenanceIdForReportType(String unitNo,
			String reportType, int platformId) {

		Parameters params = Parameters.with("1", unitNo);
		params.put("2", reportType);
		params.put("3", platformId);

		return findFirstByNativeQuery(FIND_MAINTENANCE_ID_FOR_REPORTTYPE,
				params);

	}

	public List<ChangeFormUnit> findAllForChangeForm(
			int ChangeFormId) {
		return findAll(Parameters.with("id.changeFormId", ChangeFormId));
	}

	public List<ChangeFormUnit> findAllForChangeForm(int ChangeFormId,
			String asmProcNo) {
		return findAll(Parameters.with("id.changeFormId", ChangeFormId).put("asmProcNo", asmProcNo));
	}
	
	public List<ChangeFormUnit> findAllForChangeForm(int ChangeFormId,
			String asmProcNo, String unitNo) {
		return findAll(Parameters.with("id.changeFormId", ChangeFormId).put("asmProcNo", asmProcNo).put("unitNo", unitNo));
	}

	@Override
	public List<MCViosMasterProcessDto> findAllProcessesByRevId(long revId, boolean isMapped) {
		StringBuilder queryBuilder = new StringBuilder(FIND_ALL_PROCESSES_BY_REV_ID);
		queryBuilder.append(" WHERE b.REV_ID = ?1 ");
		if(isMapped) {
			queryBuilder.append(" AND e.VIOS_PLATFORM_ID IS NOT NULL ");
		} else {
			queryBuilder.append(" AND e.VIOS_PLATFORM_ID IS NULL ");
		}
		Parameters params = Parameters.with("1", revId);
		return findAllByNativeQuery(queryBuilder.toString(), params, MCViosMasterProcessDto.class);
	}

	@Override
	public List<String> findAllUnmappedPddaProcessBy(long revId) {
		Parameters params = Parameters.with("1", revId);
		return findAllByNativeQuery(FIND_ALL_UNMAPPED_PDDA_PROCESS, params, String.class);
	}

	@Override
	public List<String> findAllProcessByPlatform(MCViosMasterPlatform platform) {
		Parameters params = Parameters.with("1", platform.getPlantLocCode())
										.put("2", platform.getDeptCode())
										.put("3", platform.getModelYearDate())
										.put("4", platform.getProdSchQty())
										.put("5", platform.getProdAsmLineNo())
										.put("6", platform.getVehicleModelCode());
		return findAllByNativeQuery(FIND_ALL_PROCESS_BY_PLATFORM, params, String.class);
	}

	@Override
	public List<MCViosMasterProcessDto> findAllUnmappedProcessByPlatform(MCViosMasterPlatform platform) {
		Parameters params = Parameters.with("1", platform.getPlantLocCode())
				.put("2", platform.getDeptCode())
				.put("3", platform.getModelYearDate())
				.put("4", platform.getProdSchQty())
				.put("5", platform.getProdAsmLineNo())
				.put("6", platform.getVehicleModelCode());
		return findAllByNativeQuery(FIND_ALL_UNMAPPED_PROCESS_BY_PLATFORM, params, MCViosMasterProcessDto.class);
	}

	@Override
	public List<String> findAllUnitNoByPlatform(MCViosMasterPlatform platform) {
		Parameters params = Parameters.with("1", platform.getPlantLocCode())
				.put("2", platform.getDeptCode())
				.put("3", platform.getModelYearDate())
				.put("4", platform.getProdSchQty())
				.put("5", platform.getProdAsmLineNo())
				.put("6", platform.getVehicleModelCode());
		return findAllByNativeQuery(FIND_ALL_UNIT_NO_BY_PLATFORM, params, String.class);
	}
}
