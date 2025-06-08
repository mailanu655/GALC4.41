package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiExternalSystemDefectMapDaoImpl Class description</h3>
 * <p> QiExternalSystemDefectMapDaoImpl description </p>
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
 * Feb 13, 2017
 */

public class QiExternalSystemDefectMapDaoImpl extends BaseDaoImpl<QiExternalSystemDefectMap, Integer> implements QiExternalSystemDefectMapDao {
	

	private String FIND_EXTERNAL_SYSTEM_NAME ="select * from galadm.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX where EXTERNAL_SYSTEM_NAME=?1 and EXTERNAL_PART_CODE=?2 and EXTERNAL_DEFECT_CODE=?3";
	
	private static final String FIND_ALL_TEXT_ENTRY_MENU_BY_ENTRY_SCREEN_AND_MODEL_IMAGE=
		"select distinct LDC.TEXT_ENTRY_MENU "+
		"from galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC "+
		"join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on  LDC.REGIONAL_DEFECT_COMBINATION_ID=ESDC.REGIONAL_DEFECT_COMBINATION_ID "+
		"join galadm.QI_ENTRY_SCREEN_TBX ES on ESDC.ENTRY_SCREEN=ES.ENTRY_SCREEN "+
		"join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID "+
		"join galadm.QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID=RDC.PART_LOCATION_ID  "+
	    "where LDC.ENTRY_SCREEN=ES.ENTRY_SCREEN and RDC.ACTIVE=1 AND (RDC.IQS_ID IS NOT NULL AND  RDC.IQS_ID <> 0) AND  COALESCE(RDC.THEME_NAME,'') != '' "+ 
		"and LDC.ENTRY_SCREEN=?1 and ES.ENTRY_MODEL=?2  and COALESCE(LDC.TEXT_ENTRY_MENU,'') != '' order by LDC.TEXT_ENTRY_MENU";
	
	private static final String FIND_ALL_TEXT_ENTRY_MENU_BY_ENTRY_SCREEN_AND_MODEL_TEXT=
		"select distinct LDC.TEXT_ENTRY_MENU "+
		"from galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC "+
		"join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on  LDC.REGIONAL_DEFECT_COMBINATION_ID=ESDC.REGIONAL_DEFECT_COMBINATION_ID "+
		"join galadm.QI_ENTRY_SCREEN_TBX ES on ESDC.ENTRY_SCREEN=ES.ENTRY_SCREEN "+
		"join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID "+
		"join galadm.QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID=RDC.PART_LOCATION_ID  "+
		"join galadm.QI_TEXT_ENTRY_MENU_TBX TEM on TEM.TEXT_ENTRY_MENU=LDC.TEXT_ENTRY_MENU "+
	    "where LDC.ENTRY_SCREEN=ES.ENTRY_SCREEN and RDC.ACTIVE=1 AND (RDC.IQS_ID IS NOT NULL AND  RDC.IQS_ID <> 0) AND  COALESCE(RDC.THEME_NAME,'') != '' "+ 
		"and LDC.ENTRY_SCREEN=TEM.ENTRY_SCREEN and LDC.ENTRY_SCREEN=?1 and ES.ENTRY_MODEL=?2  and COALESCE(LDC.TEXT_ENTRY_MENU,'') != '' order by LDC.TEXT_ENTRY_MENU";
	
