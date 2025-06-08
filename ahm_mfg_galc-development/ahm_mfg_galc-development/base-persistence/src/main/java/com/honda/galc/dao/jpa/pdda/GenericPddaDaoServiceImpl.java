package com.honda.galc.dao.jpa.pdda;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.OperationType;
import com.honda.galc.constant.PartType;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.constant.RevisionType;
import com.honda.galc.dao.conf.MCMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCOperationCheckerDao;
import com.honda.galc.dao.conf.MCOperationDao;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevPlatformDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCPartCheckerDao;
import com.honda.galc.dao.conf.MCPddaChangeDao;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.conf.MCPddaUnitDao;
import com.honda.galc.dao.conf.MCPddaUnitRevisionDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.conf.MCViosMasterOperationCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartDao;
import com.honda.galc.dao.jpa.JpaEntityManager;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.dao.pdda.ProcessDao;
import com.honda.galc.dao.pdda.ProcessUnitDao;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dao.pdda.UnitModelTypeDao;
import com.honda.galc.dao.pdda.UnitPartDao;
import com.honda.galc.dto.MCPendingProcessDto;
import com.honda.galc.dto.PddaProcess;
import com.honda.galc.dto.PddaSafetyImage;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.dto.UnitOfOperationDetails;
import com.honda.galc.entity.conf.MCMeasurementChecker;
import com.honda.galc.entity.conf.MCMeasurementCheckerId;
import com.honda.galc.entity.conf.MCOperation;
import com.honda.galc.entity.conf.MCOperationChecker;
import com.honda.galc.entity.conf.MCOperationCheckerId;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMatrixId;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationMeasurementId;
import com.honda.galc.entity.conf.MCOperationPart;
import com.honda.galc.entity.conf.MCOperationPartId;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrixId;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.entity.conf.MCOperationRevPlatform;
import com.honda.galc.entity.conf.MCOperationRevPlatformId;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.entity.conf.MCPartChecker;
import com.honda.galc.entity.conf.MCPartCheckerId;
import com.honda.galc.entity.conf.MCPddaChange;
import com.honda.galc.entity.conf.MCPddaChangeId;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCPddaUnit;
import com.honda.galc.entity.conf.MCPddaUnitId;
import com.honda.galc.entity.conf.MCPddaUnitRevision;
import com.honda.galc.entity.conf.MCPddaUnitRevisionId;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationId;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPartId;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.entity.pdda.Process;
import com.honda.galc.entity.pdda.ProcessUnit;
import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.entity.pdda.UnitModelType;
import com.honda.galc.entity.pdda.UnitPart;
import com.honda.galc.entity.pdda.UnitPartId;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;
import com.honda.galc.vios.dto.mfg.MfgCtrlDto;
import com.honda.galc.vios.dto.mfg.MfgCtrlViosDto;
import com.honda.galc.vios.dto.mfg.MfgViosPartDto;

import com.honda.galc.util.Base64Coder;


/**
 * 
 * <h3>GenericPddaDaoServiceImpl Class description</h3>
 * <p> GenericPddaDaoServiceImpl description </p>
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
 * @author Fredrick Yessaian<br>
 * Mar 6, 2014
 *
 *
 */

