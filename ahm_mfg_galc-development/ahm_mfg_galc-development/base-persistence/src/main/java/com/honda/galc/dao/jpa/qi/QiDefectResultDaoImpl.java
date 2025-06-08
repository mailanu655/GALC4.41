package com.honda.galc.dao.jpa.qi;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiMostFrequentDefectsDto;
import com.honda.galc.dto.qi.QiRecentDefectDto;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QiDefectResultDaoImpl Class description</h3>
 * <p> QiDefectResultDaoImpl description </p>
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
 * @author L&T Infotech<br>
 * Nov 26, 2016
 */

public class QiDefectResultDaoImpl extends BaseDaoImpl<QiDefectResult, Long> implements QiDefectResultDao {

	private static final String FIND_ALL_BY_PRODUCT_ID = "select qidefectResult from QiDefectResult qidefectResult " +
			"where qidefectResult.productId = :productId" ;

	private static final String FIND_ALL_BY_PRODUCTID_AND_PART_DEFECT = "SELECT * FROM galadm.QI_DEFECT_RESULT_TBX where " +
			"COALESCE(INSPECTION_PART_NAME,'')=?1 AND " +
			"COALESCE(INSPECTION_PART_LOCATION_NAME,'')=?2 AND " +
			"COALESCE(INSPECTION_PART_LOCATION2_NAME,'')=?3 AND " +
			"COALESCE(INSPECTION_PART2_NAME,'')=?4 AND " +
			"COALESCE(INSPECTION_PART2_LOCATION_NAME,'')=?5 AND " +
			"COALESCE(INSPECTION_PART2_LOCATION2_NAME,'')=?6 AND " +
			"COALESCE(INSPECTION_PART3_NAME,'')=?7 AND " +
			"COALESCE(DEFECT_TYPE_NAME,'')=?8 AND " +
			"COALESCE(DEFECT_TYPE_NAME2,'')=?9 AND " +
			"COALESCE(PRODUCT_ID,'')=?10";

	private static final String FIND_ALL_BY_MTC_MODEL_AND_ENTRY_DEPT1 = "SELECT INSPECTION_PART_NAME, INSPECTION_PART_LOCATION_NAME, INSPECTION_PART_LOCATION2_NAME, " + 
			"INSPECTION_PART2_NAME, INSPECTION_PART2_LOCATION_NAME, INSPECTION_PART2_LOCATION2_NAME, INSPECTION_PART3_NAME, DEFECT_TYPE_NAME, DEFECT_TYPE_NAME2, " + 
			"RESPONSIBLE_SITE, RESPONSIBLE_PLANT, RESPONSIBLE_DEPT, RESPONSIBLE_LEVEL1, RESPONSIBLE_LEVEL2, RESPONSIBLE_LEVEL3, max(ACTUAL_TIMESTAMP) time, " + 
			"COUNT(*) AS COUNT FROM GALADM.QI_DEFECT_RESULT_TBX WHERE PRODUCT_ID != ?2 AND APPLICATION_ID = ?4 AND " + 
			"ACTUAL_TIMESTAMP >= CURRENT TIMESTAMP - ?3 HOUR AND ";

	private static final String FIND_ALL_BY_MTC_MODEL_AND_ENTRY_DEPT2 = ") AND ENTRY_DEPT= ?1 AND PRODUCT_TYPE= ?5 AND DELETED = 0 GROUP BY INSPECTION_PART_NAME, " + 
			"INSPECTION_PART_LOCATION_NAME, INSPECTION_PART_LOCATION2_NAME, INSPECTION_PART2_NAME, INSPECTION_PART2_LOCATION_NAME, INSPECTION_PART2_LOCATION2_NAME, " + 
			"INSPECTION_PART3_NAME, DEFECT_TYPE_NAME, DEFECT_TYPE_NAME2, RESPONSIBLE_SITE, RESPONSIBLE_PLANT, RESPONSIBLE_DEPT, RESPONSIBLE_LEVEL1, " + 
			"RESPONSIBLE_LEVEL2, RESPONSIBLE_LEVEL3 ORDER BY COUNT DESC";  

	
	private static final String FIND_REPAIR_AREA_BY_ID = "select e.repairArea from QiDefectResult e where e.defectResultId = :defectResultId";

	private static final String FIND_ENTRY_PLANT_BY_ID = "select e.entryPlantName from QiDefectResult e where e.defectResultId = :defectResultId";

	private static final String UPDATE_DEFECT_ID_BY_REPAIR_AREA = "update GALADM.QI_DEFECT_RESULT_TBX  set REPAIR_AREA = ?1, UPDATE_USER = ?2" +			
			" where REPAIR_AREA = ?3";

	private static final String FIND_DEFECT_RESULT_BY_LOCAL_DEFECT_COMBINATION_ID = "SELECT DISTINCT "+
			"COALESCE(c.INSPECTION_PART_NAME,'') AS INSPECTION_PART_NAME,COALESCE(c.INSPECTION_PART_LOCATION_NAME,'') AS INSPECTION_PART_LOCATION_NAME, "+
			"COALESCE(c.INSPECTION_PART_LOCATION2_NAME,'') AS INSPECTION_PART_LOCATION2_NAME, "+
			"COALESCE(c.INSPECTION_PART2_NAME,'') AS INSPECTION_PART2_NAME,COALESCE(c.INSPECTION_PART2_LOCATION_NAME,'') AS INSPECTION_PART2_LOCATION_NAME, "+
			"COALESCE(c.INSPECTION_PART2_LOCATION2_NAME,'') AS INSPECTION_PART2_LOCATION2_NAME, "+
			"COALESCE(c.INSPECTION_PART3_NAME,'') AS INSPECTION_PART3_NAME, COALESCE(e.IQS_VERSION,'') as IQS_VERSION, COALESCE(e.IQS_CATEGORY,'') as IQS_CATEGORY, "+
			"COALESCE(b.DEFECT_TYPE_NAME,'') AS DEFECT_TYPE_NAME,COALESCE(b.DEFECT_TYPE_NAME2,'') AS DEFECT_TYPE_NAME2, COALESCE(b.THEME_NAME,'') AS THEME_NAME, "+
			"COALESCE(f.SITE,'') AS RESPONSIBLE_SITE, COALESCE(f.PLANT,'') AS RESPONSIBLE_PLANT, COALESCE(f.DEPT,'') AS RESPONSIBLE_DEPT,f.LEVEL, COALESCE(f.RESPONSIBLE_LEVEL_NAME,'') AS RESPONSIBLE_LEVEL_NAME,a.PDDA_RESPONSIBILITY_ID, "+
			"COALESCE(a.ENTRY_SITE_NAME,'') AS ENTRY_SITE_NAME, COALESCE(a.ENTRY_PLANT_NAME,'') AS ENTRY_PLANT_NAME, COALESCE(a.DEFECT_CATEGORY_NAME,'') AS DEFECT_CATEGORY_NAME,"+
			"COALESCE(a.REPORTABLE,0) AS REPORTABLE, COALESCE(a.REPAIR_AREA_NAME,'') AS REPAIR_AREA_NAME, COALESCE(a.REPAIR_METHOD,'') AS REPAIR_METHOD,COALESCE(a.LOCAL_THEME,'') AS LOCAL_THEME, "+
			"e.IQS_QUESTION_NO AS IQS_QUESTION_NO ,e.IQS_QUESTION  AS IQS_QUESTION ,g.PROCESS_NUMBER AS PROCESS_NUMBER, g.PROCESS_NAME AS PROCESS_NAME, "+
			"g.UNIT_NUMBER AS UNIT_NUMBER,g.UNIT_DESC AS UNIT_DESC,a.REPAIR_METHOD_TIME AS REPAIR_METHOD_TIME ,a.ENTRY_SCREEN AS ENTRY_SCREEN , a.ENTRY_MODEL AS ENTRY_MODEL ,a.ENGINE_FIRING_FLAG  "+
			"FROM galadm.QI_LOCAL_DEFECT_COMBINATION_TBX a "+
			"JOIN galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX b ON a.REGIONAL_DEFECT_COMBINATION_ID=b.REGIONAL_DEFECT_COMBINATION_ID "+
			"JOIN galadm.QI_PART_LOCATION_COMBINATION_TBX c ON b.PART_LOCATION_ID=c.PART_LOCATION_ID "+
			"LEFT JOIN galadm.QI_IMAGE_SECTION_TBX d ON c.PART_LOCATION_ID=d.PART_LOCATION_ID "+
			"JOIN galadm.QI_IQS_TBX e ON e.IQS_ID = b.IQS_ID "+
			"JOIN galadm.QI_RESPONSIBLE_LEVEL_TBX f ON a.RESPONSIBLE_LEVEL_ID = f.RESPONSIBLE_LEVEL_ID "+
			"LEFT JOIN galadm.QI_PDDA_RESPONSIBILITY_TBX g ON g.PDDA_RESPONSIBILITY_ID=a.PDDA_RESPONSIBILITY_ID "+
			"where a.LOCAL_DEFECT_COMBINATION_ID=?1";

