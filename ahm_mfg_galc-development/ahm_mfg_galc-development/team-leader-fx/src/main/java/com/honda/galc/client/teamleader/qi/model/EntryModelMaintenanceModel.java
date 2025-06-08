package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryModelGroupingDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiEntryScreenDeptDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiTextEntryMenuDao;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDept;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>EntryModelMaintenanceModel Class description</h3>
 * <p>
 * this class manipulates the data in Entry Model and entry model grouping
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
 * @author L&T Infotech<br>
 *         Aug 30, 2016
 * 
 * 
 */
public class EntryModelMaintenanceModel extends QiModel {


	public EntryModelMaintenanceModel() {
		super();
	}

	/**
	 * This method fetches the data of product types.
	 */
	public List<String> getProductTypes() {

		return getDao(ProductTypeDao.class).findAllProductTypes();
	}

	/**
	 * This method refresh the Entry Models on selected Product type.
	 */
	public List<QiEntryModel> getEntryModelByProductType(String productType) {
		return getDao(QiEntryModelDao.class).findAllByProductType(productType);
		
	}

	/**
	 * This method fetches the assigned Mtc Models for a particular Entry Model.
	 */
	public List<QiEntryModelGrouping> getAssginedMtcModel(String entryModel, short isUsed) {

		return getDao(QiEntryModelGroupingDao.class).findAllByEntryModel(entryModel, isUsed);

	}

	/**
	 * This method sets the Available Mtc Model based on product type selection
	 * and filter.
	 */
	public List<QiMtcToEntryModelDto> findAvailableMtcModelData(String filter, String productType) {
		List<QiMtcToEntryModelDto> availableMtcList = new ArrayList<QiMtcToEntryModelDto>();
		availableMtcList = ProductTypeUtil.getProductSpecDao(productType).findAllMtcModelYearCodesByFilter(filter, productType);
		return availableMtcList;
	}
	
	/**
	 * This method sets the Available Mtc Model based on product type selection
	 * and filter for DieCast product that excluded the MODEL_YEAR_CODE.
	 */
	public List<QiMtcToEntryModelDto> findAvailableMtcModelDataForDieCast(String filter, String productType) {
		List<QiMtcToEntryModelDto> availableMtcList = new ArrayList<QiMtcToEntryModelDto>();
		availableMtcList = ServiceFactory.getDao(ProductSpecCodeDao.class).findAllMtcModelYearCodesByFilter(filter, productType);
		return availableMtcList;
	}
	
	/**
	 * This method gets Entry Model for a particular Product Type .
	 */
	public List<QiEntryModel> getEntryModelByStatus(String status, String productType) {
		return getDao(QiEntryModelDao.class).findAllEntryModelByStatus(status, productType);

	}

	/**
	 * This method is used to create a new Entry Model .
	 */
	public void createEntryModel(QiEntryModel createdEntryModel) {
			getDao(QiEntryModelDao.class).save(createdEntryModel);
	}

	/**
	 * This method is used to update an Entry Model .
	 */
	public void updateEntryModel(QiEntryModel selectedEntryModel) {
		getDao(QiEntryModelDao.class).update(selectedEntryModel);

	}

	/**
	 * This method is used to check if the Entry Model name already exists .
	 */
	public boolean isEntryModelExist(String entryModelName) {
		List<QiEntryModel> entryModel = getDao(QiEntryModelDao.class).findAllByName(entryModelName);
		return (entryModel != null && entryModel.size() > 0);
	}

	/**
	 * This method is used to fetch all the existing Mtc Models .
	 */
	public List<QiEntryModelGrouping> findAllEntryModelGroupingByProductType(String productType) {
		return getDao(QiEntryModelGroupingDao.class).findAllAssignedMtcModelByProductType(productType);
	}