public class GenericPddaDaoServiceImpl extends JpaEntityManager implements
		GenericPddaDaoService {
	private static final String FRAME = "FRAME";
	private static final String ENGINE = "ENGINE";
	public enum ReportType {
		PCF, USR, PPS, CU, PS, SS,
	}
	

	private static String LIST_OF_OPER_FOR_PP = "SELECT STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, UNIT.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  " +
				" VARCHAR_FORMAT (UNIT.UNIT_CREATE_DATE, 'MM/DD/YYYY') AS CREATE_DATE,UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT  FROM VIOS.PVUMX1 UNIT  " +
				" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF')  " +
				" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID)  " +
				" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
				" WHERE PRODSTRU.PRODUCT_ID=CAST(?1 AS CHARACTER(17)) AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16))   " +
				" GROUP BY STRUC.PROCESS_POINT_ID,STRUC.PRODUCT_SPEC_CODE, UNIT.MAINTENANCE_ID, UNIT.UNIT_SEQ_NO,UNIT.UNIT_NO, UNIT.BASE_PART_NO, UNIT.UNIT_CREATE_DATE,  " + 
				" UNIT.UNIT_OP_DESC_TEXT, UNIT.WORK_PT_DESC_TEXT  ORDER BY UNIT.UNIT_SEQ_NO";							

	
	private static String UNIT_OF_OPERATION = "SELECT  " +
												" UNIT.MAINTENANCE_ID,  " +
												" UNIT.MIN_TORQUE_VAL_QTY,  " +
												" UNIT.MAX_TORQUE_VAL_QTY,  " +
												" UNIT.TORQUE_CHAR_VALUE, " +
												" UNIT.TOOL,  " +
												" UNIT.WORK_PT_DESC_TEXT,  " +
												" UNIT.SAFETY_PT_DESC,  " +
												" UNIT.AUX_MTRL_DESC_TEXT,  " +
												" UNIT.QLTY_PT_DESC_TEXT  " +
												" FROM VIOS.PVUMX1 UNIT  " +
												" WHERE UNIT.MAINTENANCE_ID = CAST(?1 AS INTEGER)";
	
	private static String GET_REACTION_PLANS = "SELECT REACTIONPLN.REACTION FROM VIOS.PVMRP1 REACTIONPLN WHERE REACTIONPLN.MAINTENANCE_ID = CAST(?1 AS INTEGER)";
	
	private static String GET_CONTROL_METHOD = "SELECT CTRLMETHOD.CNTRL_METH_DESC FROM VIOS.PVUMC1 CTRLMETHOD WHERE CTRLMETHOD.MAINTENANCE_ID = CAST(?1 AS INTEGER)";
	
	private static String GET_IMPACT_POINT = " SELECT CONCERNPOINT.CONCERN_POINT FROM VIOS.PVUWX1 CONCERNPOINT WHERE CONCERNPOINT.MAINTENANCE_ID = CAST(?1 AS INTEGER)";
	
	private static String GET_TRQ_TOOL = " SELECT TRQTOOL.UNIT_SHEET_TOOL FROM VIOS.PVDPT1 TRQTOOL " +
		" JOIN VIOS.PVUMX1 UNIT ON (TRQTOOL.PLANT_LOC_CODE = UNIT.PLANT_LOC_CODE AND TRQTOOL.DEPT_CODE = UNIT.DEPT_CODE ) " +
		" WHERE UNIT.MAINTENANCE_ID =CAST(?1 AS INTEGER)";
	
	
	private static String GET_SAFETY_IMAGES = "SELECT * FROM VIOS.PVUSH1 PSI WHERE PSI.MAINTENANCE_ID = CAST(?1 AS INTEGER) ORDER BY PSI.IMAGE_SEQ_NO";
	
	private static String GET_UNIT_IMAGES = "SELECT * FROM VIOS.PVUMI1 UMI WHERE UMI.MAINTENANCE_ID = CAST(?1 AS INTEGER) ORDER BY UMI.IMAGE_SEQ_NO";

	private static String GET_UNIT_CCPIMAGES = "SELECT UNITIMG.IMAGE AS IMAGE, UNITIMG.UNIT_IMAGE AS UNIT_IMAGE FROM VIOS.PVUMX1 UNIT " +
					" JOIN VIOS.PVUMI1 UNITIMG ON (UNIT.MAINTENANCE_ID = UNITIMG.MAINTENANCE_ID AND UNIT.PLANT_LOC_CODE = UNITIMG.PLANT_LOC_CODE AND UNIT.DEPT_CODE = UNITIMG.DEPT_CODE)  " +
					" JOIN VIOS.PVUMC1 UNITCON  ON (UNIT.MAINTENANCE_ID = UNITCON.MAINTENANCE_ID AND UNIT.PLANT_LOC_CODE = UNITCON.PLANT_LOC_CODE AND UNIT.DEPT_CODE = UNITCON.DEPT_CODE AND UNITCON.CNTRL_METH_DESC='CCP' ) " +
					" JOIN GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV ON (UNIT.MAINTENANCE_ID = PDDAUNITREV.APVD_UNIT_MAINT_ID AND PDDAUNITREV.PDDA_REPORT='PCF') " +
					" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID) " +
					" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV) " +
					" WHERE PRODSTRU.PRODUCT_ID=CAST(?1 AS CHARACTER(17)) AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16))";
	

	private static String GET_UNIT_SAFETYIMAGELIST = "SELECT UNITIMG.MAINTENANCE_ID AS MAINTENANCE_ID, UNITIMG.PLANT_LOC_CODE AS PLANT_LOC_CODE, UNITIMG.IMAGE_SEQ_NO  AS IMAGE_SEQ_NO,   " +
													" UNITIMG.IMAGE_TIMESTAMP AS IMAGE_TIMESTAMP, UNITIMG.DEPT_CODE AS DEPT_CODE, UNITIMG.IMAGE AS IMAGE, UNITIMG.IMAGE_NAME AS IMAGE_NAME FROM VIOS.PVUSH1 UNITIMG  " +
													" WHERE MAINTENANCE_ID IN (SELECT DISTINCT (PDDAUNITREV.APVD_UNIT_MAINT_ID) FROM GALADM.MC_PDDA_UNIT_REV_TBX PDDAUNITREV   " +
													" JOIN GALADM.MC_STRUCTURE_TBX STRUC ON (PDDAUNITREV.OPERATION_NAME = STRUC.OPERATION_NAME AND PDDAUNITREV.OP_REV = STRUC.OP_REV AND PDDAUNITREV.PDDA_PLATFORM_ID = STRUC.PDDA_PLATFORM_ID) " + 
													" JOIN GALADM.MC_PRODUCT_STRUCTURE_TBX PRODSTRU ON (STRUC.PRODUCT_SPEC_CODE = PRODSTRU.PRODUCT_SPEC_CODE AND STRUC.STRUCTURE_REV = PRODSTRU.STRUCTURE_REV)  " +
													" WHERE PRODSTRU.PRODUCT_ID=CAST(?1 AS CHARACTER(17)) AND STRUC.PROCESS_POINT_ID=CAST (?2 AS CHARACTER(16)) AND PDDAUNITREV.PDDA_REPORT IN ('PCF')) " +
													" ORDER BY UNITIMG.MAINTENANCE_ID, UNITIMG.IMAGE_SEQ_NO" ;

	
	private final String FIND_PART = "SELECT PART_NO FROM VIOS.PVUPM1 T WHERE MAINTENANCE_ID = CAST(?1 AS INTEGER) FETCH FIRST 1 ROWS ONLY ";
	
	private final String GET_SEQUENCE_NUMBER_FOR_OPERATION = " select (coalesce(d.SEQUENCE_NUMBER,0)+1)*1000000+ "
			+ " (coalesce(l.LINE_SEQUENCE_NUMBER,0)+1)*1000+(coalesce(p.SEQUENCE_NUMBER,0)+1) ALL_SEQ_NUM "
			+ " from galadm.gal214tbx p left join galadm.gal195tbx l "
			+ " on p.LINE_ID=l.LINE_ID   "
			+ "  left JOIN GALADM.GAL128TBX D "
			+ " ON L.DIVISION_ID=D.DIVISION_ID  left JOIN GALADM.GAL117TBX S ON D.SITE_NAME=S.SITE_NAME "
			+ " WHERE P.PROCESS_POINT_ID=  CAST (?1 AS CHARACTER(16)) ";
	
	private static String GET_PROCESS_DETAILS = "SELECT DISTINCT CFU.ASM_PROC_NO, PROC.ASM_PROC_NAME FROM VIOS.PVCFU1 CFU "
			+ "JOIN VIOS.PVPMX1 PROC ON (CFU.APVD_PROC_MAINT_ID = PROC.MAINTENANCE_ID AND CFU.ASM_PROC_NO = PROC.ASM_PROC_NO) "
			+ "WHERE CHANGE_FORM_ID = CAST(?1 AS INTEGER)";
	
	private static String GET_PENDING_PROCESS = "SELECT DISTINCT PC.REV_ID as revId, PR.ASM_PROC_NO as asmProcNo FROM GALADM.MC_PDDA_CHG_TBX PC "
			+ "JOIN VIOS.PVCFU1 CFU ON PC.CHANGE_FORM_ID = CFU.CHANGE_FORM_ID "
			+ "JOIN VIOS.PVPMX1 PR ON CFU.APVD_PROC_MAINT_ID = PR.MAINTENANCE_ID "
			+ "WHERE NOT EXISTS (SELECT * FROM GALADM.MC_PDDA_PLATFORM_TBX PL WHERE PC.REV_ID = PL.REV_ID AND PR.ASM_PROC_NO = PL.ASM_PROC_NO)";
	
	private static String GET_PART_DESC_FROM_OP_PART_REV = "SELECT PART_DESC from GALADM.MC_OP_PART_REV_TBX "
			+ "WHERE PART_NO LIKE ?1 ORDER BY APPROVED DESC FETCH FIRST 1 ROW ONLY WITH UR";
	
	private static String GET_PART_DESC_FROM_PDDA = "SELECT PART_NAME FROM VIOS.PVUPM1 "
			+ "WHERE PART_NO LIKE ?1 ORDER BY coalesce(UPDATE_TIMESTAMP,CREATE_TIMESTAMP) DESC, PART_NAME FETCH FIRST 1 ROW ONLY WITH UR";
	
	@SuppressWarnings("unchecked")
	public List<UnitOfOperation> getAllOperationsForProcessPoint(String productId ,String processPoint) {
		
		ArrayList<UnitOfOperation> operationLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2",processPoint);
		
		List<Object[]> operationLstObjs = findResultListByNativeQuery(LIST_OF_OPER_FOR_PP, params);
		
		if(operationLstObjs.size() > 0)
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
	public UnitOfOperationDetails getUnitOfOperationDetails(
			int maintenanceId) {
		
		Parameters params = Parameters.with("1", maintenanceId);
		UnitOfOperationDetails unitOfOperationDetails = new UnitOfOperationDetails();
		List<Object[]> unitLst = findResultListByNativeQuery(UNIT_OF_OPERATION, params);
		String strTrqCharVal = "";
		
		for(Object[] unit : unitLst){
			unitOfOperationDetails.setMaintenanceId(maintenanceId);
			
			if (unit[3] == null||unit[3].equals("MIN")) strTrqCharVal=unit[1]+" - "+unit[2];
			unitOfOperationDetails.setTool((unit[4] != null) ? unit[4].toString() : null);
			unitOfOperationDetails.setWorkPtDescText((unit[5] != null) ? unit[5].toString() : null);
			unitOfOperationDetails.setSafetyPtDescText((unit[6] != null) ? unit[6].toString() : null);
			unitOfOperationDetails.setAuxMatDescText((unit[7] != null) ? unit[7].toString() : null);
			unitOfOperationDetails.setQualityPoint((unit[8] != null) ? unit[8].toString() : null);
		}

		unitOfOperationDetails.setReactionPlan(contructStrValues(findResultListByNativeQuery(GET_REACTION_PLANS, params)));
		unitOfOperationDetails.setControlMethod(contructStrValues(findResultListByNativeQuery(GET_CONTROL_METHOD, params)));
		unitOfOperationDetails.setImpactPoint(contructStrValues(findResultListByNativeQuery(GET_IMPACT_POINT, params)));
		if (strTrqCharVal.length() != 0) unitOfOperationDetails.setTrqCharVal(strTrqCharVal+"<br>"+contructStrValues(findResultListByNativeQuery(GET_TRQ_TOOL, params)));
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

	@Transactional
	public List<PddaSafetyImage> getSafetyImages(int maintenanceId) throws SQLException{
		Blob safetyImg = null;
		Parameters params = Parameters.with("1", maintenanceId);
		List<Object[]> pddaSafetyImgLst = findAllByNativeQuery(GET_SAFETY_IMAGES, params, Object[].class);
		ArrayList<PddaSafetyImage> safetyArryLst = null;
		for(Object[] sftImg : pddaSafetyImgLst){
			if(safetyArryLst == null)
				safetyArryLst = new ArrayList<PddaSafetyImage>();

			PddaSafetyImage safety = new PddaSafetyImage();
			safety.setMaintenanceId(Integer.parseInt(sftImg[0].toString()));
			safety.setPlantLocCode(sftImg[1].toString());
			safety.setImageSeqNo((short) Integer.parseInt(sftImg[2].toString()));
			safety.setDeptCode(sftImg[4].toString());
			safetyImg = (Blob)sftImg[5];
			safety.setImage(safetyImg.getBytes(1, (int)safetyImg.length()));
			safety.setImageName(sftImg[6].toString());
			safetyArryLst.add(safety);

			safetyImg = null;
		}
		return safetyArryLst;
	}

	@Transactional
	public List<PddaUnitImage> getUnitImages(int maintenanceId) throws SQLException {
		Blob unitImage = null;
		ArrayList<PddaUnitImage> unitImgLst = null;
		Parameters params = Parameters.with("1", maintenanceId);
		List<Object[]> pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_IMAGES, params, Object[].class);
		for(Object[] unitImg : pddaUnitImgLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<PddaUnitImage>();
			
			PddaUnitImage unit = new PddaUnitImage();
			unitImage = (Blob)unitImg[5];
			
			unit.setUnitImage(new String(Base64Coder.encode(unitImage.getBytes(1, (int)unitImage.length()))).replace("\r", "").replace("\n", ""));
			unitImgLst.add(unit);

			unitImage = null;
		}
		return unitImgLst;
	}

	@Transactional
	public List<PddaUnitImage> getUnitCCPImages(String productId ,String processPoint) throws SQLException {
		Blob unitImage = null;
		ArrayList<PddaUnitImage> unitImgLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPoint);

		
		List<Object[]> pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_CCPIMAGES, params, Object[].class);
		for(Object[] unitImg : pddaUnitImgLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<PddaUnitImage>();
			
			PddaUnitImage unit = new PddaUnitImage();
			
			unitImage = (Blob)unitImg[0];
			unit.setImage(unitImage.getBytes(1, (int)unitImage.length()));
			unitImgLst.add(unit);
			unitImage = null;
		}
		return unitImgLst;
	}

	
	@Transactional
	public ArrayList<UnitOfOperation> getUnitSafetyImageList(String productId ,String processPoint) throws SQLException {
		ArrayList<UnitOfOperation> unitImgLst = null;
		Parameters params = Parameters.with("1", productId);
		params.put("2", processPoint);

		
		List<Object[]> pddaUnitImgLst = findAllByNativeQuery(GET_UNIT_SAFETYIMAGELIST, params, Object[].class);
		for(Object[] unitImg : pddaUnitImgLst){
			if(unitImgLst == null)
				unitImgLst = new ArrayList<UnitOfOperation>();
			
			UnitOfOperation unit = new UnitOfOperation();
			unit.setPddaMaintenanceId(unitImg[0].toString());
			unit.setUnitNo(unitImg[0].toString());
			
			unitImgLst.add(unit);
		}
		return unitImgLst;
	}
	
	
	public Integer getSequenceNumberForOperation(String pp) {

		Parameters param = Parameters.with("1", pp);
		Integer seqNum = findFirstByNativeQuery(
				GET_SEQUENCE_NUMBER_FOR_OPERATION, param, Integer.class);

		return seqNum;
	}

	@Transactional
	public MCRevision createRevisionForChangeForms(List<Integer> changeFormList,
			String userId, String description, String revType) {
		
		
		Long maxRevId = ServiceFactory.getDao(MCRevisionDao.class).getMaxRevId();	     
		long max = maxRevId == null ? 0 : maxRevId.longValue();

		MCRevision revision = new MCRevision();
		revision.setId(max + 1);
		revision.setAssociateNo(userId);
		revision.setStatus(RevisionStatus.MAPPING.getRevStatus());
		revision.setDescription(description);
		revision.setType(revType);
		MCRevision entityMCRev = ServiceFactory.getDao(MCRevisionDao.class).save(revision);
		for (Integer changeFormId : changeFormList) {
			MCPddaChangeId id = new MCPddaChangeId();
			id.setChangeFormId(changeFormId);
			id.setRevisionId(max + 1);
			MCPddaChange entity = new MCPddaChange();
			entity.setId(id);
			ServiceFactory.getDao(MCPddaChangeDao.class).save(entity);
		}
		//if Revision type is PDDA STD, then create platforms
		if(revType!=null && (revType.trim().equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())
								|| revType.trim().equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()))) {
			
			List<MCViosMasterProcessDto> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllProcessesByRevId(entityMCRev.getId(), true);
			for(MCViosMasterProcessDto process : processList) {
				addMCPddaPlatformRecord(process.getAsmProcNo(), entityMCRev.getId(), userId);
			}
		}
		return entityMCRev;
	}
	
	@Transactional
	public MCRevision createRevisionForOneClickApproval(List<Integer> changeFormList,
			String userId, String description, String revType) {
		
		
		Long maxRevId = ServiceFactory.getDao(MCRevisionDao.class).getMaxRevId();	     
		long max = maxRevId == null ? 0 : maxRevId.longValue();

		MCRevision revision = new MCRevision();
		revision.setId(max + 1);
		revision.setAssociateNo(userId);
		revision.setStatus(RevisionStatus.BATCH_PENDING.getRevStatus());
		revision.setDescription(description);
		revision.setType(revType);
		MCRevision entityMCRev = ServiceFactory.getDao(MCRevisionDao.class).save(revision);
		for (Integer changeFormId : changeFormList) {
			MCPddaChangeId id = new MCPddaChangeId();
			id.setChangeFormId(changeFormId);
			id.setRevisionId(max + 1);
			MCPddaChange entity = new MCPddaChange();
			entity.setId(id);
			ServiceFactory.getDao(MCPddaChangeDao.class).save(entity);
		}
		//if Revision type is PDDA STD, then create platforms
		if(revType!=null && (revType.trim().equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())
								|| revType.trim().equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType()))) {
			
			List<MCViosMasterProcessDto> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllProcessesByRevId(entityMCRev.getId(), true);
			for(MCViosMasterProcessDto process : processList) {
				addMCPddaPlatformRecord(process.getAsmProcNo(), entityMCRev.getId(), userId);
			}
		}
		return entityMCRev;
	}
	public void addMCPddaPlatformRecord(String asmProcNo, Long revId, String userId) {
		long time = System.currentTimeMillis();
		
		List<Process> processes = ServiceFactory.getDao(ProcessDao.class)
				.getAllBy(revId, asmProcNo);
		
		Process process = null;
		//deprecated process flag
		boolean isDeprecated = false;
		if(processes!=null) {
			for(Process p : processes) {
				process = p;
				if(p.getAction()!=null && p.getAction().trim().equalsIgnoreCase("D")) {
					isDeprecated = true;
				}
				else {
					isDeprecated = false;
				}
			}
		}
		if(process!=null) {
			MCPddaPlatform newPlatform = new MCPddaPlatform();
			newPlatform.setRevId(revId);
			newPlatform.setAsmProcessNo(process.getAsmProcNo());
			newPlatform.setDeptCode(process.getDeptCode());
			newPlatform.setModelYearDate(process.getModelYearDate());
			newPlatform.setPlantLocCode(process.getPlantLocCode());
			newPlatform.setProductAsmLineNo(process.getProdAsmLineNo());
			newPlatform.setProductScheduleQty(process.getProdSchQty());
			newPlatform.setVehicleModelCode(process.getVehicleModelCode());
			if(isDeprecated) {
				newPlatform.setDeprecatedRevId(revId);
				newPlatform.setDeprecated(new Timestamp(time));
				newPlatform.setDeprecaterNo(userId);
			}
			ServiceFactory.getDao(MCPddaPlatformDao.class).insert(newPlatform);
		}
	}
	
	@Transactional
	public void addPlatformChg(long revId, String userId) {
		
		/* When implementing FIF and PA & Weld in VIOS, it was decided to default the Spec_code_type column value in matrix tables to 'PRODUCT'.
		 * */
		String SPEC_CODE_TYPE = "PRODUCT";
		
		//For Revision type 'Platform change', there will always be only one platform
		List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(revId);
		if(platforms!=null && platforms.size() == 1) {
			MCPddaPlatform platform = platforms.iterator().next();
			
			List<MCPddaPlatform> opPlatforms = ServiceFactory.getDao(MCPddaPlatformDao.class)
					.getAllAprvdPDDAPlatformExceptRev(platform.getPlantLocCode(), platform.getDeptCode(),
							platform.getModelYearDate(), platform.getProductScheduleQty(),
							platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
							platform.getAsmProcessNo(), revId);
			if(opPlatforms!=null) {
				for(MCPddaPlatform opPlatform: opPlatforms) {
					List<MCPddaUnit> activeUnits = ServiceFactory.getDao(MCPddaUnitDao.class)
							.findActivePddaUnits(opPlatform.getPddaPlatformId(), opPlatform.getRevId());
					//Creating MC PDDA Unit record
					if(activeUnits!=null) {
						for(MCPddaUnit pddaUnit: activeUnits) {
							MCPddaUnit entity = new MCPddaUnit();
							MCPddaUnitId pddaUnitId = new MCPddaUnitId();
							pddaUnitId.setOperationName(pddaUnit.getId().getOperationName());
							pddaUnitId.setUnitNo(pddaUnit.getId().getUnitNo());
							pddaUnitId.setPddaPlatformId(platform.getPddaPlatformId());
							pddaUnitId.setRevId(revId);
							entity.setId(pddaUnitId);
							ServiceFactory.getDao(MCPddaUnitDao.class).insert(entity);
						}
					}
					Map<String,MCOperationRevision> operationEntityMap = new HashMap<String,MCOperationRevision>();
					List<MCOperationRevision> opRevs = ServiceFactory.getDao(MCOperationRevisionDao.class)
							.findAllActiveBy(opPlatform.getPddaPlatformId(), opPlatform.getRevId());
					if(opRevs!=null) {
						for(MCOperationRevision operRev: opRevs) {
							
							//Calculating Operation revision
							int opRev = ServiceFactory.getDao(MCOperationRevisionDao.class)
									.getMaxRevisionForOperation(operRev.getId().getOperationName());
							opRev = opRev + 1;
							
							//Creating mc_op_rev_tbx record
							MCOperationRevision mcOpRevEntity = new MCOperationRevision();
							
							MCOperationRevisionId opRevId = new MCOperationRevisionId();
							opRevId.setOperationName(operRev.getId().getOperationName());
							opRevId.setOperationRevision(opRev);
							mcOpRevEntity.setId(opRevId);
			
							mcOpRevEntity.setDescription(operRev.getDescription());
							mcOpRevEntity.setProcessor(operRev.getProcessor());
							mcOpRevEntity.setView(operRev.getView());
							mcOpRevEntity.setRevisionId(revId);
							mcOpRevEntity.setType(operRev.getType());
							mcOpRevEntity.setCheck(operRev.getCheck());
							mcOpRevEntity = ServiceFactory.getDao(MCOperationRevisionDao.class).insert(mcOpRevEntity);
							
							operationEntityMap.put(operRev.getId().getOperationName(),mcOpRevEntity);
							
							//Creating checkers
							createCheckers(operRev, opRev);
							
							//creating mc_op_rev_platform_tbx record
							MCOperationRevPlatformId platRevId = new MCOperationRevPlatformId();
							platRevId.setOperationName(operRev.getId().getOperationName());
							platRevId.setPddaPlatformId(opPlatform.getPddaPlatformId());
							platRevId.setOperationRevision(operRev.getId().getOperationRevision());
							MCOperationRevPlatform opRevPltfrm = ServiceFactory.getDao(MCOperationRevPlatformDao.class).findByKey(platRevId);
							
							
							MCOperationRevPlatform opRevPlatformEntity = new MCOperationRevPlatform();
							MCOperationRevPlatformId opRevPltfrmId = new MCOperationRevPlatformId();
							opRevPltfrmId.setOperationRevision(opRev);
							opRevPltfrmId.setPddaPlatformId(platform.getPddaPlatformId());
							opRevPltfrmId.setOperationName(opRevPltfrm.getId().getOperationName());
							opRevPlatformEntity.setId(opRevPltfrmId);

							opRevPlatformEntity.setDeviceId(opRevPltfrm.getDeviceId());
							opRevPlatformEntity.setDeviceMsg(opRevPltfrm.getDeviceMsg());
							opRevPlatformEntity.setOperationSeqNum(opRevPltfrm.getOperationSeqNum());
							opRevPlatformEntity.setOperationTime(opRevPltfrm.getOperationTime());
							ServiceFactory.getDao(MCOperationRevPlatformDao.class).insert(
									opRevPlatformEntity);
							
							//creating mc_op_matrix records
							List<MCOperationMatrix> opMatrixList =  ServiceFactory.getDao(MCOperationMatrixDao.class)
									.findAllBy(operRev.getId().getOperationName(), operRev.getId().getOperationRevision(), opPlatform.getPddaPlatformId());
							if(opMatrixList!=null) {
								for(MCOperationMatrix opMatrix: opMatrixList) {
									MCOperationMatrix opMatrixEntity = new MCOperationMatrix();
									MCOperationMatrixId opMatrixId = new MCOperationMatrixId();
									opMatrixId.setOperationName(opMatrix.getId().getOperationName());
									opMatrixId.setOperationRevision(opRev);
									opMatrixId.setPddaPlatformId(platform.getPddaPlatformId());
									opMatrixId.setSpecCodeMask(opMatrix.getId().getSpecCodeMask());
									opMatrixId.setSpecCodeType(SPEC_CODE_TYPE);
									opMatrixEntity.setId(opMatrixId);
									ServiceFactory.getDao(MCOperationMatrixDao.class).insert(opMatrixEntity);
								}
							}
							
							// creating mc_pdda_unit_rev_tbx records
							List<MCPddaUnitRevision> pur = ServiceFactory.getDao(MCPddaUnitRevisionDao.class)
									.findAllBy(operRev.getId().getOperationName(), operRev.getId().getOperationRevision(), opPlatform.getPddaPlatformId());
							if(pur!=null) {
								for(MCPddaUnitRevision pddaUnitRev: pur) {
									MCPddaUnitRevision pddaUnitRevEntity = new MCPddaUnitRevision();
									MCPddaUnitRevisionId purId = new MCPddaUnitRevisionId();
									purId.setOperationName(operRev.getId().getOperationName());
									purId.setOperationRevision(opRev);
									purId.setPddaPlatformId(platform.getPddaPlatformId());
									purId.setPddaReport(pddaUnitRev.getId().getPddaReport());
									purId.setUnitNo(pddaUnitRev.getId().getUnitNo());
									pddaUnitRevEntity.setId(purId);
									
									pddaUnitRevEntity.setApprovedProcessMaintenanceId(pddaUnitRev.getApprovedProcessMaintenanceId());
									pddaUnitRevEntity.setApprovedUnitMaintenanceId(pddaUnitRev.getApprovedUnitMaintenanceId());
									
									ServiceFactory.getDao(MCPddaUnitRevisionDao.class).insert(pddaUnitRevEntity);
								}
							}
						}
					}
					for(Entry<String, MCOperationRevision> operationEntity: operationEntityMap.entrySet()) {
						String operationName  =  operationEntity.getKey();
						MCOperationRevision operation = operationEntity.getValue(); 
						//Parts
						List<MCOperationPartRevision> parts = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
								.findAllActiveBy(operationName, opPlatform.getRevId());
						if(parts!=null) {
							// Calculating Part Revision
							int partRev = generatePartRevision(operationName);
							for(MCOperationPartRevision part: parts) {
								// Creating mc_op_part_rev_tbx record
								MCOperationPartRevision partEntity = new MCOperationPartRevision();
								MCOperationPartRevisionId partRevId = new MCOperationPartRevisionId();
								partRevId.setOperationName(part.getId().getOperationName());
								partRevId.setPartId(part.getId().getPartId());
								partRevId.setPartRevision(partRev);
								partEntity.setId(partRevId);
								partEntity.setRevisionId(revId);
								partEntity.setPartDesc(part.getPartDesc());
								partEntity.setPartType(part.getPartType());
								partEntity.setPartCheck(part.getPartCheck());
								partEntity.setPartNo(part.getPartNo());
								partEntity.setPartSectionCode(part.getPartSectionCode());
								partEntity.setPartItemNo(part.getPartItemNo());
								partEntity.setPartMask(part.getPartMask());
								partEntity.setPartMark(part.getPartMark());
								partEntity.setPartMaxAttempts(part.getPartMaxAttempts());
								partEntity.setMeasCount(part.getMeasCount());
								partEntity.setPartTime(part.getPartTime());
								partEntity.setPartCheck(part.getPartCheck());
								partEntity.setPartView(part.getPartView());
								partEntity.setPartProcessor(part.getPartProcessor());
								partEntity.setDeviceId(part.getDeviceId());
								partEntity.setDeviceMsg(part.getDeviceMsg());
								ServiceFactory.getDao(MCOperationPartRevisionDao.class).insert(partEntity);
								
								//Creating part measurements only for measurement units
								if(operation != null && isMeasurmentUnit(operation.getType()) )
										createPartMeasurements(part, partRev);
								
								// Creating mc_op_part_matrix records
								List<MCOperationPartMatrix> partMatrixList = ServiceFactory.getDao(MCOperationPartMatrixDao.class)
										.findAllSpecCodeForOperationPartIdAndPartRev(part.getId().getOperationName(), part.getId().getPartId(), part.getId().getPartRevision());
								if(partMatrixList!=null) {
									for(MCOperationPartMatrix partMatrix:partMatrixList) {
										MCOperationPartMatrix partMatrixEntity = new MCOperationPartMatrix();
										MCOperationPartMatrixId partMatrixId = new MCOperationPartMatrixId();
										partMatrixId.setOperationName(partMatrix.getId().getOperationName());
										partMatrixId.setPartId(partMatrix.getId().getPartId());
										partMatrixId.setPartRevision(partRev);
										partMatrixId.setSpecCodeMask(partMatrix.getId().getSpecCodeMask());
										partMatrixId.setSpecCodeType(SPEC_CODE_TYPE);
										partMatrixEntity.setId(partMatrixId);
										ServiceFactory.getDao(MCOperationPartMatrixDao.class).insert(partMatrixEntity);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void createPartMeasurements(MCOperationPartRevision activePart,
			int newPartRev) {
		//Getting measurements
		List<MCOperationMeasurement> measurements = ServiceFactory.getDao(MCOperationMeasurementDao.class)
				.findAllMeasurementForOperationPartAndPartRevision(activePart.getId().getOperationName(), 
						activePart.getId().getPartId(), activePart.getId().getPartRevision());
		if(measurements!=null) {
			for(MCOperationMeasurement activeMeas: measurements) {
				//Creating measurements
				createMeasurement(activeMeas.getId().getOperationName(), 
						activeMeas.getId().getPartId(), newPartRev, activeMeas);
			}
		}
	}

	private void createCheckers(MCOperationRevision activeOpRev, int newOpRev) {
		//Getting Operation Checkers
		List<MCOperationChecker> operationCheckers = ServiceFactory.getDao(MCOperationCheckerDao.class)
				.findAllBy(activeOpRev.getId().getOperationName(), 
				activeOpRev.getId().getOperationRevision());
		//Creating Operation Checkers
		if(operationCheckers!=null) {
			for(MCOperationChecker activeOpChecker: operationCheckers) {
				createOperationChecker(activeOpRev.getId().getOperationName(), newOpRev, activeOpChecker);
			}
		}
		
		//Getting Part Checkers
		List<MCPartChecker> partCheckers = ServiceFactory.getDao(MCPartCheckerDao.class)
				.findAllBy(activeOpRev.getId().getOperationName(), 
				activeOpRev.getId().getOperationRevision());
		//Creating Part Checkers
		if(partCheckers!=null) {
			for(MCPartChecker activePartChecker: partCheckers) {
				createPartChecker(activePartChecker.getId().getOperationName(),
						newOpRev, activePartChecker.getId().getPartId(), 
						activePartChecker);
			}
		}
		
		//Getting Measurement Checkers
		List<MCMeasurementChecker> measCheckers = ServiceFactory.getDao(MCMeasurementCheckerDao.class)
				.findAllBy(activeOpRev.getId().getOperationName(), 
						activeOpRev.getId().getOperationRevision());
		//Creating Measurement Checkers
		if(measCheckers!=null) {
			for(MCMeasurementChecker activeMeasChecker: measCheckers) {
				createMeaasurementChecker(activeMeasChecker.getId().getOperationName(),
						newOpRev, activeMeasChecker.getId().getPartId(), 
						activeMeasChecker);
			}
		}
	}

	private MfgCtrlDto prepareDC(ChangeFormUnit cfu, long revId, String userId) {
		MfgCtrlDto mfgDC = new MfgCtrlDto();
		mfgDC.setPropertyBean(PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class));
		mfgDC.setChangeFormUnit(cfu);
		mfgDC.setUserId(userId);
		mfgDC.setSpecCodeType("PRODUCT");
		//MC Revision
		MCRevision revision = ServiceFactory.getDao(MCRevisionDao.class).findByKey(revId);
		mfgDC.setMfgRevision(revision);
		//PDDA standard approval flag
		boolean isStdApvd = false;
		if(revision.getType().equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
			isStdApvd = true;
		}
		//Model Year from Change Form for matrix population
		Integer modelYear = cfu.getChangeForm().getModelYearDate().intValue();
		mfgDC.setModelYear(modelYear);
		//Unit
		Unit unit = ServiceFactory.getDao(UnitDao.class).findByKey(new Integer(cfu.getUnit().getId()));
		mfgDC.setUnit(unit);
		//deprecated unit flag
		boolean isDeprecated = false;
		String action = StringUtils.trimToEmpty(unit.getAction());
		//D - Delete; U - Unassigned
		if(action.equalsIgnoreCase("D") || action.equalsIgnoreCase("U")) {
			isDeprecated = true;
		}
		//Operation Name = <unit no>_<plant loc code><dept code><prod sch qty><asm line no><vmc><model year date>
		DecimalFormat formatter = new DecimalFormat("0000.0");
		String operationName = unit.getUnitNo() + "_" 
				+ unit.getPlantLocCode() + unit.getDeptCode() 
				+ formatter.format(unit.getProdSchQty().floatValue()) 
				+ unit.getProdAsmLineNo() + unit.getVehicleModelCode() 
				+ formatter.format(unit.getModelYearDate().floatValue());
		mfgDC.setOperationName(operationName);
		//Fetch PDDA Platform
		MCPddaPlatform platform = ServiceFactory
				.getDao(MCPddaPlatformDao.class).getPDDAPlatformForOperation(
						unit.getPlantLocCode(), unit.getDeptCode(),
						unit.getModelYearDate(), unit.getProdSchQty(),
						unit.getProdAsmLineNo(), unit.getVehicleModelCode(),
						cfu.getAsmProcNo(), revId);
		mfgDC.setPlatform(platform);
		//Setting new Part Revision
		mfgDC.setNewPartRev(generatePartRevision(operationName));
		//If operation exists in the revision, 
		//then this change form unit is out dated and needs to be deprecated
		List<MCOperationRevision> operations = ServiceFactory.getDao(MCOperationRevisionDao.class)
												.findAllBy(operationName, revId);
		if(operations!=null && !operations.isEmpty()) {
			isDeprecated = true;
		}
		mfgDC.setDeprecated(isDeprecated);
		//Set MfgCtrlVios Dto
		MfgCtrlViosDto mfgCtrlViosDto = prepareViosDC(mfgDC);
		mfgDC.setMfgCtrlViosDto(mfgCtrlViosDto);
		return mfgDC;
	}
	
	private List<MCOperationChecker> getOpCheckerBy(List<MCViosMasterOperationChecker> masterOpCheckers, int opRev) {
		List<MCOperationChecker> operationCheckers = new ArrayList<MCOperationChecker>();
		for(MCViosMasterOperationChecker masterOpChecker : masterOpCheckers) {
			MCOperationChecker opChecker = new MCOperationChecker();
			opChecker.setId(new MCOperationCheckerId(masterOpChecker.getOperationName(), opRev, masterOpChecker.getId().getCheckPoint(), masterOpChecker.getId().getCheckSeq()));
			opChecker.setCheckName(masterOpChecker.getCheckName());
			opChecker.setChecker(masterOpChecker.getChecker());
			opChecker.setReactionType(masterOpChecker.getReactionType());
			operationCheckers.add(opChecker);
		}
		return operationCheckers;
	}
	
	private List<MCPartChecker> getPartCheckerBy(List<MCViosMasterOperationPartChecker> masterOpPartCheckers, String partId, int opRev) {
		List<MCPartChecker> partCheckers = new ArrayList<MCPartChecker>();
		for(MCViosMasterOperationPartChecker masterOpPartChecker : masterOpPartCheckers) {
			MCPartChecker partChecker = new MCPartChecker();
			partChecker.setId(new MCPartCheckerId(masterOpPartChecker.getOperationName(), partId, opRev, masterOpPartChecker.getId().getCheckPoint(), masterOpPartChecker.getId().getCheckSeq()));
			partChecker.setCheckName(masterOpPartChecker.getCheckName());
			partChecker.setChecker(masterOpPartChecker.getChecker());
			partChecker.setReactionType(masterOpPartChecker.getReactionType());
			partCheckers.add(partChecker);
		}
		return partCheckers;
	}
	
	private MfgCtrlViosDto prepareViosDC(MfgCtrlDto mfgDC) {
		MfgCtrlViosDto mfgCtrlViosDto = new MfgCtrlViosDto();
		String operationName = mfgDC.getOperationName();
		String[] operationArray = operationName.split(Delimiter.UNDERSCORE);
		String unitNo = operationArray[0];
		String viosPlatformId = operationArray[1];
		//Setting operation details from VIOS master
		MCViosMasterOperation masterOp = ServiceFactory.getDao(MCViosMasterOperationDao.class).findByKey(new MCViosMasterOperationId(viosPlatformId, unitNo));
		if(masterOp != null) {
			List<MCViosMasterOperationChecker> masterOpCheckers = ServiceFactory.getDao(MCViosMasterOperationCheckerDao.class).findAllBy(viosPlatformId, unitNo);
			mfgCtrlViosDto.setMasterOpCheckers(masterOpCheckers);
		}
		mfgCtrlViosDto.setMasterOp(masterOp);
		
		//Fetching Measurement details from VIOS master
		List<MCViosMasterOperationMeasurement> masterOpMeasList = ServiceFactory.getDao(MCViosMasterOperationMeasurementDao.class)
				.findAllBy(viosPlatformId, unitNo);
		List<MCViosMasterOperationMeasurementChecker> masterOpMeasCheckerList = ServiceFactory.getDao(MCViosMasterOperationMeasurementCheckerDao.class)
				.findAllBy(viosPlatformId, unitNo);
		mfgCtrlViosDto.setMasterOpMeasList(masterOpMeasList);
		mfgCtrlViosDto.setMasterOpMeasCheckerList(masterOpMeasCheckerList);
		
		//Setting part details from VIOS master
		List<MCViosMasterOperationPart> masterOpParts = ServiceFactory.getDao(MCViosMasterOperationPartDao.class).findAllBy(viosPlatformId, unitNo);
		
		List<MCViosMasterOperationPartChecker> masterOpPartCheckers = ServiceFactory.getDao(MCViosMasterOperationPartCheckerDao.class)
				.findAllBy(viosPlatformId, unitNo);
		//Key: for MFG part - OperationName; for Ref Part - Part No
		Map<String, MfgViosPartDto> masterPartsMap = new HashMap<String, MfgViosPartDto>();
		Set<String> masterViosPartsMapKeySet = new HashSet<String>();
		//Part Checkers Loop
		Map<String, List<MCViosMasterOperationPartChecker>> masterViosPartCheckerMap = new HashMap<String, List<MCViosMasterOperationPartChecker>>();
		for(MCViosMasterOperationPartChecker masterOpPartChecker : masterOpPartCheckers) {
			String key = null;
			if(StringUtils.isEmpty(masterOpPartChecker.getId().getPartNo())) {
				//This is default MFG part
				key = masterOpPartChecker.getOperationName();
				
			} else {
				//This is MFG part created out of reference part
				key = StringUtils.trimToEmpty(masterOpPartChecker.getId().getPartNo());
			}
			List<MCViosMasterOperationPartChecker> checkers = null;
			//check if unit is present in map 
			if(masterViosPartCheckerMap.containsKey(key)) {
				checkers = masterViosPartCheckerMap.get(key);
			} else {
				checkers = new ArrayList<MCViosMasterOperationPartChecker>();
			}
			checkers.add(masterOpPartChecker);
			masterViosPartCheckerMap.put(key, checkers);
		}
		//Parts Loop
		Map<String, MCViosMasterOperationPart> masterViosPartMap = new HashMap<String, MCViosMasterOperationPart>();
		for(MCViosMasterOperationPart masterOpPart : masterOpParts) {
			String key = null;
			if(StringUtils.isEmpty(masterOpPart.getId().getPartNo())) {
				//This is default MFG part
				key = masterOpPart.getOperationName();
				
			} else {
				//This is MFG part created out of reference part
				key = StringUtils.trimToEmpty(masterOpPart.getId().getPartNo());
			}
			masterViosPartMap.put(key, masterOpPart);
			
			//Adding to master Parts Map
			MfgViosPartDto viosPartDto = new MfgViosPartDto();
			viosPartDto.setMasterOpPart(masterOpPart);
			if(masterViosPartCheckerMap.containsKey(key)) {
				//Checkers found!!
				viosPartDto.setMasterOpPartCheckers(masterViosPartCheckerMap.get(key));
			}
			masterPartsMap.put(key, viosPartDto);
			masterViosPartsMapKeySet.add(key);
		}
		//Rest of the Checkers
		for(String key: masterViosPartCheckerMap.keySet()) {
			if(!masterViosPartsMapKeySet.contains(key)) {
				// create new master vios part
				MCViosMasterOperationPartChecker partChecker = masterViosPartCheckerMap.get(key).iterator().next();
				MCViosMasterOperationPart opPart = new MCViosMasterOperationPart();
				MCViosMasterOperationPartId id = new  MCViosMasterOperationPartId(partChecker.getId().getViosPlatformId(), partChecker.getId().getUnitNo(), partChecker.getId().getPartNo(), PartType.MFG);
				opPart.setId(id);
				opPart.setPartCheck(PartCheck.DEFAULT);
				opPart.setPartMask(mfgDC.getPropertyBean().getPartMask());
				MfgViosPartDto viosPartDto = new MfgViosPartDto();
				viosPartDto.setMasterOpPart(opPart);
				viosPartDto.setMasterOpPartCheckers(masterViosPartCheckerMap.get(key));
				masterPartsMap.put(key, viosPartDto);
				masterViosPartsMapKeySet.add(key);
			}
		}
		//clear the data
		masterViosPartCheckerMap = null;
		masterViosPartsMapKeySet = null;
		
		mfgCtrlViosDto.setMasterPartsMap(masterPartsMap);
		return mfgCtrlViosDto;
	}
	
	private void createMCOperation(MfgCtrlDto mfgDC) {
		//Creating Operation
		MCOperation operation = ServiceFactory.getDao(MCOperationDao.class).findByKey(mfgDC.getOperationName());
		if(operation == null) {
			//Operation does not exist. Create an operation
			operation = new MCOperation();
			operation.setName(mfgDC.getOperationName());
			operation.setRevision(0);
			ServiceFactory.getDao(MCOperationDao.class).save(operation);
		}
	}
	
	private void createMCPddaUnit(MfgCtrlDto mfgDC) {
		long revId = mfgDC.getMfgRevision().getId();
		//Creating MC PDDA Unit record
		MCPddaUnit pddaUnit = new MCPddaUnit();
		MCPddaUnitId pddaUnitId = new MCPddaUnitId();
		pddaUnitId.setRevId(revId);
		pddaUnitId.setPddaPlatformId(mfgDC.getPlatform().getPddaPlatformId());
		pddaUnitId.setOperationName(mfgDC.getOperationName());
		pddaUnitId.setUnitNo(mfgDC.getUnit().getUnitNo());
		pddaUnit.setId(pddaUnitId);
		if(mfgDC.isDeprecated()) {
			pddaUnit.setDeprecated(new Timestamp(mfgDC.getCurrentTime()));
			pddaUnit.setDeprecatedRevId(revId);
			pddaUnit.setDeprecaterNo(mfgDC.getUserId());
		}
		 ServiceFactory.getDao(MCPddaUnitDao.class).save(pddaUnit);
	}
	
	private void createMCOperationRevision(MfgCtrlDto mfgDC) {
		//Calculating Operation revision
		int opRev = ServiceFactory.getDao(MCOperationRevisionDao.class)
				.getMaxRevisionForOperation(mfgDC.getOperationName());
		opRev = opRev + 1;
		//Fetching default operation view and processor
		String opView = mfgDC.getPropertyBean().getOpView();
		String opProcessor = mfgDC.getPropertyBean().getOpProcessor();
		String commonName = null;
		int check = mfgDC.getPropertyBean().getOpCheck();
		//Get Master Details
		if(mfgDC.getMfgCtrlViosDto().getMasterOp() != null) {
			MCViosMasterOperation masterOp = mfgDC.getMfgCtrlViosDto().getMasterOp();
			if(StringUtils.isNotEmpty(masterOp.getView()) && StringUtils.isNotEmpty(masterOp.getProcessor())) {
				opView = masterOp.getView();
				opProcessor = masterOp.getProcessor();
			}
			commonName = masterOp.getCommonName();
			check = masterOp.getOpCheck();
		}
		//Creating mc_op_rev_tbx record
		MCOperationRevision mcOpRev = new MCOperationRevision();
		MCOperationRevisionId id = new MCOperationRevisionId();
		id.setOperationName(mfgDC.getOperationName());
		id.setOperationRevision(opRev);
		mcOpRev.setId(id);
		mcOpRev.setDescription(mfgDC.getUnit().getUnitOpDescText());
		mcOpRev.setCheck(check);
		mcOpRev.setProcessor(opProcessor);
		mcOpRev.setView(opView);
		mcOpRev.setRevisionId(mfgDC.getMfgRevision().getId());
		mcOpRev.setCommonName(commonName);
		//Setting Operation type with respect to tool field
		OperationType opType = OperationType.INSTRUCTION;
		String tool = mfgDC.getChangeFormUnit().getUnit().getTool();
		if(StringUtils.isNotBlank(tool)) {
			try {
				opType = OperationType.get(StringUtils.upperCase(tool));
			}
			catch (IllegalArgumentException iae) {
				getLogger().debug("Skipping IllegalArgumentException for unit: "+mfgDC.getChangeFormUnit().getUnit().getUnitNo()
						+ ", tool: "+tool+". Assigning default operation type as INSTRUCTION");
			}
		}
		mcOpRev.setType(opType);
		if(mfgDC.isDeprecated()) {
			mcOpRev.setDeprecated(mfgDC.getCurrentDate());
			mcOpRev.setDeprecatedRevisionId(mfgDC.getMfgRevision().getId());
		}
		mcOpRev = ServiceFactory.getDao(MCOperationRevisionDao.class).save(mcOpRev);
		//Saving new operation in Data Container
		mfgDC.setNewOperation(mcOpRev);
		//Creating Operation Checkers
		createOperationCheckers(mfgDC);
	}
	
	private void createOperationCheckers(MfgCtrlDto mfgDC) {
		//Get master op checkers details
		if(mfgDC.getMfgCtrlViosDto().getMasterOpCheckers() != null) {
			List<MCOperationChecker> opCheckerList = getOpCheckerBy(mfgDC.getMfgCtrlViosDto().getMasterOpCheckers(), mfgDC.getNewOperation().getId().getOperationRevision());
			ServiceFactory.getDao(MCOperationCheckerDao.class).insertAll(opCheckerList);
		}
	}
	
	private void createOperationChecker(String operationName,
			int operationRevision, MCOperationChecker activeOpChecker) {
		MCOperationChecker mcOpChecker = new MCOperationChecker();
		MCOperationCheckerId mcOpCheckerId = new MCOperationCheckerId();
		mcOpCheckerId.setOperationName(operationName);
		mcOpCheckerId.setOperationRevision(operationRevision);
		mcOpCheckerId.setCheckPoint(activeOpChecker.getId().getCheckPoint());
		mcOpCheckerId.setCheckSeq(activeOpChecker.getId().getCheckSeq());
		mcOpChecker.setId(mcOpCheckerId);
		mcOpChecker.setChecker(activeOpChecker.getChecker());
		mcOpChecker.setCheckName(activeOpChecker.getCheckName());
		mcOpChecker.setReactionType(activeOpChecker.getReactionType());
		ServiceFactory.getDao(MCOperationCheckerDao.class).save(mcOpChecker);
	}

	private void createMCOpRevPlatform(MfgCtrlDto mfgDC) {
		//creating mc_op_rev_platform_tbx record
		MCOperationRevPlatform opRevPlatform = new MCOperationRevPlatform();
		MCOperationRevPlatformId platRevId = new MCOperationRevPlatformId();
		platRevId.setOperationName(mfgDC.getOperationName());
		platRevId.setPddaPlatformId(mfgDC.getPlatform().getPddaPlatformId());
		platRevId.setOperationRevision(mfgDC.getNewOperation().getId().getOperationRevision());
		opRevPlatform.setId(platRevId);
		opRevPlatform.setDeviceId("");
		opRevPlatform.setDeviceMsg("");
		opRevPlatform.setOperationSeqNum(getUnitSeqNo(mfgDC));
		opRevPlatform.setOperationTime((int)Math.round(mfgDC.getUnit().getUnitTotTime()));
		ServiceFactory.getDao(MCOperationRevPlatformDao.class).save(
				opRevPlatform);
	}
	
	private int getUnitSeqNo(MfgCtrlDto mfgDC) {
		ProcessUnit processUnit = ServiceFactory.getDao(ProcessUnitDao.class).findBy(
				mfgDC.getChangeFormUnit().getId().getApprovedProcMaintId(), 
				mfgDC.getUnit().getUnitNo());
		//return unit sequence number from PVPMU1 if is not null, otherwise use PVUMX1
		return (processUnit!=null) ? 
				processUnit.getId().getUnitSeqNo() : 
					mfgDC.getUnit().getUnitSeqNo();
	}
	
	private void createMCOpMatrix(MfgCtrlDto mfgDC, Map<String, String> yearCodeMap) {
		//creating mc_op_matrix records
		//List to use while creating part
		Set<String> opSpecCodeMaskList = new HashSet<String>();
		List<UnitModelType> modelTypes = ServiceFactory.getDao(
				UnitModelTypeDao.class).findAllByMaintenanceId(mfgDC.getUnit());

		for (UnitModelType modelType : modelTypes) {
			String modelYearCode = yearCodeMap.get(mfgDC.getModelYear().toString().trim());
			if(StringUtils.isBlank(modelYearCode)) {
				//Model Year code not fount in 133/144 table
				getLogger().debug("Operation Matrix : year description & code : "+mfgDC.getModelYear()+" combination does not exists in 133/144 table");
			}
			else {
				//Model Year Code found
				String specCodeMask = modelYearCode.trim()+modelType.getId().getMtcModel()+modelType.getId().getMtcType()+"*";
				MCOperationMatrix opMatrix = new MCOperationMatrix();
				MCOperationMatrixId matrixId = new MCOperationMatrixId();
				matrixId.setPddaPlatformId(mfgDC.getPlatform().getPddaPlatformId());
				matrixId.setOperationName(mfgDC.getOperationName());
				matrixId.setOperationRevision(mfgDC.getNewOperation().getId().getOperationRevision());
				matrixId.setSpecCodeMask(specCodeMask);
				matrixId.setSpecCodeType(mfgDC.getSpecCodeType());
				opMatrix.setId(matrixId);
				ServiceFactory.getDao(MCOperationMatrixDao.class).save(opMatrix);
				opSpecCodeMaskList.add(specCodeMask);
			}
		}
		//Setting operation matrix in Data container
		mfgDC.setNewOperationSpecCodeMasks(opSpecCodeMaskList);
	}
	
	private void createMCPddaUnitRevTbx(MfgCtrlDto mfgDC) {
		// creating mc_pdda_unit_rev_tbx records 
		// (for PCF_IN, USR_IN, PPS_IN, CU_IN, PS_IN, SS_IN)
		for (ReportType rType : ReportType.values()) {
			MCPddaUnitRevision pddaUnitRev = new MCPddaUnitRevision();
			MCPddaUnitRevisionId unitRevd = new MCPddaUnitRevisionId();
			unitRevd.setOperationName(mfgDC.getOperationName());
			unitRevd.setOperationRevision(mfgDC.getNewOperation().getId().getOperationRevision());
			unitRevd.setPddaPlatformId(mfgDC.getPlatform().getPddaPlatformId());
			unitRevd.setPddaReport(rType.toString());
			unitRevd.setUnitNo(mfgDC.getChangeFormUnit().getUnitNo());
			pddaUnitRev.setId(unitRevd);
			pddaUnitRev.setApprovedProcessMaintenanceId(mfgDC.getChangeFormUnit().getProcess()
					.getMaintenanceId());
			pddaUnitRev.setApprovedUnitMaintenanceId(mfgDC.getChangeFormUnit().getUnit()
					.getMaintenanceId());
			if(!isReportType(mfgDC.getChangeFormUnit(),rType)) {
				//When report type is N, set earlier latest revision details
				MCPddaUnitRevision prevUnitRev = ServiceFactory.getDao(MCPddaUnitRevisionDao.class)
						.getLatestRevision(mfgDC.getOperationName(), mfgDC.getPlatform().getPddaPlatformId(), rType.toString());
				if(prevUnitRev!=null) {
					pddaUnitRev.setApprovedProcessMaintenanceId(prevUnitRev.getApprovedProcessMaintenanceId());
					pddaUnitRev.setApprovedUnitMaintenanceId(prevUnitRev.getApprovedUnitMaintenanceId());
					ServiceFactory.getDao(MCPddaUnitRevisionDao.class).save(pddaUnitRev);
				}
			}
			else {
				ServiceFactory.getDao(MCPddaUnitRevisionDao.class).save(pddaUnitRev);
			}
		}
	}
	
	private void addOperationInMfgCtrl(MfgCtrlDto mfgDC, Map<String, String> yearCodeMap) {
		//Creating MC_OP_TBX record
		createMCOperation(mfgDC);
		//Creating MC_PDDA_UNIT_TBX record
		createMCPddaUnit(mfgDC);
		//Creating mc_op_rev_tbx record
		createMCOperationRevision(mfgDC);
		//creating mc_op_rev_platform_tbx record
		createMCOpRevPlatform(mfgDC);
		//creating mc_op_matrix records
		createMCOpMatrix(mfgDC, yearCodeMap);
		//creating mc_pdda_unit_rev_tbx records 
		createMCPddaUnitRevTbx(mfgDC);	
	}
	
	private MCOperationPartRevision getDefaultMfgPart(MfgCtrlDto mfgDC, Map<String, Integer> partIdIndexMap) {
		//Create method to create parts
		String partId = ApplicationConstants.DEFAULT_MFG_PART_ID;
		//Adding part id number in map for further generation of part id
		partIdIndexMap.put(mfgDC.getOperationName(), 0);
		// Calculating Part Revision
		int partRev = mfgDC.getNewPartRev();
		// Creating mc_op_part_rev object 
		MCOperationPartRevision part = new MCOperationPartRevision();
		MCOperationPartRevisionId partRevId = new MCOperationPartRevisionId();
		partRevId.setOperationName(mfgDC.getOperationName());
		partRevId.setPartRevision(partRev);
		partRevId.setPartId(partId);
		part.setId(partRevId);
		//Get previous PART_DESC from MC_OP_PART_REV_TBX for standard approval
		//If null is returned or mass approval, get PART_DESC from PVUPM1
		String partDesc = getPartDescFromOpPartRev(mfgDC.getUnit().getBasePartNo());
		if (StringUtils.isEmpty(partDesc)) {
			partDesc = getPartDescFromPDDA(mfgDC.getUnit().getBasePartNo());
		}
		part.setPartDesc(partDesc);			
		part.setRevisionId(mfgDC.getMfgRevision().getId());
		part.setPartType(PartType.MFG);
		part.setPartCheck(PartCheck.DEFAULT);
		part.setPartNo(mfgDC.getUnit().getBasePartNo());
		String partMask = mfgDC.getPropertyBean().getPartMask();
		if(StringUtils.isNotEmpty(partMask)) {
			part.setPartMask(partMask);
		}
		part.setDeviceId("");
		part.setDeviceMsg("");
		if(mfgDC.isDeprecated()) {
			part.setDeprecated(new Timestamp(mfgDC.getCurrentTime()));
			part.setDeprecatedRevisionId(mfgDC.getMfgRevision().getId());
		}
		return part;
	}
	
	private String generatePartId(MfgCtrlDto mfgDC,
			Map<String, Integer> partIdIndexMap) {
		int partIdNum = 0;
		if(partIdIndexMap.containsKey(mfgDC.getOperationName())) {
			partIdNum = partIdIndexMap.get(mfgDC.getOperationName()) + 1;
		}
		partIdIndexMap.put(mfgDC.getOperationName(), partIdNum);
		return "A" + String.format("%04d", partIdNum);
	}
	
	private int generatePartRevision(String operationName) {
		// Calculating Part Revision
		int partRev = ServiceFactory.getDao(MCOperationPartRevisionDao.class)
				.getMaxRevisionForOperationPart(operationName);
		partRev = partRev + 1;
		return partRev;
	}
	
	private void createMfgParts(MfgCtrlDto mfgDC,
			Map<String, Integer> partIdIndexMap,  MfgViosPartDto viosMfgPartDC, UnitPart unitPart, 
			String uniquePartItemSectionCombination, Map<String, String> mfgPartsIdMap) {
		if(viosMfgPartDC != null) {
				MCViosMasterOperationPart viosMfgPart = viosMfgPartDC.getMasterOpPart();
				if(viosMfgPart != null) {
					//Generation part id
					String partId = generatePartId(mfgDC, partIdIndexMap);
					// Create MC Operation Part
					createMcOpPart(partId, mfgDC.getOperationName());
					//Getting part revision
					int partRev = mfgDC.getNewPartRev();
					// Creating mc_op_part_rev_tbx record
					MCOperationPartRevision part = new MCOperationPartRevision();
					MCOperationPartRevisionId partRevId = new MCOperationPartRevisionId();
					partRevId.setOperationName(mfgDC.getOperationName());
					partRevId.setPartRevision(partRev);
					partRevId.setPartId(partId);
					part.setId(partRevId);
					part.setRevisionId(mfgDC.getMfgRevision().getId());
					part.setPartDesc(unitPart.getId().getPartName());
					part.setPartType(viosMfgPart.getId().getPartType());
					if(viosMfgPart.getPartCheck() != null) {
						part.setPartCheck(viosMfgPart.getPartCheck());
					} else {
						part.setPartCheck(PartCheck.DEFAULT);
					}
					
					part.setPartMask(viosMfgPart.getPartMask());
					part.setPartNo(unitPart.getId().getPartNo());
					part.setPartSectionCode(unitPart.getId().getPartSectionCode());
					part.setPartItemNo(unitPart.getId().getPartItemNo());
					part.setPartMark(unitPart.getId().getPartMarkingNo());
					part.setMeasCount(unitPart.getId().getPartQty());
					part.setDeviceId(viosMfgPart.getDeviceId());
					part.setDeviceMsg(viosMfgPart.getDeviceMsg());
					part.setPartView(viosMfgPart.getPartView());
					part.setPartProcessor(viosMfgPart.getPartProcessor());
					if(mfgDC.isDeprecated()) {
						part.setDeprecated(new Timestamp(mfgDC.getCurrentTime()));
						part.setDeprecatedRevisionId(mfgDC.getMfgRevision().getId());
					}
					ServiceFactory.getDao(MCOperationPartRevisionDao.class).save(part);
					//Setting mfg parts id map
					String mfgPartValue = (mfgPartsIdMap.containsKey(uniquePartItemSectionCombination))
							? mfgPartsIdMap.get(uniquePartItemSectionCombination) : partRev+","+partId;
					mfgPartsIdMap.put(uniquePartItemSectionCombination, mfgPartValue);
					//Creating part checkers
					if(viosMfgPartDC.getMasterOpPartCheckers() != null && !viosMfgPartDC.getMasterOpPartCheckers().isEmpty()) {
						List<MCPartChecker> partCheckers = getPartCheckerBy(viosMfgPartDC.getMasterOpPartCheckers(), partId, mfgDC.getNewOperation().getId().getOperationRevision());
						if(partCheckers.size() > 0)
							ServiceFactory.getDao(MCPartCheckerDao.class).insertAll(partCheckers);
					}
					//Create measurements using master
					//Creating measurements
					createMeasurements(mfgDC, partId, partRev, mfgDC.getMfgCtrlViosDto().getMasterOpMeasList());
					//Create measurement checkers
					createMeaasurementCheckers(mfgDC, partId, mfgDC.getMfgCtrlViosDto().getMasterOpMeasCheckerList());
				}
		 }
	}
	
	private void createMeaasurementCheckers(MfgCtrlDto mfgDC,
			String partId, List<MCViosMasterOperationMeasurementChecker> masterOpMeasCheckerList) {
		if(masterOpMeasCheckerList!=null) {
			for (MCViosMasterOperationMeasurementChecker masterOpMeasChecker : masterOpMeasCheckerList) {
				createMeaasurementChecker(mfgDC.getOperationName(), 
						mfgDC.getNewOperation().getId().getOperationRevision(), 
						partId, masterOpMeasChecker);
			}
		}
	}

	private void createMeaasurementChecker(String operationName,
			int operationRevision, String partId,
			MCMeasurementChecker measChecker) {
		MCMeasurementChecker mfgMeasChecker = new MCMeasurementChecker();
		MCMeasurementCheckerId mfgMeasCheckerId = new MCMeasurementCheckerId();
		mfgMeasCheckerId.setOperationName(operationName);
		mfgMeasCheckerId.setOperationRevision(operationRevision);
		mfgMeasCheckerId.setPartId(partId);
		mfgMeasCheckerId.setMeasurementSeqNumber(measChecker.getId().getMeasurementSeqNumber());
		mfgMeasCheckerId.setCheckPoint(measChecker.getId().getCheckPoint());
		mfgMeasCheckerId.setCheckSeq(measChecker.getId().getCheckSeq());
		mfgMeasCheckerId.setCheckName(measChecker.getId().getCheckName());
		mfgMeasChecker.setId(mfgMeasCheckerId);
		mfgMeasChecker.setChecker(measChecker.getChecker());
		mfgMeasChecker.setReactionType(measChecker.getReactionType());
		ServiceFactory.getDao(MCMeasurementCheckerDao.class).save(mfgMeasChecker);
	}
	
	private void createMeaasurementChecker(String operationName,
			int operationRevision, String partId, MCViosMasterOperationMeasurementChecker masterOpMeasChecker) {
		MCMeasurementChecker mfgMeasChecker = new MCMeasurementChecker();
		MCMeasurementCheckerId mfgMeasCheckerId = new MCMeasurementCheckerId();
		mfgMeasCheckerId.setOperationName(operationName);
		mfgMeasCheckerId.setOperationRevision(operationRevision);
		mfgMeasCheckerId.setPartId(partId);
		mfgMeasCheckerId.setMeasurementSeqNumber(masterOpMeasChecker.getId().getMeasurementSeqNum());
		mfgMeasCheckerId.setCheckPoint(masterOpMeasChecker.getId().getCheckPoint());
		mfgMeasCheckerId.setCheckSeq(masterOpMeasChecker.getId().getCheckSeq());
		mfgMeasCheckerId.setCheckName(masterOpMeasChecker.getId().getCheckName());
		mfgMeasChecker.setId(mfgMeasCheckerId);
		mfgMeasChecker.setChecker(masterOpMeasChecker.getChecker());
		mfgMeasChecker.setReactionType(masterOpMeasChecker.getReactionType());
		ServiceFactory.getDao(MCMeasurementCheckerDao.class).save(mfgMeasChecker);
	}

	private void createPartChecker(String operationName, int operationRevision,
			String partId, MCPartChecker partChecker) {
		MCPartChecker mfgPartChecker = new MCPartChecker();
		MCPartCheckerId mfgPartCheckerId = new MCPartCheckerId();
		mfgPartCheckerId.setOperationName(operationName);
		mfgPartCheckerId.setOperationRevision(operationRevision);
		mfgPartCheckerId.setPartId(partId);
		mfgPartCheckerId.setCheckPoint(partChecker.getId().getCheckPoint());
		mfgPartCheckerId.setCheckSeq(partChecker.getId().getCheckSeq());
		mfgPartChecker.setId(mfgPartCheckerId);
		mfgPartChecker.setChecker(partChecker.getChecker());
		mfgPartChecker.setCheckName(partChecker.getCheckName());
		mfgPartChecker.setReactionType(partChecker.getReactionType());
		ServiceFactory.getDao(MCPartCheckerDao.class).save(mfgPartChecker);
	}

	private void createMeasurements(MfgCtrlDto mfgDC, String partId,
			int partRev, List<MCViosMasterOperationMeasurement> masterOpMeasList) {
		if(masterOpMeasList!=null) {
			for (MCViosMasterOperationMeasurement masterOpMeas : masterOpMeasList) {
				createMeasurement(mfgDC.getOperationName(), partId, partRev, masterOpMeas);
			}
		}
	}

	private void createMeasurement(String operationName, String partId,
			int partRev, MCOperationMeasurement opMeas) {
		MCOperationMeasurement meas = new MCOperationMeasurement();
		MCOperationMeasurementId measId = new MCOperationMeasurementId();
		measId.setOperationName(operationName);
		measId.setPartId(partId);
		measId.setPartRevision(partRev);
		measId.setOpMeasSeqNum(opMeas.getId().getMeasurementSeqNum());
		meas.setId(measId);
		meas.setCheck(opMeas.getCheck());
		meas.setDeviceId(opMeas.getDeviceId());
		meas.setDeviceMsg(opMeas.getDeviceMsg());
		meas.setMaxAttempts(opMeas.getMaxAttempts());
		meas.setMaxLimit(opMeas.getMaxLimit());
		meas.setMinLimit(opMeas.getMinLimit());
		meas.setView(opMeas.getView());
		meas.setProcessor(opMeas.getProcessor());
		meas.setTime(opMeas.getTime());
		meas.setType(opMeas.getType());
		ServiceFactory.getDao(MCOperationMeasurementDao.class).save(meas);
	}
	
	private void createMeasurement(String operationName, String partId,
			int partRev, MCViosMasterOperationMeasurement masterOpMeas) {
		MCOperationMeasurement meas = new MCOperationMeasurement();
		MCOperationMeasurementId measId = new MCOperationMeasurementId();
		measId.setOperationName(operationName);
		measId.setPartId(partId);
		measId.setPartRevision(partRev);
		measId.setOpMeasSeqNum(masterOpMeas.getId().getMeasurementSeqNum());
		meas.setId(measId);
		meas.setCheck(masterOpMeas.getCheck());
		meas.setDeviceId(masterOpMeas.getDeviceId());
		meas.setDeviceMsg(masterOpMeas.getDeviceMsg());
		meas.setMaxAttempts(masterOpMeas.getMaxAttempts());
		meas.setMaxLimit(masterOpMeas.getMaxLimit());
		meas.setMinLimit(masterOpMeas.getMinLimit());
		meas.setView(masterOpMeas.getView());
		meas.setProcessor(masterOpMeas.getProcessor());
		meas.setTime(masterOpMeas.getTime());
		meas.setType(masterOpMeas.getType());
		ServiceFactory.getDao(MCOperationMeasurementDao.class).save(meas);
	}
	
	

	private void createReferencePart(MfgCtrlDto mfgDC,
			Map<String, Integer> partIdIndexMap, UnitPart unitPart, 
			int partRev, String partId) {
		// Create MC Operation Part
		createMcOpPart(partId, mfgDC.getOperationName());
		// Creating mc_op_part_rev_tbx record
		MCOperationPartRevision part = new MCOperationPartRevision();
		MCOperationPartRevisionId partRevId = new MCOperationPartRevisionId();
		partRevId.setOperationName(mfgDC.getOperationName());
		partRevId.setPartRevision(partRev);
		partRevId.setPartId(partId);
		part.setId(partRevId);
		part.setRevisionId(mfgDC.getMfgRevision().getId());
		part.setPartDesc(unitPart.getId().getPartName());
		part.setPartType(PartType.REFERENCE);
		part.setPartCheck(PartCheck.DEFAULT);
		part.setPartNo(unitPart.getId().getPartNo());
		part.setPartSectionCode(unitPart.getId().getPartSectionCode());
		part.setPartItemNo(unitPart.getId().getPartItemNo());
		part.setPartMark(unitPart.getId().getPartMarkingNo());
		part.setMeasCount(unitPart.getId().getPartQty());
		part.setDeviceId("");
		part.setDeviceMsg("");
		if(mfgDC.isDeprecated()) {
			part.setDeprecated(new Timestamp(mfgDC.getCurrentTime()));
			part.setDeprecatedRevisionId(mfgDC.getMfgRevision().getId());
		}
		ServiceFactory.getDao(MCOperationPartRevisionDao.class).save(part);
	}

	private void createPartMatrix(MfgCtrlDto mfgDC, String partId,
			int partRev, String specCodeMask) {
		MCOperationPartMatrix partMatrix = new MCOperationPartMatrix();
		MCOperationPartMatrixId partMatrixId = new MCOperationPartMatrixId();
		partMatrixId.setOperationName(mfgDC.getOperationName());
		partMatrixId.setPartRevision(partRev);
		partMatrixId.setPartId(partId);
		partMatrixId.setSpecCodeType(mfgDC.getSpecCodeType());
		partMatrixId.setSpecCodeMask(specCodeMask);
		partMatrix.setId(partMatrixId);
		ServiceFactory.getDao(MCOperationPartMatrixDao.class).save(partMatrix);
	}

	private void createReferenceParts(MfgCtrlDto mfgDC,
			Map<String, Integer> partIdIndexMap, Map<String, String> yearCodeMap, boolean doCreateMfgPart) {
		Integer maintenanceIdForParts = mfgDC.getChangeFormUnit().getId().getApprovedUnitMaintId();
		if (maintenanceIdForParts!=null) {
			Map<String,String> partIdMap = new HashMap<String,String>();
			Map<String,String> mfgPartsIdMap = new HashMap<String,String>(); //Map<uniquePartItemSectionCombination, (MFG_PART_ID,MFG_PART_REV)>
			List<UnitPart> unitParts = ServiceFactory.getDao(UnitPartDao.class)
					.findAllByMaintenanceId(maintenanceIdForParts);
			if(unitParts!=null && !unitParts.isEmpty()) {
				for (UnitPart unitPart : unitParts) {
					// Unique Part Combination for MFG parts: Part No
					UnitPartId unitPartId = unitPart.getId();
					
					String uniquePartItemSectionCombination = StringUtils.trimToEmpty(unitPartId.getPartNo())
							+"_"+StringUtils.trimToEmpty(unitPartId.getPartSectionCode())
							+"_"+StringUtils.trimToEmpty(unitPartId.getPartItemNo());
				
					String uniqueMasterPartCombination = StringUtils.trimToEmpty(unitPartId.getPartNo());
					
					// Creating part and part revisions for unique part combination
					if(!partIdMap.containsKey(uniquePartItemSectionCombination) ){
						// Generation part id
						String partId = generatePartId(mfgDC, partIdIndexMap);
						// Getting part revision
						int partRev = mfgDC.getNewPartRev();
						// Creating reference part
						createReferencePart(mfgDC, partIdIndexMap, unitPart, partRev, partId);
						partIdMap.put(uniquePartItemSectionCombination, partRev+","+partId);
						
						if(doCreateMfgPart) {
							
							
							//Get Master Part Data here
							Map<String, MfgViosPartDto> mfgViosPartsMap =
									mfgDC.getMfgCtrlViosDto().getMasterPartsMap();
							if(mfgViosPartsMap != null && mfgViosPartsMap.containsKey(uniqueMasterPartCombination)) {
								//Active MFG part is present
								createMfgParts(mfgDC, partIdIndexMap, mfgViosPartsMap.get(uniqueMasterPartCombination),
										unitPart, uniquePartItemSectionCombination, mfgPartsIdMap);
							}
						}
						
					}
					String modelYearCode = yearCodeMap.get(mfgDC.getModelYear().toString().trim());
					if(StringUtils.isBlank(modelYearCode)) {
						//Model Year code not found in 133/144 table
						getLogger().debug("Operation Matrix : year description & code : "+mfgDC.getModelYear()+" combination does not exists in 133/144 table");
					}else {
						// Creating mc_op_part_matrix records
						String specCodeMask = modelYearCode.trim()+unitPartId.getMtcModel()+unitPartId.getMtcType()+"*";
						createPartMatrix(mfgDC, specCodeMask, uniquePartItemSectionCombination, partIdMap, false); 
						if(doCreateMfgPart) {
							createPartMatrix(mfgDC, specCodeMask, uniquePartItemSectionCombination, mfgPartsIdMap, true);
						}
					}
				}
			}
		}
	}
	
	private void createPartMatrix(MfgCtrlDto mfgDC, String specCodeMask,
			String uniquePartItemSectionCombination, Map<String, String> map, boolean isMFGPart) {
		//Creating Reference Part
		if(map.containsKey(uniquePartItemSectionCombination)) {
			String partDetails[] = map.get(uniquePartItemSectionCombination).split(",");
			int partRev = Integer.parseInt(partDetails[0]);
			String partId = partDetails[1];
			createPartMatrix(mfgDC, partId, partRev, specCodeMask);
			
			if(isMFGPart)
				mfgDC.getNewOperationSpecCodeMasks().remove(specCodeMask);
		}
	}
	
	private void createDefaultMfgPart(MfgCtrlDto mfgDC,
			MCOperationPartRevision defaultMfgPart) {
		String partId=defaultMfgPart.getId().getPartId();
		int partRev=defaultMfgPart.getId().getPartRevision();
		//Checking if active mfg part is present
		List<MCPartChecker> partCheckers = null;
		//Get Master Measurements
		List<MCViosMasterOperationMeasurement> masterOpMeasList = mfgDC.getMfgCtrlViosDto().getMasterOpMeasList();
		List<MCViosMasterOperationMeasurementChecker> masterOpMeasCheckerList = mfgDC.getMfgCtrlViosDto().getMasterOpMeasCheckerList();
		
		Map<String, MfgViosPartDto> mfgViosPartsMap = mfgDC.getMfgCtrlViosDto().getMasterPartsMap();
		String uniquePartCombination = defaultMfgPart.getId().getOperationName();
		
		if(mfgViosPartsMap != null && mfgViosPartsMap.containsKey(uniquePartCombination)) {
			MfgViosPartDto defaultMfgPartDC = mfgViosPartsMap.get(uniquePartCombination);
			if(defaultMfgPartDC != null) {
				if(defaultMfgPartDC != null && defaultMfgPartDC.getMasterOpPart()!=null) {
					//Master Default MFG part found
					MCViosMasterOperationPart masterOpPart = defaultMfgPartDC.getMasterOpPart();
					if(StringUtils.isNotEmpty(masterOpPart.getPartMask())) {
						defaultMfgPart.setPartMask(masterOpPart.getPartMask());
					}
					if(masterOpPart.getPartCheck() != null) {
						defaultMfgPart.setPartCheck(masterOpPart.getPartCheck());
					}else {
						defaultMfgPart.setPartCheck(PartCheck.DEFAULT);
					}
					defaultMfgPart.setDeviceId(StringUtils.EMPTY);
					defaultMfgPart.setDeviceMsg(StringUtils.EMPTY);
					defaultMfgPart.setPartView(StringUtils.EMPTY);
					defaultMfgPart.setPartProcessor(StringUtils.EMPTY);
					if(defaultMfgPartDC.getMasterOpPartCheckers() != null && !defaultMfgPartDC.getMasterOpPartCheckers().isEmpty())
						partCheckers = getPartCheckerBy(defaultMfgPartDC.getMasterOpPartCheckers(), partId, mfgDC.getNewOperation().getId().getOperationRevision());
				}
			}
		}
		
		// Create MC Operation Part
		createMcOpPart(partId, mfgDC.getOperationName());
		// Creating mc_op_part_rev_tbx record
		ServiceFactory.getDao(MCOperationPartRevisionDao.class).save(defaultMfgPart);
		// Creating mc_op_part_matrix
		if(mfgDC.getNewOperationSpecCodeMasks()!=null) {
			for(String specCodeMask: mfgDC.getNewOperationSpecCodeMasks()) {
				createPartMatrix(mfgDC, partId, partRev, specCodeMask);
			}
		}
		//Creating part checkers
		if(partCheckers!=null) {
			//createPartCheckers(mfgDC, partId, partCheckers);
			ServiceFactory.getDao(MCPartCheckerDao.class).saveAll(partCheckers);
		}
		//Creating measurements
		if(masterOpMeasList!=null) {
			createMeasurements(mfgDC, partId, partRev, masterOpMeasList);
		}
		//Create measurement checkers
		if(masterOpMeasCheckerList!=null) {
			createMeaasurementCheckers(mfgDC, partId, masterOpMeasCheckerList);
		}
	}

	private void addOpPartsInMfgCtrl(MfgCtrlDto mfgDC,
			Map<String, String> yearCodeMap) {
		boolean doCreateMfgPart = false;
		if(getPartType(mfgDC.getNewOperation().getType()).equals(PartType.MFG)) {
			doCreateMfgPart = true;
		}
		Map<String, Integer> partIdIndexMap = new HashMap<String, Integer>();
		//Getting default MFG part
		MCOperationPartRevision defaultMfgPart = null;
		if(doCreateMfgPart) {
			defaultMfgPart = getDefaultMfgPart(mfgDC, partIdIndexMap);
		}
		//Create reference parts
		createReferenceParts(mfgDC, partIdIndexMap, yearCodeMap, doCreateMfgPart);
		if(defaultMfgPart!=null) {
			//Create Default MFG Part
			createDefaultMfgPart(mfgDC, defaultMfgPart);
		}
	}
	
	public void createMfgCtrlRecords(long revId, Map<String, String> yearCodeMap, String userId) {
		List<ChangeFormUnit> chgFrmUnits = ServiceFactory.getDao(
				ChangeFormUnitDao.class).findAllChangeFormUnitIdForRev(revId);
			if(chgFrmUnits!=null) {
				for (ChangeFormUnit unit : chgFrmUnits) {
					addMCRecord(unit, revId, yearCodeMap, userId);
				}
			}
			ServiceFactory.getDao(MCRevisionDao.class).setRevisionStatus(revId, RevisionStatus.DEVELOPING);
	}

	public void createMfgCtrlRecordsForOneClick(long revId, Map<String, String> yearCodeMap, String userId) {
		List<ChangeFormUnit> chgFrmUnits = ServiceFactory.getDao(
				ChangeFormUnitDao.class).findAllChangeFormUnitIdForRev(revId);
			if(chgFrmUnits!=null) {
				for (ChangeFormUnit unit : chgFrmUnits) {
					addMCRecord(unit, revId, yearCodeMap, userId);
				}
			}
	}
	
	@Transactional
	public void addMCRecord(ChangeFormUnit cfu, long revId, Map<String, String> yearCodeMap, String userId) {

		//Preparing Data Container
		MfgCtrlDto mfgDC = prepareDC(cfu, revId, userId);
		
		//Creating Operation data in Manufacturing Control
		addOperationInMfgCtrl(mfgDC, yearCodeMap);
		
		//Creating Operation parts in Manufacturing Control
		addOpPartsInMfgCtrl(mfgDC, yearCodeMap);
		
		//Clearing Data Container
		clearMfgDC(mfgDC);
	}

	private void clearMfgDC(MfgCtrlDto mfgDC) {
		mfgDC.clear();
		mfgDC=null;
	}
	
	private PartType getPartType(OperationType opType) {
		if(opType!=null) {
			//return part type as MFG if operation type is scan or measurement related
			if(opType.equals(OperationType.GALC_SCAN)
					 || opType.equals(OperationType.GALC_MEAS)
					 || opType.equals(OperationType.GALC_MEAS_MANUAL)
					 || opType.equals(OperationType.GALC_SCAN_WITH_MEAS)
					 || opType.equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL)
					 || opType.equals(OperationType.GALC_MADE_FROM)) {
				return PartType.MFG;
			}
			else {
				//return part type as REFERENCE if operation type is not null
				return PartType.REFERENCE;
			}
			
		}
		return PartType.NONE;
	}

	private void createMcOpPart(String partId, String operationName) {
		// Creating mc_op_part_tbx record if it does not exist
		MCOperationPartId opPartId = new MCOperationPartId();
		opPartId.setOperationName(operationName);
		opPartId.setPartId(partId);
		MCOperationPart opPart = ServiceFactory.getDao(MCOperationPartDao.class).findByKey(opPartId);
		if(opPart == null) {
			//Operation Part Does not exist. Need to create a record
			opPart = new MCOperationPart();
			opPart.setId(opPartId);
			opPart.setPartRev(0);
			ServiceFactory.getDao(MCOperationPartDao.class).save(opPart);
		}
	}
	
	private boolean isReportType(ChangeFormUnit cfu,ReportType rtype) {
		String typeStr = "";
		switch(rtype){
			case CU: typeStr = cfu.getCuIn(); 
				break;
			case PCF: typeStr =cfu.getPcfIn(); 
				break;
			case PPS: typeStr = cfu.getPpsIn(); 
				break;
			case PS: typeStr = cfu.getPsIn(); 
				break;
			case SS: typeStr  = cfu.getSsIn(); 
				break;
			case USR: typeStr  = cfu.getUsrIn(); 
				break;
		}
		return "Y".equals(typeStr);
	}

	@SuppressWarnings("unchecked")
	public List<PddaProcess> getProcessDetailForChangeForm(Integer changeFormId) {
		List<PddaProcess> processList = new ArrayList<PddaProcess>();
		Parameters params = Parameters.with("1", changeFormId);
		List<Object[]> pddaProcessList = findResultListByNativeQuery(GET_PROCESS_DETAILS, params);
		
		for(Object[] process : pddaProcessList){
			if(process!=null && process.length == 2) {
				String procNo = (process[0]!=null)?process[0].toString().trim():"";
				String procName = (process[1]!=null)?process[1].toString().trim():"";
				processList.add(new PddaProcess(procNo, procName));
			}
		}
		return processList;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getModelYearCodeMap(String productType) {
		Map<String, String> modelYrCodeMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(productType)) {
			String selectQry = "";
			if(productType.trim().equalsIgnoreCase(FRAME)) {
				selectQry = "SELECT DISTINCT MODEL_TYPE_CODE, MODEL_CODE, MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE FROM GALADM.GAL144TBX";
			} else if(productType.trim().equalsIgnoreCase(ENGINE)) {
				selectQry = "SELECT DISTINCT MODEL_TYPE_CODE, MODEL_CODE, MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE FROM GALADM.GAL133TBX";
			}
			if(!selectQry.equals("")) {
				List<Object[]> modelList = findResultListByNativeQuery(selectQry, null);
				if(modelList != null) {
					for(Object[] model: modelList) {
						String modelTypeCode = (model[0]!=null)?model[0].toString().trim():"";
						String modelCode = (model[1]!=null)?model[1].toString().trim():"";
						String modelYr = (model[2]!=null)?model[2].toString().trim():"";
						String modelYrCode = (model[3]!=null)?model[3].toString().trim():"";
						modelYrCodeMap.put(modelCode+"-"+modelTypeCode+"-"+modelYr, modelYrCode);
					}
				}
			}
		}
		return modelYrCodeMap;
	}
	
	public void approveRevision(long revId, String userId) {
		long time = System.currentTimeMillis();
		List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(revId);
		if(platforms!=null) {
			//Creating sets for mc_op_tbx and mc_op_part_tbx
			Set<String> revOperations = new HashSet<String>();
			Map<String, Set<String>> revOperationParts = new HashMap<String, Set<String>>();
			MCOperationRevisionDao mcOpRevDao = ServiceFactory.getDao(MCOperationRevisionDao.class);
			MCOperationPartRevisionDao mcOpPartRevDao = ServiceFactory.getDao(MCOperationPartRevisionDao.class);
			for(MCPddaPlatform  platform: platforms) {
				//Approving Platform
				platform.setApproved(new Timestamp(time));
				platform.setApproverNo(userId);
				platform = ServiceFactory.getDao(MCPddaPlatformDao.class).update(platform);
				
				//Approving PDDA Unit
				List<MCPddaUnit> pddaUnits = ServiceFactory.getDao(MCPddaUnitDao.class).findBy(platform.getPddaPlatformId(), platform.getRevId());
				if(pddaUnits!=null) {
					for(MCPddaUnit pddaUnit : pddaUnits) {
						pddaUnit.setApproved(new Timestamp(time));
						pddaUnit.setApproverNo(userId);
						ServiceFactory.getDao(MCPddaUnitDao.class).update(pddaUnit);
					}
				}
				
				//Getting operations
				List<MCOperationRevision> operationRevList = mcOpRevDao.findAllBy(platform.getPddaPlatformId(), platform.getRevId());
				Set<String> operationNameSet = new HashSet<String>();
				//Approving Operations
				if(operationRevList!=null) {
					for(MCOperationRevision opRev : operationRevList) {
						opRev.setApproved(new Timestamp(time));
						mcOpRevDao.update(opRev);
						operationNameSet.add(opRev.getId().getOperationName());
					}
				}
				//Getting Operation part revisions
				for(String operationName : operationNameSet) {
					//setting operation name in revision operation set
					revOperations.add(operationName);
					List<MCOperationPartRevision> operationPartRevList = mcOpPartRevDao
							.findAllBy(operationName, platform.getRevId());
					//Approving Operation Parts
					if(operationPartRevList!=null && !operationPartRevList.isEmpty()) {
						//Parts for this operation in entire revision
						Set<String> revParts = (revOperationParts.containsKey(operationName))?
								revOperationParts.get(operationName):new HashSet<String>();
						for(MCOperationPartRevision operationPartRev: operationPartRevList) {
							MCOperationPartRevisionId opPartRevId = operationPartRev.getId();
							String partId = opPartRevId.getPartId();
							operationPartRev.setApproved(new Timestamp(time));
							operationPartRev = mcOpPartRevDao.update(operationPartRev);
							//Adding part id in revision operation part 
							revParts.add(partId);
						}
						revOperationParts.put(operationName, revParts);
					}
				}
			}
			//Updating mc_op_tbx
			MCOperationDao mcOpDao = ServiceFactory.getDao(MCOperationDao.class);
			for(String operationName : revOperations) {
				//Setting Max approved operation revision
				int maxAprvdOpRev = mcOpRevDao
						.getMaxAprvdRevisionForOperation(operationName);
				//Getting Operation
				MCOperation operation = mcOpDao
						.findByKey(operationName);
				//Update Operation with active operation revision
				operation.setRevision(maxAprvdOpRev);
				mcOpDao.update(operation);
			}
			//Updating mc_op_part_tbx
			MCOperationPartDao mcOpPartDao = ServiceFactory.getDao(MCOperationPartDao.class);
			for(String operationName : revOperationParts.keySet()) {
				for (String partId : revOperationParts.get(operationName)) {
					//Setting Max approved operation part revision
					int maxAprvdOpPartRev = mcOpPartRevDao
							.getMaxAprvdRevisionForOperationPart(operationName, partId);
					//Getting Operation part
					MCOperationPartId opPartId = new MCOperationPartId();
					opPartId.setOperationName(operationName);
					opPartId.setPartId(partId);
					MCOperationPart operationPart = mcOpPartDao.findByKey(opPartId);
					
					//Update Operation Part with active part revision
					operationPart.setPartRev(maxAprvdOpPartRev);
					mcOpPartDao.update(operationPart);
				}
			}
		}
	}

	public RevisionType getRevisionType(int changeFormId) {
		List<ChangeFormUnit> chgFrmUnits = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllForChangeForm(changeFormId);
		if(chgFrmUnits!=null) {
			String processAction ="";
			String unitAction="";
			Set<String> actionSet = new HashSet<String>();
			Set<String> stdAprdActionSet = new HashSet<String>(Arrays.asList("A", "E", "D", "U"));
			for(ChangeFormUnit chgFrmUnit:chgFrmUnits) {
				processAction = chgFrmUnit.getProcess().getAction();
				processAction = (StringUtils.isNotEmpty(processAction))?processAction.trim().toUpperCase():"";
				actionSet.add(processAction);
				
				unitAction = chgFrmUnit.getUnit().getAction();
				unitAction = (StringUtils.isNotEmpty(unitAction))?unitAction.trim().toUpperCase():"";
				actionSet.add(unitAction);
				
				if(stdAprdActionSet.contains(processAction) || stdAprdActionSet.contains(unitAction)) {
					return RevisionType.PDDA_STD;
				}
			}
			
			if(actionSet.size() == 1) {
				if(actionSet.iterator().next().equalsIgnoreCase("M")) {
					return RevisionType.PDDA_MASS;
				}
			}
		}
		return RevisionType.INVALID;
	}

	public ChangeForm getChangeFormForPddaPlatform(long pddaPlatformId) {
		MCPddaPlatform platform = ServiceFactory.getDao(MCPddaPlatformDao.class).findByKey(Long.valueOf(pddaPlatformId).intValue());
		MCRevision revision =  ServiceFactory.getDao(MCRevisionDao.class).findByKey(platform.getRevId());
		List<ChangeForm> changForms = ServiceFactory.getDao(ChangeFormDao.class).getChangeFormsForRevId(platform.getRevId());
		if(changForms!=null && !changForms.isEmpty()) {
			for(ChangeForm changeForm:changForms) {
				//Verification of Platform
				if(verifyPlatform(changeForm, platform)) {
					List<PddaProcess> processList= getProcessDetailForChangeForm(changeForm.getId());
					if(processList!=null) {
						for(PddaProcess process: processList) {
							//Verification of ASM Process Number
							if(process.getAsmProcNumber().equalsIgnoreCase(platform.getAsmProcessNo())) {
								return changeForm;
							}
						}
					}
				}
			}
		}
		else if(revision.getType()!=null && revision.getType().equalsIgnoreCase(RevisionType.PLATFORM_CHG.getRevType())){
			//If Revision Type is 'PLATFORM CHG', only then perform recursion
			long deprRevId = platform.getDeprecatedRevId();
			if(deprRevId > 0.0) {
				MCPddaPlatform parent = ServiceFactory
						.getDao(MCPddaPlatformDao.class).getPDDAPlatformForOperation(
								platform.getPlantLocCode(), platform.getDeptCode(),
								platform.getModelYearDate(), platform.getProductScheduleQty(),
								platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
								platform.getAsmProcessNo(), deprRevId);
				return getChangeFormForPddaPlatform(parent.getPddaPlatformId());
			}
		}
		return null;
	}

	public List<MCPendingProcessDto> getPendingProcesses() {
		return findAllByNativeQuery(GET_PENDING_PROCESS, null, MCPendingProcessDto.class);
	}
	
	public List<MCPendingProcessDto> getPendingProcesses(long revId) {
		String query = GET_PENDING_PROCESS + " AND PC.REV_ID = " + revId;
		return findAllByNativeQuery(query, null, MCPendingProcessDto.class);
	}
	
	@Transactional
	public void performApproval(long revId, String userId) {
		//MC Revision Type
		MCRevision revision = ServiceFactory.getDao(MCRevisionDao.class).findByKey(revId);
		String revisionType = revision.getType();
		revisionType = revisionType!=null?revisionType:"";
		
		if(revisionType.equalsIgnoreCase(RevisionType.PLATFORM_CHG.getRevType())) {
			List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(revId);
			if(platforms!=null && platforms.size() > 0) {
				//For Revision type 'Platform change', there will always be only one platform
				MCPddaPlatform platform = platforms.iterator().next();
				if(platform.getDeprecatedRevId() > 0) {
					//Deprecation
					MCPddaPlatform parent = ServiceFactory
							.getDao(MCPddaPlatformDao.class).getPDDAPlatformForOperation(
									platform.getPlantLocCode(), platform.getDeptCode(),
									platform.getModelYearDate(), platform.getProductScheduleQty(),
									platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
									platform.getAsmProcessNo(), platform.getDeprecatedRevId());
					
					//Deprecating parent platform
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					parent.setDeprecated(timestamp);
					parent.setDeprecaterNo(userId);
					parent = ServiceFactory.getDao(MCPddaPlatformDao.class).update(parent);
					
					//Deprecating parent operations
					//deprecateOperations(parent.getPddaPlatformId(), userId, revId);
					
					//Fetching List of all platforms with same platform details
					List<MCPddaPlatform> listOfPlatforms = ServiceFactory
							.getDao(MCPddaPlatformDao.class).getAllAprvdPDDAPlatformExceptRev(
									platform.getPlantLocCode(), platform.getDeptCode(),
									platform.getModelYearDate(), platform.getProductScheduleQty(),
									platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
									platform.getAsmProcessNo(), revId);
					
					if(listOfPlatforms!=null){
						for(MCPddaPlatform deprPlatform: listOfPlatforms) {
							long deprRevId = deprPlatform.getRevId();
							int deprPlatformId = deprPlatform.getPddaPlatformId();
							if(deprRevId != revId) {
								//Deprecate PDDA Units
								deprecatePddaUnits(deprPlatformId, deprRevId, userId, revId);
								//Deprecating Operations
								Set<String> operationNames = deprecateOperations(deprPlatformId, deprRevId, revId);
								for(String operationName : operationNames) {
									//Deprecating Operation Parts
									deprecateOperationParts(operationName, deprRevId, revId);
								}
							}
						}
					}
					
					//Approval
					approveRevision(revId, userId);
				}
			}
		}
		else if(revisionType.equalsIgnoreCase(RevisionType.PDDA_MASS.getRevType())) {
			//Deprecation
			deprecateMassApproval(revId, userId);
			//Approval
			approveRevision(revId, userId);
		}
		else if(revisionType.equalsIgnoreCase(RevisionType.PDDA_STD.getRevType())) {
			//Deprecation
			deprecateStdApproval(revId, userId);
			//Approval
			approveRevision(revId, userId);
		}
		//Setting status to approved
		revision.setStatus(RevisionStatus.APPROVED.getRevStatus());
		ServiceFactory.getDao(MCRevisionDao.class).update(revision);
	}
	
	public void deprecateOperations(int platformId, String userId, long deprRevId) {
		//Getting Platform
		MCPddaPlatform platform = ServiceFactory.getDao(MCPddaPlatformDao.class).findByKey(platformId);
		//Deprecating PDDA Units
		deprecatePddaUnits(platform.getPddaPlatformId(), platform.getRevId(), userId, deprRevId);
		//Deprecating Operations
		Set<String> operationNames = deprecateOperations(platform.getPddaPlatformId(), platform.getRevId(), deprRevId);
		for(String operationName : operationNames) {
			//Deprecating Operation Parts
			deprecateOperationParts(operationName, platform.getRevId(), deprRevId);
		}
	}
	
	public void deprecateMassApproval(long revId, String userId) {
		List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(revId);
		if(platforms!=null) {
			for(MCPddaPlatform platform: platforms) {
				//Fetching List of all platforms with same platform details
				List<MCPddaPlatform> listOfPlatforms = ServiceFactory
						.getDao(MCPddaPlatformDao.class).getAllAprvdPDDAPlatformExceptRev(
								platform.getPlantLocCode(), platform.getDeptCode(),
								platform.getModelYearDate(), platform.getProductScheduleQty(),
								platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
								platform.getAsmProcessNo(), revId);
				
				if(listOfPlatforms!=null){
					for(MCPddaPlatform deprPlatform: listOfPlatforms) {
						long deprRevId = deprPlatform.getRevId();
						int deprPlatformId = deprPlatform.getPddaPlatformId();
						if(deprRevId != revId) {
							//Deprecating Platforms
							deprecatePlatform(deprPlatform, revId, userId);
							//Deprecate PDDA Units
							deprecatePddaUnits(deprPlatformId, deprRevId, userId, revId);
							//Deprecating Operations
							Set<String> operationNames = deprecateOperations(deprPlatformId, deprRevId, revId);
							for(String operationName : operationNames) {
								//Deprecating Operation Parts
								deprecateOperationParts(operationName, deprRevId, revId);
							}
						}
					}
				}
			}
		}
	}
	
	public void deprecateStdApproval(long revId, String userId) {
		List<MCPddaPlatform> platforms = ServiceFactory.getDao(MCPddaPlatformDao.class).getPlatformsForRevision(revId);
		if(platforms!=null) {
			for(MCPddaPlatform platform: platforms) {
				//Find Operation Names for this platform
				List<MCPddaUnit> pddaUnitsForPltform = ServiceFactory.getDao(MCPddaUnitDao.class).findBy(platform.getPddaPlatformId(), revId);
				Set<String> opeationNames = new HashSet<String>();
				if(pddaUnitsForPltform!=null) {
					for(MCPddaUnit pddaUnit: pddaUnitsForPltform) {
						opeationNames.add(pddaUnit.getId().getOperationName());
					}
				}
				//Approved PDDA Platform Id will always be only one for a particular platform,
				//which we will be considering for deprecation
				MCPddaPlatform activePlatform = ServiceFactory
						.getDao(MCPddaPlatformDao.class).getAprvdPDDAPlatformExceptRev(
								platform.getPlantLocCode(), platform.getDeptCode(),
								platform.getModelYearDate(), platform.getProductScheduleQty(),
								platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
								platform.getAsmProcessNo(), revId);
				if(activePlatform!=null) {
					//Deprecating active platform
					deprecatePlatform(activePlatform, revId, userId);
					
					//But, for operation we need to get all prev. revisions
					List<MCPddaPlatform> opPlatforms = ServiceFactory.getDao(MCPddaPlatformDao.class)
							.getAllAprvdPDDAPlatformExceptRev(platform.getPlantLocCode(), platform.getDeptCode(),
									platform.getModelYearDate(), platform.getProductScheduleQty(),
									platform.getProductAsmLineNo(), platform.getVehicleModelCode(),
									platform.getAsmProcessNo(), revId);
					
					for(String opeationName: opeationNames) {
						if(opPlatforms!=null) {
							for(MCPddaPlatform opPlatform: opPlatforms) {
								if(opPlatform.getPddaPlatformId().compareTo(platform.getPddaPlatformId()) != 0) {
									//Deprecate PDDA Units
									deprecatePddaUnits(opPlatform.getPddaPlatformId(), opPlatform.getRevId(), userId, revId, opeationName);
									//Deprecating Operations
									Set<String> operationNamesForParts = deprecateOperations(opPlatform.getPddaPlatformId(), opPlatform.getRevId(), revId, opeationName);
									for(String operationName : operationNamesForParts) {
										//Deprecating Operation Parts
										deprecateOperationParts(operationName, opPlatform.getRevId(), revId);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private MCPddaPlatform deprecatePlatform(MCPddaPlatform platform, long deprRevId, String userId) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if(platform.getDeprecated()==null) {
			platform.setDeprecated(timestamp);
			platform.setDeprecaterNo(userId);
			platform.setDeprecatedRevId(deprRevId);
			return ServiceFactory.getDao(MCPddaPlatformDao.class).update(platform);
		}
		return platform;
	}
	
	private void deprecatePddaUnits(int pddaPlatformId, long revId,
			String userId, long deprRevId) {
		long time = System.currentTimeMillis();
		List<MCPddaUnit> pddaUnits = ServiceFactory.getDao(MCPddaUnitDao.class).findBy(pddaPlatformId, revId);
		if(pddaUnits!=null) {
			for(MCPddaUnit pddaUnit : pddaUnits) {
				if(pddaUnit.getDeprecated()==null) {
					pddaUnit.setDeprecated(new Timestamp(time));
					pddaUnit.setDeprecaterNo(userId);
					pddaUnit.setDeprecatedRevId(deprRevId);
					ServiceFactory.getDao(MCPddaUnitDao.class).update(pddaUnit);
				}
			}
		}
	}
	
	private void deprecatePddaUnits(int pddaPlatformId, long revId,
			String userId, long deprRevId, String oprationName) {
		long time = System.currentTimeMillis();
		List<MCPddaUnit> pddaUnits = ServiceFactory.getDao(MCPddaUnitDao.class).findBy(pddaPlatformId, revId, oprationName);
		if(pddaUnits!=null) {
			for(MCPddaUnit pddaUnit : pddaUnits) {
				if(pddaUnit.getDeprecated()==null) {
					pddaUnit.setDeprecated(new Timestamp(time));
					pddaUnit.setDeprecaterNo(userId);
					pddaUnit.setDeprecatedRevId(deprRevId);
					ServiceFactory.getDao(MCPddaUnitDao.class).update(pddaUnit);
				}
			}
		}
	}
	
	private Set<String> deprecateOperations(int pddaPlatformId, long revId,
			long deprRevId) {
		long time = System.currentTimeMillis();
		//Getting operations
		List<MCOperationRevision> operationRevList = ServiceFactory.getDao(MCOperationRevisionDao.class).findAllBy(pddaPlatformId, revId);
		Set<String> operationNames = new HashSet<String>();
		//Deprecating Operations
		if(operationRevList!=null) {
			for(MCOperationRevision opRev : operationRevList) {
				operationNames.add(opRev.getId().getOperationName());
				if(opRev.getDeprecated()==null) {
					opRev.setDeprecated(new Date(time));
					opRev.setDeprecatedRevisionId(deprRevId);
					ServiceFactory.getDao(MCOperationRevisionDao.class).update(opRev);
				}
			}
		}
		return operationNames;
	}
	
	private Set<String> deprecateOperations(int pddaPlatformId, long revId,
			long deprRevId, String operationName) {
		long time = System.currentTimeMillis();
		//Getting operations
		List<MCOperationRevision> operationRevList = ServiceFactory.getDao(MCOperationRevisionDao.class).findAllBy(pddaPlatformId, revId, operationName);
		Set<String> operationNames = new HashSet<String>();
		//Deprecating Operations
		if(operationRevList!=null) {
			for(MCOperationRevision opRev : operationRevList) {
				operationNames.add(opRev.getId().getOperationName());
				if(opRev.getDeprecated()==null) {
					opRev.setDeprecated(new Date(time));
					opRev.setDeprecatedRevisionId(deprRevId);
					ServiceFactory.getDao(MCOperationRevisionDao.class).update(opRev);
				}
			}
		}
		return operationNames;
	}
	
	private void deprecateOperationParts(String operationName, long revId,
			long deprRevId) {
		long time = System.currentTimeMillis();
		List<MCOperationPartRevision> operationPartRevList = ServiceFactory.getDao(MCOperationPartRevisionDao.class).findAllBy(operationName, revId);
		if(operationPartRevList!=null) {
			for(MCOperationPartRevision operationPartRev: operationPartRevList) {
				if(operationPartRev.getDeprecated()==null) {
					operationPartRev.setDeprecated(new Timestamp(time));
					operationPartRev.setDeprecatedRevisionId(deprRevId);
					ServiceFactory.getDao(MCOperationPartRevisionDao.class).update(operationPartRev);
				}
			}
		}
	}
	
	/**
	 * Check whether platform is same.
	 * @param chgFrm
	 * @param platform
	 * @return boolean
	 */
	private boolean verifyPlatform(ChangeForm chgFrm, MCPddaPlatform platform) {
		boolean verifyPlatform = false;
		if(chgFrm!=null && platform!=null) {
			if(chgFrm.getPlantLocCode().equalsIgnoreCase(platform.getPlantLocCode()) 
					&& chgFrm.getDeptCode().equalsIgnoreCase(platform.getDeptCode())
					&& chgFrm.getProdSchQty().compareTo(platform.getProductScheduleQty()) == 0
					&& chgFrm.getProdAsmLineNo().equalsIgnoreCase(platform.getProductAsmLineNo())
					&& chgFrm.getModelYearDate().compareTo(platform.getModelYearDate()) == 0
					&& chgFrm.getVehicleModelCode().equalsIgnoreCase(platform.getVehicleModelCode())) {
				verifyPlatform = true;
			}
		}
		return verifyPlatform;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getYearDescriptionCodeMap(String productType) {
		Map<String, String> yearCodeMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(productType)) {
			String selectQry = "";
			if(productType.trim().equalsIgnoreCase(FRAME)) {
				selectQry = "SELECT DISTINCT MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE FROM GALADM.GAL144TBX";
			} else if(productType.trim().equalsIgnoreCase(ENGINE)) {
				selectQry = "SELECT DISTINCT MODEL_YEAR_DESCRIPTION, MODEL_YEAR_CODE FROM GALADM.GAL133TBX";
			}
			if (!selectQry.equals("")) {
				List<Object[]> resultList = findResultListByNativeQuery(
						selectQry, null);
				if (resultList != null) {
					for (Object[] data : resultList) {
						String modelYr = (data[0]!=null)?data[0].toString().trim():"";
						String modelYrCode = (data[1]!=null)?data[1].toString().trim():"";
						yearCodeMap.put(modelYr, modelYrCode);
					}
				}
			}
		}
		return yearCodeMap;
	}
	
	private String getPartDescFromOpPartRev(String partNo) {
		if (StringUtils.isEmpty(partNo)) {
			return null;
		}
		Parameters params = Parameters.with("1", partNo + "%");
		return findFirstByNativeQuery(GET_PART_DESC_FROM_OP_PART_REV, params, String.class);
	}
	
	private String getPartDescFromPDDA(String partNo) {
		if (StringUtils.isEmpty(partNo)) {
			return null;
		}
		Parameters params = Parameters.with("1", partNo + "%");
		return findFirstByNativeQuery(GET_PART_DESC_FROM_PDDA, params, String.class);
	}
	
	private Boolean isMeasurmentUnit(OperationType opType) {
		if(opType!=null) {
			//return true for Measurement Unit
			if(opType.equals(OperationType.GALC_MEAS)
					 || opType.equals(OperationType.GALC_MEAS_MANUAL)
					 || opType.equals(OperationType.GALC_SCAN_WITH_MEAS)
					 || opType.equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL)) 
				return true;
			}
			
		return false;
	}
}
