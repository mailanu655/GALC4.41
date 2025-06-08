package com.honda.galc.dao.jpa.qi;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiEntryScreenDeptDao;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qi.QiEntryScreenDept;
import com.honda.galc.entity.qi.QiEntryScreenDeptId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiEntryScreenDeptDaoImpl Class description</h3>
 * <p> QiEntryScreenDeptDaoImpl description </p>
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
 *
 *
 */

public class QiEntryScreenDeptDaoImpl extends BaseDaoImpl<QiEntryScreenDept, QiEntryScreenDeptId> implements QiEntryScreenDeptDao {
	private static String GET_DEPARTMENT_BY_SCREEN_NAME="select e.id.divisionId from QiEntryScreenDept e where e.id.entryScreen = :screenName ";
	private static final String UPDATE_ENTRY_MODEL_NAME = "update QiEntryScreenDept e set e.id.entryModel=:newEntryModel, e.updateUser=:updateUser where e.id.entryModel=:oldEntryModel and e.id.isUsed=:isUsed";
	private static final String UPDATE_ENTRY_SCREEN_DEPT = "update GALADM.QI_ENTRY_SCREEN_DEPT_TBX e set e.ENTRY_SCREEN= ?1 , e.ENTRY_MODEL = ?2 , " +			
			" e.UPDATE_USER = ?3, e.UPDATE_TIMESTAMP = ?4 where e.ENTRY_SCREEN= ?5 and e.ENTRY_MODEL = ?6 and e.IS_USED = ?7";
	
	private static final String DELETE_ENTRY_SCREEN_NAME = "delete from GALADM.QI_ENTRY_SCREEN_DEPT_TBX where  DIVISION_ID=?1 and ENTRY_SCREEN=?2" ;	
	
	private final  String FIND_ALL_BY_PLANT_PRODUCT_TYPE_AND_ENTRY_MODEL = "SELECT DISTINCT ESD.DIVISION_ID FROM  QI_ENTRY_SCREEN_DEPT_TBX ESD " 
			+  " JOIN QI_ENTRY_SCREEN_TBX ESM ON ESM.ENTRY_SCREEN = ESD.ENTRY_SCREEN "
			+ " JOIN GAL128TBX P ON ESD.DIVISION_ID = P.DIVISION_ID "
			+ " WHERE P.PLANT_NAME = ?1 AND  "
			+ " 	 ESM.PRODUCT_TYPE = ?2 AND "
			+ " 	 ESM.ENTRY_MODEL = ?3 "
			+ " 	 AND ESM.ACTIVE = 1  order by ESD.DIVISION_ID ";

	private final String Find_All_Division_By_Plant_ProductType_And_EntryModel =
			"SELECT P.* FROM  QI_ENTRY_SCREEN_DEPT_TBX ESD " 
			+ " JOIN QI_ENTRY_SCREEN_TBX ESM ON ESM.ENTRY_SCREEN = ESD.ENTRY_SCREEN "
			+ " JOIN GAL128TBX P ON ESD.DIVISION_ID = P.DIVISION_ID "
			+ " WHERE P.PLANT_NAME = ?1 AND  "
			+ " 	 ESM.PRODUCT_TYPE = ?2 AND "
			+ " 	 ESM.ENTRY_MODEL = ?3 "
			+ " 	 AND ESM.ACTIVE = 1  order by ESD.DIVISION_ID ";
	
	private static final String UPDATE_VERSION_VALUE = "update GALADM.QI_ENTRY_SCREEN_DEPT_TBX e set e.IS_USED = ?1 where e.ENTRY_MODEL = ?2 and e.IS_USED=?3";
	
	@SuppressWarnings("unchecked")
	public List<String> findDepartmentsForEntryScreen(String screenName) {
		 Parameters params = Parameters.with("screenName", screenName);
		return findResultListByQuery(GET_DEPARTMENT_BY_SCREEN_NAME, params);
	}
	
	@Transactional
	public void removeAllEntryScreenDepartment(List<QiEntryScreenDept> inOldButNotInNewList,String oldEntryScreenName) {
		for(QiEntryScreenDept entryDept:inOldButNotInNewList){
		Parameters params = Parameters.with("1", entryDept.getId().getDivisionId())			
				.put("2", oldEntryScreenName);
		executeNativeUpdate(DELETE_ENTRY_SCREEN_NAME, params);
		}
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
	public void updateAllEntryScreenDept(List<QiEntryScreenDept> qiEntryScreenDeptList, QiEntryScreenDto oldQiEntryScreenDto) {
		for(QiEntryScreenDept qiEntryDept:qiEntryScreenDeptList){
			Parameters params = Parameters.with("1", qiEntryDept.getId().getEntryScreen())
					.put("2", qiEntryDept.getId().getEntryModel())
					.put("3", qiEntryDept.getUpdateUser())
					.put("4", new Date())
					.put("5", oldQiEntryScreenDto.getEntryScreen())
					.put("6", oldQiEntryScreenDto.getEntryModel())
					.put("7", oldQiEntryScreenDto.getIsUsedVersion());					
			executeNativeUpdate(UPDATE_ENTRY_SCREEN_DEPT, params);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel){
		Parameters params = Parameters.with("1", plant).put("2", selectedProductType).put("3", entryModel);
		return findResultListByNativeQuery(FIND_ALL_BY_PLANT_PRODUCT_TYPE_AND_ENTRY_MODEL, params);
	}

	@SuppressWarnings("unchecked")
	public List<Division> findAllDivisionByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel){
		Parameters params = Parameters.with("1", plant).put("2", selectedProductType).put("3", entryModel);
		return findAllByNativeQuery(Find_All_Division_By_Plant_ProductType_And_EntryModel, params, Division.class );
	}

	public List<QiEntryScreenDept> findAllByEntryModelAndIsUsed(String entryModel, short version){
	    return findAll(Parameters.with("id.entryModel", entryModel).put("id.isUsed", version));	
	}
	
	@Transactional
	public void updateVersionValue(String entryModel, short oldVersion, short newVersion) {
		executeNativeUpdate(UPDATE_VERSION_VALUE,Parameters.with("1", newVersion).put("2", entryModel).put("3", oldVersion));	
	}
	
	@Transactional
	public void removeByEntryModelAndVersion(String entryModel, short version) {
		delete(Parameters.with("id.entryModel",entryModel).put("id.isUsed", version));
	}
	
	
	@Transactional
	public void removeByEntryScreenModelAndVersion(String entryScreen, String entryModel, short version) {
		delete(Parameters.with("id.entryModel", entryModel).put("id.entryScreen", entryScreen)
				.put("id.isUsed", version));
	}
}
