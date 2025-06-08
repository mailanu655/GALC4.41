package com.honda.galc.dao.jpa.qi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.enumtype.QiEntryScreenType;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.service.Parameters;

public class QiEntryScreenDaoImpl extends BaseDaoImpl<QiEntryScreen, QiEntryScreenId> implements QiEntryScreenDao{
	
	private static String FIND_ALL_ENTRY_SCREEN_DATA =
			"SELECT DISTINCT es.ENTRY_SCREEN, es.ENTRY_SCREEN_DESCRIPTION, "
			+ "es.PRODUCT_TYPE,es.IS_IMAGE,es.ACTIVE,es.ENTRY_MODEL,sd.DIVISION_ID,pd.PLANT_NAME, "
			+ "es.IS_USED as IS_USED_VERSION, es.UPDATE_TIMESTAMP "
			+ "FROM GALADM.QI_ENTRY_SCREEN_TBX es "					
			+ "join GALADM.QI_ENTRY_SCREEN_DEPT_TBX sd on es.ENTRY_SCREEN = sd.ENTRY_SCREEN and es.IS_USED = sd.IS_USED and es.ENTRY_MODEL = sd.ENTRY_MODEL "
			+ "join GALADM.GAL128TBX pd on sd.DIVISION_ID = pd.DIVISION_ID "			
			+ "where (es.ENTRY_SCREEN like ?1 or "
			+ "es.ENTRY_SCREEN_DESCRIPTION like ?1 or "
			+ "es.PRODUCT_TYPE like ?1 or "
			+ "es.ENTRY_MODEL like ?1 or "
			+ "sd.DIVISION_ID like ?1 or "
			+ "UPPER(pd.PLANT_NAME) like ?1 or "
			+ "es.IS_IMAGE like ?2 or "
			+ "es.ACTIVE = ?3) "
			+ "and es.ACTIVE in (?4,?5) and pd.SITE_NAME = ?6 "
			+ "UNION "
			+ "SELECT DISTINCT es.ENTRY_SCREEN, es.ENTRY_SCREEN_DESCRIPTION, "
			+ "es.PRODUCT_TYPE,es.IS_IMAGE,es.ACTIVE,es.ENTRY_MODEL,'','', "
			+ "es.IS_USED as IS_USED_VERSION, es.UPDATE_TIMESTAMP "
			+ "FROM GALADM.QI_ENTRY_SCREEN_TBX es "					
			+ "where "
			+ "(select count(*) from GALADM.QI_ENTRY_SCREEN_DEPT_TBX sd where es.entry_model=sd.entry_model and es.entry_screen=sd.entry_screen) = 0 "
			+ "and (es.ENTRY_SCREEN like ?1 or "
			+ "es.ENTRY_SCREEN_DESCRIPTION like ?1 or "
			+ "es.PRODUCT_TYPE like ?1 or "
			+ "es.ENTRY_MODEL like ?1 or "
			+ "es.IS_IMAGE like ?2 or "
			+ "es.ACTIVE = ?3) "
			+ "and es.ACTIVE in (?4,?5) "
			+ "order by PRODUCT_TYPE ";
		
	private static final String FIND_ENTRY_SCREEN_MODEL =
			"SELECT DISTINCT entryScreen.ENTRY_SCREEN  entryScreen, entryScreen.ENTRY_MODEL entryModel, entryScreen.IMAGE_NAME imageName ," +
			"case when entryScreenDept.DIVISION_ID  is null then '' else entryScreenDept.DIVISION_ID  end divisionId, " +
			"CASE WHEN entryScreen.IS_IMAGE = 1 then 'Image' else 'Text' end isImage," +
			"case when DEFECT.ENTRY_SCREEN is null THEN 'No' else 'Yes' end used," +
			"entryScreen.IS_USED as IS_USED_VERSION, DEPT.PLANT_NAME as plantName, entryModel.PRODUCT_TYPE as productType, DEPT.DIVISION_NAME as divisionName  " +
			"FROM GALADM.QI_ENTRY_SCREEN_DEPT_TBX entryScreenDept " +
			"JOIN GALADM.QI_ENTRY_SCREEN_TBX entryScreen ON entryScreen.ENTRY_SCREEN = entryScreenDept.ENTRY_SCREEN AND entryScreen.ENTRY_MODEL = entryScreenDept.ENTRY_MODEL AND entryScreen.IS_USED = entryScreenDept.IS_USED " +
			"JOIN GALADM.GAL128TBX DEPT ON DEPT.DIVISION_ID = entryScreenDept.DIVISION_ID AND  DEPT.PLANT_NAME = ?2 " +
			"LEFT OUTER JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX DEFECT ON entryScreen.ENTRY_SCREEN = DEFECT.ENTRY_SCREEN " +
			"LEFT OUTER JOIN QI_ENTRY_MODEL_TBX entryModel on entryModel.ENTRY_MODEL=entryScreen.ENTRY_MODEL " +
			"WHERE entryScreen.ENTRY_MODEL = ?1 AND entryScreen.ACTIVE = 1 " +
			"ORDER BY entryScreen.ENTRY_SCREEN ";

