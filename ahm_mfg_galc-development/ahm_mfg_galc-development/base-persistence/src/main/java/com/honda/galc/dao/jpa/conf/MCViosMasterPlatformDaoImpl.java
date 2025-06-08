package com.honda.galc.dao.jpa.conf;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.Parameters;
import com.honda.galc.vios.dto.PddaPlatformDto;
/**
 * <h3>MCViosMasterPlatformDaoImpl Class description</h3>
 * <p>
 * DaoImpl class for MCViosMasterPlatform
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public class MCViosMasterPlatformDaoImpl extends BaseDaoImpl<MCViosMasterPlatform, String> implements MCViosMasterPlatformDao {
	
	private static final String FIND_ALL_PLANTS = "SELECT DISTINCT PLANT_LOC_CODE FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX ORDER BY PLANT_LOC_CODE";
	
	private static final String FIND_ALL_DEPT_BY = "SELECT DISTINCT DEPT_CODE FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 ORDER BY DEPT_CODE";
	
	private static final String FIND_ALL_MODEL_YEAR_BY = "SELECT DISTINCT MODEL_YEAR_DATE FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 ORDER BY MODEL_YEAR_DATE";
	
	private static final String FIND_ALL_PROD_SCH_QTY_BY = "SELECT DISTINCT PROD_SCH_QTY FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 AND MODEL_YEAR_DATE = ?3 ORDER BY PROD_SCH_QTY";
	
	private static final String FIND_ALL_LINE_NO_BY = "SELECT DISTINCT PROD_ASM_LINE_NO FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 AND MODEL_YEAR_DATE = ?3 AND PROD_SCH_QTY = ?4 ORDER BY PROD_ASM_LINE_NO";
	
	private static final String FIND_ALL_VMC_BY = "SELECT DISTINCT VEHICLE_MODEL_CODE FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX WHERE PLANT_LOC_CODE = ?1 AND DEPT_CODE = ?2 AND MODEL_YEAR_DATE = ?3 AND PROD_SCH_QTY = ?4 AND PROD_ASM_LINE_NO = ?5 ORDER BY VEHICLE_MODEL_CODE";

	private static final String FIND_ALL_BY_PLANT_CODE_AND_DEPT_CODE = "SELECT DISTINCT p.MODEL_YEAR_DATE, p.PROD_SCH_QTY, p.PROD_ASM_LINE_NO, p.VEHICLE_MODEL_CODE "
			+ " FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX p WHERE p.PLANT_LOC_CODE=?1 AND p.DEPT_CODE=?2 AND ACTIVE=1 ORDER BY p.MODEL_YEAR_DATE DESC, p.PROD_SCH_QTY DESC";

	private static final String FIND_ALL_BY_PROCESS_POINT_ID_AND_MODEL_YEAR = "SELECT DISTINCT P.PLANT_LOC_CODE, P.DEPT_CODE, P.MODEL_YEAR_DATE, P.PROD_ASM_LINE_NO, P.PROD_SCH_QTY, P.VEHICLE_MODEL_CODE " 
			+ " FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX P " 
			+ " JOIN galadm.MC_VIOS_MASTER_ASM_PROC_TBX A on P.VIOS_PLATFORM_ID=A.VIOS_PLATFORM_ID " 
			+ " WHERE P.ACTIVE = 1 AND A.PROCESS_POINT_ID=?1 ";
	
	
	private static final String FIND_ALL_BY_PLATFORM_ID = "SELECT * FROM galadm.MC_VIOS_MASTER_PLATFORM_TBX WHERE VIOS_PLATFORM_ID = ?1";

	@Override
	public List<MCViosMasterPlatform> findAllPlatforms() {
		return findAll(null, new String[] {"plantLocCode","deptCode","modelYearDate","prodSchQty","prodAsmLineNo","vehicleModelCode"});
	}
	
	@Transactional
	@Override
	public MCViosMasterPlatform removeAndInsert(MCViosMasterPlatform oldPlatform, MCViosMasterPlatform newPlatform) {
		remove(oldPlatform);
		return insert(newPlatform);
	}

	@Override
	public List<String> findAllPlants() {
		return findAllByNativeQuery(FIND_ALL_PLANTS, null, String.class);
	}

	@Override
	public List<String> findAllDeptBy(String plantCode) {
		Parameters params = Parameters.with("1", plantCode);
		return findAllByNativeQuery(FIND_ALL_DEPT_BY, params, String.class);
	}

	@Override
	public List<BigDecimal> findAllModelYearBy(String plantCode, String dept) {
		Parameters params = Parameters.with("1", plantCode)
							.put("2", dept);
		return findAllByNativeQuery(FIND_ALL_MODEL_YEAR_BY, params, BigDecimal.class);
	}

	@Override
	public List<BigDecimal> findAllProdQtyBy(String plantCode, String dept, BigDecimal modelYearDate) {
		Parameters params = Parameters.with("1", plantCode)
							.put("2", dept)
							.put("3", modelYearDate);
		return findAllByNativeQuery(FIND_ALL_PROD_SCH_QTY_BY, params, BigDecimal.class);
	}

	@Override
	public List<String> findAllLineNoBy(String plantCode, String dept, BigDecimal modelYearDate,
			BigDecimal prodSchQty) {
		Parameters params = Parameters.with("1", plantCode)
							.put("2", dept)
							.put("3", modelYearDate)
							.put("4", prodSchQty);
		return findAllByNativeQuery(FIND_ALL_LINE_NO_BY, params, String.class);
	}

	@Override
	public List<String> findAllVMCBy(String plantCode, String dept, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String lineNo) {
		Parameters params = Parameters.with("1", plantCode)
							.put("2", dept)
							.put("3", modelYearDate)
							.put("4", prodSchQty)
							.put("5", lineNo);
		return findAllByNativeQuery(FIND_ALL_VMC_BY, params, String.class);
	}

	@Override
	public List<PddaPlatformDto> findAllPlatformsByPlantLocCodeAndDeptCode(String plantLocCode, String deptCode) {
		Parameters params = Parameters.with("1", plantLocCode)
				.put("2", deptCode);
		return findAllByNativeQuery(FIND_ALL_BY_PLANT_CODE_AND_DEPT_CODE, params, PddaPlatformDto.class);
	}

	@Override
	public List<PddaPlatformDto> findAllActivePlatformBy(String processPointId, BigDecimal modelYearDate) {
		Parameters params = Parameters.with("1", processPointId);
		StringBuilder query = new StringBuilder(FIND_ALL_BY_PROCESS_POINT_ID_AND_MODEL_YEAR);
		if(modelYearDate != null) {
			params.put("2", modelYearDate);
			query.append(" AND P.MODEL_YEAR_DATE=?2 ");
		}
				
		return findAllByNativeQuery(query.toString(), params, PddaPlatformDto.class);
	}
	
	

	@Override
	public List<MCViosMasterPlatform> findAllData(String viosPlatformId) {
		Parameters params = Parameters.with("1", viosPlatformId);
		return findAllByNativeQuery(FIND_ALL_BY_PLATFORM_ID, params, MCViosMasterPlatform.class);
	}
	
	
	@Transactional
	@Override
	public void saveEntity(MCViosMasterPlatform mcopsPlatform) {
	try {
		remove(mcopsPlatform);	
		insert(mcopsPlatform);
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}

}
