package com.honda.galc.dao.jpa.qi;


import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiLocalDefectCombinationDto;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiLocalDefectCombinationDaoImpl Class description</h3>
 * <p> QiLocalDefectCombinationDaoImpl description </p>
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
 * Sep 22 2016
 *
 *
 */
public class QiLocalDefectCombinationDaoImpl  extends BaseDaoImpl<QiLocalDefectCombination, Integer> implements QiLocalDefectCombinationDao {
	
	private static final String UPDATE_ENTRY_SCREEN_AND_MODEL_NAME = "update QiLocalDefectCombination e "
			+ "set e.entryScreen=:newEntryScreen, e.entryModel=:newEntryModel, e.updateUser=:updateUser "
			+ "where e.entryScreen=:oldEntryScreen and e.entryModel=:oldEntryModel and e.isUsed=:isUsed";
	
	private static final String UPDATE_ENTRY_MODEL_NAME = "update QiLocalDefectCombination e set e.entryModel=:newEntryModel, e.updateUser=:updateUser where e.entryModel=:oldEntryModel and e.isUsed=:isUsed";


	private static final String UPDATE_REPAIR_METHOD_NAME = "update GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX e set e.REPAIR_METHOD = null , e.UPDATE_USER =?1 where e.REPAIR_METHOD = ?2";

	private static final String UPDATE_LOCAL_DEFECT_COMBINATIONS_BY_REPAIR_METHOD = "update GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX  set REPAIR_METHOD = ?1, UPDATE_USER = ?2" +			
			" where REPAIR_METHOD = ?3";
	
	private static final String UPDATE_LOCAL_DEFECT_COMBINATIONS_BY_REPAIR_AREA = "update GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX  set REPAIR_AREA_NAME = ?1, UPDATE_USER = ?2" +			
			" where REPAIR_AREA_NAME = ?3";
    private final static String FIND_ALL_LOCAL_ATTRIBUTES_BY_PART_DFECT_ID  ="select e from QiLocalDefectCombination e where e.regionalDefectCombinationId in (:partDefectIdList)";
	private final static String FIND_ALL_LOCAL_ATTRIBUTES_BY_PART_LOCATION_ID  ="select e FROM QiLocalDefectCombination e WHERE e.regionalDefectCombinationId in(SELECT f.regionalDefectCombinationId FROM QiPartDefectCombination f where f.partLocationId in(:partLocationIdList))";
	
	private static final String FIND_ALL_PART_DEFECT_COMB_BY_DEFECT_ENTRY_TEXT_FILTER = "SELECT DISTINCT qiLocalDefectCombination.TEXT_ENTRY_MENU,qiLocalDefectCombination.RESPONSIBLE_LEVEL_ID,qiLocalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID,qiLocalDefectCombination.ENTRY_SCREEN, qiPartLocationCombination.INSPECTION_PART_NAME, qiPartLocationCombination.INSPECTION_PART_LOCATION_NAME, qiPartLocationCombination.INSPECTION_PART_LOCATION2_NAME, " + 
			"qiPartLocationCombination.INSPECTION_PART2_NAME, qiPartLocationCombination.INSPECTION_PART2_LOCATION_NAME, qiPartLocationCombination.INSPECTION_PART2_LOCATION2_NAME, " +
			"qiPartLocationCombination.INSPECTION_PART3_NAME, qiRegionalDefectCombination.DEFECT_TYPE_NAME, qiRegionalDefectCombination.DEFECT_TYPE_NAME2, " +
			"qiIqs.IQS_VERSION, qiIqs.IQS_CATEGORY, qiIqs.IQS_QUESTION_NO, qiIqs.IQS_QUESTION, " +
			"qiRegionalDefectCombination.THEME_NAME, qiLocalDefectCombination.REPORTABLE, qiLocalDefectCombination.REPAIR_AREA_NAME, qiLocalDefectCombination.REPAIR_METHOD, qiLocalDefectCombination.REPAIR_METHOD_TIME, qiLocalDefectCombination.LOCAL_THEME, " +
			"qiPddaResponsibility.PROCESS_NUMBER, qiPddaResponsibility.PROCESS_NAME, qiPddaResponsibility.UNIT_NUMBER, qiPddaResponsibility.UNIT_DESC, qiLocalDefectCombination.DEFECT_CATEGORY_NAME, qiLocalDefectCombination.ENGINE_FIRING_FLAG , qiLocalDefectCombination.LOCAL_DEFECT_COMBINATION_ID, " +
			"qiPddaResponsibility.MODEL_YEAR, qiPddaResponsibility.VEHICLE_MODEL_CODE " +
			"FROM GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX qiLocalDefectCombination " +
			"JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX qiRegionalDefectCombination on qiLocalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID=qiRegionalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID " +
			"JOIN QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC ON ESDC.REGIONAL_DEFECT_COMBINATION_ID = qiRegionalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID AND ESDC.ENTRY_SCREEN=qiLocalDefectCombination.ENTRY_SCREEN " +
			"JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX qiPartLocationCombination on qiPartLocationCombination.PART_LOCATION_ID=qiRegionalDefectCombination.PART_LOCATION_ID " + 
			"LEFT OUTER JOIN GALADM.QI_PDDA_RESPONSIBILITY_TBX qiPddaResponsibility ON qiLocalDefectCombination.PDDA_RESPONSIBILITY_ID=qiPddaResponsibility.PDDA_RESPONSIBILITY_ID " + 
			"JOIN GALADM.QI_IQS_TBX qiIqs ON qiIqs.IQS_ID=qiRegionalDefectCombination.IQS_ID " +
			"@MBPN_JOIN@" +
			"WHERE qiLocalDefectCombination.IS_USED=1 AND qiLocalDefectCombination.ENTRY_SCREEN=?1 AND qiLocalDefectCombination.ENTRY_MODEL=?2";
	