	private static final String FIND_ENTRY_SCREEN_MODEL_IS_IMAGE =
			"SELECT DISTINCT entryScreen.ENTRY_SCREEN  entryScreen, entryScreen.ENTRY_MODEL entryModel, entryScreen.IMAGE_NAME imageName ," +
			"case when entryScreenDept.DIVISION_ID  is null then '' else entryScreenDept.DIVISION_ID  end divisionId, " +
			"CASE WHEN entryScreen.IS_IMAGE = 1 then 'Image' else 'Text' end isImage," +
			"case when DEFECT.ENTRY_SCREEN is null THEN 'No' else 'Yes' end used," +
			"entryScreen.IS_USED as IS_USED_VERSION, DEPT.PLANT_NAME as plantName, entryModel.PRODUCT_TYPE as productType, DEPT.DIVISION_NAME as divisionName   " +
			"FROM GALADM.QI_ENTRY_SCREEN_DEPT_TBX entryScreenDept " +
			"JOIN GALADM.QI_ENTRY_SCREEN_TBX entryScreen ON entryScreen.ENTRY_SCREEN = entryScreenDept.ENTRY_SCREEN AND entryScreen.ENTRY_MODEL = entryScreenDept.ENTRY_MODEL AND entryScreen.IS_USED = entryScreenDept.IS_USED " +
			"JOIN GALADM.GAL128TBX DEPT ON DEPT.DIVISION_ID = entryScreenDept.DIVISION_ID AND  DEPT.PLANT_NAME = ?2 " +
			"LEFT OUTER JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX DEFECT ON entryScreen.ENTRY_SCREEN = DEFECT.ENTRY_SCREEN " +
			"LEFT OUTER JOIN QI_ENTRY_MODEL_TBX entryModel on entryModel.ENTRY_MODEL=entryScreen.ENTRY_MODEL " +
			"WHERE entryScreen.ENTRY_MODEL = ?1 AND entryScreen.ACTIVE = 1 AND entryScreen.IS_IMAGE = ?3 " +
			"ORDER BY entryScreen.ENTRY_SCREEN ";

	private static final String FIND_USED_ENTRY_SCREEN_MODEL =
			"SELECT DISTINCT entryScreen.ENTRY_SCREEN  entryScreen, entryScreen.ENTRY_MODEL entryModel, entryScreen.IMAGE_NAME imageName, " +
			"case when entryScreenDept.DIVISION_ID  is null then '' else entryScreenDept.DIVISION_ID  end divisionId, " +
			"CASE WHEN entryScreen.IS_IMAGE = 1 then 'Image' else 'Text' end isImage ," +
			"case when DEFECT.ENTRY_SCREEN is null THEN 'No' else 'Yes' end used, " +
			"entryScreen.IS_USED as IS_USED_VERSION, DEPT.PLANT_NAME as plantName,entryModel.PRODUCT_TYPE as productType, DEPT.DIVISION_NAME as divisionName   " +
			"FROM GALADM.QI_ENTRY_SCREEN_DEPT_TBX entryScreenDept " +
			"JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX DEFECT ON DEFECT.ENTRY_SCREEN = entryScreenDept.ENTRY_SCREEN " +
			"JOIN GALADM.QI_ENTRY_SCREEN_TBX  entryScreen ON entryScreen.ENTRY_SCREEN = DEFECT.ENTRY_SCREEN " +
			"JOIN GALADM.QI_ENTRY_MODEL_TBX entryModel ON entryModel.ENTRY_MODEL = entryScreen.ENTRY_MODEL " +
			"JOIN GALADM.GAL128TBX DEPT ON DEPT.DIVISION_ID = entryScreenDept.DIVISION_ID AND  DEPT.PLANT_NAME = ?2 " +
			"WHERE entryScreen.ENTRY_MODEL = ?1 AND entryScreen.ACTIVE = 1 " +
			"ORDER BY entryScreen.ENTRY_SCREEN  ";
	
	
	