	private static final String FIND_ALL_BY_AF_ON_SEQ_NO =" select a from QiDefectResult a where afOnSequenceNumber between :prodAssSeqNo and :mostRecentAssSeqNo ";

	private static final String FIND_ALL_BY_EXTERNAL_PART_CODE =
			"select * from galadm.QI_DEFECT_RESULT_TBX DR  where "+
					"DR.INSPECTION_PART_NAME || COALESCE (DR.INSPECTION_PART_LOCATION_NAME, '') || "+
					"COALESCE(DR.INSPECTION_PART_LOCATION2_NAME, '') || COALESCE(DR.INSPECTION_PART2_NAME,'')|| ' ' || "+
					"COALESCE(DR.INSPECTION_PART2_LOCATION_NAME, '') || COALESCE(DR.INSPECTION_PART2_LOCATION2_NAME, '') || "+
					"COALESCE(DR.INSPECTION_PART3_NAME ,'') || COALESCE(DR.DEFECT_TYPE_NAME, '') || COALESCE(DR.DEFECT_TYPE_NAME2, '') in "+
					"(select distinct PLC.INSPECTION_PART_NAME || COALESCE(PLC.INSPECTION_PART_LOCATION_NAME, '') || COALESCE (PLC.INSPECTION_PART_LOCATION2_NAME, '') || "+
					"COALESCE (PLC.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE (PLC.INSPECTION_PART2_LOCATION_NAME, '') || COALESCE  (PLC.INSPECTION_PART2_LOCATION2_NAME, '') ||"+
					"COALESCE (PLC.INSPECTION_PART3_NAME ,'') || COALESCE (RDC.DEFECT_TYPE_NAME, '') ||  COALESCE (RDC.DEFECT_TYPE_NAME2, '')  as PDC "+
					"from galadm.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX ESDM "+
					"join galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC   on ESDM.LOCAL_DEFECT_COMBINATION_ID =LDC.LOCAL_DEFECT_COMBINATION_ID "+
					"join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on  LDC.REGIONAL_DEFECT_COMBINATION_ID=ESDC.REGIONAL_DEFECT_COMBINATION_ID "+
					"join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID "+ 
					"join galadm.QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID=RDC.PART_LOCATION_ID "+
					"where LDC.ENTRY_SCREEN= ESDC.ENTRY_SCREEN and ESDM.EXTERNAL_PART_CODE=?1 and ESDM.EXTERNAL_SYSTEM_NAME =?2) and DR.PRODUCT_ID=?3 " ;

	private static final String FIND_SEARCH_RESULT = "SELECT * FROM galadm.QI_DEFECT_RESULT_TBX c LEFT JOIN galadm.QI_INCIDENT_TBX g ON c.INCIDENT_ID = g.INCIDENT_ID where ";

	private static final String UPDATE_DEFECT_RESULT_INCIDENT_ID="UPDATE GALADM.QI_DEFECT_RESULT_TBX SET INCIDENT_ID = ?1 , UPDATE_USER = ?2 ,UPDATE_TIMESTAMP=?3  WHERE  DEFECTRESULTID IN ";
	private static final String FIND_DEFECT_RESULT_AND_INCIDENT_TYPE ="SELECT * FROM (SELECT  c.DEFECTRESULTID AS DEFECT_RESULT_ID, "
			+ "COALESCE(c.PRODUCT_ID,'') AS PRODUCT_ID, c.INCIDENT_ID AS INCIDENT_ID, c.ACTUAL_TIMESTAMP AS ACTUAL_TIMESTAMP," 
			+ "case when c.INCIDENT_ID =0 or c.INCIDENT_ID is null then 'CHRONIC' else COALESCE(g.INCIDENT_TYPE,'') end AS INCIDENT_TYPE, COALESCE(c.INSPECTION_PART_NAME,'') AS INSPECTION_PART_NAME,COALESCE(c.INSPECTION_PART_LOCATION_NAME,'') AS INSPECTION_PART_LOCATION_NAME, "
			+ "COALESCE(c.INSPECTION_PART_LOCATION2_NAME,'') AS INSPECTION_PART_LOCATION2_NAME, COALESCE(c.INSPECTION_PART2_NAME,'') AS INSPECTION_PART2_NAME,"
			+ "COALESCE(c.INSPECTION_PART2_LOCATION_NAME,'') AS INSPECTION_PART2_LOCATION_NAME, COALESCE(c.INSPECTION_PART2_LOCATION2_NAME,'') AS INSPECTION_PART2_LOCATION2_NAME, "
			+ "COALESCE(c.INSPECTION_PART3_NAME,'') AS INSPECTION_PART3_NAME,  COALESCE(C.DEFECT_TYPE_NAME,'') AS DEFECT_TYPE_NAME,"
			+ "COALESCE(C.DEFECT_TYPE_NAME2,'') AS DEFECT_TYPE_NAME2 ,COALESCE(g.INCIDENT_TITLE,'') AS INCIDENT_TITLE ,g.INCIDENT_DATE as INCIDENT_DATE ,"
			+ " (REPLACE(REPLACE(REPLACE(c.INSPECTION_PART_NAME || ' ' || COALESCE "
			+ " (c.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE "
			+ " (c.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "
			+ " (c.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE " 
			+ " (c.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE  "
			+ "(c.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "
			+ "(c.INSPECTION_PART3_NAME, '') || ' ' || COALESCE  "
			+ "(c.DEFECT_TYPE_NAME, '') || ' ' || COALESCE  "
			+ " (c.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) AS FULL_PART_DESC "
			+ "FROM galadm.QI_DEFECT_RESULT_TBX c LEFT JOIN galadm.QI_INCIDENT_TBX g ON c.INCIDENT_ID = g.INCIDENT_ID WHERE "
			+ "c.deleted = 0 and ";

	private static final String FIND_ALL_BY_PRODUCT_ID_AND_ENTRY_DEPT = "select qidefectResult from QiDefectResult qidefectResult " +
			"where qidefectResult.productId = :productId and qidefectResult.deleted = 0 and qidefectResult.entryDept in ( " ;

	private static final String FIND_ALL_BY_IMAGE_NAME_AND_ENTRY_DEPT = "select qidefectResult from QiDefectResult qidefectResult " +
			"where qidefectResult.imageName = :imageName and qidefectResult.productId = :productId and qidefectResult.deleted = 0 and qidefectResult.entryDept in  ( ";

	private static final String FIND_ALL_BY_PRODUCT_ID_AND_TIMESTAMP = "select a from QiDefectResult a where a.actualTimestamp > ( select MIN(z.actualTimestamp) from QiDefectResult z where z.productId= :productId)";