	private static final String FIND_ALL_PART_DEFECT_COMB_BY_DEFECT_ENTRY_IMAGE_FILTER = "SELECT DISTINCT qiImageSection.IMAGE_SECTION_ID,qiImageSection.PART_LOCATION_ID,qiLocalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID,qiLocalDefectCombination.RESPONSIBLE_LEVEL_ID,qiImageSection.IMAGE_NAME," + 
			"COALESCE(qiPartLocationCombination.INSPECTION_PART_NAME,'') AS INSPECTION_PART_NAME,COALESCE(qiPartLocationCombination.INSPECTION_PART_LOCATION_NAME,'') AS INSPECTION_PART_LOCATION_NAME, " + 
			"COALESCE(qiPartLocationCombination.INSPECTION_PART_LOCATION2_NAME,'') AS INSPECTION_PART_LOCATION2_NAME, " + 
			"COALESCE(qiPartLocationCombination.INSPECTION_PART2_NAME,'') AS INSPECTION_PART2_NAME,COALESCE(qiPartLocationCombination.INSPECTION_PART2_LOCATION_NAME,'') AS INSPECTION_PART2_LOCATION_NAME, " + 
			"COALESCE(qiPartLocationCombination.INSPECTION_PART2_LOCATION2_NAME,'') AS INSPECTION_PART2_LOCATION2_NAME, COALESCE(qiPartLocationCombination.INSPECTION_PART3_NAME,'') AS INSPECTION_PART3_NAME, " + 
			"COALESCE(qiRegionalDefectCombination.DEFECT_TYPE_NAME,'') AS DEFECT_TYPE_NAME,COALESCE(qiRegionalDefectCombination.DEFECT_TYPE_NAME2,'') AS DEFECT_TYPE_NAME2, " + 
			"qiIqs.IQS_VERSION, qiIqs.IQS_CATEGORY, qiIqs.IQS_QUESTION_NO, qiIqs.IQS_QUESTION, " + 
			"qiRegionalDefectCombination.THEME_NAME, qiLocalDefectCombination.REPORTABLE, qiLocalDefectCombination.REPAIR_AREA_NAME, qiLocalDefectCombination.REPAIR_METHOD, qiLocalDefectCombination.REPAIR_METHOD_TIME, qiLocalDefectCombination.LOCAL_THEME, " + 
			"qiPddaResponsibility.PROCESS_NUMBER, qiPddaResponsibility.PROCESS_NAME, qiPddaResponsibility.UNIT_NUMBER, qiPddaResponsibility.UNIT_DESC, qiLocalDefectCombination.DEFECT_CATEGORY_NAME, qiLocalDefectCombination.ENGINE_FIRING_FLAG, " + 
			"qiPddaResponsibility.MODEL_YEAR, qiPddaResponsibility.VEHICLE_MODEL_CODE, qiLocalDefectCombination.LOCAL_DEFECT_COMBINATION_ID " + 
			"FROM GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX qiLocalDefectCombination " + 
			"JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX qiRegionalDefectCombination ON qiLocalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID=qiRegionalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID " + 
			"JOIN QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC ON ESDC.REGIONAL_DEFECT_COMBINATION_ID = qiRegionalDefectCombination.REGIONAL_DEFECT_COMBINATION_ID AND ESDC.ENTRY_SCREEN=qiLocalDefectCombination.ENTRY_SCREEN  " +
			"JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX qiPartLocationCombination ON qiRegionalDefectCombination.PART_LOCATION_ID=qiPartLocationCombination.PART_LOCATION_ID " + 
			"JOIN GALADM.QI_IMAGE_SECTION_TBX qiImageSection ON qiPartLocationCombination.PART_LOCATION_ID=qiImageSection.PART_LOCATION_ID AND IMAGE_NAME = ?1 " + 
			"LEFT OUTER JOIN GALADM.QI_PDDA_RESPONSIBILITY_TBX qiPddaResponsibility ON qiLocalDefectCombination.PDDA_RESPONSIBILITY_ID=qiPddaResponsibility.PDDA_RESPONSIBILITY_ID " + 
			"JOIN GALADM.QI_IQS_TBX qiIqs ON qiIqs.IQS_ID=qiRegionalDefectCombination.IQS_ID " +
			"@MBPN_JOIN@" +
			"WHERE qiLocalDefectCombination.IS_USED=1 AND qiLocalDefectCombination.ENTRY_SCREEN=?2 AND qiLocalDefectCombination.ENTRY_MODEL=?3";
	
	private static final String MBPN_JOIN = "JOIN GALADM.QI_BOM_QICS_PART_MAPPING_TBX qiBomQicsPartMapping ON qiBomQicsPartMapping.INSPECTION_PART_NAME = qiPartLocationCombination.INSPECTION_PART_NAME ";
	
	private static final String FIND_COUNT_BY_ENTRY_SCREEN_AND_REGIONAL_DEFECT_COMBINATION_ID  = "select count(e) FROM QiLocalDefectCombination e where e.entryScreen = :entryScreen"
			+ " AND e.regionalDefectCombinationId IN("; 
	