	private static final String FIND_EXTERNAL_SYSTEM_DEFECT= 
			"select DISTINCT REPLACE(REPLACE(REPLACE(PARTLOC.INSPECTION_PART_NAME || ' ' || "+
			"COALESCE  (PARTLOC.INSPECTION_PART_LOCATION_NAME, '') || ' ' || "+
			"COALESCE (PARTLOC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || "+
			"COALESCE (PARTLOC.INSPECTION_PART2_NAME,'')|| ' ' || "+
			"COALESCE  (PARTLOC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || "+
			"COALESCE (PARTLOC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' ||  "+
			"COALESCE (PARTLOC.INSPECTION_PART3_NAME, '') || ' ' || "+
			"COALESCE (RDC.DEFECT_TYPE_NAME, '') || ' ' ||  "+
			"COALESCE (RDC.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ') AS FULL_PART_DESC, "+
			"ESDM.EXTERNAL_PART_CODE,ESDM.EXTERNAL_DEFECT_CODE,LDC.TEXT_ENTRY_MENU ,ES.IS_IMAGE ,  "+ 
			"ESDM.IS_QICS_REPAIR_REQD,  "+ 
			"ESDM.IS_EXT_SYS_REPAIR_REQD,  "+ 
			"ES.ENTRY_MODEL,ES.ENTRY_SCREEN ,LDC.LOCAL_DEFECT_COMBINATION_ID, "+
			"ESDM.UPDATE_TIMESTAMP " + 
			"from galadm.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX ESDM "+
			"join galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC on LDC.LOCAL_DEFECT_COMBINATION_ID=ESDM.LOCAL_DEFECT_COMBINATION_ID "+
			"join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on  LDC.REGIONAL_DEFECT_COMBINATION_ID=ESDC.REGIONAL_DEFECT_COMBINATION_ID and LDC.ENTRY_MODEL = ESDC.ENTRY_MODEL "+
			"join galadm.QI_ENTRY_SCREEN_TBX ES on ESDC.ENTRY_SCREEN=ES.ENTRY_SCREEN and ESDC.ENTRY_MODEL = ES.ENTRY_MODEL "+
			"join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID  "+
			"join galadm.QI_PART_LOCATION_COMBINATION_TBX PARTLOC on PARTLOC.PART_LOCATION_ID=RDC.PART_LOCATION_ID "+
			"where LDC.ENTRY_SCREEN=ES.ENTRY_SCREEN and  RDC.ACTIVE=1 AND (RDC.IQS_ID IS NOT NULL AND  RDC.IQS_ID <> 0)" +
			"and  COALESCE(RDC.THEME_NAME,'') != '' and  LDC.ENTRY_SCREEN=?1 and ESDM.EXTERNAL_SYSTEM_NAME=?2 and LDC.ENTRY_MODEL =?3 "+
			"and LDC.IS_USED = 1 and ES.IS_USED = 1 and ESDC.IS_USED = 1 " +
			" UNION  " +
			" select 'NOT FOUND LDC ' || ESDM.LOCAL_DEFECT_COMBINATION_ID AS FULL_PART_DESC, "+
			"ESDM.EXTERNAL_PART_CODE,ESDM.EXTERNAL_DEFECT_CODE,'',0, "+ 
			"ESDM.IS_QICS_REPAIR_REQD,  "+ 
			"ESDM.IS_EXT_SYS_REPAIR_REQD,  "+ 
			"ESDM.ENTRY_MODEL,'' ,0, "+
			"ESDM.UPDATE_TIMESTAMP " + 
			" from galadm.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX ESDM "+
			" where ESDM.EXTERNAL_SYSTEM_NAME=?2 and ESDM.ENTRY_MODEL =?3 " +
			" and ESDM.LOCAL_DEFECT_COMBINATION_ID not in (SELECT B.LOCAL_DEFECT_COMBINATION_ID from QI_LOCAL_DEFECT_COMBINATION_TBX B where B.ENTRY_MODEL=?3) " +
			" order by FULL_PART_DESC";
			


	
	private static final String FIND_ALL_PART_DEFECT_COMB_BY_ENTRY_SCREEN_AND_MODEL= 
		"select distinct TRIM(REPLACE(REPLACE(REPLACE(PLC.INSPECTION_PART_NAME || ' ' || "+
		"COALESCE  (PLC.INSPECTION_PART_LOCATION_NAME, '') || ' ' || "+
		"COALESCE (PLC.INSPECTION_PART_LOCATION2_NAME, '') || ' ' || "+
		"COALESCE (PLC.INSPECTION_PART2_NAME,'')|| ' ' || "+
		"COALESCE  (PLC.INSPECTION_PART2_LOCATION_NAME, '') || ' ' || "+
		"COALESCE (PLC.INSPECTION_PART2_LOCATION2_NAME, '') || ' ' || "+
		"COALESCE (PLC.INSPECTION_PART3_NAME, '') || ' ' || "+
		"COALESCE (RDC.DEFECT_TYPE_NAME, '') || ' ' ||  "+
		"COALESCE (RDC.DEFECT_TYPE_NAME2, ''),' ','{}'),'}{',''),'{}',' ')) AS FULL_PART_DESC ,LDC.LOCAL_DEFECT_COMBINATION_ID  "+
		"from galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC "+
		"join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on  LDC.REGIONAL_DEFECT_COMBINATION_ID=ESDC.REGIONAL_DEFECT_COMBINATION_ID and LDC.ENTRY_MODEL = ESDC.ENTRY_MODEL "+
		"join galadm.QI_ENTRY_SCREEN_TBX ES on ESDC.ENTRY_SCREEN=ES.ENTRY_SCREEN and ESDC.ENTRY_MODEL=ES.ENTRY_MODEL "+
		"join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on LDC.REGIONAL_DEFECT_COMBINATION_ID=RDC.REGIONAL_DEFECT_COMBINATION_ID  "+
		"join galadm.QI_PART_LOCATION_COMBINATION_TBX PLC ON PLC.PART_LOCATION_ID=RDC.PART_LOCATION_ID  "+
	    "where LDC.ENTRY_SCREEN=ES.ENTRY_SCREEN and RDC.ACTIVE=1 AND (RDC.IQS_ID IS NOT NULL AND  RDC.IQS_ID <> 0) AND  COALESCE(RDC.THEME_NAME,'')  != '' "+ 
	    "and LDC.ENTRY_SCREEN=?1 and ES.ENTRY_MODEL=?2 and ES.IS_USED = 1 and LDC.IS_USED = 1 and ESDC.IS_USED = 1 ";		
	
