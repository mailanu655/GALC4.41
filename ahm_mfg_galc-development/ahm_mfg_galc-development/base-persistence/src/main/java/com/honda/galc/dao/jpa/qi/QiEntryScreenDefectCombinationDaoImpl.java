package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombinationId;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.service.Parameters;

public class QiEntryScreenDefectCombinationDaoImpl extends BaseDaoImpl<QiEntryScreenDefectCombination,QiEntryScreenDefectCombinationId> implements QiEntryScreenDefectCombinationDao{

	private static final String UPDATE_ENTRY_SCREEN_AND_MODEL_NAME = "update QiEntryScreenDefectCombination e "
			+ "set e.id.entryScreen=:newEntryScreen, e.id.entryModel=:newEntryModel, e.updateUser=:updateUser "
			+ "where e.id.entryScreen=:oldEntryScreen and e.id.entryModel=:oldEntryModel and e.id.isUsed=:isUsed";
	
	private static final String UPDATE_ENTRY_MODEL_NAME = "update QiEntryScreenDefectCombination e set e.id.entryModel=:newEntryModel, e.updateUser=:updateUser where e.id.entryModel=:oldEntryModel and e.id.isUsed=:isUsed";
	
	private static final String UPDATE_TEXT_ENTRY_NAME = "update GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX e "
			+ "set e.TEXT_ENTRY_MENU = ?1 , e.UPDATE_USER =?2 "
			+ "where e.TEXT_ENTRY_MENU = ?3 AND ENTRY_SCREEN = ?4 AND ENTRY_MODEL = ?5 AND IS_USED = ?6";
		
	private static final String FIND_AUDIT_PRIMARY_KEY_COMBINATION = " SELECT distinct ESDCT.ENTRY_SCREEN,PLCT.INSPECTION_PART_NAME,PLCT.INSPECTION_PART_LOCATION_NAME,PLCT.INSPECTION_PART_LOCATION2_NAME,PLCT.INSPECTION_PART2_NAME,"+
			" PLCT.INSPECTION_PART2_LOCATION_NAME,PLCT.INSPECTION_PART2_LOCATION2_NAME,PLCT.INSPECTION_PART3_NAME,"+
			" RDCT.DEFECT_TYPE_NAME,RDCT.DEFECT_TYPE_NAME2 FROM GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX ESDCT "+
			" LEFT OUTER JOIN GALADM.QI_REGIONAL_DEFECT_COMBINATION_TBX  RDCT on RDCT.REGIONAL_DEFECT_COMBINATION_ID=ESDCT.REGIONAL_DEFECT_COMBINATION_ID "+
			" Left OUTER JOIN GALADM.QI_PART_LOCATION_COMBINATION_TBX PLCT on PLCT.PART_LOCATION_ID=RDCT.PART_LOCATION_ID"+
			" WHERE ESDCT.REGIONAL_DEFECT_COMBINATION_ID=?1 AND ESDCT.ENTRY_SCREEN=?2 and ESDCT.ENTRY_MODEL = ?3";
	
	private final static String FIND_ALL_ENTRY_SCREENS_BY_PART_DEFECT_ID  ="select e from QiEntryScreenDefectCombination e where e.id.regionalDefectCombinationId in (:partDefectIdList)";
	private final static String FIND_ALL_ENTRY_SCREENS_BY_PART_LOCATION_ID  ="select e FROM QiEntryScreenDefectCombination e WHERE e.id.regionalDefectCombinationId in (select f.regionalDefectCombinationId FROM QiPartDefectCombination f where f.partLocationId in (:partLocationIdList))";
	private static final String UPDATE_VERSION_VALUE = "update GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX e set e.IS_USED = ?1 where e.ENTRY_MODEL = ?2 and e.IS_USED=?3";