	private static final String FIND_ALL_BY_ENTRY_SCREEN_AND_REGIONAL_DEFECT_COMBIANTION_ID = " SELECT * FROM QI_LOCAL_DEFECT_COMBINATION_TBX e "
			+ " WHERE  e.ENTRY_SCREEN = ?1 AND e.ENTRY_MODEL = ?2 AND e.IS_USED = ?3 AND e.REGIONAL_DEFECT_COMBINATION_ID NOT"
			+ " IN(select distinct a.REGIONAL_DEFECT_COMBINATION_ID from QI_LOCAL_DEFECT_COMBINATION_TBX a join "
			+ " QI_LOCAL_DEFECT_COMBINATION_TBX b on a.REGIONAL_DEFECT_COMBINATION_ID = b.REGIONAL_DEFECT_COMBINATION_ID where "
			+ " a.ENTRY_SCREEN =?1 AND a.ENTRY_MODEL = ?2 AND a.IS_USED = ?3 AND b.ENTRY_SCREEN = ?4 AND b.ENTRY_MODEL = ?5 AND b.IS_USED = ?6 ) and e.REGIONAL_DEFECT_COMBINATION_ID IN( ";

	
	private static final String FIND_ALL_MAPPED_BY_ENTRY_SCREENS_AND_REGIONAL_DEFECT_COMBIANTION_ID = "select e.localDefectCombinationId "
			+ "from QiLocalDefectCombination e where e.entryScreen = :toEntryScreen and e.entryModel = :toEntryModel and e.isUsed = :toIsUsed and e.regionalDefectCombinationId in"
			+ " (select a.regionalDefectCombinationId from QiLocalDefectCombination a where a.entryScreen = :fromEntryScreen and a.entryModel = :fromEntryModel and a.isUsed = :fromIsUsed and"
			+ " a.localDefectCombinationId = :defectCombId )";

	private static final String FIND_ALL_MODIFIED_BY_ENTRY_SCREENS = "SELECT QLDC.REGIONAL_DEFECT_COMBINATION_ID, "
			+ " QLDC.ENTRY_SITE_NAME, QLDC.ENTRY_PLANT_NAME, QLDC.RESPONSIBLE_LEVEL_ID, QLDC.PDDA_RESPONSIBILITY_ID, QLDC.REPAIR_METHOD, QLDC.REPAIR_METHOD_TIME, "
			+ " QLDC.ESTIMATED_TIME_TO_FIX, QLDC.LOCAL_THEME, QLDC.ENGINE_FIRING_FLAG, QLDC.REPAIR_AREA_NAME, QLDC.DEFECT_CATEGORY_NAME, QLDC.REPORTABLE FROM"
			+ " GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX QLDC WHERE QLDC.ENTRY_SCREEN = ?1  AND QLDC.ENTRY_MODEL = ?2 AND QLDC.IS_USED = ?3  "
			+ " EXCEPT "
			+ " SELECT E.REGIONAL_DEFECT_COMBINATION_ID, E.ENTRY_SITE_NAME, E.ENTRY_PLANT_NAME,  E.RESPONSIBLE_LEVEL_ID, "
			+ " E.PDDA_RESPONSIBILITY_ID, E.REPAIR_METHOD, E.REPAIR_METHOD_TIME, E.ESTIMATED_TIME_TO_FIX, "
			+ " E.LOCAL_THEME, E.ENGINE_FIRING_FLAG, E.REPAIR_AREA_NAME, E.DEFECT_CATEGORY_NAME, E.REPORTABLE FROM "
			+ " GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX E WHERE E.ENTRY_SCREEN = ?4 AND E.ENTRY_MODEL = ?5 AND E.IS_USED = ?6 ";

	private static final String UPDATE_VERSION_VALUE = "update GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX e set e.IS_USED = ?1 where e.ENTRY_MODEL = ?2 and e.IS_USED=?3";
	
	private static final String FIND_ALL_BY_SITE_PLANT = "SELECT e FROM QiLocalDefectCombination e, QiResponsibleLevel l1 "
			+ "WHERE l1.responsibleLevelId = e.responsibleLevelId AND l1.site = :site AND l1.plant = :plant AND l1.level = 1";
	
	private static final String FIND_ALL_BY_SITE_PLANT_DEPARTMENT = "SELECT e FROM QiLocalDefectCombination e, QiResponsibleLevel l1 "
	+ "WHERE l1.responsibleLevelId = e.responsibleLevelId AND l1.site = :site AND l1.plant = :plant AND l1.department = :dept AND l1.level = 1";
	
	private static final String FIND_ALL_BY_RESPONSIBILITY_LEVEL1 = "SELECT e FROM QiLocalDefectCombination e, QiResponsibleLevel l1 "
			+ "WHERE l1.responsibleLevelId = e.responsibleLevelId AND l1.site = :site AND l1.plant = :plant AND l1.department = :dept AND l1.responsibleLevelName = :responsibleLevel";
	
	private static final String FIND_ALL_BY_RESPONSIBILITY_LEVEL2 = "SELECT e FROM QiLocalDefectCombination e WHERE e.responsibleLevelId in ( "
			+ "SELECT DISTINCT l1.responsibleLevelId FROM QiResponsibleLevel l1, QiResponsibleLevel l2 "
			+ "WHERE l1.upperResponsibleLevelId = l2.responsibleLevelId AND l2.site = :site AND l2.plant = :plant AND l2.department = :dept "
			+ "AND l2.responsibleLevelName = :responsibleLevel )";
	
	private static final String FIND_ALL_BY_RESPONSIBILITY_LEVEL3 = "SELECT e FROM QiLocalDefectCombination e WHERE e.responsibleLevelId in ( "
			+ "SELECT DISTINCT l1.responsibleLevelId FROM QiResponsibleLevel l1, QiResponsibleLevel l2, QiResponsibleLevel l3 "
			+ "WHERE l1.upperResponsibleLevelId = l2.responsibleLevelId AND l2.upperResponsibleLevelId = l3.responsibleLevelId "
			+ "AND l3.site = :site AND l3.plant = :plant AND l3.department = :dept AND l3.responsibleLevelName = :responsibleLevel )";
	
	private static final String UPDATE_LOCAL_THEME = "update GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX e set e.LOCAL_THEME = ?1, e.UPDATE_USER =?2 where e.LOCAL_THEME = ?3";
	