		private static final String FIND_BY_LOCAL_COMBINATION_ID=
			"select * from galadm.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX  where LOCAL_DEFECT_COMBINATION_ID in (";
		
		private static final String FIND_BY_REGIONAL_COMBINATION_ID=
			"select * from galadm.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX ESDM  "+
			"join galadm.QI_LOCAL_DEFECT_COMBINATION_TBX LDC on LDC.LOCAL_DEFECT_COMBINATION_ID=ESDM.LOCAL_DEFECT_COMBINATION_ID "+
			"join galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDC on ESDC.REGIONAL_DEFECT_COMBINATION_ID=LDC.REGIONAL_DEFECT_COMBINATION_ID "+
			"join galadm.QI_REGIONAL_DEFECT_COMBINATION_TBX RDC on RDC.REGIONAL_DEFECT_COMBINATION_ID=ESDC.REGIONAL_DEFECT_COMBINATION_ID and  ESDC.ENTRY_SCREEN=LDC.ENTRY_SCREEN "+
			"where rdc.REGIONAL_DEFECT_COMBINATION_ID in (";
		
		

		private static final String FIND_COUNT_BY_ENTRY_SCREEN_AND_REGIONAL_DEFECT_COMB_ID = "select count(e) FROM QiExternalSystemDefectMap e "
				+ "where e.localDefectCombinationId in(select a.localDefectCombinationId"
				+ " FROM QiLocalDefectCombination a where a.entryScreen = :entryScreen AND a.regionalDefectCombinationId IN( ";
			
		private static final String FIND_ALL_NEW_BY_ENTRY_SCREENS_AND_REGIONAL_DEFECTS_COMB_IDS = "select * from QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX d "
				+ "where d.LOCAL_DEFECT_COMBINATION_ID in(SELECT e.LOCAL_DEFECT_COMBINATION_ID FROM QI_LOCAL_DEFECT_COMBINATION_TBX e WHERE "
				+ " e.ENTRY_SCREEN = ?1 and e.ENTRY_MODEL = ?2 and e.IS_USED = ?3 and e.REGIONAL_DEFECT_COMBINATION_ID Not IN(SELECT a.REGIONAL_DEFECT_COMBINATION_ID FROM "
				+ " QI_LOCAL_DEFECT_COMBINATION_TBX a WHERE  a.ENTRY_SCREEN = ?4 and a.ENTRY_MODEL = ?5 and a.IS_USED = ?6 and a. LOCAL_DEFECT_COMBINATION_ID in "
				+ " (select b.LOCAL_DEFECT_COMBINATION_ID from QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX b where b.LOCAL_DEFECT_COMBINATION_ID "
				+ " in(SELECT e.LOCAL_DEFECT_COMBINATION_ID FROM QI_LOCAL_DEFECT_COMBINATION_TBX e WHERE "
				+ " e.ENTRY_SCREEN = ?4 and e.ENTRY_MODEL = ?5 and e.IS_USED = ?6 and e.REGIONAL_DEFECT_COMBINATION_ID in( ";
		