	private static final  String UPDATE_GDP_DEFECT="update galadm.QI_DEFECT_RESULT_TBX  set GDP_DEFECT=1 ,UPDATE_USER=?1 where  CURRENT_DEFECT_STATUS=?2 and PRODUCT_ID=?3 and GDP_DEFECT!=1";
	private static final  String UPDATE_TRPU_DEFECT="update galadm.QI_DEFECT_RESULT_TBX  set TRPU_DEFECT=1,UPDATE_USER=?1 where  CURRENT_DEFECT_STATUS=?2 and PRODUCT_ID=?3 and TRPU_DEFECT!=1";
	
	private static final String FIND_ALL_YEAR_MODEL = "select distinct (SUBSTR(PRODUCT_SPEC_CODE,1,4)) from galadm.QI_DEFECT_RESULT_TBX where PRODUCT_TYPE != '" + ProductType.MBPN.name() + "'";
	
	private static final String FIND_ALL_MBPN_MODEL = "select distinct (SUBSTR(PRODUCT_SPEC_CODE,6,3)) from galadm.QI_DEFECT_RESULT_TBX where PRODUCT_TYPE = '" + ProductType.MBPN.name() + "'";

	private static final String FIND_MOST_FREQ_BY_PPT_ENTRYSCR_TS = 
			"select A.DEFECT_TYPE_NAME,A.DEFECT_TYPE_NAME2,count(*) AS DEFECT_COUNT from qi_defect_result_tbx A "
			+ " where A.APPLICATION_ID=?1 and A.ENTRY_SCREEN=?2 and A.ENTRY_MODEL=?3 and A.ENTRY_DEPT=?4 "
			+ " and A.ACTUAL_TIMESTAMP >  ?5 "
			+ " group by A.DEFECT_TYPE_NAME,A.DEFECT_TYPE_NAME2 order by count(*) desc";
	
	private static final String FIND_PRODUCT_IDS_BY_GROUP_TRANS_ID = "select distinct d.PRODUCT_ID from QI_DEFECT_RESULT_TBX d where d.DEFECT_TRANSACTION_GROUP_ID = ?1 and d.PRODUCT_TYPE = ?2";

	private static final String FIND_PRODUCT_IDS_BY_GROUP_TRANS_ID_PROD_TYPE = "select distinct d.PRODUCT_ID from QI_DEFECT_RESULT_TBX d where d.DEFECT_TRANSACTION_GROUP_ID = ?1 and d.PRODUCT_TYPE = ?2 and d.CURRENT_DEFECT_STATUS = ?3";
	
	private static final String FIND_OUTSTANDING_PRODUCT_IDS_BY_GROUP_TRANS_ID = "select d.PRODUCT_ID from QI_DEFECT_RESULT_TBX d where d.DEFECT_TRANSACTION_GROUP_ID = ?1 and d.CURRENT_DEFECT_STATUS = ?2";
	
	private static final String FIND_NOT_FIXED_AND_NOT_KICKOUT_BY_PRODUCT_ID = "select d from QiDefectResult d where d.productId = :productId and d.currentDefectStatus = :currentDefectStatus"
			+ " and d.kickoutId = 0 and d.deleted = :deleted";
	
	private static final String FIND_NOT_FIXED_AND_KICKOUT_BY_PRODUCT_ID = "select d from QiDefectResult d where d.productId = :productId and d.currentDefectStatus = :currentDefectStatus"
			+ " and d.kickoutId > 0 and d.deleted = :deleted";
	
	private static final String  GET_NAQ_DEFECT_RESULT_ID = "select qidefectResult from QiDefectResult qidefectResult where qidefectResult.defectTypeName = :defectTypeName and qidefectResult.productId = :productId "
			+ "and qidefectResult.currentDefectStatus=:defectStatus order by qidefectResult.defectResultId desc ";

	private static final String GET_NAQ_DEFECT_DATA = "SELECT C.INSPECTION_PART_NAME,C.INSPECTION_PART_LOCATION_NAME,C.INSPECTION_PART_LOCATION2_NAME,A.DEFECT_TYPE_NAME,B.DEFECT_TYPE_NAME2,D.IQS_VERSION, "
	+" D.IQS_CATEGORY,D.IQS_QUESTION_NO,D.IQS_QUESTION,B.THEME_NAME,E.REPORTABLE,H.SITE,H.PLANT,H.DEPT,H.RESPONSIBLE_LEVEL_NAME,E.ENTRY_SITE_NAME,E.ENTRY_PLANT_NAME, "
	+" CASE WHEN TIME(CURRENT_TIMESTAMP) > '05:00:00' and TIME(CURRENT_TIMESTAMP) < '15:30:00' then '01' else '02' END AS SHIFT,"
	+" G.PRODUCT_SPEC_CODE,G.PRODUCTION_LOT, G.KD_LOT_NUMBER,G.AF_ON_SEQUENCE_NUMBER,A.DEFECT_CATEGORY_NAME,E.LOCAL_THEME,E.ENTRY_SCREEN,E.ENTRY_MODEL,G.PRODUCT_ID " 
	+" FROM GALADM.QI_DEFECT_TBX A, GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX B, GALADM.QI_PART_LOCATION_COMBINATION_TBX C, GALADM.QI_IQS_TBX D, "
	+" GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX E, GALADM.QI_ENTRY_MODEL_GROUPING_TBX F, GALADM.GAL143TBX G, GALADM.QI_RESPONSIBLE_LEVEL_TBX H "
	+" where A.DEFECT_TYPE_NAME = B.DEFECT_TYPE_NAME and B.PART_LOCATION_ID = C.PART_LOCATION_ID and A.DEFECT_TYPE_NAME = ?1 "
	+" and B.IQS_ID = D.IQS_ID and E.REGIONAL_DEFECT_COMBINATION_ID = B.REGIONAL_DEFECT_COMBINATION_ID and E.ENTRY_MODEL = F.ENTRY_MODEL "
	+" and G.PRODUCT_ID = ?2 and SUBSTR(G.PRODUCT_SPEC_CODE,1,4)= F.MTC_MODEL and E.RESPONSIBLE_LEVEL_ID = H.RESPONSIBLE_LEVEL_ID ";
	
	private static final String[] ORDER_BY = {"actualTimestamp"};
	private static final String CHECK_LET_DEFECT_EXISTS="select qidefectResult from QiDefectResult qidefectResult where qidefectResult.defectTypeName = :defectTypeName and qidefectResult.productId = :productId";
	/**
	 * This method is used to find All Defects by Product Id.
	 * @param productId
	 */
	public List<QiDefectResult> findAllByProductId(String productId, List<Short> statusList) {
		StringBuilder selectedValue = new StringBuilder(FIND_ALL_BY_PRODUCT_ID);
		Parameters params = Parameters.with("productId", productId);
		if(!statusList.isEmpty()) {
			params.put("status", statusList);
			selectedValue.append(" and qidefectResult.currentDefectStatus in (:status)");
		}
		return findAllByQuery(selectedValue.toString(), params);
	}
	
	public List<QiDefectResult> findAllByProductId(String productId) {
		return findAll(Parameters.with("productId", productId));
	}
	