	private static final String DELETE_BY_PLANT_ENTRY_SCREEN_MODEL = "delete from QiLocalDefectCombination e "
			+ "where trim(e.entryPlantName)=:plantName and trim(e.entryModel)=:entryModel and trim(e.entryScreen)=:entryScreen";
	
	private static final String DELETE_BY_PLANT_ENTRY_SCREEN_MENU_MODEL = "delete from QiLocalDefectCombination e "
			+ "where trim(e.entryPlantName)=:plantName and trim(e.entryModel)=:entryModel and trim(e.entryScreen)=:entryScreen "
			+ "and trim(e.textEntryMenu)=:menu";
	
	private static final String DELETE_LIST_BY_PLANT_ENTRY_SCREEN_MENU_MODEL = "delete from QiLocalDefectCombination e "
			+ "where trim(e.entryPlantName)=:plantName and trim(e.entryModel)=:entryModel and trim(e.entryScreen)=:entryScreen "
			+ "and trim(e.textEntryMenu)=:menu and e.regionalDefectCombinationId in (%s)";
	
	private static final String FIND_FIRST_X_BY_PLANT_AND_ENTRY_MODEL = "SELECT * FROM GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX "
			+ "WHERE ENTRY_MODEL = ?1 AND ENTRY_PLANT_NAME = ?2 AND LOCAL_DEFECT_COMBINATION_ID > ?3 "
			+ "ORDER BY LOCAL_DEFECT_COMBINATION_ID FETCH FIRST 1000 ROWS ONLY";
	
	public QiLocalDefectCombination findByRegionalDefectCombinationId(Integer regionalDefectCombinationId, String entryScreen){
		return findFirst(Parameters.with("regionalDefectCombinationId", regionalDefectCombinationId).put("entryScreen", entryScreen));
	}

	public List<QiLocalDefectCombination> findAllByEntryScreen(String entryScreenName) {						
		return findAll(Parameters.with("entryScreen", entryScreenName));
	}

	public List<QiLocalDefectCombination> findAllByRepairMethod(String repairMethodName) {						
		return findAll(Parameters.with("repairMethod", repairMethodName));
	}

	@Transactional
	public void setAllLocalDefectCombinationsToUnassigned(String oldRepairMethodName, String userId) {
		Parameters params = Parameters.with("1", userId)				
				.put("2", oldRepairMethodName);
		executeNativeUpdate(UPDATE_REPAIR_METHOD_NAME, params);
	}

	@Transactional
	public void updateAllByRepairMethod(String repairMethodName, String oldRepairMethodName, String updateUser) {
		Parameters params = Parameters.with("1", repairMethodName)
				.put("2", updateUser)
				.put("3", oldRepairMethodName);			
		executeNativeUpdate(UPDATE_LOCAL_DEFECT_COMBINATIONS_BY_REPAIR_METHOD, params);
	}
    public List<QiLocalDefectCombination> findAllLocalAttributesByPartDefectId(List<Integer> partDefectIdList) {
		Parameters params = Parameters.with("partDefectIdList", partDefectIdList);
		return findAllByQuery(FIND_ALL_LOCAL_ATTRIBUTES_BY_PART_DFECT_ID,params);
	
	}

	public List<QiLocalDefectCombination> findAllLocalAttributesByPartLocationId(List<Integer> partLocationIdList) {
		Parameters params = Parameters.with("partLocationIdList", partLocationIdList);
		return findAllByQuery(FIND_ALL_LOCAL_ATTRIBUTES_BY_PART_LOCATION_ID,params);	
	}

	
	/**
	 * This method finds all local attribute assigned for a regionalDefectCombinationId
	 * @param regionalDefectCombinationId
	 * @return List<QiLocalDefectCombination>
	 */
	public List<QiLocalDefectCombination> findAllByRegionalDefectCombinationId(Integer regionalDefectCombinationId){
		return findAll(Parameters.with("regionalDefectCombinationId", regionalDefectCombinationId));
	}
	