		private static final String FIND_OLD_APP_ID_BY_APP_ID =
				"select OLD_APPLICATION_ID from galadm.QI_MAPPING_APPLICATION_TBX where  APPLICATION_ID=?1";
			
		private static final String FIND_OLD_IMG_BY_IMG =
			"select OLD_IMAGE_NAME from galadm.QI_MAPPING_IMAGE_NAME_TBX where IMAGE_NAME=?1";
		
		private static final String FIND_OLD_IQS_BY_IQS =
			"select OLD_IQS_CATEGORY_NAME as OLD_IQS_CATEGORY_NAME, OLD_IQS_ITEM_NAME  as OLD_IQS_ITEM_NAME  from galadm.QI_MAPPING_IQS_TBX " +
			"where IQS_VERSION = ?1  and IQS_CATEGORY = ?2 and IQS_QUESTION_NO =?3 and IQS_QUESTION = ?4";
		
		private static final String FIND_OLD_THEME_BY_THEME =
			"select OLD_THEME from galadm.QI_MAPPING_THEME_TBX WHERE THEME = ?1";
		
		private static final String FIND_OLD_ENTRY_DEPT_BY_ENTRY_DEPT_SITE_AND_PLANT = 
			" select OLD_ENTRY_SITE as ENTRY_SITE_NAME , OLD_ENTRY_PLANT as ENTRY_PLANT_NAME , OLD_ENTRY_PROD_LINE_NO as  ENTRY_PROD_LINE_NO ," +
			"  OLD_ENTRY_DEPT  as ENTRY_DEPARTMENT  from galadm.QI_MAPPING_ENTRY_DEPT_TBX where ENTRY_SITE_NAME = ?1 and ENTRY_PLANT_NAME = ?2 and ENTRY_DEPT =?3 ";
		
		private static final String FIND_OLD_REPAIR_METHOD_BY_REPAIR_METHOD = 
			"select OLD_REPAIR_METHOD from galadm.QI_MAPPING_REPAIR_METHOD_TBX where REPAIR_METHOD =?1 ";
		
		private static final String FIND_OLD_WRITE_UP_BY_WRITE_UP =
			"select OLD_WRITE_UP_DEPARTMENT from galadm.QI_MAPPING_WRITE_UP_DEPT_TBX where RESPONSIBLE_SITE =?1 and RESPONSIBLE_PLANT =?2 and WRITE_UP_DEPARTMENT=?3 ";
		
		private static final String FIND_OLD_RESPONSIBILITY_BY_RESPONSIBILITY =
			"select OLD_RESPONSIBLE_DEPT as RESPONSIBLE_DEPT , OLD_RESPONSIBLE_LEVEL2 as LEVEL_TWO , OLD_RESPONSIBLE_LEVEL1  as LEVEL from "+
			"galadm.QI_MAPPING_RESPONSIBILITY_TBX where RESPONSIBLE_SITE = ?1 and RESPONSIBLE_PLANT =?2 "+
			"and RESPONSIBLE_DEPT =?3 and RESPONSIBLE_LEVEL3 =?4 and RESPONSIBLE_LEVEL2 =?5 and RESPONSIBLE_LEVEL1 =?6 ";
		