	private static final String FIND_NOT_USED_ENTRY_SCREEN_MODEL =
			"SELECT DISTINCT entryScreen.ENTRY_SCREEN  entryScreen, entryScreen.ENTRY_MODEL entryModel, entryScreen.IMAGE_NAME imageName , " +
			"case when entryScreenDept.DIVISION_ID  is null then '' else entryScreenDept.DIVISION_ID  end divisionId, " +
			"CASE WHEN entryScreen.IS_IMAGE = 1 then 'Image' else 'Text' end isImage , " +
			"case when DEFECT.ENTRY_SCREEN is null THEN 'No' else 'Yes' end used, " +
			"entryScreen.IS_USED as IS_USED_VERSION, DEPT.PLANT_NAME as plantName,entryModel.PRODUCT_TYPE as productType, DEPT.DIVISION_NAME as divisionName   " +
			"FROM GALADM.QI_ENTRY_SCREEN_DEPT_TBX entryScreenDept " +
			"JOIN GALADM.QI_ENTRY_SCREEN_TBX entryScreen ON entryScreen.ENTRY_SCREEN = entryScreenDept.ENTRY_SCREEN " +
			"JOIN GALADM.QI_ENTRY_MODEL_TBX entryModel ON entryModel.ENTRY_MODEL = entryScreen.ENTRY_MODEL " +
			"JOIN GALADM.GAL128TBX DEPT ON DEPT.DIVISION_ID = entryScreenDept.DIVISION_ID AND DEPT.PLANT_NAME = ?2 " +
			"LEFT OUTER JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX DEFECT ON entryScreen.ENTRY_SCREEN = DEFECT.ENTRY_SCREEN " +
			"WHERE entryModel.ENTRY_MODEL = ?1 AND entryScreen.ACTIVE = 1 " +
			"AND entryScreen.ENTRY_SCREEN NOT IN (SELECT ENTRY_SCREEN FROM GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX) " +
			"ORDER BY entryScreen.ENTRY_SCREEN ";
	
	private final static String FIND_ENTRYSCREENS_BY_ENTRY_MODEL = "SELECT e.entryScreen FROM QiEntryScreen e WHERE e.isImage = 1 AND e.active = 1 AND e.id.entryModel = :entryModel order by e.id.entryScreen";
			
	private static final String FIND_ENTRY_SCREENS ="select DISTINCT e.ENTRY_SCREEN,a.DIVISION_ID, e.IMAGE_NAME, e.ENTRY_MODEL, e.IS_USED as IS_USED_VERSION, e.UPDATE_TIMESTAMP from galadm.QI_ENTRY_SCREEN_TBX e, galadm.QI_ENTRY_SCREEN_DEPT_TBX a where e.IS_IMAGE=1 and e.ACTIVE=1 and a.ENTRY_SCREEN=e.ENTRY_SCREEN and e.ENTRY_MODEL=?1";

	

	private static final String UPDATE_REPAIR_METHOD = "update GALADM.QI_ENTRY_SCREEN_TBX  set ACTIVE = ?1, ENTRY_SCREEN= ?2 , " +
			"ENTRY_SCREEN_DESCRIPTION = ?3, IS_IMAGE = ?4, PRODUCT_TYPE = ?5,  " +
			" UPDATE_USER = ?6, UPDATE_TIMESTAMP = ?7, ENTRY_MODEL= ?8, IMAGE_NAME = ?9 where ENTRY_SCREEN= ?10 and ENTRY_MODEL = ?11 " +
			" and IS_USED = ?12";

	private static final String UPDATE_ENTRY_MODEL_NAME ="update QiEntryScreen e set e.id.entryModel=:newEntryModel, e.updateUser=:updateUser where e.id.entryModel=:oldEntryModel and e.id.isUsed=:isUsed";
	
	private final  String FIND_ALL_BY_PLANT_PRODUCT_TYPE_AND_ENTRY_MODEL = "SELECT DISTINCT EST.ENTRY_SCREEN FROM galadm.QI_ENTRY_SCREEN_TBX EST "
			+ " JOIN galadm.QI_ENTRY_SCREEN_TBX ESM ON EST.ENTRY_SCREEN = ESM.ENTRY_SCREEN "
			+ " JOIN galadm.QI_ENTRY_SCREEN_DEPT_TBX ESD ON ESM.ENTRY_SCREEN = ESD.ENTRY_SCREEN "
			+ " JOIN galadm.GAL128TBX P ON ESD.DIVISION_ID = P.DIVISION_ID "
			+ " WHERE P.PLANT_NAME = ?1 AND  "
			+ " 	 EST.PRODUCT_TYPE = ?2 AND "
			+ " 	 ESM.ENTRY_MODEL = ?3 AND ESM.IS_USED = 1 "
			+ " 	 AND EST.ACTIVE = 1  order by EST.ENTRY_SCREEN ";
	