	public boolean isRepairAreaUsed(String repairAreaName) {						
		QiLocalDefectCombination qiLocalDefectCombination = findFirst(Parameters.with("repairAreaName", repairAreaName)); 
		if (qiLocalDefectCombination != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional
	public void updateAllByRepairArea(String repairMethodName, String oldRepairMethodName, String updateUser) {
		Parameters params = Parameters.with("1", repairMethodName)
				.put("2", updateUser)
				.put("3", oldRepairMethodName);			
		executeNativeUpdate(UPDATE_LOCAL_DEFECT_COMBINATIONS_BY_REPAIR_AREA, params);
	}
	
	public List<QiLocalDefectCombination> findAllByTextEntryMenu(String entryScreen, String textEntryMenu) {
		return findAll(Parameters.with("entryScreen", entryScreen).put("textEntryMenu", textEntryMenu));
		
	}
	
	public List<QiLocalDefectCombination> findAllByTextMenuAndEntryScreen(String entryScreen, String textEntryMenu,String entryModel) {
		Parameters params = Parameters.with("entryScreen", entryScreen)
				.put("textEntryMenu",textEntryMenu).put("entryModel", entryModel);
		return findAll(params);
	}

	public List<QiLocalDefectCombination> findAllByPlantAndSite(String plant,String siteName) {
		return findAll(Parameters.with("entryPlantName", plant).put("entrySiteName",siteName));
	}
	
	public List<QiLocalDefectCombination> findAllBySite(String siteName) {
		return findAll(Parameters.with("entrySiteName",siteName));
	}
	
	
	
	/**
	 * This method is used to find all Part Defect Combination by filter
	 */
	public List<QiDefectResultDto> findAllPartDefectCombByDefectEntryTextFilter(String entryScreen, String entryModel, String inspectionPartName, String mainPartNo) { 
		Parameters params = Parameters.with("1", entryScreen).put("2", entryModel);
		StringBuilder findCombForSelectedValue = new StringBuilder(FIND_ALL_PART_DEFECT_COMB_BY_DEFECT_ENTRY_TEXT_FILTER.replace("@MBPN_JOIN@", StringUtils.isBlank(mainPartNo) ? "" : MBPN_JOIN));
		if(!StringUtils.isBlank(inspectionPartName)){
			params.put("3", inspectionPartName);
			findCombForSelectedValue.append(" AND qiPartLocationCombination.INSPECTION_PART_NAME=?3");
			if (!StringUtils.isBlank(mainPartNo)) {
				params.put("4", mainPartNo);
				findCombForSelectedValue.append(" AND qiBomQicsPartMapping.MAIN_PART_NO=?4");
			}
		} else if (!StringUtils.isBlank(mainPartNo)) {
			params.put("3", mainPartNo);
			findCombForSelectedValue.append(" AND qiBomQicsPartMapping.MAIN_PART_NO=?3");
		}
		findCombForSelectedValue.append(" ORDER BY qiPartLocationCombination.INSPECTION_PART_NAME");

		List<Object[]> partCombObjectArrayList= findAllByNativeQuery(findCombForSelectedValue.toString(), params, Object[].class);
		List<QiDefectResultDto> qiDefectResultDtoList=new ArrayList<QiDefectResultDto>();
		for(Object[] partCombObjectArray: partCombObjectArrayList)
		{
			QiDefectResultDto defectResultDto=new QiDefectResultDto();
			defectResultDto.setTextEntryMenu((String) partCombObjectArray[0]);
			if(partCombObjectArray[1]!=null)
				defectResultDto.setResponsibleLevelId((Integer)partCombObjectArray[1]);
			if(partCombObjectArray[2]!=null)
				defectResultDto.setRegionalDefectCombinationId((Integer)partCombObjectArray[2]);
			defectResultDto.setEntryScreen((String)partCombObjectArray[3]);
			defectResultDto.setInspectionPartName((String)partCombObjectArray[4]);
			defectResultDto.setInspectionPartLocationName((String)partCombObjectArray[5]);
			defectResultDto.setInspectionPartLocation2Name((String)partCombObjectArray[6]);
			defectResultDto.setInspectionPart2Name((String)partCombObjectArray[7]);
			defectResultDto.setInspectionPart2LocationName((String)partCombObjectArray[8]);
			defectResultDto.setInspectionPart2Location2Name((String)partCombObjectArray[9]);
			defectResultDto.setInspectionPart3Name((String)partCombObjectArray[10]);
			defectResultDto.setDefectTypeName((String)partCombObjectArray[11]);
			defectResultDto.setDefectTypeName2((String)partCombObjectArray[12]);
			defectResultDto.setIqsVersion((String)partCombObjectArray[13]);
			defectResultDto.setIqsCategory((String)partCombObjectArray[14]);
			if(partCombObjectArray[15]!=null)
				defectResultDto.setIqsQuestionNo((Integer)partCombObjectArray[15]);
			defectResultDto.setIqsQuestion((String)partCombObjectArray[16]);
			defectResultDto.setThemeName((String)partCombObjectArray[17]);
			if(partCombObjectArray[18]!=null)
				defectResultDto.setReportable(((Integer)partCombObjectArray[18]).shortValue());
			defectResultDto.setRepairAreaName((String)partCombObjectArray[19]);
			defectResultDto.setRepairMethod((String)partCombObjectArray[20]);
			if(partCombObjectArray[21]!=null)
				defectResultDto.setRepairMethodTime(((Integer)partCombObjectArray[21]).shortValue());
			defectResultDto.setLocalTheme((String)partCombObjectArray[22]);
			defectResultDto.setProcessNumber((String)partCombObjectArray[23]);
			defectResultDto.setProcessName((String)partCombObjectArray[24]);
			defectResultDto.setUnitNumber((String)partCombObjectArray[25]);
			defectResultDto.setUnitDesc((String)partCombObjectArray[26]);
			defectResultDto.setDefectCategoryName((String)partCombObjectArray[27]);
			if(partCombObjectArray[28]!=null)
				defectResultDto.setEngineFiringFlag(((Integer)partCombObjectArray[28]).shortValue());
			if(partCombObjectArray[29]!=null)
				defectResultDto.setLocalDefectCombinationId((Integer)partCombObjectArray[29]);
			if(partCombObjectArray[30]!=null)
				defectResultDto.setModelYear(((BigDecimal)partCombObjectArray[30]).floatValue());
			defectResultDto.setVehicleModelCode((String)partCombObjectArray[31]);
			qiDefectResultDtoList.add(defectResultDto);
		}
		return qiDefectResultDtoList;
	}
	
	/**
	 * This method is used to fetch All Part Defect Combination by Defect Entry Filter
	 * @param imageSectionId
	 * @param defectTypeName
	 * @param defectTypeName2
	 * @param partLocationId
	 * @param mtcModel
	 * @param processPointId
	 * @param productKind
	 * @return
	 */
	public List<QiDefectResultDto> findAllPartDefectCombByDefectEntryImageFilter(String imageName, String entryScreen, String entryModel, String inspectionPartName, String mainPartNo) {
		Parameters params = Parameters.with("1", imageName).put("2", entryScreen).put("3", entryModel);
		StringBuilder findCombForSelectedValue = new StringBuilder(FIND_ALL_PART_DEFECT_COMB_BY_DEFECT_ENTRY_IMAGE_FILTER.replace("@MBPN_JOIN@", StringUtils.isBlank(mainPartNo) ? "" : MBPN_JOIN));
		if(!StringUtils.isBlank(inspectionPartName)) {
			params.put("4", inspectionPartName);
			findCombForSelectedValue.append(" AND qiPartLocationCombination.INSPECTION_PART_NAME=?4");
			if (!StringUtils.isBlank(mainPartNo)) {
				params.put("5", mainPartNo);
				findCombForSelectedValue.append(" AND qiBomQicsPartMapping.MAIN_PART_NO=?5");
			}
		} else if (!StringUtils.isBlank(mainPartNo)) {
			params.put("4", mainPartNo);
			findCombForSelectedValue.append(" AND qiBomQicsPartMapping.MAIN_PART_NO=?4");
		}
		List<Object[]> partCombObjectArrayList= findAllByNativeQuery(findCombForSelectedValue.toString(), params, Object[].class);
		List<QiDefectResultDto> qiDefectResultDtoList=new ArrayList<QiDefectResultDto>();
		for(Object[] partCombObjectArray: partCombObjectArrayList)
		{
			QiDefectResultDto defectResultDto=new QiDefectResultDto();
			if(partCombObjectArray[0]!=null)
				defectResultDto.setImageSectionId((Integer) partCombObjectArray[0]);
			if(partCombObjectArray[1]!=null)
				defectResultDto.setPartLocationId((Integer)partCombObjectArray[1]);
			if(partCombObjectArray[2]!=null)
				defectResultDto.setRegionalDefectCombinationId((Integer)partCombObjectArray[2]);
			if(partCombObjectArray[3]!=null)
				defectResultDto.setResponsibleLevelId((Integer)partCombObjectArray[3]);
			defectResultDto.setImageName((String)partCombObjectArray[4]);
			defectResultDto.setInspectionPartName((String)partCombObjectArray[5]);
			defectResultDto.setInspectionPartLocationName((String)partCombObjectArray[6]);
			defectResultDto.setInspectionPartLocation2Name((String)partCombObjectArray[7]);
			defectResultDto.setInspectionPart2Name((String)partCombObjectArray[8]);
			defectResultDto.setInspectionPart2LocationName((String)partCombObjectArray[9]);
			defectResultDto.setInspectionPart2Location2Name((String)partCombObjectArray[10]);
			defectResultDto.setInspectionPart3Name((String)partCombObjectArray[11]);
			defectResultDto.setDefectTypeName((String)partCombObjectArray[12]);
			defectResultDto.setDefectTypeName2((String)partCombObjectArray[13]);
			defectResultDto.setIqsVersion((String)partCombObjectArray[14]);
			defectResultDto.setIqsCategory((String)partCombObjectArray[15]);
			if(partCombObjectArray[16]!=null)
				defectResultDto.setIqsQuestionNo((Integer)partCombObjectArray[16]);
			defectResultDto.setIqsQuestion((String)partCombObjectArray[17]);
			defectResultDto.setThemeName((String)partCombObjectArray[18]);
			if(partCombObjectArray[19]!=null)
				defectResultDto.setReportable(((Integer)partCombObjectArray[19]).shortValue());
			defectResultDto.setRepairAreaName((String)partCombObjectArray[20]);
			defectResultDto.setRepairMethod((String)partCombObjectArray[21]);
			if(partCombObjectArray[22]!=null)
				defectResultDto.setRepairMethodTime(((Integer)partCombObjectArray[22]).shortValue());
			defectResultDto.setLocalTheme((String)partCombObjectArray[23]);
			defectResultDto.setProcessNumber((String)partCombObjectArray[24]);
			defectResultDto.setProcessName((String)partCombObjectArray[25]);
			defectResultDto.setUnitNumber((String)partCombObjectArray[26]);
			defectResultDto.setUnitDesc((String)partCombObjectArray[27]);
			defectResultDto.setDefectCategoryName((String)partCombObjectArray[28]);
			if(partCombObjectArray[29]!=null)
				defectResultDto.setEngineFiringFlag(((Integer)partCombObjectArray[29]).shortValue());
			if(partCombObjectArray[30]!=null)
				defectResultDto.setModelYear(((BigDecimal)partCombObjectArray[30]).floatValue());
			defectResultDto.setVehicleModelCode((String)partCombObjectArray[31]);
			defectResultDto.setLocalDefectCombinationId((Integer) partCombObjectArray[32]);
			qiDefectResultDtoList.add(defectResultDto);
		}
		return qiDefectResultDtoList;
	}
	
	
	
	public long findCountByRegionalAndScreenName(String entryScreen, String regionlIds) {
		return count(FIND_COUNT_BY_ENTRY_SCREEN_AND_REGIONAL_DEFECT_COMBINATION_ID + regionlIds +" )", Parameters.with("entryScreen", entryScreen));
	}

	public boolean isLocalThemeInUseByLocalDefect(String localTheme) {
		return null != findFirst(Parameters.with("localTheme", localTheme));
	}
	
	@Transactional
	public void updateLocalThemeForLocalDefects(String newLocalThemeName, String userId,
			String oldLocalThemeName) {
		executeNativeUpdate(UPDATE_LOCAL_THEME,Parameters.with("1", newLocalThemeName).put("2", userId).put("3", oldLocalThemeName));
	}
	
	public QiLocalDefectCombination findByRegionalDefectCombinationIdAndEntryScreen(Integer regionalDefectCombinationId, String entryScreen){
		return findFirst(Parameters.with("regionalDefectCombinationId", regionalDefectCombinationId).put("entryScreen", entryScreen));
	}
	
	public QiLocalDefectCombination findByRegionalDefectCombinationId(Integer regionalDefectCombinationId, String entryScreen, String entryModel, short version){
		return findFirst(Parameters.with("regionalDefectCombinationId", regionalDefectCombinationId)
				.put("entryScreen", entryScreen)
				.put("entryModel", entryModel)
				.put("isUsed", version));
	}

	public List<QiLocalDefectCombination> findAllByPlantEntryScreenModelMenuAndRegionalId(Integer regionalDefectCombinationId, String plant, String entryScreen, String entryModel, String entryMenu, short version){
		return findAll(Parameters.with("regionalDefectCombinationId", regionalDefectCombinationId)
				.put("entryPlantName", plant)
				.put("entryScreen", entryScreen)
				.put("entryModel", entryModel)
				.put("textEntryMenu", entryMenu)
				.put("isUsed", version));
	}

	public List<QiLocalDefectCombination> findAllByEntryScreenAndModel(String entryScreen, String entryModel, short version) {
		return findAll(Parameters.with("entryScreen", entryScreen)
				.put("entryModel", entryModel)
				.put("isUsed", version));
	}

	@Transactional
	public void updateEntryScreenAndModel(QiEntryScreenId newEntryScreen, String oldEntryScreen, String oldEntryModel, short oldVersion, String userID) {
		Parameters params = Parameters.with("newEntryScreen", newEntryScreen.getEntryScreen())
				.put("newEntryModel", newEntryScreen.getEntryModel())
				.put("updateUser", userID)
				.put("oldEntryScreen", oldEntryScreen)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", oldVersion);
		executeUpdate(UPDATE_ENTRY_SCREEN_AND_MODEL_NAME, params);
	}
	
	@Transactional
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel, short isUsed) {
		Parameters params = Parameters.with("newEntryModel", newEntryModel)
				.put("updateUser", userId)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", isUsed);
		executeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	
	public List<QiLocalDefectCombination> findAllByEntryModelAndIsUsed(String entryModel, short version){
	    return findAll(Parameters.with("entryModel", entryModel).put("isUsed", version));	
	}
	
	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion) {
		executeNativeUpdate(UPDATE_VERSION_VALUE,Parameters.with("1", newVersion).put("2", entryModel).put("3", oldVersion));
	}
	
	@Transactional
	public void removeByEntryModelAndVersion(String entryModel, short version) {
		delete(Parameters.with("entryModel",entryModel).put("isUsed", version));
	}
	
	@Transactional
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version) {
		delete(Parameters.with("entryModel", entryModel).put("entryScreen", entryScreen)
				.put("isUsed", version));
	}
	