	public QiDefectResult findFirstMatch(QiDefectResult defectResult, String applicationId) {
		Parameters params = new Parameters();
		if(StringUtils.isNotEmpty(defectResult.getInspectionPartName())) params.put("inspectionPartName", defectResult.getInspectionPartName());
		if(StringUtils.isNotEmpty(defectResult.getInspectionPartLocationName())) params.put("inspectionPartLocationName", defectResult.getInspectionPartLocationName());
		if(StringUtils.isNotEmpty(defectResult.getInspectionPartLocation2Name())) params.put("inspectionPartLocation2Name", defectResult.getInspectionPartLocation2Name());
		if(StringUtils.isNotEmpty(defectResult.getInspectionPart2Name())) params.put("inspectionPart2Name", defectResult.getInspectionPart2Name());
		if(StringUtils.isNotEmpty(defectResult.getInspectionPart2LocationName())) params.put("inspectionPart2LocationName", defectResult.getInspectionPart2LocationName());
		if(StringUtils.isNotEmpty(defectResult.getInspectionPart2Location2Name())) params.put("inspectionPart2Location2Name", defectResult.getInspectionPart2Location2Name());
		if(StringUtils.isNotEmpty(defectResult.getInspectionPart3Name())) params.put("inspectionPart3Name", defectResult.getInspectionPart3Name());
		if(StringUtils.isNotEmpty(defectResult.getDefectTypeName())) params.put("defectTypeName", defectResult.getDefectTypeName());
		if(StringUtils.isNotEmpty(defectResult.getDefectTypeName2())) params.put("defectTypeName2", defectResult.getDefectTypeName2());
		if(defectResult.getActualTimestamp() != null) params.put("actualTimestamp", defectResult.getActualTimestamp());
		if(StringUtils.isNotEmpty(applicationId)) params.put("applicationId", applicationId);
		params.put("deleted", (short) 0);
		return findFirst(params, ORDER_BY, false);
	}
	
	/**
	 * This method is used to check if there are existing defects with the given defect properties.
	 * @param defectResult QiDefectResult
	 */
	public boolean checkDefectResultExist(QiDefectResult defectResult) {
		if(defectResult == null)  return false;
		List<QiDefectResult> defectList = findAllByProductIdAndPartDefect(defectResult);
		return (defectList != null && !defectList.isEmpty());
	}

	/**
	 * This method is used to check if there are existing defects with the given defect properties.
	 * @param defectResult QiDefectResult
	 */
	public List<QiDefectResult> findDuplicateDefects(QiDefectResult defectResult) {
		if(defectResult == null)  return null;
		List<QiDefectResult> duplicates = null;
		List<QiDefectResult> matchingDefects = findAllByProductIdAndPartDefect(defectResult);
		if(matchingDefects != null && !matchingDefects.isEmpty())  {
			duplicates = new ArrayList<QiDefectResult>();
			for(QiDefectResult qiDefect : matchingDefects)  {
				if(qiDefect != null && qiDefect.getDefectResultId() != defectResult.getDefectResultId())  {
					duplicates.add(qiDefect);
				}
			}
		}
		return duplicates;
	}

	/**
	 * This method is used to check if there are existing NOT_FIXED defects with the given defect properties.
	 * @param defectResult QiDefectResult
	 */
	@Override
	public List<QiDefectResult> findMatchingDefectsByNotFixed(QiDefectResult sourceDefect) {
		if(sourceDefect == null)  return null;
		List<QiDefectResult> notFixed = null;
		List<QiDefectResult> matchingDefects = findAllByProductIdAndPartDefect(sourceDefect);
		if(matchingDefects != null && !matchingDefects.isEmpty())  {
			notFixed = new ArrayList<QiDefectResult>();
			for(QiDefectResult qiDefect : matchingDefects)  {
				if (qiDefect != null && qiDefect.getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId()
						&& qiDefect.getDefectResultId() != sourceDefect.getDefectResultId()) {
					notFixed.add(qiDefect);
				}
			}
		}
		return notFixed;
	}

	/**
	 * This method is used to get defects matching the given defect properties.
	 * @param defectResult QiDefectResult
	 */
	@Override
	public List<QiDefectResult> findAllByProductIdAndPartDefect(QiDefectResult defectResult) {
		if(defectResult == null)  return null;
		Parameters params = Parameters.with("1", defectResult.getInspectionPartName())
				.put("2", defectResult.getInspectionPartLocationName())
				.put("3", defectResult.getInspectionPartLocation2Name())
				.put("4", defectResult.getInspectionPart2Name())
				.put("5", defectResult.getInspectionPart2LocationName())
				.put("6", defectResult.getInspectionPart2Location2Name())
				.put("7", defectResult.getInspectionPart3Name())
				.put("8", defectResult.getDefectTypeName())
				.put("9", defectResult.getDefectTypeName2())
				.put("10", defectResult.getProductId());
		return findAllByNativeQuery(FIND_ALL_BY_PRODUCTID_AND_PART_DEFECT, params);
	}

	/**
	 * This method is used to find all not repaired defects that are not associated with a kickout
	 */
	@Transactional
	public  List<QiDefectResult> findAllNotRepairedNotKickoutDefects(String productId) {	
		Parameters params = Parameters.with("productId",productId);
		params.put("deleted", (short)0);
		params.put("currentDefectStatus", (short)DefectStatus.NOT_FIXED.getId());
		return findAllByQuery(FIND_NOT_FIXED_AND_NOT_KICKOUT_BY_PRODUCT_ID, params);
	}
	
	/**
	 * This method is used to find all not repaired defects that are associated with a kickout
	 */
	@Transactional
	public  List<QiDefectResult> findAllNotRepairedKickoutDefects(String productId) {	
		Parameters params = Parameters.with("productId",productId);
		params.put("deleted", (short)0);
		params.put("currentDefectStatus", (short)DefectStatus.NOT_FIXED.getId());
		return findAllByQuery(FIND_NOT_FIXED_AND_KICKOUT_BY_PRODUCT_ID, params);
	}
	
	/**
	 * This method is used to find all not repaired defects
	 */
	@Transactional
	public  List<QiDefectResult> findAllNotRepairedDefects(String productId) {	
		Parameters params = Parameters.with("productId",productId);
		params.put("deleted", (short)0);
		params.put("currentDefectStatus", (short)DefectStatus.NOT_FIXED.getId());
		return findAll(params);
	}

	/**
	 * This method is used to find All Defects by MtcModel and Entry Dept
	 * @param prodSpecCode
	 * @param entryDept
	 * @param productId
	 */
	public List<QiRecentDefectDto> findAllByMtcModelAndEntryDept(String mtcModel,String entryDept, String productId, Integer recentDefectRange,String processPointId, String productType){
		
		String productSpecCodeCondition = "SUBSTR(PRODUCT_SPEC_CODE,1,4) in ( " + mtcModel;
		if (ProductType.MBPN.name().equals(productType)) {
			productSpecCodeCondition = "SUBSTR(PRODUCT_SPEC_CODE,6,3) in ( " + mtcModel;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_MTC_MODEL_AND_ENTRY_DEPT1).append(productSpecCodeCondition)
		.append(FIND_ALL_BY_MTC_MODEL_AND_ENTRY_DEPT2);
		Parameters params = Parameters.with("1", entryDept).put("2", productId).put("3", recentDefectRange).put("4", processPointId).put("5", productType);
		List<QiRecentDefectDto> resultList = findAllByNativeQuery(sb.toString(), params, QiRecentDefectDto.class);
		return resultList;
	}

	public String findRepairAreaById(long defectResultId) {
		Parameters params = Parameters.with("defectResultId", defectResultId);
		return findFirstByQuery(FIND_REPAIR_AREA_BY_ID, String.class, params);
	}
	public String findEntryPlantById(long defectResultId) {
		Parameters params = Parameters.with("defectResultId", defectResultId);
		return findFirstByQuery(FIND_ENTRY_PLANT_BY_ID, String.class, params);
	}

	@Transactional
	public void updateRepairArea(long defectResultId, String updateUser) {
		update(Parameters.with("repairArea", null).put("updateUser", updateUser), Parameters.with("defectResultId", defectResultId));		
	}

	@Transactional
	public void updateRepairArea(String plant, String dept, String repairArea, String updateUser, long defectResultId) {
		update(Parameters.with("entryPlantName", plant).put("entryDept", dept).put("repairArea", repairArea)
				.put("updateUser", updateUser), Parameters.with("defectResultId", defectResultId));
	}