	private static final String FIND_ENTRY_SCREENS_BY_MODEL_AND_DEPT ="select distinct es.ENTRY_MODEL,es.ENTRY_SCREEN, case when es.IS_IMAGE=1 then es.IMAGE_NAME when es.IS_IMAGE=0 then 'TEXT' end as IMAGE_NAME "
			+ "from galadm.QI_ENTRY_SCREEN_TBX es,galadm.QI_ENTRY_SCREEN_DEPT_TBX esd where " 
			+ "es.ENTRY_MODEL=?1 and es.ACTIVE=1 and es.ENTRY_SCREEN=esd.ENTRY_SCREEN and esd.DIVISION_ID=?2 and es.IS_USED = ?3 "
			+ "and (es.IS_IMAGE=0 or es.IMAGE_NAME is not null) "
			+ "order by es.ENTRY_SCREEN ";
	private final static String UPDATE_IMAGE_NAME_FOR_ENTRY_SCREEN = "UPDATE GALADM.QI_ENTRY_SCREEN_TBX SET IMAGE_NAME= ?1, UPDATE_USER= ?2 WHERE IMAGE_NAME= ?3";
	
	
	private static final String FIND_ENTRY_SCREENS_BY_ENTRY_SCREEN_DEFECT= "select ENTRY_SCREEN FROM galadm.QI_ENTRY_SCREEN_TBX where ENTRY_SCREEN in(select ENTRY_SCREEN "
			+ "FROM galadm.QI_STATION_ENTRY_SCREEN_TBX) AND IMAGE_NAME= ?1";
	
	
	private static final String FIND_ENTRY_SCREENS_BY_DEFECT_COMBINATION= "select ENTRY_SCREEN FROM galadm.QI_ENTRY_SCREEN_TBX where ENTRY_SCREEN in"
			+ "(select ENTRY_SCREEN from galadm.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX) AND IMAGE_NAME = ?1";
	private static final String UPDATE_VERSION_VALUE = "update GALADM.QI_ENTRY_SCREEN_TBX e set e.IS_USED = ?1 where e.ENTRY_MODEL = ?2 and e.IS_USED=?3";
	
	
	private final  String FIND_ALL_BY_PLANT_PRODUCT_TYPE_ENTRY_MODEL_AND_ENTRY_DEPT = "SELECT DISTINCT EST.ENTRY_SCREEN FROM QI_ENTRY_SCREEN_TBX EST "
			+ " JOIN QI_ENTRY_SCREEN_TBX ESM ON EST.ENTRY_SCREEN = ESM.ENTRY_SCREEN "
			+ " JOIN QI_ENTRY_SCREEN_DEPT_TBX ESD ON ESM.ENTRY_SCREEN = ESD.ENTRY_SCREEN "
			+ " JOIN GAL128TBX P ON ESD.DIVISION_ID = P.DIVISION_ID "
			+ " WHERE P.PLANT_NAME = ?1 AND  "
			+ " 	 EST.PRODUCT_TYPE = ?2 AND "
			+ " 	 ESM.ENTRY_MODEL = ?3 AND "
			+ " 	 ESD.DIVISION_ID = ?4 "
			+ " 	 AND EST.ACTIVE = 1  order by EST.ENTRY_SCREEN ";
	
	private static final String FIND_ENTRY_SCREENS_BY_PRODUCT_TYPE= "select DISTINCT ENTRY_SCREEN FROM galadm.QI_ENTRY_SCREEN_TBX where PRODUCT_TYPE = ?1";

	private static final String FIND_ENTRY_SCREEN_BY_PRODUCT_TYPE =
			"SELECT DISTINCT entryScreen.ENTRY_SCREEN entryScreen, entryScreen.ENTRY_MODEL entryModel, entryScreen.IMAGE_NAME imageName,"
			+ "case when entryScreenDept.DIVISION_ID is null then '' else entryScreenDept.DIVISION_ID end divisionId, "
			+ "CASE WHEN entryScreen.IS_IMAGE = 1 then 'Image' else 'Text' end isImage, "
			+ "case when DEFECT.ENTRY_SCREEN is null THEN 'No' else 'Yes' end used, "
			+ "entryScreen.IS_USED as IS_USED_VERSION, DEPT.PLANT_NAME as plantName, entryModel.PRODUCT_TYPE as productType, DEPT.DIVISION_NAME as divisionName   "
			+ "FROM GALADM.QI_ENTRY_SCREEN_DEPT_TBX entryScreenDept "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_TBX entryScreen ON entryScreen.ENTRY_SCREEN = entryScreenDept.ENTRY_SCREEN "
			+ "JOIN GALADM.GAL128TBX DEPT ON DEPT.DIVISION_ID = entryScreenDept.DIVISION_ID AND  DEPT.PLANT_NAME = ?1 "
			+ "LEFT OUTER JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX DEFECT ON entryScreen.ENTRY_SCREEN = DEFECT.ENTRY_SCREEN "
			+ "LEFT OUTER JOIN QI_ENTRY_MODEL_TBX entryModel on entryModel.ENTRY_MODEL=entryScreen.ENTRY_MODEL "
			+ "WHERE entryScreen.PRODUCT_TYPE = ?2 AND entryScreen.ACTIVE = 1 "
			+ "AND entryScreen.IS_IMAGE = ?3 ";