	public List<QiLocalDefectCombination> findAllByEntryModelAndVersion(String entryModel, short version) {
		return findAll(Parameters.with("entryModel", entryModel)
				.put("isUsed", version));
		
	}

	public List<QiLocalDefectCombination> findAllByLocalDefectCombination(QiLocalDefectCombination localCombination, short version) {
		return findAll(Parameters.with("entryModel", localCombination.getEntryModel())
				.put("regionalDefectCombinationId", localCombination.getRegionalDefectCombinationId())
				.put("entrySiteName", localCombination.getEntrySiteName())
				.put("entryPlantName", localCombination.getEntryPlantName())
				.put("responsibleLevelId", localCombination.getResponsibleLevelId())
				.put("pddaResponsibilityId", localCombination.getPddaResponsibilityId())
				.put("entryScreen", localCombination.getEntryScreen())
				.put("textEntryMenu", localCombination.getTextEntryMenu())
				.put("repairMethod", localCombination.getRepairMethod())
				.put("repairMethodTime", localCombination.getRepairMethodTime())
				.put("estimatedTimeToFix", localCombination.getEstimatedTimeToFix())
				.put("engineFiringFlag", localCombination.getEngineFiringFlag())
				.put("repairAreaName", localCombination.getRepairAreaName())
				.put("defectCategoryName", localCombination.getDefectCategoryName())
				.put("reportable", localCombination.getReportable())
				.put("isUsed", version));
	}
	