		private static final String FIND_ALL_MODIFIED_BY_ENTRY_SCREENS = "select D.EXTERNAL_SYSTEM_NAME, D.EXTERNAL_PART_CODE, "
				+ "D.EXTERNAL_DEFECT_CODE , E.REGIONAL_DEFECT_COMBINATION_ID from GALADM.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX D "
				+ "JOIN GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX E ON D.LOCAL_DEFECT_COMBINATION_ID = E.LOCAL_DEFECT_COMBINATION_ID "
				+ "WHERE E.ENTRY_SCREEN = ?1 AND E.ENTRY_MODEL = ?2 AND E.IS_USED = ?3 "
				+ " EXCEPT select D.EXTERNAL_SYSTEM_NAME, D.EXTERNAL_PART_CODE, "
				+ "D.EXTERNAL_DEFECT_CODE , E.REGIONAL_DEFECT_COMBINATION_ID from GALADM.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX D "
				+ "JOIN GALADM.QI_LOCAL_DEFECT_COMBINATION_TBX E ON D.LOCAL_DEFECT_COMBINATION_ID = E.LOCAL_DEFECT_COMBINATION_ID "
				+ "WHERE E.ENTRY_SCREEN = ?4 AND E.ENTRY_MODEL = ?5 AND E.IS_USED = ?6";
		
		private static final String FIND_OLD_REPAIR_AREA_BY_REPAIR_AREA = 
				"select OLD_REPAIR_AREA from galadm.QI_MAPPING_REPAIR_AREA_TBX where REPAIR_AREA = ?1 ";		
		
		private static final String UPDATE_LOCAL_ATTRIBUTE_ID = "update GALADM.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX set UPDATE_USER =?1, LOCAL_DEFECT_COMBINATION_ID = ?2"
				+ " where LOCAL_DEFECT_COMBINATION_ID = ?3";
		
		private static final String DELETE_BY_PLANT_ENTRY_SCREEN_MODEL = 
				"delete from GALADM.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX A "
				+ " where A.LOCAL_DEFECT_COMBINATION_ID in "
				+ " (select B.LOCAL_DEFECT_COMBINATION_ID from QI_LOCAL_DEFECT_COMBINATION_TBX B where "
				+ " TRIM(B.ENTRY_PLANT_NAME)=?1 AND TRIM(B.ENTRY_SCREEN)=?2 AND TRIM(B.ENTRY_MODEL)=?3)";
		
		private static final String DELETE_BY_PLANT_ENTRY_SCREEN_MENU_MODEL = 
				"delete from GALADM.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX A "
				+ " where A.LOCAL_DEFECT_COMBINATION_ID in "
				+ " (select B.LOCAL_DEFECT_COMBINATION_ID from QI_LOCAL_DEFECT_COMBINATION_TBX B where "
				+ " TRIM(B.ENTRY_PLANT_NAME)=?1 AND TRIM(B.ENTRY_SCREEN)=?2 AND TRIM(B.TEXT_ENTRY_MENU)=?3 AND TRIM(B.ENTRY_MODEL)=?4)";
		
		private static final String DELETE_LIST_BY_PLANT_ENTRY_SCREEN_MENU_MODEL = 
				"delete from QiExternalSystemDefectMap e "
				+ "where e.localDefectCombinationId in "
				+ "(select f.localDefectCombinationId from QiLocalDefectCombination f where "
				+ "trim(f.entryPlantName)=:plantName and trim(f.entryModel)=:entryModel and "
				+ "trim(f.entryScreen)=:entryScreen and trim(f.textEntryMenu)=:entryMenu "
				+ "and f.regionalDefectCombinationId in (%s))";
		
		private static final String UPDATE_ENTRY_MODEL_NAME = "update GALADM.QI_EXTERNAL_SYSTEM_DEFECT_MAP_TBX e "
				+ "set e.ENTRY_MODEL = ?1, e.UPDATE_USER =?2 where e.ENTRY_MODEL = ?3";
		
	/**
	 * This method finds all External System defects.
	 * @param externalSystemName
	 * @param entryScreen
	 * @return List<DefectMapDto>
	 */
		public List<DefectMapDto> findAllExternalSystemDefect(String externalSystemName, String entryScreen, String entryModel){
			Parameters params = Parameters.with("1", entryScreen).put("2", externalSystemName).put("3", entryModel);
			return findAllByNativeQuery(FIND_EXTERNAL_SYSTEM_DEFECT, params, DefectMapDto.class);
		}