	private final static String FIND_ENTRYSCREENS_BY_PLANT_ENTRY_MODEL_PDC = "SELECT DISTINCT e.ENTRY_SCREEN FROM galadm.QI_ENTRY_SCREEN_TBX e "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX comb ON e.ENTRY_SCREEN = comb.ENTRY_SCREEN "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_DEPT_TBX d ON e.ENTRY_SCREEN = d.ENTRY_SCREEN AND e.ENTRY_MODEL = d.ENTRY_MODEL "
			+ "JOIN GALADM.GAL128TBX p ON p.DIVISION_ID = d.DIVISION_ID "
			+ "WHERE e.ACTIVE = 1 AND e.ENTRY_MODEL = ?2 AND p.PLANT_NAME = ?1 order by e.ENTRY_SCREEN";
	
	private final static String FIND_ENTRYSCREENS_BY_PLANT_AND_ENTRY_MODEL =
			"SELECT DISTINCT A.* FROM galadm.QI_ENTRY_SCREEN_TBX A "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_DEPT_TBX B ON A.ENTRY_SCREEN = B.ENTRY_SCREEN AND A.ENTRY_MODEL = B.ENTRY_MODEL "
			+ "JOIN GALADM.GAL128TBX D ON D.PLANT_NAME = ?1 AND D.DIVISION_ID = B.DIVISION_ID "
			+ "WHERE A.ACTIVE = 1 AND A.ENTRY_MODEL = ?2 order by A.ENTRY_SCREEN";
	
	private final static String COUNT_ENTRYSCREENS_BY_PLANT_AND_ENTRY_MODEL =
			"select count(*) from (SELECT DISTINCT A.* FROM galadm.QI_ENTRY_SCREEN_TBX A "
			+ "JOIN GALADM.QI_ENTRY_SCREEN_DEPT_TBX B ON A.ENTRY_SCREEN = B.ENTRY_SCREEN AND A.ENTRY_MODEL = B.ENTRY_MODEL "
			+ "JOIN GALADM.GAL128TBX D ON D.PLANT_NAME = ?1 AND D.DIVISION_ID = B.DIVISION_ID "
			+ "WHERE A.ACTIVE = 1 AND A.ENTRY_MODEL = ?2 order by A.ENTRY_SCREEN)";
	/**
	 * This method is used to deassign the image assigned to EntryScreen
	 * 
	 * @param qiEntryScreen
	 * @param updateUser
	 */	
	@Transactional
	public void  deassignImage(String qiEntryScreen, String updateUser) {
		 update(Parameters.with("imageName",null).put("updateUser", updateUser), Parameters.with("entryScreen", qiEntryScreen));
	}

	/**
	 * This method is used to assign the imageName assigned for EntryScreen
	 * 
	 * @param screenName
	 * @param imageName
	 * @param updateUser
	 */
	@Transactional
	public void updateAssignImage(String qiEntryScreen, String imageName, String updateUser) {
		update(Parameters.with("imageName", imageName).put("updateUser", updateUser), Parameters.with("entryScreen", qiEntryScreen));
	}
	
	/**
	 * This method is used to get the EntryScreen and its details
	 * 
	 * @param productType
	 * @param entryModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findEntryScreensByEntryModel( String entryModel) {
		 Parameters params = Parameters.with("entryModel", entryModel);
		return findResultListByQuery(FIND_ENTRYSCREENS_BY_ENTRY_MODEL, params);
	}
	
	/**
	 * This method is used to get the EntryScreen and its details
	 * 
	 * @param entryScreen
	 * @return
	 */
	public QiEntryScreen findByEntryScreenNameAndIsImage(String entryScreen, String entryModel, short version) {
		return findFirst(Parameters.with("id.entryScreen",entryScreen).put("id.entryModel", entryModel).put("id.isUsed", version)
				.put("isImage", (short)1).put("active", (short)1));
	}

	/**
	 * This method is used to update the EntryScreen and its details
	 * @param entryScreen
	 * @param oldEntryScreenName
	 */
	
	
	
