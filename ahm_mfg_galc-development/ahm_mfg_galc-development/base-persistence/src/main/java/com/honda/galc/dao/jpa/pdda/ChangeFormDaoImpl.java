package com.honda.galc.dao.jpa.pdda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.service.Parameters;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class ChangeFormDaoImpl extends BaseDaoImpl<ChangeForm, Integer>
		implements ChangeFormDao {
	
	private static final String GET_NEW_CHANGE_FORMS = "SELECT A.* " +
            "FROM VIOS.PVCFR1 A WHERE A.CHANGE_FORM_ID NOT IN (SELECT B.CHANGE_FORM_ID FROM MC_PDDA_CHG_TBX B) " +
            "ORDER BY A.CHANGE_FORM_ID";

    private static final String GET_NEW_CHANGE_FORM_COUNT = "SELECT COUNT(*) FROM VIOS.PVCFR1 A WHERE A.CHANGE_FORM_ID NOT IN (SELECT B.CHANGE_FORM_ID FROM MC_PDDA_CHG_TBX B)";

    private static final String GET_PROCESS_CHANGE_FOR_PROCESS_POINT = "SELECT DISTINCT CNG.UNIT_NO,CNG.CHANGE_DESC,CNG.CREATE_TIMESTAMP,PDDAPLTFRM.ASM_PROC_NO, CHGFRM.APVD_USER, REV.CREATE_TIMESTAMP,  " +
    													" PDDAPLTFRM.DEPT_CODE,PDDAPLTFRM.MODEL_YEAR_DATE,CNG.PLANT_LOC_CODE,PDDAPLTFRM.PROD_ASM_LINE_NO,PDDAPLTFRM.PROD_SCH_QTY,PDDAPLTFRM.VEHICLE_MODEL_CODE,CHGFRM.CONTROL_NO  FROM GALADM.MC_PDDA_PLATFORM_TBX PDDAPLTFRM  " +
    													" JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDAPLTFRM.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDAPLTFRM.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDAPLTFRM.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDAPLTFRM.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDAPLTFRM.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDAPLTFRM.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
														" JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDAPLTFRM.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
    													" JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREVPLAT  ON (PDDAPLTFRM.PDDA_PLATFORM_ID = OPREVPLAT.PDDA_PLATFORM_ID)  " +
    													" JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPREVPLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREVPLAT.OP_REV = OPREV.OP_REV)  " +
    													" JOIN GALADM.MC_REV_TBX REV ON (OPREV.REV_ID = REV.REV_ID)  " +
    													" JOIN GALADM.MC_PDDA_CHG_TBX PDDACHG ON (REV.REV_ID = PDDACHG.REV_ID)  " +
    													" JOIN VIOS.PVCFR1 CHGFRM ON (PDDACHG.CHANGE_FORM_ID = CHGFRM.CHANGE_FORM_ID)  " +
    													" JOIN VIOS.PVCFU1  CNG ON (PDDACHG.CHANGE_FORM_ID = CNG.CHANGE_FORM_ID AND CHGFRM.CHANGE_FORM_ID = CNG.CHANGE_FORM_ID) "+
    													" WHERE VIOS_MAST_PROC.PROCESS_POINT_ID=CAST(?1 AS CHARACTER(16)) AND  (((DAYS (VARCHAR_FORMAT(OPREV.APPROVED,'MM/DD/YYYY')) - DAYS(VARCHAR_FORMAT(CURRENT TIMESTAMP,'MM/DD/YYYY')))  <= 0) " +  
    													" AND (DAYS (VARCHAR_FORMAT(OPREV.APPROVED,'MM/DD/YYYY')) - DAYS(VARCHAR_FORMAT(CURRENT TIMESTAMP,'MM/DD/YYYY')) >=(-1 * %d)))  " +
    													" GROUP BY PDDAPLTFRM.ASM_PROC_NO, CHGFRM.APVD_USER, REV.CREATE_TIMESTAMP, CNG.UNIT_NO,CNG.CHANGE_DESC,CNG.CREATE_TIMESTAMP, " +
    													" PDDAPLTFRM.DEPT_CODE,PDDAPLTFRM.MODEL_YEAR_DATE,PDDAPLTFRM.PROD_ASM_LINE_NO,PDDAPLTFRM.VEHICLE_MODEL_CODE,CHGFRM.CONTROL_NO,PDDAPLTFRM.PROD_SCH_QTY,CNG.PLANT_LOC_CODE  " +
    													" ORDER BY REV.CREATE_TIMESTAMP DESC  FETCH FIRST %d ROW ONLY ";
    
    private static final String GET_CHANGEFORMS_FOR_REV_ID = "SELECT A.* FROM VIOS.PVCFR1 A WHERE A.CHANGE_FORM_ID  IN (SELECT B.CHANGE_FORM_ID FROM MC_PDDA_CHG_TBX B  WHERE B.REV_ID= ?1)";
    
    private static final String GET_UNPROCESSED_CHANGE_FORMS = "SELECT DISTINCT CF.CHANGE_FORM_ID FROM VIOS.PVCFR1 CF JOIN VIOS.PVCFU1 CFU ON CF.CHANGE_FORM_ID=CFU.CHANGE_FORM_ID " +
    		"JOIN VIOS.PVUMX1 U ON CFU.APVD_UNIT_MAINT_ID=U.MAINTENANCE_ID " +
    		"WHERE CF.PLANT_LOC_CODE=?1 AND CF.DEPT_CODE=?2 AND CF.MODEL_YEAR_DATE=?3 AND CF.PROD_SCH_QTY=?4 AND CF.PROD_ASM_LINE_NO=?5 AND CF.VEHICLE_MODEL_CODE=?6 AND " +
    		"CF.CHANGE_FORM_ID NOT IN (@changeFormList@) AND CF.CHANGE_FORM_ID NOT IN (SELECT CHANGE_FORM_ID FROM GALADM.MC_PDDA_CHG_TBX) AND " +
    		"CFU.UNIT_NO IN (SELECT UNIT_NO FROM VIOS.PVCFU1 WHERE CHANGE_FORM_ID IN (@changeFormList@)) AND " +
    		"U.MAINT_DATE < (SELECT MIN(U1.MAINT_DATE) FROM VIOS.PVCFU1 CFU1 JOIN VIOS.PVUMX1 U1 ON CFU1.APVD_UNIT_MAINT_ID=U1.MAINTENANCE_ID WHERE CFU1.CHANGE_FORM_ID IN (@changeFormList@) AND CFU1.UNIT_NO=CFU.UNIT_NO) " +
    		"ORDER BY CF.CHANGE_FORM_ID";
    
    private static final String GET_ALL_NEW_PLATFORMS = "SELECT DISTINCT a.PLANT_LOC_CODE, a.DEPT_CODE, a.MODEL_YEAR_DATE, a.PROD_SCH_QTY, a.PROD_ASM_LINE_NO, a.VEHICLE_MODEL_CODE FROM vios.PVCFR1 a " 
    		+ "LEFT JOIN galadm.MC_VIOS_MASTER_PLATFORM_TBX b "
    		+ "ON a.PLANT_LOC_CODE = b.PLANT_LOC_CODE AND a.DEPT_CODE = b.DEPT_CODE AND a.MODEL_YEAR_DATE = b.MODEL_YEAR_DATE " 
    		+ "AND a.PROD_SCH_QTY = b.PROD_SCH_QTY AND a.PROD_ASM_LINE_NO = b.PROD_ASM_LINE_NO AND a.VEHICLE_MODEL_CODE = b.VEHICLE_MODEL_CODE "
    		+ "WHERE b.VIOS_PLATFORM_ID IS NULL "
    		+ "ORDER BY a.PLANT_LOC_CODE, a.DEPT_CODE, a.MODEL_YEAR_DATE, a.PROD_SCH_QTY, a.PROD_ASM_LINE_NO, a.VEHICLE_MODEL_CODE";
    
    private static final String GET_ALL_NEW_CHANGE_FORMS_BY_PLATFORM = "SELECT A.* " +
    		" FROM VIOS.PVCFR1 A WHERE A.CHANGE_FORM_ID NOT IN (SELECT B.CHANGE_FORM_ID FROM galadm.MC_PDDA_CHG_TBX B) " +
    		" AND A.PLANT_LOC_CODE = ?1 AND A.DEPT_CODE = ?2 AND A.MODEL_YEAR_DATE = ?3 AND A.PROD_SCH_QTY = ?4 AND A.PROD_ASM_LINE_NO = ?5 and A.VEHICLE_MODEL_CODE = ?6 " +
    		" ORDER BY A.CHANGE_FORM_ID";
    
    public int getChangeFormCount() {
	    Integer count = findFirstByNativeQuery(GET_NEW_CHANGE_FORM_COUNT,null,Integer.class);
	    return count.intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ChangeForm> getGetAllChangeForms() {
		List<ChangeForm> changeFormList  = findAllByNativeQuery(GET_NEW_CHANGE_FORMS, null);
		return changeFormList;
	}


	@SuppressWarnings("unchecked")
	public List<Object[]> getProcessChangeForProcessPoint(String processPoint, int processChangeHistoryDays, int processChangeDisplayRows) {
		Parameters params = Parameters.with("1", processPoint);
		return findResultListByNativeQuery(String.format(GET_PROCESS_CHANGE_FOR_PROCESS_POINT, processChangeHistoryDays, processChangeDisplayRows), params);
	}
	
	@Transactional(readOnly = true)
	public List<ChangeForm> getChangeFormsForRevId(long revId) {
		Parameters params = Parameters.with("1", revId);
		List<ChangeForm> changeFormList  = findAllByNativeQuery(GET_CHANGEFORMS_FOR_REV_ID, params);
		return changeFormList;
	}
	
	public List<String> getUnprocessedChangeForms(List<Integer> changeFormIds) {
		List<String> unprocessedChangeForms = new ArrayList<String>();
		if(changeFormIds!=null && !changeFormIds.isEmpty()) {
			ChangeForm changeForm = findByKey(changeFormIds.iterator().next());
			if (changeForm!=null) {
				Parameters params = Parameters.with("1", changeForm.getPlantLocCode());
				params.put("2", changeForm.getDeptCode());
				params.put("3", changeForm.getModelYearDate());
				params.put("4", changeForm.getProdSchQty());
				params.put("5", changeForm.getProdAsmLineNo());
				params.put("6", changeForm.getVehicleModelCode());
				unprocessedChangeForms = findAllByNativeQuery
						(StringUtils.replace(GET_UNPROCESSED_CHANGE_FORMS, 
								"@changeFormList@", StringUtils.join(changeFormIds, Delimiter.COMMA)),
								params, String.class);
			}
		}
		return unprocessedChangeForms;
	}


	@Override
	public List<PddaPlatformDto> getAllNewPlatforms() {
		return findAllByNativeQuery(GET_ALL_NEW_PLATFORMS, null, PddaPlatformDto.class);
	}

	@Override
	public List<ChangeForm> findAllByPddaPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate,
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = Parameters.with("1", plantLocCode)
				.put("2", deptCode)
				.put("3", modelYearDate)
				.put("4", prodSchQty)
				.put("5", prodAsmLineNo)
				.put("6", vehicleModelCode);
		return findAllByNativeQuery(GET_ALL_NEW_CHANGE_FORMS_BY_PLATFORM, params);
	}
}