	/**
	 * This method finds part defect combination by entry screen and entry model
	 * @param entryModel
	 * @param entryScreen
	 * @return List<DefectMapDto>
	 */
	public List<DefectMapDto> findAllPartDefectCombByEntryScreenModelMenu(String entryScreen,String entryModel ,String textEntryMenu){
		StringBuilder queryStr=new StringBuilder();
		queryStr.append(FIND_ALL_PART_DEFECT_COMB_BY_ENTRY_SCREEN_AND_MODEL);
		if(textEntryMenu.equals(StringUtils.EMPTY)){
			queryStr.append("and (LDC.TEXT_ENTRY_MENU='' OR LDC.TEXT_ENTRY_MENU IS NULL) order by FULL_PART_DESC");
		}
		else
		{
			queryStr.append("and  LDC.TEXT_ENTRY_MENU='"+textEntryMenu+ "' order by FULL_PART_DESC");
		}
		Parameters params = Parameters.with("1", entryScreen).put("2",entryModel);
		return findAllByNativeQuery(queryStr.toString(), params, DefectMapDto.class);

	}
	/**
	 * This method finds external system defect data based on external part code ,external defect code
	 * and external system name
	 * @param partCode
	 * @param defectCode
	 * @param externalSystemName
	 * @return QiExternalSystemDefectMap
	 */
	public QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystem(String partCode,String defectCode, String externalSystemName) {
		Parameters params = Parameters.with("externalPartCode", partCode).put("externalDefectCode",defectCode).put("externalSystemName", externalSystemName);
		return findFirst( params);
	}
	
	/**
	 * This method finds external system defect data based on external part code ,external defect code
	 * and external system name
	 * @param partCode
	 * @param defectCode
	 * @param externalSystemName
	 * @param entryModel
	 * @return QiExternalSystemDefectMap
	 */
	@Override
	public QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystemAndEntryModel(String partCode,String defectCode, String externalSystemName, String entryModel) {
		Parameters params = Parameters.with("externalPartCode", partCode)
				.put("externalDefectCode",defectCode)
				.put("externalSystemName", externalSystemName)
				.put("entryModel", entryModel);
		return findFirst( params);
	}
	
	/**
	 * This method finds external system defect data based on external system name and local defect combination Id
	 * @param externalSystemName
	 * @param localDefectCombinationId
	 * @return QiExternalSystemDefectMap
	 */
	public QiExternalSystemDefectMap findByLocalCombIdAndExternalSystemName(String externalSystemName, Integer localDefectCombinationId) {
		Parameters params = Parameters.with("externalSystemName", externalSystemName).put("localDefectCombinationId",localDefectCombinationId);
		return  findFirst(params);
	}
	
	/**
	 * This method finds text entry menu base on entry model and entry screen
	 * @param entryScreenName
	 * @param entryModel
	 * @param isImageEntryScreen
	 * @return List<DefectMapDto>
	 */
	public List<DefectMapDto> findAllByEntryScreenAndModel(String entryScreenName,String entryModel, boolean isImageEntryScreen) {	
		Parameters params = Parameters.with("1", entryScreenName).put("2", entryModel);
		if(isImageEntryScreen)
			return findAllByNativeQuery(FIND_ALL_TEXT_ENTRY_MENU_BY_ENTRY_SCREEN_AND_MODEL_IMAGE, params,DefectMapDto.class);
		else
			return findAllByNativeQuery(FIND_ALL_TEXT_ENTRY_MENU_BY_ENTRY_SCREEN_AND_MODEL_TEXT, params,DefectMapDto.class);
	}
	
	public QiExternalSystemDefectMap findByExternalSystem(String externalSystemName,
			String externalSystemPartCode, String externalSystemDefectCode) {
		Parameters params = Parameters.with("1", externalSystemName);
		params.put("2", externalSystemPartCode);
		params.put("3", externalSystemDefectCode);
		return findFirstByNativeQuery(FIND_EXTERNAL_SYSTEM_NAME, params);
	}
	
