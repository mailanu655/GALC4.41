package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiPartDefectCombinationDao;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
/**
 * 
 * <h3>HeadlessDefectEntryModel Class description</h3>
 * <p> HeadlessDefectEntryModel description </p>
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
public class HeadlessDefectEntryModel extends QiModel {

	
	/** This method finds list of all active plant
	 * @return List<String>
	 */
	public List<String> findAllBySite(String siteName) {
		return getDao(DivisionDao.class).findPlantBySite(siteName);
	}
	
	
	/** This method finds all Product Type
	 * @return List<String>
	 */
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}
	
	/** This method finds all entry model based on  plant and product type
	 * @param plant
	 * @param selectedProductType
	 * @return  List<String>
	 */
	public List<String> findAllByPlantAndProductType(String plant, String selectedProductType) {
		return getDao(QiEntryModelDao.class).findAllByPlantAndProductType(plant, selectedProductType);
	}
	
	
	/** This method finds all entry screen based on  plant,product type and entryModel
	 * @param plant
	 * @param selectedProductType
	 * @return List<String>
	 */
	public List<String> findAllByPlantProductTypeAndEntryModel(String plant, String selectedProductType, String entryModel) {
		return getDao(QiEntryScreenDao.class).findAllByPlantProductTypeAndEntryModel(plant, selectedProductType, entryModel);
	}
	
	/**
	 * This method finds all External System defects.
	 * @param externalSystemName
	 * @param entryScreen
	 * @return List<DefectMapDto>
	 */
	public List<DefectMapDto> findAllExternalSystemDefect(String externalSystemName, String entryScreen, String entryModel){
		return getDao(QiExternalSystemDefectMapDao.class).findAllExternalSystemDefect(externalSystemName, entryScreen, entryModel);
	}

	
	/**
	 * This method finds all Text Entry Entry Menu based on entry screen and entry model
	 * @param entryScreen
	 * @param entryModel
	 * @return List<DefectMapDto>
	 */
	public List<DefectMapDto> findAllTextEntryMenu(String entryScreen,String entryModel, boolean isImageEntryScreen) {
		return  getDao(QiExternalSystemDefectMapDao.class).findAllByEntryScreenAndModel(entryScreen, entryModel,isImageEntryScreen);
	}

	/**
	 * This method finds part defect Combination by entry screen,entry model and text entry menu
	 * @param entryScreen
	 * @param entryModel
	 * @param textEntryMenu
	 * @param isImageEntryScreen
	 * @return List<DefectMapDto>
	 */
	public List<DefectMapDto> findAllPartDefectCombByEntryScreenModelMenu(String entryScreen,String entryModel, String textEntryMenu) {
		return  getDao(QiExternalSystemDefectMapDao.class).findAllPartDefectCombByEntryScreenModelMenu(entryScreen, entryModel,textEntryMenu);
	}
	
	/**
	 * This method insert external system defect data
	 */
	public void addExternalSystemData(QiExternalSystemDefectMap externalSystemDefectMap){
			 getDao(QiExternalSystemDefectMapDao.class).insert(externalSystemDefectMap);
		
	}
	/**
	 * This method finds external System defect by Id 
	 * @return QiExternalSystemDefectMap
	 */
	public QiExternalSystemDefectMap findById(Integer externalSystemDefectMapId){
		 return getDao(QiExternalSystemDefectMapDao.class).findByKey(externalSystemDefectMapId);
	
	}
	
	/**
	 * This method update External System defect data 
	 * @param externalSystemDefectMap
	 */
	public void updateExternalSystemData(QiExternalSystemDefectMap externalSystemDefectMap){
			 getDao(QiExternalSystemDefectMapDao.class).update(externalSystemDefectMap);
	}
	
	/**
	 * This method finds external system defect data based on external part code and external defect code
	 * @param externalPartCode
	 * @param externalDefectCode
	 * @param externalSystemName
	 * @return QiExternalSystemDefectMap
	 */
	public QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystem(String externalPartCode, String externalDefectCode, String externalSystemName) {
		 return getDao(QiExternalSystemDefectMapDao.class).findByPartAndDefectCodeExternalSystem(externalPartCode,externalDefectCode,externalSystemName);
	}

	/**
	 * This method finds external system defect data based on external part code and external defect code
	 * @param externalPartCode
	 * @param externalDefectCode
	 * @param externalSystemName
	 * @param entryModel
	 * @return QiExternalSystemDefectMap
	 */
	public QiExternalSystemDefectMap findByPartAndDefectCodeExternalSystemAndEntryModel(String externalPartCode, String externalDefectCode, String externalSystemName, String entryModel) {
		 return getDao(QiExternalSystemDefectMapDao.class).findByPartAndDefectCodeExternalSystemAndEntryModel(externalPartCode,externalDefectCode,externalSystemName, entryModel);
	}

	/**
	 * This method delete External System defect data
	 */
	public void deleteExternalData(QiExternalSystemDefectMap qiExternalSystemDefectMap) {
		 getDao(QiExternalSystemDefectMapDao.class).remove(qiExternalSystemDefectMap);
	}
	
	/**
	 * This method finds external system defect data based on external part code and external defect code
	 * @param externalSystemName
	 * @param localDefectCombinationId
	 * @return QiExternalSystemDefectMap
	 */
	public QiExternalSystemDefectMap findByLocalCombIdAndExternalSystemName(String externalSystemName, Integer localDefectCombinationId) {
		return getDao(QiExternalSystemDefectMapDao.class).findByLocalCombIdAndExternalSystemName(externalSystemName,localDefectCombinationId);
	}

	/**
	 * This method finds entry screen is of type image or not
	 * @param entryScreen
	 * @return QiEntryScreen
	 */
	public Boolean findIsImageEntryScreen(String entryModel, String entryScreen) {
		List<QiEntryScreen> qiEntryScreenList = getDao(QiEntryScreenDao.class).findAllByEntryModelAndScreen(entryModel, entryScreen);
		for(QiEntryScreen qiEntryScreen : qiEntryScreenList) {
			if(qiEntryScreen.getIsImage() == (short)1)
				return true;
		}
		return false;
	}
	
	/**
	 * This method is used to create primary key for audit log
	 * @param regionalDefectCombinationId
	 * @return String
	 */
	public String getAuditPrimaryKeyValue(Integer regionalDefectCombinationId) {
		return getDao(QiPartDefectCombinationDao.class).fetchAuditPrimaryKeyValue(regionalDefectCombinationId);
		
	}

	/**
	 * This method finds Local data by local Combination Id
	 * @param localDefectCombinationId
	 * @return QiLocalDefectCombination
	 */
	public QiLocalDefectCombination findbyLocalCombinationId(Integer localDefectCombinationId) {
		return getDao(QiLocalDefectCombinationDao.class).findByKey(localDefectCombinationId);
	}
	
	public boolean isVersionCreated(String entryModel) {
		return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
}