	/**
	 * This method is used to fetch all the existing data by Mtc Models.
	 */
	public QiEntryModelGrouping findEntryModelGroupingByMtcModel(String mtcModel, short version) {
		return getDao(QiEntryModelGroupingDao.class).findByMtcModelAndVersion(mtcModel, version);
	}

	/**
	 * This method is used to remove data from QiEntryModelGrouping .
	 */
	public void removeEntryModelGrouping(List<QiEntryModelGrouping> qiEntryModelGroupingList) {
		getDao(QiEntryModelGroupingDao.class).removeAll(qiEntryModelGroupingList);

	}

	/**
	 * This method is used to insert data in QiEntryModelGrouping .
	 */
	public void updateEntryModelGrouping(List<QiEntryModelGrouping> newCombinationOfMtcModels) {
		getDao(QiEntryModelGroupingDao.class).saveAll(newCombinationOfMtcModels);

	}

	/**
	 * This method is used to update entry Model Name .
	 */
	public void updateEntryModelName(String newEntryModelName, String userId, String oldEntryModelName, short isUsed) {
		getDao(QiEntryModelDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);

	}
	/**
	 * This method is used to update entry Model Name in Entry Model Grouping .
	 */
	public void updateEntryModelGroupingId(String newEntryModelName, String userId, String oldEntryModelName, short isUsed) {
		getDao(QiEntryModelGroupingDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);

	}
	/**
	 * This method is used to update entry Model Name in Entry Screen Model .
	 */
	public void updateModelInAssociateTable(String newEntryModelName, String oldEntryModelName, String userId, short isUsed) {
		getDao(QiEntryScreenDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);
		getDao(QiEntryScreenDeptDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);
		getDao(QiEntryScreenDefectCombinationDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);
		getDao(QiTextEntryMenuDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);
		getDao(QiLocalDefectCombinationDao.class).updateEntryModelName(newEntryModelName, userId, oldEntryModelName, isUsed);
		getDao(QiStationEntryScreenDao.class).updateEntryModelName(newEntryModelName, oldEntryModelName, userId);
		getDao(QiExternalSystemDefectMapDao.class).updateEntryModelName(newEntryModelName, oldEntryModelName, userId);
	}

	public List<QiEntryScreen> findAllByEntryModel(String entryModel, short isUsed) {
		return getDao(QiEntryScreenDao.class).findAllEntryScreenByEntryModel(entryModel, isUsed);
	}

	public void removeEntryModel(QiEntryModel entryModel) {
		getDao(QiEntryModelDao.class).remove(entryModel);	
	}
	
	/*
	 * This method is used to delete inactivate entry screens
	 */
	public void deleteInactiveEntryScreens(List<QiEntryScreen> deletedEntryScreenList) {
		getDao(QiEntryScreenDao.class).removeAll(deletedEntryScreenList);
	}
	
	/*
	 * This method is used to find Entry Model by key
	 */
	public QiEntryModel findByKey(QiEntryModelId id){
		return getDao(QiEntryModelDao.class).findByKey(id);
	}

	public List<QiEntryScreenDept> findAllDeptByEntryModel(String entryModel, short version) {
		return getDao(QiEntryScreenDeptDao.class).findAllByEntryModelAndIsUsed(entryModel, version);
	}

	public List<QiTextEntryMenu> findAllTextMenuByEntryModel(String entryModel, short version) {
		return getDao(QiTextEntryMenuDao.class).findAllByEntryModelAndIsUsed(entryModel, version);
	}