	@SuppressWarnings("unchecked")
	public List<QiEntryScreenDto> findEntryScreenData(String entryModel,String plant) {
		List<QiEntryScreenDto> entryModelDtoList = new ArrayList<QiEntryScreenDto>();
		Parameters params = Parameters.with("1", entryModel).put("2", plant);
		List<Object[]> entryScreenObjList = findResultListByNativeQuery(FIND_ENTRY_SCREEN_MODEL, params);
		setEntryModelDtoList(entryModelDtoList, entryScreenObjList);
		return entryModelDtoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<QiEntryScreenDto> findEntryScreenData(String entryModel,String plant, boolean isImage) {
		List<QiEntryScreenDto> entryModelDtoList = new ArrayList<QiEntryScreenDto>();
		Parameters params = Parameters.with("1", entryModel).put("2", plant);
		int i = (isImage ? 1 : 0);
		params.put("3", i);
		List<Object[]> entryScreenObjList = findResultListByNativeQuery(FIND_ENTRY_SCREEN_MODEL_IS_IMAGE, params);
		setEntryModelDtoList(entryModelDtoList, entryScreenObjList);
		return entryModelDtoList;
	}
	
	private void setEntryModelDtoList(List<QiEntryScreenDto> entryModelDtoList,List<Object[]> entryScreenObjList) {
		QiEntryScreenDto qiEntryScreenDto;
		if(entryScreenObjList!=null && !entryScreenObjList.isEmpty()){
			for(Object[] entryModelObj : entryScreenObjList){
				
				qiEntryScreenDto = new QiEntryScreenDto();
				qiEntryScreenDto.setEntryScreen(null!=entryModelObj[0] ? entryModelObj[0].toString() : "" );
				qiEntryScreenDto.setEntryModel(null!=entryModelObj[1] ? entryModelObj[1].toString() : "" );
				qiEntryScreenDto.setImageName(null!=entryModelObj[2] ? entryModelObj[2].toString() : "");
				qiEntryScreenDto.setDivisionId(null!=entryModelObj[3] ? entryModelObj[3].toString() : "" );
				qiEntryScreenDto.setImageValue(null!=entryModelObj[4] ? entryModelObj[4].toString() : "");
				qiEntryScreenDto.setUsed(null!=entryModelObj[5] ? entryModelObj[5].toString() : "");
				qiEntryScreenDto.setIsUsedVersion(null!=entryModelObj[6] ?Short.valueOf(entryModelObj[6].toString()) : null);
				qiEntryScreenDto.setPlantName(entryModelObj.length > 7 && null != entryModelObj[7] ? String.valueOf(entryModelObj[7].toString()) : null);
				qiEntryScreenDto.setProductType(entryModelObj.length > 8 && null != entryModelObj[8] ? String.valueOf(entryModelObj[8].toString()) : null);
				qiEntryScreenDto.setDivisionName(entryModelObj.length > 9 && null != entryModelObj[9] ? String.valueOf(entryModelObj[9].toString()) : null);
				entryModelDtoList.add(qiEntryScreenDto);
			}
		}
	}
	
	public List<QiEntryScreenDto> getUsedEntryModelData(String entryModel, String plant) {
		List<QiEntryScreenDto> entryModelDtoList = new ArrayList<QiEntryScreenDto>();
		Parameters params = Parameters.with("1", entryModel).put("2", plant);
		@SuppressWarnings("unchecked")
		List<Object[]> entryScreenObjList = findResultListByNativeQuery(FIND_USED_ENTRY_SCREEN_MODEL, params);
		setEntryModelDtoList(entryModelDtoList, entryScreenObjList);
		return entryModelDtoList;
	}
	
	public List<QiEntryScreenDto> getNotUsedEntryModelData(String entryModel,String plant) {
		List<QiEntryScreenDto> entryModelDtoList = new ArrayList<QiEntryScreenDto>();
		Parameters params = Parameters.with("1", entryModel).put("2", plant);
		@SuppressWarnings("unchecked")
		List<Object[]> entryScreenObjList = findResultListByNativeQuery(FIND_NOT_USED_ENTRY_SCREEN_MODEL, params);
		setEntryModelDtoList(entryModelDtoList, entryScreenObjList);
		return entryModelDtoList;
	}
	
	public List<QiEntryScreenDto> findAllByFilter(String filterData,List<Short> statusList,String siteName) {
		Parameters params;
		params = Parameters.with("1", "%"+filterData+"%")
				.put("2", ((filterData.equalsIgnoreCase(QiEntryScreenType.IMAGE.getName()))?(short)1:(filterData.equalsIgnoreCase(QiEntryScreenType.TEXT.getName()))?(short)0:(short)2))
				.put("3", ((filterData.equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()))?(short)1:(filterData.equalsIgnoreCase(QiActiveStatus.INACTIVE.getName()))?(short)0:(short)2))
				.put("4", statusList.get(0))
				.put("5", statusList.get(1))
				.put("6", siteName);

			return findAllByNativeQuery(FIND_ALL_ENTRY_SCREEN_DATA, params, QiEntryScreenDto.class);
	}
		