	public boolean isRepairAreaUsed(String repairAreaName) {
		QiDefectResult qiDefectResult = findFirst(Parameters.with("repairArea", repairAreaName)); 
		if (qiDefectResult != null) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public void updateAllByRepairArea(String repairAreaName, String oldRepairAreaName, String updateUser) {
		Parameters params = Parameters.with("1", repairAreaName)
				.put("2", updateUser)
				.put("3", oldRepairAreaName);			
		executeNativeUpdate(UPDATE_DEFECT_ID_BY_REPAIR_AREA, params);
	}

	public List<QiDefectResult> findAllCurrentDefectStatusByProductId(String productId) {
		return findAll(Parameters.with("productId", productId));
	}
	public DefectMapDto findDefectResultByLocalCombinationId(int localDefectCombinationId) {
		Parameters params = Parameters.with("1", localDefectCombinationId);
		return (DefectMapDto) findFirstByNativeQuery(FIND_DEFECT_RESULT_BY_LOCAL_DEFECT_COMBINATION_ID, params, DefectMapDto.class);
	}

	public List<QiDefectResult> findAllByExternalSystemDataAndProductId(String externalPartCode, String externalSystemName ,String productId) {
		Parameters params = Parameters.with("1", externalPartCode).put("2", externalSystemName).put("3", productId);
		return findAllByNativeQuery(FIND_ALL_BY_EXTERNAL_PART_CODE, params);
	}

	public List<QiDefectResult> findAllByFilter(String searchString,String filterData,int defectDataRange,int searchResultLimit){
		StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(FIND_SEARCH_RESULT).append(searchString ).append(" AND DELETED = 0 ");
		if(filterData!=null && !filterData.equalsIgnoreCase("")){
			searchQuery.append(" AND (REPLACE(REPLACE(REPLACE(c.INSPECTION_PART_NAME || ' ' || COALESCE (c.INSPECTION_PART_LOCATION_NAME, '') || ' ' || COALESCE (c.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || COALESCE "+
					" (c.INSPECTION_PART2_NAME,'')|| ' ' || COALESCE (c.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || COALESCE (c.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || COALESCE  "+
					" (c.INSPECTION_PART3_NAME, '') || ' ' || COALESCE  (c.DEFECT_TYPE_NAME, '') || ' ' || COALESCE  (c.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) like  '%" +filterData+ "%'");
		}
		searchQuery.append(" AND ACTUAL_TIMESTAMP > current timestamp - "+(defectDataRange-1)+" DAY");
		return findAllByNativeQuery(searchQuery.toString(), null,searchResultLimit);
	}

	@Transactional
	public void updateIncidentIdByDefectResultId(Integer qiIncidentId, String user, String defectResultIdSet,Timestamp updatedTimestamp) {
		Parameters params = Parameters.with("1", qiIncidentId).put("2", user).put("3", updatedTimestamp);
		executeNativeUpdate(UPDATE_DEFECT_RESULT_INCIDENT_ID+defectResultIdSet, params);	
	}

	public List<QiDefectResultDto> findAllBySearchFilter(String searchString,String filterData){
		String SEARCHED_QUERY = FIND_DEFECT_RESULT_AND_INCIDENT_TYPE+searchString+ " ) WHERE (PRODUCT_ID like ?1 or INCIDENT_TYPE like ?1 or INCIDENT_TITLE like ?1 or FULL_PART_DESC like ?1 or INCIDENT_DATE like ?1)";
		return findAllByNativeQuery(SEARCHED_QUERY, Parameters.with("1", "%"+filterData+"%"),QiDefectResultDto.class,50);
	}

	@Transactional
	public void deleteDefectResult(long defectResultId, String reasonForChange, String correctionRequester, String updateUser) {
		update(Parameters.with("deleted", 1).put("reasonForChange", reasonForChange).put("correctionRequestBy", correctionRequester).put("updateUser", updateUser), 
				Parameters.with("defectResultId", defectResultId));
	}

	/**
	 * This method is used to find All Defects by Product Id.
	 * @param productId
	 */
	public List<QiDefectResult> findAllByProductIdAndEntryDept(String productId, String entryDept) {
		StringBuilder query = new StringBuilder(FIND_ALL_BY_PRODUCT_ID_AND_ENTRY_DEPT);
		Parameters params = Parameters.with("productId", productId);
		return findAllByQuery(query.append(entryDept).append(" ) ").toString(), params);
	}

	public List<QiDefectResult> findAllByImageNameAndEntryDept(String productId,String imageName,String entryDept) {
		StringBuilder query = new StringBuilder(FIND_ALL_BY_IMAGE_NAME_AND_ENTRY_DEPT);
		Parameters params = Parameters.with("imageName", imageName).put("productId", productId);
		return findAllByQuery(query.append(entryDept).append(" ) ").toString() , params);
	}
	
	public List<QiDefectResult> findAllByProductIdAndCurrentDefectStatus(String productId, short defectStatus) {
		return findAll(Parameters.with("productId", productId).put("currentDefectStatus", defectStatus));
	}

	public List<QiDefectResult> findAllByProductIdAndApplicationId(String productId, String applicationId) {
		return findAll(Parameters.with("productId", productId).put("applicationId", applicationId));
	}

	/** This method is used to return list of defect result data between given AF_ON_SEQ_NO.
	 * @param prodAssSeqNo
	 * @param mostRecentAssSeqNo
	 * @return List<QiDefectResult>
	 */
	public List<QiDefectResult> findAllByAfOnSeqNo(int prodAssSeqNo,int mostRecentAssSeqNo) {
		Parameters params = Parameters.with("prodAssSeqNo", prodAssSeqNo).put("mostRecentAssSeqNo", mostRecentAssSeqNo);
		return findAllByQuery(FIND_ALL_BY_AF_ON_SEQ_NO, params);
	}

	/** This method is used to return list of defect result data based on product id.
	 * @param productId
	 * @return List<QiDefectResult>
	 */
	public List<QiDefectResult> findAllByProductIdAndTimeStamp(String productId) {
		Parameters params = Parameters.with("productId", productId);
		return findAllByQuery(FIND_ALL_BY_PRODUCT_ID_AND_TIMESTAMP, params);
	}

	@Transactional
	public int updateResponsibleAssociateByDefectId(int defectResultId, String responsibleAssociate, String userId) {
		return update(Parameters.with("responsibleAssociate", responsibleAssociate).put("updateUser", userId),
				Parameters.with("defectResultId", defectResultId));
	}

	@Transactional
	public int updateGdpDefects(String productId, String userId) {
		if (userId != null) {
			userId = userId.trim().toUpperCase();
		}
		
		String query = UPDATE_GDP_DEFECT;
		//get VQ_GDP_PROCESS_POINT_ID
		String[] vqGdpProcessPointIdArray = PropertyService.getPropertyBean(QiPropertyBean.class).getVqGdpProcessPointId();
		if (vqGdpProcessPointIdArray.length > 0) {
			String vqGdpProcessPointIds = "'" + vqGdpProcessPointIdArray[0];
			for (int i = 1; i < vqGdpProcessPointIdArray.length; i++) {
				vqGdpProcessPointIds += "', '" +vqGdpProcessPointIdArray[i];
			}
			vqGdpProcessPointIds += "'";
			
			query = query + " and APPLICATION_ID not in (" + vqGdpProcessPointIds + ")";
		}
		return executeNativeUpdate(query, Parameters.with("1", userId).put("2", (short)DefectStatus.NOT_FIXED.getId()).put("3", productId));
	}

	@Override
	@Transactional
	public int updateTrpuDefects(String productId, String userId) {
		if (userId != null) {
			userId = userId.trim().toUpperCase();
		}
		return executeNativeUpdate(UPDATE_TRPU_DEFECT,
				Parameters.with("1", userId).put("2", (short) DefectStatus.NOT_FIXED.getId()).put("3", productId));
	}

	public Long findNotFixedDefectCountByProductId(String productId) {
		Parameters params = Parameters.with("productId",productId);
		params.put("deleted", (short)0);
		params.put("currentDefectStatus", (short)DefectStatus.NOT_FIXED.getId());
		return (Long)count(params);
	}
	@Override
	public List<String> findAllYearModel(String productType) {
		if (ProductType.MBPN.name().equals(productType)) {
			return findAllByNativeQuery(FIND_ALL_MBPN_MODEL,null, String.class);
		} else {
			return findAllByNativeQuery(FIND_ALL_YEAR_MODEL,null, String.class);
		}
	}
	
	//create QiDefectResult in database
	//if previousQiDefectResult is not null, reuse the Actual_timestamp from previousQiDefectResult 
	//so that both QiDefectResult share same Actual_timestamp, Production_date, Shift, Team 
	@Transactional
	public QiDefectResult createQiDefectResult(QiDefectResult qiDefectResult, QiDefectResult previousQiDefectResult) {
		
		if (previousQiDefectResult == null) {
			//update fields based on DB server timestamp
			Timestamp timestamp = getDatabaseTimeStamp();
			qiDefectResult.setActualTimestamp(timestamp);
			qiDefectResult.setGroupTimestamp(timestamp);
			
			String applicationId = qiDefectResult.getApplicationId();
			ProcessPoint processPoint = getDao(ProcessPointDao.class).findByKey(applicationId);
			GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByKey(processPoint.getDivisionId());
				
			if (gpcsDivision != null) {
				DailyDepartmentSchedule schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
						gpcsDivision.getGpcsLineNo(), gpcsDivision.getGpcsProcessLocation(), 
						gpcsDivision.getGpcsPlantCode(), timestamp);
				
				if (schedule != null) {
					qiDefectResult.setProductionDate(schedule.getId().getProductionDate());
					qiDefectResult.setShift(schedule.getId().getShift());
					TeamRotation teamRotation= getDao(TeamRotationDao.class).findTeamDetails(
							gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
					if(teamRotation != null) {
						qiDefectResult.setTeam(StringUtils.trimToEmpty(teamRotation.getId().getTeam()));
					}
				}
			}
		} else  {
			qiDefectResult.setActualTimestamp(previousQiDefectResult.getActualTimestamp());
			qiDefectResult.setGroupTimestamp(previousQiDefectResult.getGroupTimestamp());
			qiDefectResult.setProductionDate(previousQiDefectResult.getProductionDate());
			qiDefectResult.setShift(previousQiDefectResult.getShift());
			qiDefectResult.setTeam(previousQiDefectResult.getTeam());
			qiDefectResult.setCreateUser(previousQiDefectResult.getCreateUser());
			qiDefectResult.setUpdateUser(previousQiDefectResult.getCreateUser());
		}
		if(qiDefectResult.getDefectTransactionGroupId() == null || qiDefectResult.getDefectTransactionGroupId() == 0) {
			Long max = max("defectTransactionGroupId", Long.class);
			long maxId = max == null ? 1 : max + 1;
			qiDefectResult.setDefectTransactionGroupId(maxId);
		}
		
		QiResponsibleLevel defaultResponsibleLevel1 = qiDefectResult.getDefaultResponsibleLevel1();
		QiResponsibleLevel defaultResponsibleLevel2 = null;
		QiResponsibleLevel defaultResponsibleLevel3 = null;
		String defaultResponsibleLevel1Name = "";
		String defaultResponsibleLevel2Name = "";
		String defaultResponsibleLevel3Name = "";
		if (defaultResponsibleLevel1 != null && defaultResponsibleLevel1.getUpperResponsibleLevelId() != null) {
			defaultResponsibleLevel2 = getDao(QiResponsibleLevelDao.class).findByKey(defaultResponsibleLevel1.getUpperResponsibleLevelId());
			if (defaultResponsibleLevel2 != null) {
				if (defaultResponsibleLevel2.getLevel() == 2) { //upper level is 2
					if (defaultResponsibleLevel2.getUpperResponsibleLevelId() != null) {
						defaultResponsibleLevel3 =  getDao(QiResponsibleLevelDao.class).findByKey(defaultResponsibleLevel2.getUpperResponsibleLevelId());
					}
				} else { //upper level is 3, there is no level 2
					defaultResponsibleLevel3 = defaultResponsibleLevel2;
					defaultResponsibleLevel2 = null;
				}
			}
			defaultResponsibleLevel1Name = defaultResponsibleLevel1.getResponsibleLevelName();
			defaultResponsibleLevel2Name = defaultResponsibleLevel2 == null? StringUtils.EMPTY : defaultResponsibleLevel2.getResponsibleLevelName();
			defaultResponsibleLevel3Name = defaultResponsibleLevel3 == null? StringUtils.EMPTY : defaultResponsibleLevel3.getResponsibleLevelName();
		}
		
	
		if (defaultResponsibleLevel1 != null && 
				(
				!qiDefectResult.getResponsibleSite().equals(defaultResponsibleLevel1.getSite())
				|| !qiDefectResult.getResponsiblePlant().equals(defaultResponsibleLevel1.getPlant())
				|| !qiDefectResult.getResponsibleDept().equals(defaultResponsibleLevel1.getDepartment())
				|| !qiDefectResult.getResponsibleLevel1().equals(defaultResponsibleLevel1Name)
				|| !StringUtils.trimToEmpty(qiDefectResult.getResponsibleLevel2()).equals(defaultResponsibleLevel2Name)
				|| !StringUtils.trimToEmpty(qiDefectResult.getResponsibleLevel3()).equals(defaultResponsibleLevel3Name)
				)
			)  {
			//if responsibility gets overwritten, create defect result with default responsibility first
			//then create a history record and update defect result with overwritten responsibility
			QiDefectResult defaultQiDefectResult = null;
			try {
				defaultQiDefectResult = qiDefectResult.clone();
				defaultQiDefectResult.setResponsibleSite(defaultResponsibleLevel1.getSite());
				defaultQiDefectResult.setResponsiblePlant(defaultResponsibleLevel1.getPlant());
				defaultQiDefectResult.setResponsibleDept(defaultResponsibleLevel1.getDepartment());
				defaultQiDefectResult.setResponsibleLevel1(defaultResponsibleLevel1Name);
				defaultQiDefectResult.setResponsibleLevel2(defaultResponsibleLevel2Name);
				defaultQiDefectResult.setResponsibleLevel3(defaultResponsibleLevel3Name);
				defaultQiDefectResult = super.insert(defaultQiDefectResult);
				
				// create a defect result history record
				QiDefectResultHist qiDefectResultHist = new QiDefectResultHist(defaultQiDefectResult);
				qiDefectResultHist.setChangeUser(defaultQiDefectResult.getCreateUser());
				
				//update defect result with overwritten responsibility
				defaultQiDefectResult.setResponsibleSite(qiDefectResult.getResponsibleSite());
				defaultQiDefectResult.setResponsiblePlant(qiDefectResult.getResponsiblePlant());
				defaultQiDefectResult.setResponsibleDept(qiDefectResult.getResponsibleDept());
				defaultQiDefectResult.setResponsibleLevel1(qiDefectResult.getResponsibleLevel1());
				defaultQiDefectResult.setResponsibleLevel2(qiDefectResult.getResponsibleLevel2());
				defaultQiDefectResult.setResponsibleLevel3(qiDefectResult.getResponsibleLevel3());
				defaultQiDefectResult.setUpdateUser(defaultQiDefectResult.getCreateUser());
				defaultQiDefectResult.setReasonForChange("Responsibility got overwritten at initial defect entry");
				defaultQiDefectResult = super.update(defaultQiDefectResult);
				
				getDao(QiDefectResultHistDao.class).insert(qiDefectResultHist);
				
			} catch (CloneNotSupportedException e) {
				
			}
			return defaultQiDefectResult;
		} else {
			return super.insert(qiDefectResult);
		}
	}
	
	public List<QiMostFrequentDefectsDto> findMostFrequentDefectsByProcessPointEntryScreenDuration(String processPoint, String entryScreen, String entryModel, String entryDept, Date actualDate)
	{
		Timestamp actualTs = new Timestamp(actualDate.getTime());
		Parameters params = Parameters.with("1", processPoint)
				.put("2", entryScreen)
				.put("3", entryModel)
				.put("4", entryDept).put("5", actualTs);
		return findAllByNativeQuery(FIND_MOST_FREQ_BY_PPT_ENTRYSCR_TS, params, QiMostFrequentDefectsDto.class);
	}
	
	public List<QiDefectResult> findAllCurrentDefectStatusByProductIds(List<String> productIds) {
		return findAll(Parameters.with("productId", productIds));
	}
	
	public Long getNextDefectTransactionGrounpId(QiDefectResult qiDefectResult) {
		long maxId = 1L;
		if(qiDefectResult.getDefectTransactionGroupId() == null || qiDefectResult.getDefectTransactionGroupId() == 0) {
			IdCreate newId = ServiceFactory.getDao(IdCreateDao.class).
					incrementIdWithNewTransaction("QI_DEF_RES_TBX",	"DEFECT_TRANSACTION_GROUP_ID");
			if(newId != null)  {
				maxId = newId.getCurrentId();
			}
		}else {
			maxId = qiDefectResult.getDefectTransactionGroupId();
		}
		return maxId;
	}

	public List<QiDefectResult> findAllByGroupTransIdProdType(long transactionId,String prodType) {
		return findAll(Parameters.with("defectTransactionGroupId", transactionId).put("productType", prodType));
	}

	public List<String> findProductIdsByGroupTransId(long transactionId,String prodType) {
		Parameters params = Parameters.with("1", transactionId).put("2", prodType);
		return findAllByNativeQuery(FIND_PRODUCT_IDS_BY_GROUP_TRANS_ID, params, String.class);
	}
	
	public List<String> findProductIdsByGroupTransIdProdType(long transactionId,String prodType, int defectStatus) {
		Parameters params = Parameters.with("1", transactionId).put("2", prodType).put("3", defectStatus);
		return findAllByNativeQuery(FIND_PRODUCT_IDS_BY_GROUP_TRANS_ID_PROD_TYPE, params, String.class);
	}
			
	public List<String> findOutstandingProductIdsByGroupTransId(long transactionId){
		Parameters params = Parameters.with("1", transactionId).put("2", (short) DefectStatus.NOT_FIXED.getId());
		return findAllByNativeQuery(FIND_OUTSTANDING_PRODUCT_IDS_BY_GROUP_TRANS_ID, params, String.class);
	}
	

	public List<QiDefectResult> findAllByGroupTransId(long transactionId) {
		Parameters params = Parameters.with("defectTransactionGroupId", transactionId);
		return findAll(params);
	}

	public List<QiDefectResult> findAllByCreateUser(String createUser) {
		Parameters params = Parameters.with("createUser", createUser);
		return findAll(params);
	}

	public List<QiDefectResult> findAllByDefectType(String defectType) {
		Parameters params = Parameters.with("defectTypeName", defectType);
		return findAll(params);
	}
	
	public long findDefectTransactionGroupCount(long defectTransactionGroupId) {
		return count(Parameters.with("defectTransactionGroupId", defectTransactionGroupId));
	}
		
	/**
	 * This is method to get faked defectResultId used for training mode
	 */
	public long findMaxDefectResultId() {
		return max("defectResultId", Long.class);
	}
	
	public List<QiDefectResult> findByProductIdAndDefectType(String defectTypeName, String productId, String defectStatus) {
		StringBuilder selectedValue = new StringBuilder(GET_NAQ_DEFECT_RESULT_ID);
		Parameters params = Parameters.with("productId", productId);
		params.put("defectTypeName",defectTypeName);
		params.put("defectStatus", Short.parseShort(defectStatus));
		
		return findAllByQuery(selectedValue.toString(), params);
	}
	
	@Transactional
	public QiDefectResult createDefect(String productId, String defectTypeName, String processPoint, String createUser, String dept, String div) {
		StringBuilder selectedValue = new StringBuilder(GET_NAQ_DEFECT_DATA);
		Parameters params = Parameters.with("2", productId);
		params.put("1",defectTypeName);
		List<Object[]> resultSetList = findResultListByNativeQuery(selectedValue.toString(), params);
		
		if(resultSetList.size()>0) {
			Object[] vResultSet = resultSetList.get(0);
		
			String inspectionPartName = StringUtils.defaultString((String) vResultSet[0]);
			String inspectionPartLoc = StringUtils.defaultString((String)vResultSet[1]);
			String inspectionPartLoc2 = StringUtils.defaultString((String)vResultSet[2]);
			String defectType = StringUtils.defaultString((String)vResultSet[3]);
			String defectType2 = StringUtils.defaultString((String)vResultSet[4]);
			String iqsVersion = StringUtils.defaultString((String)vResultSet[5]);
			String iqsCategory = StringUtils.defaultString((String)vResultSet[6]);
			Integer iqsQuestionNo = vResultSet[7]==null?null:((Integer)vResultSet[7]);
			String iqsQuestion = StringUtils.defaultString((String)vResultSet[8]);
			String themeName = StringUtils.defaultString((String)vResultSet[9]);
			Short reportable = vResultSet[10]== null?null:(Short.parseShort(String.valueOf(vResultSet[10])));
			String responsibleSite = StringUtils.defaultString((String)vResultSet[11]);
			String responsiblePlant = StringUtils.defaultString((String)vResultSet[12]);
			String responsibleDept = StringUtils.defaultString((String)vResultSet[13]);
			String responsibleLevel1 = StringUtils.defaultString((String)vResultSet[14]);
			String entrySite = StringUtils.defaultString((String)vResultSet[15]);
			String entryPlant = StringUtils.defaultString((String)vResultSet[16]);
			String shift = StringUtils.defaultString((String)vResultSet[17]);
			String productSpecCode = StringUtils.defaultString((String)vResultSet[18]);
			String productionLot = StringUtils.defaultString((String)vResultSet[19]);
			String kdLot = StringUtils.defaultString((String)vResultSet[20]);
			Integer afOnSeq = vResultSet[21]== null?null:((Integer)vResultSet[21]);
			String defectCategoryName = StringUtils.defaultString((String)vResultSet[22]);
			String localTheme =StringUtils.defaultString((String) vResultSet[23]);
			String entryScreen = StringUtils.defaultString((String)vResultSet[24]);
			String entryModel = StringUtils.defaultString((String)vResultSet[25]);
			String product = StringUtils.defaultString((String)vResultSet[26]);
			
			QiDefectResult defectResult = new QiDefectResult();
			defectResult.setAfOnSequenceNumber(afOnSeq );
			defectResult.setInspectionPartName(inspectionPartName);
			defectResult.setInspectionPartLocationName(inspectionPartLoc);
			defectResult.setInspectionPartLocation2Name(inspectionPartLoc2);
			defectResult.setDefectTypeName(defectType);
			defectResult.setDefectTypeName2(defectType2);
			defectResult.setIqsVersion(iqsVersion);
			defectResult.setIqsCategoryName(iqsCategory);
			defectResult.setIqsQuestionNo(iqsQuestionNo);
			defectResult.setIqsQuestion(iqsQuestion);
			defectResult.setThemeName(themeName);
			defectResult.setReportable(reportable);
			defectResult.setResponsibleSite(responsibleSite);
			defectResult.setResponsiblePlant(responsiblePlant);
			defectResult.setResponsibleDept(responsibleDept);
			defectResult.setResponsibleLevel1(responsibleLevel1);
			defectResult.setEntrySiteName(entrySite);
			defectResult.setEntryPlantName(entryPlant);
			defectResult.setShift(shift);
			defectResult.setProductSpecCode(productSpecCode);
			defectResult.setProductionLot(productionLot);
			defectResult.setKdLotNumber(kdLot);
			defectResult.setDefectCategoryName(defectCategoryName);
			defectResult.setLocalTheme(localTheme);
			defectResult.setEntryScreen(entryScreen);
			defectResult.setEntryModel(entryModel);
			defectResult.setProductId(product);
			defectResult.setApplicationId(processPoint);
			defectResult.setDefectTransactionGroupId(0L);
			defectResult.setOriginalDefectStatus((short)DefectStatus.NOT_REPAIRED.getId());
			defectResult.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
			defectResult.setWriteUpDept(dept);
			defectResult.setEntryDept(div);
			defectResult.setActualTimestamp(getDatabaseTimeStamp());
			defectResult.setProductType(ProductType.FRAME.getProductName());
			defectResult.setCreateUser(createUser);
			defectResult.setEngineFiringFlag((short)0);
			defectResult.setPddaModelYear(0.0f);
			defectResult.setGdpDefect((short)0);
			defectResult.setTrpuDefect((short)0);
			defectResult.setIncidentId(0);
			defectResult.setDeleted((short)0);
				
			return save(defectResult);
		}
		return null;
	}

	@Override
	public List<QiDefectResult> checkLETDefectExists(String productId,String defectTypeName){
		StringBuilder selectedValue = new StringBuilder(CHECK_LET_DEFECT_EXISTS);
		Parameters params = Parameters.with("productId", productId);
		params.put("defectTypeName",defectTypeName);
		
		return findAllByQuery(selectedValue.toString(), params);
	}
	
	@Override
	@Transactional
	public QiDefectResult createLETDefect(String productId,String productType, String defectTypeName, String processPoint,
			String createUser, String writeUpDept, String entryDept, boolean isGDPProcessPoint,
			boolean isTrpuProcessPoint,boolean isGlobalGDPEnabled) {
		StringBuilder selectedValue = new StringBuilder(GET_NAQ_DEFECT_DATA);
		Parameters params = Parameters.with("2", productId);
		params.put("1",defectTypeName);
		List<Object[]> resultSetList = findResultListByNativeQuery(selectedValue.toString(), params);

		if(resultSetList.size()>0) {
			Object[] vResultSet = resultSetList.get(0);
		
			QiDefectResult defectResult = new QiDefectResult();
			//Data configured from the query
			defectResult.setAfOnSequenceNumber(vResultSet[21]== null?null:((Integer)vResultSet[21]));
			defectResult.setInspectionPartName(StringUtils.defaultString((String) vResultSet[0]));
			defectResult.setInspectionPartLocationName(StringUtils.defaultString((String)vResultSet[1]));
			defectResult.setInspectionPartLocation2Name(StringUtils.defaultString((String)vResultSet[2]));
			defectResult.setDefectTypeName(StringUtils.defaultString((String)vResultSet[3]));
			defectResult.setDefectTypeName2(StringUtils.defaultString((String)vResultSet[4]));
			defectResult.setIqsVersion(StringUtils.defaultString((String)vResultSet[5]));
			defectResult.setIqsCategoryName(StringUtils.defaultString((String)vResultSet[6]));
			defectResult.setIqsQuestionNo(vResultSet[7]==null?null:((Integer)vResultSet[7]));
			defectResult.setIqsQuestion(StringUtils.defaultString((String)vResultSet[8]));
			defectResult.setThemeName(StringUtils.defaultString((String)vResultSet[9]));
			defectResult.setReportable(vResultSet[10]== null?null:(Short.parseShort(String.valueOf(vResultSet[10]))));
			defectResult.setResponsibleSite(StringUtils.defaultString((String)vResultSet[11]));
			defectResult.setResponsiblePlant(StringUtils.defaultString((String)vResultSet[12]));
			defectResult.setResponsibleDept(StringUtils.defaultString((String)vResultSet[13]));
			defectResult.setResponsibleLevel1(StringUtils.defaultString((String)vResultSet[14]));
			defectResult.setEntrySiteName(StringUtils.defaultString((String)vResultSet[15]));
			defectResult.setEntryPlantName(StringUtils.defaultString((String)vResultSet[16]));
			defectResult.setProductSpecCode(StringUtils.defaultString((String)vResultSet[18]));
			defectResult.setProductionLot(StringUtils.defaultString((String)vResultSet[19]));
			defectResult.setKdLotNumber(StringUtils.defaultString((String)vResultSet[20]));
			defectResult.setDefectCategoryName(StringUtils.defaultString((String)vResultSet[22]));
			defectResult.setLocalTheme(StringUtils.defaultString((String) vResultSet[23]));
			defectResult.setEntryScreen(StringUtils.defaultString((String)vResultSet[24]));
			defectResult.setEntryModel(StringUtils.defaultString((String)vResultSet[25]));
			defectResult.setProductId(StringUtils.defaultString((String)vResultSet[26]));
			defectResult.setInspectionPart2Name("");
			defectResult.setInspectionPart2LocationName("");
			defectResult.setInspectionPart2Location2Name("");
			defectResult.setInspectionPart3Name("");
			
			
			//Data received from method
			defectResult.setApplicationId(processPoint);
			defectResult.setWriteUpDept(writeUpDept);
			defectResult.setEntryDept(entryDept);
			defectResult.setCreateUser(createUser.toUpperCase());
			defectResult.setProductType(productType);
			
			defectResult.setDefectTransactionGroupId(getNextDefectTransactionGrounpId(defectResult));
			Terminal terminal = getDao(TerminalDao.class).findFirstByProcessPointId(processPoint);
			defectResult.setTerminalName(terminal==null?processPoint:terminal.getHostName());
			defectResult.setGdpDefect(isGDPProcessPoint?(short)1:(short)0);
			if(isGlobalGDPEnabled) {
				defectResult.setGdpDefect((short)1);				
			}
			defectResult.setTrpuDefect(isTrpuProcessPoint?(short)1:(short)0);
			Timestamp timestamp = getDatabaseTimeStamp();
			defectResult.setActualTimestamp(timestamp);
			defectResult.setGroupTimestamp(timestamp);
			String applicationId = defectResult.getApplicationId();
			ProcessPoint processPointData = getDao(ProcessPointDao.class).findByKey(applicationId);
			GpcsDivision gpcsDivision = getDao(GpcsDivisionDao.class).findByKey(processPointData.getDivisionId());
				
			if (gpcsDivision != null) {
				DailyDepartmentSchedule schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(
						gpcsDivision.getGpcsLineNo(), gpcsDivision.getGpcsProcessLocation(), 
						gpcsDivision.getGpcsPlantCode(), timestamp);
				
				if (schedule != null) {
					defectResult.setProductionDate(schedule.getId().getProductionDate());
					defectResult.setShift(schedule.getId().getShift());
					TeamRotation teamRotation= getDao(TeamRotationDao.class).findTeamDetails(
							gpcsDivision, schedule.getId().getShift(), schedule.getId().getProductionDate());
					if(teamRotation != null) {
						defectResult.setTeam(StringUtils.trimToEmpty(teamRotation.getId().getTeam()));
					}
				}
			}

			//Default data
			defectResult.setOriginalDefectStatus((short)DefectStatus.NOT_REPAIRED.getId());
			defectResult.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
			defectResult.setEngineFiringFlag((short)0);
			defectResult.setPddaModelYear(0.0f);		
			defectResult.setIncidentId(0);
			defectResult.setDeleted((short)0);
			
			return save(defectResult);
		}
		return null;

	}

}