	public List<QiEntryScreenDefectCombination> findAllDefectCombinationByEntryModel(String entryModel, short version) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllByEntryModelAndVersion(entryModel, version);
	}

	public List<QiLocalDefectCombination> findAllAssociatedLocalDefectCombinationByEntryModelAndIsUsed(String entryModel,short version) {
		return getDao(QiLocalDefectCombinationDao.class).findAllByEntryModelAndIsUsed(entryModel, version);
	}

	public void insertVersionDataInDependentTables(List<QiEntryModelGrouping> qiEntryModelGroupingVersionData,
			List<QiEntryScreen> qiEntryScreenNewVersion, List<QiEntryScreenDept> qiEntryScreenDeptVersionData, 
			List<QiTextEntryMenu> qiTextEntryMenuVersionData, List<QiEntryScreenDefectCombination> qiEntryScreenDefectCombinationVersionData 
			) {

		getDao(QiEntryModelGroupingDao.class).insertAll(qiEntryModelGroupingVersionData);
		getDao(QiEntryScreenDao.class).insertAll(qiEntryScreenNewVersion);
		getDao(QiEntryScreenDeptDao.class).insertAll(qiEntryScreenDeptVersionData);
		getDao(QiTextEntryMenuDao.class).insertAll(qiTextEntryMenuVersionData);
		getDao(QiEntryScreenDefectCombinationDao.class).insertAll(qiEntryScreenDefectCombinationVersionData);
	}
	
	public void deleteVersionDataFromDependentTables(String entryModel, short version) {
		
		getDao(QiLocalDefectCombinationDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryScreenDefectCombinationDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryScreenDeptDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiTextEntryMenuDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryScreenDao.class).removeByEntryModelAndVersion(entryModel, version);
		getDao(QiEntryModelGroupingDao.class).removeByEntryModelAndVersion(entryModel, version);
	}
	
	public void updateEntryModelVersion(String entryModel, short oldVersion, short newVersion, boolean updateEntryModel) {
		getDao(QiLocalDefectCombinationDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryScreenDefectCombinationDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryScreenDeptDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiTextEntryMenuDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryScreenDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		getDao(QiEntryModelGroupingDao.class).updateVersionValue(entryModel, oldVersion, newVersion);
		
		if(updateEntryModel)
			getDao(QiEntryModelDao.class).updateVersionValue(entryModel, oldVersion, (short)1, getUserId());
	}
	
	public List<QiLocalDefectCombination> findAllByEntryModelAndVersion(String entryModel, short version) {
		return getDao(QiLocalDefectCombinationDao.class).findAllByEntryModelAndIsUsed(entryModel, version);
	}
	
	/**
	 * This method finds localDefectCombinationId List mapping data based on local combination id
	 * @param localCombinationId
	 * @return List<QiExternalSystemDefectMap>ExternalSystemDefectMap
	 */
	public List<QiExternalSystemDefectMap> findAllByLocalCombinationId(int localCombinationId) {
		return getDao(QiExternalSystemDefectMapDao.class).findAllByLocalCombinationId(String.valueOf(localCombinationId));
	}
	
	public List<QiLocalDefectCombination> findAllByLocalDefectCombination(QiLocalDefectCombination localCombination) {
		return getDao(QiLocalDefectCombinationDao.class).findAllByLocalDefectCombination(localCombination, (short) 0);
	}
	
	public void updateExternalSystem(Map<Integer, Integer> localDefectVersionMap) {
		for (Integer externalSystemId : localDefectVersionMap.keySet()) {
			getDao(QiExternalSystemDefectMapDao.class).updateLocalDefectCombinationId(externalSystemId,
					localDefectVersionMap.get(externalSystemId), getUserId());
		}
	}
	
	/**
	 * This method is used to update an Entry Model .
	 */
	public void updateAllEntryScreen(List<QiEntryScreen> qiEntryScreenList) {
		getDao(QiEntryScreenDao.class).updateAll(qiEntryScreenList);
	}
	
	public void createLocalDefectCombinationDao(QiLocalDefectCombination localDefect) {
		getDao(QiLocalDefectCombinationDao.class).insert(localDefect);
	}
	
	public boolean isVersionCreated(String entryModel) {
		return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
	public boolean isMtcAssignedToModel(String entryModel, short version) {
		return (getDao(QiEntryModelGroupingDao.class).findAllByEntryModel(entryModel, version).size() > 0);
	}
}