	public List<QiLocalDefectCombination> findAllLocalDefectCombinationsByLocalDefectId(QiEntryScreenId fromEntryScreenId, QiEntryScreenId toEntryScreenId,
			List<Integer> regionalDefectCombList) {
		return findAllByNativeQuery(FIND_ALL_BY_ENTRY_SCREEN_AND_REGIONAL_DEFECT_COMBIANTION_ID + StringUtils.join(regionalDefectCombList, ',') + ")",
				Parameters.with("1", fromEntryScreenId.getEntryScreen()).put("2", fromEntryScreenId.getEntryModel()).put("3", fromEntryScreenId.getIsUsed())
				.put("4", toEntryScreenId.getEntryScreen()).put("5", toEntryScreenId.getEntryModel()).put("6", toEntryScreenId.getIsUsed()), 
				QiLocalDefectCombination.class);	
	}

	public Integer findMappingOfMatchingLocalDefectsIds(QiEntryScreenId fromEntryScreen, QiEntryScreenId toEntryScreen,
			Integer regionalDefectCombId) {
		
		return findFirstByQuery(FIND_ALL_MAPPED_BY_ENTRY_SCREENS_AND_REGIONAL_DEFECT_COMBIANTION_ID, Integer.class, 
				Parameters.with("toEntryScreen", toEntryScreen.getEntryScreen()).put("toEntryModel", toEntryScreen.getEntryModel()).put("toIsUsed", toEntryScreen.getIsUsed())
				.put("fromEntryScreen", fromEntryScreen.getEntryScreen()).put("fromEntryModel", fromEntryScreen.getEntryModel()).put("fromIsUsed", fromEntryScreen.getIsUsed())
				.put("defectCombId", regionalDefectCombId));
	}

