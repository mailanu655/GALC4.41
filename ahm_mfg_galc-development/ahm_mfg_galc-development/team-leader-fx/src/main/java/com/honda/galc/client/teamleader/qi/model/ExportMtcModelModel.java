package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiEntryScreenDeptDao;
import com.honda.galc.dao.qi.QiEntryScreenModelDao;
import com.honda.galc.dao.qi.QiLocalDefectCombinationDao;
import com.honda.galc.dao.qi.QiStationEntryScreenDao;
import com.honda.galc.dao.qi.QiTextEntryMenuDao;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QiEntryModelVersioningStatus;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDept;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiEntryScreenModel;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.collections.FXCollections;

/**
 * 
 * <h3>EntryScreenMaintenanceModel Class description</h3>
 * <p> EntryScreenMaintenanceModel description </p>
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
 * July 07, 2016
 *
 *
 */
public class ExportMtcModelModel extends QiModel {

	private String entryModel = "";
	private String productType = "";
	
	public ExportMtcModelModel() {
		super();
	}

	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}

	public List<String> findEntryModelByProductType(String productType){
		return getDao(QiEntryModelDao.class).findEntryModelsByProductType(productType);
	}

	
	public List<ProductTypeData> getAllProductType() {
		return getDao(ProductTypeDao.class).findAll();
	}

	public List<QiEntryModel> getEntryModelForProductType(String selectedProductType) {
		return FXCollections.observableArrayList(getDao(QiEntryModelDao.class).findAllByProductType(selectedProductType));
	}

	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();

	}

	public List<String> findPlantBySite(String productType){
		return getDao(DivisionDao.class).findPlantBySite(productType);
	}

	public List<QiEntryScreen> findAllEntryScreenByPlantAndEntryModel(String plant, String entryModel) {
		return getDao(QiEntryScreenDao.class).findAllEntryScreenByPlantAndEntryModel(plant, entryModel);
	}

	public List<QiEntryModel> findAllEntryModelByName(String entryModel) {
		return getDao(QiEntryModelDao.class).findAllByName(entryModel);
	}

	public List<QiTextEntryMenu> findAllTextEntryMenuByEntryModel(String entryModel) {
		return getDao(QiTextEntryMenuDao.class).findAllTextEntryMenuByEntryModel(entryModel);
	}

	public List<QiEntryScreenDefectCombination> findAllEntryScreenDefectCombinationByEntryModel(String entryModel) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllByEntryModel(entryModel);
	}

	public List<QiEntryScreenDefectCombination> findAllEntryScreenDefectCombinationByEntryModelAndScreen(String entryModel, String entryScreen) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllByEntryScreenAndEntryModel(entryScreen, entryModel);
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getEntryModel() {
		return entryModel;
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	public List<QiLocalDefectCombination> findFirstXByPlantAndModel(String entryModel2, String plant, int startId) {
		return getDao(QiLocalDefectCombinationDao.class).findFirstXByPlantAndModel(entryModel, plant, startId);
	}
	
	public int countTextEntryMenuByEntryModel(String entryModel) {
		return (int)getDao(QiTextEntryMenuDao.class).countTextEntryMenuByEntryModel(entryModel);		
	}

	public int countEntryScreenByPlantAndEntryModel(String plant, String entryModel) {
		return (int)getDao(QiEntryScreenDao.class).countEntryScreenByPlantAndEntryModel(plant, entryModel);
	}
	
	public int countESDCByEntryModelAndScreen(String entryScreen, String entryModel) {
		return (int)getDao(QiEntryScreenDefectCombinationDao.class).countByEntryModelAndScreen(entryScreen, entryModel);
	}

	public int countLDCByEntryModelAndPlant(String entryPlant, String entryModel) {
		return (int)getDao(QiLocalDefectCombinationDao.class).countByEntryModelAndPlant(entryPlant, entryModel);
	}
	
}