	private static final String DELETE_PDC_BY_PLANT_ENTRY_SCREEN_MENU_MODEL = "delete from QiEntryScreenDefectCombination e "
			+ "where trim(e.id.entryModel)=:entryModel and trim(e.id.entryScreen)=:entryScreen and e.id.isUsed=:isUsed "
			+ "and trim(e.textEntryMenu)=:menu and e.id.entryScreen in (select f.id.entryScreen from QiEntryScreenDept f, Division d "
			+ "where f.id.divisionId=d.divisionId and f.id.isUsed=:isUsed and trim(d.plantName)=:plantName)";
	
	private static final String DELETE_PDC_BY_PLANT_ENTRY_SCREEN_MODEL = "delete from QiEntryScreenDefectCombination e "
			+ "where trim(e.id.entryModel)=:entryModel and trim(e.id.entryScreen)=:entryScreen and e.id.isUsed=:isUsed "
			+ "and e.id.entryScreen in (select f.id.entryScreen from QiEntryScreenDept f, Division d "
			+ "where f.id.divisionId=d.divisionId and f.id.isUsed=:isUsed and trim(d.plantName)=:plantName)";
	
	private static final String FIND_ALL_BY_ENTRY_MODEL = "SELECT * FROM GALADM.QI_ENTRY_SCREEN_DEFECT_COMBINATION_TBX "
			+ "WHERE ENTRY_MODEL = ?1";
	
	@Transactional
	public void removeByTextEntryId(QiTextEntryMenuId textEntryMenu) {
		 delete(Parameters.with("id.entryScreen", textEntryMenu.getEntryScreen())
				 .put("id.entryModel", textEntryMenu.getEntryModel())
				 .put("id.isUsed", textEntryMenu.getIsUsed())
				 .put("textEntryMenu", textEntryMenu.getTextEntryMenu()));
	}
	
	@Transactional
	public void updateEntryScreenNameAndModel(QiEntryScreenId newEntryScreen, String oldEntryScreen, String oldEntryModel, short oldVersion, String userId) {
		Parameters params = Parameters.with("newEntryScreen", newEntryScreen.getEntryScreen())
				.put("newEntryModel", newEntryScreen.getEntryModel())
				.put("updateUser", userId)
				.put("oldEntryScreen", oldEntryScreen)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", oldVersion);
		executeUpdate(UPDATE_ENTRY_SCREEN_AND_MODEL_NAME, params);
	}

	@Transactional
	public void updateEntryModelName(String newEntryModel, String userId, String oldEntryModel,short isUsed) {
		Parameters params = Parameters.with("newEntryModel", newEntryModel)
				.put("updateUser", userId)
				.put("oldEntryModel", oldEntryModel)
				.put("isUsed", isUsed);
		executeUpdate(UPDATE_ENTRY_MODEL_NAME, params);
	}

	@Transactional
	public void updateTextEntryMenuName(QiTextEntryMenuId newTextEntryMenuId, String oldTextEntryMenu, String userId) {
		Parameters params = Parameters.with("1", newTextEntryMenuId.getTextEntryMenu())
				.put("2", userId)
				.put("3", oldTextEntryMenu)
				.put("4", newTextEntryMenuId.getEntryScreen())
				.put("5", newTextEntryMenuId.getEntryModel())
				.put("6", newTextEntryMenuId.getIsUsed());
		executeNativeUpdate(UPDATE_TEXT_ENTRY_NAME, params);
	}
	
	public List<QiEntryScreenDefectCombination> findAllByEntryScreen(String entryScreen, String entryModel, short version) {
		return findAll(Parameters.with("id.entryScreen", entryScreen)
				.put("id.entryModel", entryModel).put("id.isUsed", version));
	}
	
	@Override
	public long countByEntryModelAndScreen(String entryScreen, String entryModel) {
		return count(Parameters.with("id.entryScreen", entryScreen)
				.put("id.entryModel", entryModel));
	}
	
	@Override
	public long countByEntryModel(String entryModel) {
		return count(Parameters.with("id.entryModel", entryModel));
	}
	
	@Override
	public List<QiEntryScreenDefectCombination> findAllByEntryScreenAndEntryModel(String entryScreen, String entryModel) {
		return findAll(Parameters.with("id.entryScreen", entryScreen)
				.put("id.entryModel", entryModel));
	}
	