	public List<QiLocalDefectCombinationDto> findAllModifiedByEntryScreens(QiEntryScreenId fromEntryScreen, QiEntryScreenId toEntryScreen) {
		return findAllByNativeQuery(FIND_ALL_MODIFIED_BY_ENTRY_SCREENS,
				Parameters.with("1", fromEntryScreen.getEntryScreen()).put("2", fromEntryScreen.getEntryModel()).put("3", fromEntryScreen.getIsUsed())
				.put("4", toEntryScreen.getEntryScreen()).put("5", toEntryScreen.getEntryModel()).put("6", toEntryScreen.getIsUsed())
				, QiLocalDefectCombinationDto.class);
	}


	/**
	 * This method finds all local attribute assigned for a pddaResponsibilityid
	 * @param pddaResponsibilityId
	 * @return List<QiLocalDefectCombination>
	 */
	public List<QiLocalDefectCombination> findAllByPddaResponsibilityId(Integer pddaResponsibilityId) {
		return findAll(Parameters.with("pddaResponsibilityId", pddaResponsibilityId));
	}
	
	public List<QiLocalDefectCombination> findAllBySiteAndPlant(String site, String plantName) {
		return findAllByQuery(FIND_ALL_BY_SITE_PLANT,Parameters.with("site", site).put("plant", plantName));
	}

	public List<QiLocalDefectCombination> findAllBySitePlantAndDepartment(String site, String plantName,
			String department) {
		return findAllByQuery(FIND_ALL_BY_SITE_PLANT_DEPARTMENT,Parameters.with("site", site).put("plant", plantName).put("dept", department));
	}

	/*this method should not be used to fetch a list for the sole purpose of getting a count
	because there will be a large number of rows.  Use the countAllByResponsibleLevel method instead
	@see countAllByResponsibleLevel
	*/
	public List<QiLocalDefectCombination> findAllByResponsibleLevel(String site,
			String plantName, String department, String responsibility, short level) {
		Parameters params = Parameters.with("site", site).put("plant", plantName)
				.put("dept", department).put("responsibleLevel", responsibility);
		if(level == 1) {
			return findAllByQuery(FIND_ALL_BY_RESPONSIBILITY_LEVEL1, params);
		}
		else if(level == 2) {
			return findAllByQuery(FIND_ALL_BY_RESPONSIBILITY_LEVEL2, params);
			
		} else if (level == 3) {
			return findAllByQuery(FIND_ALL_BY_RESPONSIBILITY_LEVEL3, params);
		}
		return null;
	}
	
	/**
	 * This method is count all assigned Local Defect Combinations by responsibleLevelId
	 */
	public long countByResponsibleLevelId(int responsibleLevelId) {
		Parameters params = Parameters.with("responsibleLevelId", responsibleLevelId);
		return count(params);
	}

	/**
	 * This method is count all assigned station responsibilities by responsibleLevelId
	 */
	private static final String COUNT_ALL_BY_RESPONSIBILITY_LEVEL2 =
			"SELECT count(e) FROM QiLocalDefectCombination e WHERE e.responsibleLevelId in "
			+ "(SELECT l1.responsibleLevelId FROM QiResponsibleLevel l1 "
			+ "WHERE l1.upperResponsibleLevelId= :responsibleLevelId )";
	
	public long countByResponsibleLevel2(int responsibleLevelId) {
		Parameters params = Parameters.with("responsibleLevelId", responsibleLevelId);
		return count(COUNT_ALL_BY_RESPONSIBILITY_LEVEL2, params);
	}

	private static final String COUNT_ALL_BY_RESPONSIBILITY_LEVEL3 =
			"SELECT count(e) FROM QiLocalDefectCombination e WHERE e.responsibleLevelId in "
			+ "(SELECT l1.responsibleLevelId FROM QiResponsibleLevel l1, QiResponsibleLevel l2 "
			+ "WHERE l1.upperResponsibleLevelId = l2.responsibleLevelId AND l2.upperResponsibleLevelId = :responsibleLevelId) ";
		
	public long countByResponsibleLevel3(int responsibleLevelId) {
		Parameters params = Parameters.with("responsibleLevelId", responsibleLevelId);
		return count(COUNT_ALL_BY_RESPONSIBILITY_LEVEL3, params);
	}

	@Transactional
	public void deleteLDCByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel)  {
		Parameters params = Parameters.with("plantName", plantName.trim())
				.put("entryScreen", entryScreen.trim())
				.put("entryModel", entryModel.trim());
		executeUpdate(DELETE_BY_PLANT_ENTRY_SCREEN_MODEL, params);
		
	}

	@Transactional
	public void deleteLDCByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel)  {
		Parameters params = Parameters.with("plantName", plantName.trim())
				.put("entryScreen", entryScreen.trim())
				.put("menu", menu.trim())
				.put("entryModel", entryModel.trim());
		executeUpdate(DELETE_BY_PLANT_ENTRY_SCREEN_MENU_MODEL, params);
		
	}
	@Transactional
	public void deleteListByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel, String regionalIdList)  {
		Parameters params = Parameters.with("plantName", plantName.trim())
				.put("entryScreen", entryScreen.trim())
				.put("menu", menu.trim())
				.put("entryModel", entryModel.trim());
		executeUpdate(String.format(DELETE_LIST_BY_PLANT_ENTRY_SCREEN_MENU_MODEL, regionalIdList), params);		
	}
	public List<QiLocalDefectCombination> findFirstXByPlantAndModel(String entryModel, String plant, int lastHighId) {
		 Parameters params = Parameters.with("1", entryModel).put("2", plant).put("3", lastHighId);
		return findAllByNativeQuery(FIND_FIRST_X_BY_PLANT_AND_ENTRY_MODEL, params);
	}
	
	@Override
	public long countByEntryModelAndPlant(String entryPlant, String entryModel) {
		return count(Parameters.with("entryPlantName", entryPlant)
				.put("entryModel", entryModel));
	}
	


}