	@Transactional
	public void updateByEntryScreen(QiEntryScreen qiEntryScreen, String oldEntryScreen, String oldEntryModel, short oldVersion) {
		Parameters params = Parameters.with("1", qiEntryScreen.getActiveValue())
				.put("2", qiEntryScreen.getId().getEntryScreen()).put("3",qiEntryScreen.getEntryScreenDescription())
				.put("4", qiEntryScreen.getIsImage()).put("5",qiEntryScreen.getProductType())
				.put("6", qiEntryScreen.getUpdateUser()).put("7",new Date()).put("8", qiEntryScreen.getId().getEntryModel())
				.put("9",qiEntryScreen.getImageName()).put("10", oldEntryScreen)
				.put("11", oldEntryModel).put("12", oldVersion);
		executeNativeUpdate(UPDATE_REPAIR_METHOD, params);
	}
	
	public List<QiEntryScreen> findAllEntryScreenByEntryModel(String entryModel, short isUsed) {
		return findAll(Parameters.with("id.entryModel", StringUtils.trimToEmpty(entryModel)).put("id.isUsed", isUsed));
	}
	
	@Transactional
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel, short isUsed) {
		Parameters params = Parameters.with("newEntryModel", newEntryModel)
				.put("updateUser", userId)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", isUsed);
		executeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel){
		Parameters params = Parameters.with("1", plant).put("2", selectedProductType).put("3", entryModel);
		return findResultListByNativeQuery(FIND_ALL_BY_PLANT_PRODUCT_TYPE_AND_ENTRY_MODEL, params);
	}
	
	/**
	 * This method is used to get list of entryScreens with dept and image Name assigned based on entryModel selection
	 * 
	 * @param entryModel
	 */
	public List<QiEntryScreenDto> findAllEntryScreensByEntryModel(String entryModel) {
		Parameters params = Parameters.with("1", entryModel);
		return findAllByNativeQuery(FIND_ENTRY_SCREENS, params,QiEntryScreenDto.class);
	}
	/**
	 * This method is used to findAll entryScreen based on Model and Dept
	 */
	public List<QiEntryScreenDto> findAllByModelAndDept(String entryModel,String entryDepartment, short version){
		Parameters params = Parameters.with("1", entryModel).put("2", entryDepartment).put("3", version);
		return findAllByNativeQuery(FIND_ENTRY_SCREENS_BY_MODEL_AND_DEPT, params, QiEntryScreenDto.class);
	}	