	/**
	 * This method will be used to derive referenced field values from different
	 * table for auditing purpose. All the values will be separated by space.
	 * 
	 */
	public String fetchAuditPrimaryKeyValue(int regionalDefectCombinationId, String entryScreenName, String entryModel) {
		Parameters params = Parameters.with("1", regionalDefectCombinationId).put("2", entryScreenName).put("3", entryModel);
		return fetchAuditPrimaryKeyValue(FIND_AUDIT_PRIMARY_KEY_COMBINATION,params);
	}

	public Long findCountByEntryScreenName(String entryScreen) {
		return count(Parameters.with("id.entryScreen", entryScreen));
	}
	/*
	 * this method is used to find all QiEntryScreenDefectCombination by regionalDefectCombinationId
	 */
	public List<QiEntryScreenDefectCombination> findAllByRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		return findAll(Parameters.with("id.regionalDefectCombinationId", regionalDefectCombinationId));
	}
	/**
	 * This method is used to find All Entry Screen By Part Defect Id. 
	 * 
	 * @param partDefectIdList
	 */
	public List<QiEntryScreenDefectCombination> findAllEntryScreensByPartDefectId(List<Integer> partDefectIdList) {
		Parameters params = Parameters.with("partDefectIdList", partDefectIdList);
		return findAllByQuery(FIND_ALL_ENTRY_SCREENS_BY_PART_DEFECT_ID,params);
	}
	/**
	 * This method is used to find All Entry Screen By Part Location Id. 
	 * 
	 * @param partLocationIdList
	 */
	public List<QiEntryScreenDefectCombination> findAllEntryScreensByPartLocationId(List<Integer> partLocationIdList) {
		Parameters params = Parameters.with("partLocationIdList", partLocationIdList);
		return findAllByQuery(FIND_ALL_ENTRY_SCREENS_BY_PART_LOCATION_ID,params);
		
	}
	
	public List<QiEntryScreenDefectCombination> findAllByEntryModelAndVersion(String entryModel, short version){
	    return findAll(Parameters.with("id.entryModel", entryModel).put("id.isUsed", version));	
	}
	
	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion) {
		executeNativeUpdate(UPDATE_VERSION_VALUE,Parameters.with("1", newVersion).put("2", entryModel).put("3", oldVersion));	
	}
	
	@Transactional
	public void removeByEntryModelAndVersion(String entryModel, short version) {
		delete(Parameters.with("id.entryModel",entryModel)
				.put("id.isUsed", version));
	}
	
	@Transactional
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version) {
		delete(Parameters.with("id.entryModel", entryModel).put("id.entryScreen", entryScreen)
				.put("id.isUsed", version));
	}
	
	@Transactional
	public void deleteByEntryScreenModelMenuAndVersion(String plantName, String entryScreen, String entryModel, String menu, short version) {
		Parameters params =
				Parameters.with("plantName",plantName.trim())
				.put("entryScreen", entryScreen.trim())
				.put("entryModel", entryModel.trim())
				.put("menu", menu.trim())
				.put("isUsed", version);
		executeUpdate(DELETE_PDC_BY_PLANT_ENTRY_SCREEN_MENU_MODEL, params);
	}
	
	@Transactional
	public void deleteByEntryScreenModelAndVersion(String plantName, String entryScreen, String entryModel, short version) {
		Parameters params =
				Parameters.with("plantName", plantName.trim())
				.put("entryScreen", entryScreen.trim())
				.put("entryModel", entryModel.trim())
				.put("isUsed", version);
		executeUpdate( DELETE_PDC_BY_PLANT_ENTRY_SCREEN_MODEL, params);
	}

	@SuppressWarnings("unchecked")
	public List<QiEntryScreenDefectCombination> findAllByEntryModel(String entryModel) {
		return findAll(Parameters.with("id.entryModel", entryModel));
	}

}