	/**
	 * This method finds external mapping data based on local comb id
	 * @param localCombIdListValue
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByLocalCombinationId(String localCombIdListValue) {
		return findAllByNativeQuery(FIND_BY_LOCAL_COMBINATION_ID + localCombIdListValue + ")", null);
	}
	
	/**
	 * This method finds external mapping data based on regional comb id
	 * @param regionalCombIdListValue
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByRegionalCombinationId(String regionalCombIdListValue) {
		return findAllByNativeQuery(FIND_BY_REGIONAL_COMBINATION_ID + regionalCombIdListValue + ")", null);
	}
	
	public List<QiExternalSystemDefectMap> findAllByLocalDefectCombIds(Integer localId) {
		return findAll(Parameters.with("localDefectCombinationId", localId));	
	}

	public long findCountByRegionalAndScreenName(String entryScreen, String regionalIds) {
		return count(FIND_COUNT_BY_ENTRY_SCREEN_AND_REGIONAL_DEFECT_COMB_ID + regionalIds + "))", Parameters.with("entryScreen", entryScreen));
	}
	
	public String findOldAppIdByAppId(String appId) {
		return findFirstByNativeQuery(FIND_OLD_APP_ID_BY_APP_ID , Parameters.with("1", appId), String.class);	
	}

	public String findOldImgByImg(String imgName) {
		return findFirstByNativeQuery(FIND_OLD_IMG_BY_IMG, Parameters.with("1", imgName) ,String.class );	
	}
	public String findOldRepairMethodByRepairMethod(String repairMethod) {
		return findFirstByNativeQuery(FIND_OLD_REPAIR_METHOD_BY_REPAIR_METHOD, Parameters.with("1", repairMethod) ,String.class );	
	}
	public String findOldThemeByTheme(String theme) {
		return findFirstByNativeQuery(FIND_OLD_THEME_BY_THEME, Parameters.with("1", theme) ,String.class );	
	}
	public DefectMapDto findOldIqsByIqs(QiDefectResult qiDefectResult) {
		Parameters parameters =Parameters.with("1", qiDefectResult.getIqsVersion())
				.put("2", qiDefectResult.getIqsCategoryName())
				.put("3", qiDefectResult.getIqsQuestionNo())
				.put("4", qiDefectResult.getIqsQuestion());
		return findFirstByNativeQuery(FIND_OLD_IQS_BY_IQS  ,parameters ,DefectMapDto.class);	 
	}
	
	public DefectMapDto findOldEntryDeptByEntryDept(QiDefectResult qiDefectResult) {
		Parameters parameters =Parameters.with("1", qiDefectResult.getEntrySiteName())
				.put("2", qiDefectResult.getEntryPlantName())
				.put("3", qiDefectResult.getEntryDept());
		return findFirstByNativeQuery(FIND_OLD_ENTRY_DEPT_BY_ENTRY_DEPT_SITE_AND_PLANT,parameters ,DefectMapDto.class );	
	}
	
	public String findOldWriteUpByWriteUpAndResponsibility(QiDefectResult qiDefectResult) {
		Parameters parameters =Parameters.with("1", qiDefectResult.getResponsibleSite())
				.put("2", qiDefectResult.getResponsiblePlant())
				.put("3", qiDefectResult.getWriteUpDept());
		return findFirstByNativeQuery(FIND_OLD_WRITE_UP_BY_WRITE_UP,parameters ,String.class );	
	}
	
	public DefectMapDto findOldResponsiblityByResponsibility(QiDefectResult qiDefectResult) {
		Parameters parameters =Parameters.with("1", qiDefectResult.getResponsibleSite())
				.put("2", qiDefectResult.getResponsiblePlant())
				.put("3", qiDefectResult.getResponsibleDept())
				.put("4", qiDefectResult.getResponsibleLevel3())
				.put("5", qiDefectResult.getResponsibleLevel2())
				.put("6", qiDefectResult.getResponsibleLevel1());
		return findFirstByNativeQuery(FIND_OLD_RESPONSIBILITY_BY_RESPONSIBILITY,parameters ,DefectMapDto.class );	
	}
	
	public String findOldRepairAreaByRepairArea(String repairArea) {
		return findFirstByNativeQuery(FIND_OLD_REPAIR_AREA_BY_REPAIR_AREA, Parameters.with("1", repairArea) ,String.class );	
	}

	@Transactional
	public void updateLocalDefectCombinationId(int oldLocalDefectCombinationId, int newLocalDefectCombinationId, String userId) {
		Parameters params = Parameters.with("2", newLocalDefectCombinationId).put("1", userId).put("3",
				oldLocalDefectCombinationId);
		executeNativeUpdate(UPDATE_LOCAL_ATTRIBUTE_ID, params);
	}
	
	public List<QiExternalSystemDefectMap> findAllNewByEntryScreenAndLocalDefectComb(QiEntryScreenId fromEntryScreenId,
			QiEntryScreenId toEntrySreenId, List<Integer> regionalIds) {
		Parameters param = Parameters.with("1", fromEntryScreenId.getEntryScreen()).put("2", fromEntryScreenId.getEntryModel()).put("3", fromEntryScreenId.getIsUsed())
				.put("4", toEntrySreenId.getEntryScreen()).put("5", toEntrySreenId.getEntryModel()).put("6", toEntrySreenId.getIsUsed());
		return findAllByNativeQuery(FIND_ALL_NEW_BY_ENTRY_SCREENS_AND_REGIONAL_DEFECTS_COMB_IDS + StringUtils.join(regionalIds, ',') + " )))))",
				param, QiExternalSystemDefectMap.class);
	}
	
	public List<DefectMapDto> findAllModifiedHeadlessData(QiEntryScreenId fromEntryScreen, QiEntryScreenId toEntryScreen) {
		return findAllByNativeQuery(FIND_ALL_MODIFIED_BY_ENTRY_SCREENS,
				Parameters.with("1", fromEntryScreen.getEntryScreen()).put("2", fromEntryScreen.getEntryModel()).put("3", fromEntryScreen.getIsUsed())
				.put("4", toEntryScreen.getEntryScreen()).put("5", toEntryScreen.getEntryModel()).put("6", toEntryScreen.getIsUsed()), 
				DefectMapDto.class);
	}
	
	@Transactional
	public void deleteLDCByPlantEntryScreenAndModel(String plantName, String entryScreen, String entryModel)  {
		Parameters params = Parameters.with("1", plantName.trim())
				.put("2", entryScreen.trim())
				.put("3", entryModel.trim());
		executeNativeUpdate(DELETE_BY_PLANT_ENTRY_SCREEN_MODEL, params);
		
	}

	@Transactional
	public void deleteLDCByPlantEntryScreeMenuAndModel(String plantName, String entryScreen, String menu, String entryModel)  {
		Parameters params = Parameters.with("1", plantName.trim())
				.put("2", entryScreen.trim())
				.put("3", menu.trim())
				.put("4", entryModel.trim());
		executeNativeUpdate(DELETE_BY_PLANT_ENTRY_SCREEN_MENU_MODEL, params);
		
	}
	
	@Transactional
	public void deleteListByPlantEntryScreenMenuAndModel(String plantName, String entryScreen, String menu, String entryModel,String regionalIdList) {
		Parameters params = Parameters.with("plantName", plantName.trim())
				.put("entryScreen", entryScreen.trim())
				.put("entryMenu", menu.trim())
				.put("entryModel", entryModel.trim());
		executeUpdate(String.format(DELETE_LIST_BY_PLANT_ENTRY_SCREEN_MENU_MODEL, regionalIdList), params);		
	}
	
	@Override
	@Transactional
	public void deleteHeadlessMappings(List<QiExternalSystemDefectMap>  deleteList)  {
		removeAll(deleteList);
	}
	@Override
	@Transactional
	public void saveHeadlessMappings(List<QiExternalSystemDefectMap>  insertList)  {
		saveAll(insertList);		
	}
	
	@Override
	public boolean isExternalSystemNameUsed(String externalSystemName) {
		Parameters params = Parameters.with("externalSystemName", externalSystemName);
		return findFirst(params) != null? true : false;
	}
	
	@Override
	@Transactional
	public void updateEntryModelName(String newEntryModel, String oldEntryModel, String userId) {
		Parameters params = Parameters.with("1", newEntryModel)
				.put("2", userId)
				.put("3", oldEntryModel);
		executeNativeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
}