/** This method will update imageName for the entry screen.
	 * 
	 * @param newImageName
	 * @param updateUser
	 * @param oldImageName
	 */
	@Transactional
	public void updateImageName(String newImageName, String updateUser, String oldImageName) {
		Parameters params = Parameters.with("1", newImageName).put("2", updateUser).put("3", oldImageName);
		executeNativeUpdate(UPDATE_IMAGE_NAME_FOR_ENTRY_SCREEN, params);
	}

	/** This method return count of entry screens which have given image assigned to that.
	 * 
	 * @param imageName
	 */
	public Long findCountByImageName(String imageName) {
		return count(Parameters.with("imageName", imageName));
	}


	/**  This method is used to determine if entry screen is assigned to that model
	 * 
	 * @param entryScreen
	 * @param entryModel
	 */
	public long countByEntryScreenAndModel(String entryScreen, String entryModel) {
		return count(Parameters.with("id.entryScreen", entryScreen).put("id.entryModel", entryModel));
	}
	
	/** This method return list of entry screens name which have given 
	 *  image assigned to that and have part defect combinations assigned.
	 * 
	 * @param imageName
	 */
	public List<String> findAllByEntryScreenDefect(String imageName) {
		return findResultListByNativeQuery(FIND_ENTRY_SCREENS_BY_DEFECT_COMBINATION, Parameters.with("1", imageName));
	}


	/** This method return list of entry screens name which have given 
	 *  image assigned to that and have entry station configured.
	 * 
	 * @param imageName
	 */
	public List<String> findAllByStationEntry(String imageName) {
		return findResultListByNativeQuery(FIND_ENTRY_SCREENS_BY_ENTRY_SCREEN_DEFECT, Parameters.with("1", imageName));
	}

	@Transactional
	public void updateEntryScreenForImageName(String imageName, String updateUser, String entryScreen, String entryModel, short version) {
		update(Parameters.with("imageName", imageName).put("updateUser", updateUser), 
				Parameters.with("id.entryScreen", entryScreen).put("id.entryModel", entryModel)
				.put("id.isUsed", version));	
	}
	
	
	
	/**
	 * This method is used to fetch EntryScreen based on Plant,productType,entryModel,entryDept
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllByPlantProductTypeEntryModelAndEntryDept(String plant, String selectedProductType, String entryModel,String entryDept){
		Parameters params = Parameters.with("1", plant).put("2", selectedProductType).put("3", entryModel).put("4", entryDept);
		return findResultListByNativeQuery(FIND_ALL_BY_PLANT_PRODUCT_TYPE_ENTRY_MODEL_AND_ENTRY_DEPT, params);
	}
	/**
	 * This method is used to fetch EntryScreen based on Product Type
	 */
	public List<String> findAllByProductType(String productType) {
		return findResultListByNativeQuery(FIND_ENTRY_SCREENS_BY_PRODUCT_TYPE, Parameters.with("1", productType));
	}

	public List<QiEntryScreenDto> findAllByPlantAndProductType(String plant, String productType, boolean isImage) {

		List<QiEntryScreenDto> entryModelDtoList = new ArrayList<QiEntryScreenDto>();
		
		Parameters params = Parameters.with("1", plant).put("2", productType);
		
		StringBuilder queryString = new StringBuilder(FIND_ENTRY_SCREEN_BY_PRODUCT_TYPE);
		if (isImage) {
			params.put("3", 1);
			queryString.append("  AND entryScreen.IMAGE_NAME IS NOT NULL  ORDER BY entryScreen.ENTRY_SCREEN");
		}

		else {
			params.put("3", 0);
			queryString.append(" ORDER BY entryScreen.ENTRY_SCREEN");
		}
		
		List<Object[]> entryScreenObjList = findResultListByNativeQuery(queryString.toString(), params);
		setEntryModelDtoList(entryModelDtoList, entryScreenObjList);
		return entryModelDtoList;
	
	}

	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion) {
		executeNativeUpdate(UPDATE_VERSION_VALUE,Parameters.with("1", newVersion).put("2", entryModel).put("3", oldVersion));	
	}
	
	@Transactional
	public void removeByEntryModelAndVersion(String entryModel, short version) {
		delete(Parameters.with("id.entryModel",entryModel).put("id.isUsed", version));
	}

	/**
	 * This method is used to get the EntryScreen and its details
	 * 
	 * @param entryScreen
	 * @return
	 */
	public List<QiEntryScreen> findAllByEntryScreenName(String entryScreenName) {
		return findAll(Parameters.with("id.entryScreen", entryScreenName));
	}
	
	/**
	 * This method is used to get the EntryScreen and its details
	 * 
	 * @param productType
	 * @param entryModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findAllEntryScreensByPlantAndEntryModelAndPdc(String plant, String entryModel) {
		 Parameters params = Parameters.with("1", plant).put("2", entryModel);
		return findResultListByNativeQuery(FIND_ENTRYSCREENS_BY_PLANT_ENTRY_MODEL_PDC, params);
	}

	/**
	 * This method is used to get the EntryScreen for given plant and entry model
	 * 
	 * @param plant
	 * @param entryModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<QiEntryScreen> findAllEntryScreenByPlantAndEntryModel(String plant, String entryModel) {
		 Parameters params = Parameters.with("1", plant).put("2", entryModel);
		return findAllByNativeQuery(FIND_ENTRYSCREENS_BY_PLANT_AND_ENTRY_MODEL, params);
	}

	/**
	 * This method is used to get the EntryScreen for given plant and entry model
	 * 
	 * @param plant
	 * @param entryModel
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public long countEntryScreenByPlantAndEntryModel(String plant, String entryModel) {
		 Parameters params = Parameters.with("1", plant).put("2", entryModel);
		return countByNativeSql(COUNT_ENTRYSCREENS_BY_PLANT_AND_ENTRY_MODEL, params);
	}

	/**
	 * This method is used to get the EntryScreen used in station
	 * 
	 * @param entryScreen
	 * @return
	 */
	public List<QiEntryScreen> findAllScreenUsedInStationByModel(String entryModel, short version) {
		return findAll(Parameters.with("id.entryModel", entryModel).put("id.isUsed", version).put("screenIsUsed",(short) 1));
	}
	
	public int findUsedScreenCount(String entryModel) {
		return (int) count(Parameters.with("id.entryModel", entryModel).put("screenIsUsed", (short)1));
	}
	
	@Transactional
	public void updateScreenIsUsed(String entryModel, String entryScreen, short screenIsUsed){
		update(Parameters.with("screenIsUsed", screenIsUsed), 
			   Parameters.with("id.entryModel", entryModel).put("id.entryScreen", entryScreen).put("id.isUsed", (short)1));
	}
	
	public List<QiEntryScreen> findAllByEntryModelAndScreen(String entryModel, String entryScreen) {
		return findAll(Parameters.with("id.entryModel", entryModel).put("id.entryScreen", entryScreen));
	}
}
