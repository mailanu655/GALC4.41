package com.honda.galc.dao.jpa.pdda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitConcernPointDao;
import com.honda.galc.dao.pdda.UnitControlMethodDao;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dao.pdda.UnitMostDao;
import com.honda.galc.dao.pdda.UnitReactionPlanDao;
import com.honda.galc.dao.pdda.UnitSpecialControlDao;
import com.honda.galc.dto.TrainingDataDto;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.dto.UnitOfOperationDetails;
import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.entity.pdda.UnitConcernPoint;
import com.honda.galc.entity.pdda.UnitControlMethod;
import com.honda.galc.entity.pdda.UnitMost;
import com.honda.galc.entity.pdda.UnitReactionPlan;
import com.honda.galc.entity.pdda.UnitSpecialControl;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitDaoImpl extends BaseDaoImpl<Unit, Integer>
		implements UnitDao {
	
	private static final String LIST_OF_OPER_FOR_PROCESS = "SELECT distinct UNIT.UNIT_NO ,PROCESS.ASM_PROC_NO " +
			"FROM VIOS.PVUMX1 UNIT  " +
			"JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF')  " +
			"JOIN VIOS.PVPMX1 PROCESS ON (PROCESS.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID)  " +
			"JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME " +
			"AND PDDAUNITREV.OP_REV = STRUC.OP_REV  " +
			"AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)   " +
			"JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV)  " +
			"WHERE  PROCESS.ASM_PROC_NO = ?1 AND UNIT.MODEL_YEAR_DATE =?2 AND UNIT.PLANT_LOC_CODE =?3 AND UNIT.DEPT_CODE =?4 AND UNIT.PROD_ASM_LINE_NO=?5";

	public static String GET_ERGOS_FOR_PROCESS_POINT = "SELECT UNIT.UNIT_NO, UNIT.UNIT_OP_DESC_TEXT, UNIT.SAFETY_ERGO_PT, UNIT.SAFETY_ERGO_INST " +
										" FROM VIOS.PVUMX1 UNIT WHERE UNIT.MAINTENANCE_ID IN (SELECT PDDAUNITREV.APVD_UNIT_MAINT_ID FROM GALADM.MC_PDDA_PLATFORM_TBX PDDAPLTFRM " +
										" JOIN GALADM.MC_VIOS_MASTER_PLATFORM_TBX VIOS_MAST_PLAT ON PDDAPLTFRM.PLANT_LOC_CODE=VIOS_MAST_PLAT.PLANT_LOC_CODE AND PDDAPLTFRM.DEPT_CODE=VIOS_MAST_PLAT.DEPT_CODE AND PDDAPLTFRM.MODEL_YEAR_DATE=VIOS_MAST_PLAT.MODEL_YEAR_DATE AND PDDAPLTFRM.PROD_SCH_QTY=VIOS_MAST_PLAT.PROD_SCH_QTY AND PDDAPLTFRM.PROD_ASM_LINE_NO=VIOS_MAST_PLAT.PROD_ASM_LINE_NO AND PDDAPLTFRM.VEHICLE_MODEL_CODE=VIOS_MAST_PLAT.VEHICLE_MODEL_CODE " +
										" JOIN GALADM.MC_VIOS_MASTER_ASM_PROC_TBX VIOS_MAST_PROC ON VIOS_MAST_PLAT.VIOS_PLATFORM_ID=VIOS_MAST_PROC.VIOS_PLATFORM_ID AND PDDAPLTFRM.ASM_PROC_NO=VIOS_MAST_PROC.ASM_PROC_NO " +
										" JOIN GALADM.MC_OP_REV_PLATFORM_TBX OPREVPLAT  ON (PDDAPLTFRM.PDDA_PLATFORM_ID = OPREVPLAT.PDDA_PLATFORM_ID) " +
										" JOIN GALADM.MC_OP_REV_TBX OPREV ON (OPREVPLAT.OPERATION_NAME = OPREV.OPERATION_NAME AND OPREVPLAT.OP_REV = OPREV.OP_REV " +
										" AND OPREV.APPROVED <= CURRENT_TIMESTAMP AND OPREV.DEPRECATED IS NULL ) " +
										" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (OPREV.OPERATION_NAME = PDDAUNITREV.OPERATION_NAME AND OPREV.OP_REV = PDDAUNITREV.OP_REV) " +
										" JOIN VIOS.PVPPI1 UNITPPEIMG ON (UNITPPEIMG.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PPS') " +
										" WHERE VIOS_MAST_PROC.PROCESS_POINT_ID=CAST(?1 AS CHARACTER(16))  AND PDDAPLTFRM.APPROVED <= CURRENT_TIMESTAMP " +
										" AND PDDAPLTFRM.DEPRECATED IS NULL " +
										" GROUP BY PDDAUNITREV.APVD_UNIT_MAINT_ID) " +
										" AND  ((UNIT.SAFETY_ERGO_PT IS NOT NULL)  OR (UNIT.SAFETY_ERGO_INST IS NOT NULL))";
	
	private static String GET_LINE_SPEED = "SELECT MAX(LINE_SPEED)  FROM VIOS.PVUMX1 UNIT  " +
	" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF')  " +
	" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)  " +
	" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
	" WHERE PRODSTRU.PRODUCT_ID=?1 AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16))   ";

	private static String GET_TRQ_TOOL = " SELECT TRQTOOL.UNIT_SHEET_TOOL FROM VIOS.PVDPT1 TRQTOOL " +
	" JOIN VIOS.PVUMX1 UNIT ON (TRQTOOL.PLANT_LOC_CODE = UNIT.PLANT_LOC_CODE AND TRQTOOL.DEPT_CODE = UNIT.DEPT_CODE ) " +
	" WHERE UNIT.MAINTENANCE_ID =CAST(?1 AS INTEGER)";


	private static String LIST_OF_OPER_FOR_PP_DIVISION_MODE = "SELECT STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, PTOU.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  " +
		" VARCHAR_FORMAT (UNIT.UNIT_CREATE_DATE, 'MM/DD/YYYY') AS CREATE_DATE,UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT,PROCESS.ASM_PROC_NO, PROCESS.ASM_PROC_NAME  FROM VIOS.PVUMX1 UNIT  " +
		" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF')  " +
		" JOIN VIOS.PVPMX1 PROCESS ON (PROCESS.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID) "+
		" JOIN VIOS.PVPMU1 PTOU ON (PTOU.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID AND UNIT.UNIT_NO = PTOU.UNIT_NO) "+
		" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)  " +
		" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
		" WHERE PRODSTRU.PRODUCT_ID=?1 AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16))   " +
		" GROUP BY STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, PTOU.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  " + 
		" UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT,PROCESS.ASM_PROC_NO, PROCESS.ASM_PROC_NAME  ORDER BY PTOU.UNIT_SEQ_NO";							

	private static String LIST_OF_OPER_FOR_PP_PROCESS_POINT_MODE =	"SELECT STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, PTOU.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  "  +
		" VARCHAR_FORMAT (UNIT.UNIT_CREATE_DATE, 'MM/DD/YYYY') AS CREATE_DATE,UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT,PROCESS.ASM_PROC_NO, PROCESS.ASM_PROC_NAME  FROM VIOS.PVUMX1 UNIT   " +
		" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF')   " +
		" JOIN VIOS.PVPMX1 PROCESS ON (PROCESS.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID)  " +
		" JOIN VIOS.PVPMU1 PTOU ON (PTOU.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID AND UNIT.UNIT_NO = PTOU.UNIT_NO)  " +
		" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID) " +  
		" JOIN GALADM.MC_PRODUCT_STRU_FOR_PROCESS_POINT_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV)  " +
		" WHERE PRODSTRU.PRODUCT_ID=?1 AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16))    " +
		" GROUP BY STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, PTOU.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  " +
		" UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT,PROCESS.ASM_PROC_NO, PROCESS.ASM_PROC_NAME  ORDER BY PTOU.UNIT_SEQ_NO";
	
	private static String GET_ASM_PROC_INFO = "SELECT PROCESS.ASM_PROC_NO, PROCESS.ASM_PROC_NAME  FROM VIOS.PVUMX1 UNIT  " +
	" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF')  " +
	" JOIN VIOS.PVPMX1 PROCESS ON (PROCESS.MAINTENANCE_ID = PDDAUNITREV.APVD_PROC_MAINT_ID) "+
	" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)  " +
	" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
	" WHERE PRODSTRU.PRODUCT_ID=?1 AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16)) AND UNIT.MAINTENANCE_ID=CAST (?3 AS INTEGER)  " +
	" GROUP BY STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, UNIT.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  " + 
	" UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT,PROCESS.ASM_PROC_NO, PROCESS.ASM_PROC_NAME  ORDER BY UNIT.UNIT_SEQ_NO";							

	private final String FIND_PART = "SELECT PART_NO FROM VIOS.PVUPM1 T WHERE MAINTENANCE_ID = CAST(?1 AS INTEGER) FETCH FIRST 1 ROWS ONLY ";
	private final String GET_UNIT_TOT_TIME = "SELECT UNIT_TOT_TIME FROM VIOS.PVUMX1 T WHERE MAINTENANCE_ID = CAST(?1 AS INTEGER) FETCH FIRST 1 ROWS ONLY ";
	
	private static final String FIND_BY_UNIT_NO_AND_PLATFORM = "SELECT * FROM vios.PVUMX1 WHERE UNIT_NO=?1 AND PLANT_LOC_CODE=?2 AND DEPT_CODE=?3 AND MODEL_YEAR_DATE=?4 " 
			+ " AND PROD_SCH_QTY=?5 AND PROD_ASM_LINE_NO=?6 AND VEHICLE_MODEL_CODE=?7 ";
	
	private static final String FIND_BASE_PART_NO_BY_UNIT_NO = "SELECT distinct c.BASE_PART_NO "
			+ " FROM vios.PVCFU1 a "
			+ " join vios.PVCFR1 b on a.CHANGE_FORM_ID=b.CHANGE_FORM_ID "
			+ " join vios.PVUMX1 c on a.APVD_UNIT_MAINT_ID=c.MAINTENANCE_ID "
			+ " where a.UNIT_NO=?1 "
			+ " and b.PLANT_LOC_CODE||b.DEPT_CODE||SUBSTR(DIGITS(b.PROD_SCH_QTY),1,4)||'.'||SUBSTR(DIGITS(b.PROD_SCH_QTY),5)||b.PROD_ASM_LINE_NO||b.VEHICLE_MODEL_CODE||SUBSTR(DIGITS(b.MODEL_YEAR_DATE),1,4)||'.'||SUBSTR(DIGITS(b.MODEL_YEAR_DATE),5)=?2";
	
	@SuppressWarnings("unchecked")
	public List<UnitOfOperation> getAllOperationsForProcessPoint(String productId ,String processPoint, String mode) {
		ArrayList<UnitOfOperation> operationLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2",processPoint);
		
		List<Object[]> operationLstObjs = null;
		
		if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString()))
			operationLstObjs = findResultListByNativeQuery(LIST_OF_OPER_FOR_PP_DIVISION_MODE, params);
		else if (StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString()))
			operationLstObjs = findResultListByNativeQuery(LIST_OF_OPER_FOR_PP_PROCESS_POINT_MODE, params);
		
		if(operationLstObjs != null && operationLstObjs.size() > 0)
			operationLst= new ArrayList<UnitOfOperation>();
		
		for(Object[] operObj : operationLstObjs){
			UnitOfOperation pddaUnitOfOperation = new UnitOfOperation();
			pddaUnitOfOperation.setProcessPointId(operObj[0].toString());
			pddaUnitOfOperation.setProdSpecCode(operObj[1].toString());
			pddaUnitOfOperation.setPddaMaintenanceId(operObj[2].toString());
			pddaUnitOfOperation.setUnitSeqNo(Integer.parseInt(operObj[3].toString()));
			pddaUnitOfOperation.setUnitNo(operObj[4].toString());
			pddaUnitOfOperation.setUnitBasePartNo(operObj[5].toString());
			pddaUnitOfOperation.setUnitCreateDateStr(operObj[7].toString());
			pddaUnitOfOperation.setUnitOperationDesc(operObj[8].toString());
			pddaUnitOfOperation.setWorkPtDescText(operObj[9].toString());
			pddaUnitOfOperation.setAsmProcessNo(operObj[10].toString());
			pddaUnitOfOperation.setAsmProcessName(operObj[11].toString());
			
			//get Part Flag
			Parameters param = Parameters.with("1", (Integer)operObj[2]);
			String partFlg = findFirstByNativeQuery(FIND_PART, param, String.class);
			if (null == partFlg) partFlg = "";
			else partFlg = "P";
			pddaUnitOfOperation.setPartFlg(partFlg);
			
			operationLst.add(pddaUnitOfOperation);
		}
		
		return operationLst;
	}


	@SuppressWarnings("unchecked")
	public List<Object[]> findAllSftyErgoForProcessPoint(String processPoint) {
		Parameters params = Parameters.with("1", processPoint);
		return findResultListByNativeQuery(GET_ERGOS_FOR_PROCESS_POINT, params);
	}
	
	@SuppressWarnings("unchecked")
	public int getLineSpeed(String productId ,String processPoint) {
		Parameters params = Parameters.with("1", productId);
		params.put("2",processPoint);
		Integer time = findFirstByNativeQuery(GET_LINE_SPEED, params,Integer.class);
		return time == null ? 0 : time;
	}

	@SuppressWarnings("unchecked")
	public UnitOfOperationDetails getUnitOfOperationDetails(
			int maintenanceId,String ProcessPointID,String ProductID) {
		
		UnitOfOperationDetails unitOfOperationDetails = new UnitOfOperationDetails();
		//List<Object[]> unitLst = findResultListByNativeQuery(UNIT_OF_OPERATION, params);
		List<Unit> unitLst = findAll(Parameters.with("maintenanceId", maintenanceId));
		if ( unitLst==null || unitLst.size() == 0 ) return null;
		String strTrqCharVal = "";
		
		for(Unit unit : unitLst){
			unitOfOperationDetails.setMaintenanceId(maintenanceId);
			
			if (unit.getTorqueCharValue() == null||unit.getTorqueCharValue().equals("MIN")) {
				strTrqCharVal=unit.getMinTorqueValQty()+" - "+unit.getMaxTorqueValQty();
			} else if( unit.getTorqueCharValue().equals("PRE")) {
				strTrqCharVal=unit.getMinTorqueValQty().toString();
			}
			unitOfOperationDetails.setTool(unit.getTool());
			unitOfOperationDetails.setWorkPtDescText(unit.getWorkPtDescText());
			unitOfOperationDetails.setSafetyPtDescText(unit.getSafetyPtDesc());
			unitOfOperationDetails.setAuxMatDescText(unit.getAuxMtrlDescText());
			unitOfOperationDetails.setAuxMsdsNo(unit.getAuxMsdsNo());
			unitOfOperationDetails.setQualityPoint(unit.getQltyPtDescText());
			unitOfOperationDetails.setUnitOperationDesc(unit.getUnitOpDescText());
			unitOfOperationDetails.setUnitNo(unit.getUnitNo());
			unitOfOperationDetails.setUnitRank(unit.getUnitRank());
			unitOfOperationDetails.setMaintDate(unit.getMaintDate());
		}

		Parameters params = Parameters.with("1", maintenanceId);

		
		
		List<UnitReactionPlan> planLst = ServiceFactory.getDao(UnitReactionPlanDao.class).findAllReactionPlans(maintenanceId);
		String reactionplan="";
		if (planLst!=null)  {
			for(UnitReactionPlan plan : planLst){
				reactionplan = reactionplan + plan.getId().getReaction()+" ";
			}
		}
		unitOfOperationDetails.setReactionPlan(reactionplan);

		List<UnitControlMethod> methodLst = ServiceFactory.getDao(UnitControlMethodDao.class).findAllControlMethods(maintenanceId);
		String controlmethod="";
		if (methodLst!=null)  {
			for(UnitControlMethod method : methodLst){
				controlmethod = controlmethod + method.getId().getControlMethodDescription()+" ";
			}
		}
		unitOfOperationDetails.setControlMethod(controlmethod);
		
		List<UnitMost> workingPointDtlLst = ServiceFactory.getDao(UnitMostDao.class).findAllWorkingPointDtl(maintenanceId);
		String workingPointDtl="";
		if (workingPointDtlLst!=null)  {
			int size = workingPointDtlLst.size();
			for(UnitMost workingPoint : workingPointDtlLst){
				if (--size > 0)	workingPointDtl = workingPointDtl + workingPoint.getId().getWorkingPointDtl()+",";
				else workingPointDtl = workingPointDtl + workingPoint.getId().getWorkingPointDtl();
			}
		}
		unitOfOperationDetails.setWorkPtDetail(workingPointDtl);

		List<UnitSpecialControl> specialControlLst = ServiceFactory.getDao(UnitSpecialControlDao.class).findAllSpecialControl(maintenanceId);
		String specialcontrol="";
		if (specialControlLst!=null)  {
			for(UnitSpecialControl specialmethod : specialControlLst){
				specialcontrol = specialcontrol + specialmethod.getId().getControlApplied()+" ";
			}
		}
		unitOfOperationDetails.setSpecialControl(specialcontrol);

		
		String UnitConcernPoint = "";
		List<UnitConcernPoint> concernpointLst = ServiceFactory.getDao(UnitConcernPointDao.class).findAllConcernPoint(maintenanceId);
		if (concernpointLst!=null) {
			for(UnitConcernPoint point : concernpointLst){
				UnitConcernPoint = UnitConcernPoint + point.getConcernPoint()+" ";
			}
		}
		unitOfOperationDetails.setImpactPoint(UnitConcernPoint);
		
		if (strTrqCharVal.length() != 0) unitOfOperationDetails.setTrqCharVal((strTrqCharVal.trim()+contructStrValues(findResultListByNativeQuery(GET_TRQ_TOOL, params))).trim());
		
		Parameters processparams = Parameters.with("1", ProductID);
		processparams.put("2",ProcessPointID);
		processparams.put("3",maintenanceId);
		
		List<Object[]> ProcessResultSet = findResultListByNativeQuery(GET_ASM_PROC_INFO, processparams);
		String processNo = "";
		for (Object[] operObj : ProcessResultSet) {
			processNo = operObj[0].toString();
		}
		unitOfOperationDetails.setProcessNo(processNo);
		
		return unitOfOperationDetails;
	}

	private String contructStrValues(List<String> LstOfObjs){
		
		String strValue = " ";
		
		if (null == LstOfObjs) return strValue;
		
		for(String str : LstOfObjs){
			strValue = strValue + str;
		}
		return strValue;
	}

	public float getUnitTotTime(int maintenanceId){
		Parameters params = Parameters.with("1", maintenanceId);
		float time = findFirstByNativeQuery(GET_UNIT_TOT_TIME, params,float.class);
		return time;
		
	}


	@SuppressWarnings("unchecked")
	public List<UnitOfOperation> getUnitOfOperationDetails(TrainingDataDto trainingDataDto) {
		
		
		ArrayList<UnitOfOperation> operationLst = null;
		Parameters params = Parameters.with("1",trainingDataDto.getProcessNum()).put("2", trainingDataDto.getModelYear()).put("3", trainingDataDto.getPlantLocationCode()).put("4", trainingDataDto.getDeptCode()).put("5", trainingDataDto.getLineNum());
		List<Object[]> operationLstObjs = findResultListByNativeQuery(LIST_OF_OPER_FOR_PROCESS, params);
		
		if(operationLstObjs.size() > 0)
			operationLst= new ArrayList<UnitOfOperation>();
		
		for(Object[] operObj : operationLstObjs){
			UnitOfOperation pddaUnitOfOperation = new UnitOfOperation();
			String unitNo = operObj[0].toString();
			pddaUnitOfOperation.setUnitNo(unitNo);
			operationLst.add(pddaUnitOfOperation);
		}
		
		return operationLst;
	}


	@Override
	public Unit findByUnitNoAndPlatform(String unitNo, String plantLocCode, String deptCode, BigDecimal modelYearDate,
			BigDecimal prodSchQty, String prodAsmLineNo, String vehicleModelCode) {
		Parameters params = new Parameters();
		params.put("1", unitNo);
		params.put("2", plantLocCode);
		params.put("3", deptCode);
		params.put("4", modelYearDate);
		params.put("5", prodSchQty);
		params.put("6", prodAsmLineNo);
		params.put("7", vehicleModelCode);
		return findFirstByNativeQuery(FIND_BY_UNIT_NO_AND_PLATFORM, params);
	}


	@Override
	public String findBasePartNoByUnit(String unitNo, String viosPlatformId) {
		Parameters params = new Parameters();
		params.put("1", unitNo);
		params.put("2", viosPlatformId);
		return findFirstByNativeQuery(FIND_BASE_PART_NO_BY_UNIT_NO, params, String.class);
	}
	
}
