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
public class EntryScreenMaintenanceModel extends QiModel {

	public EntryScreenMaintenanceModel() {
		super();
	}

	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}

	public List<QiEntryScreenDto> findAllEntryScreensByFilter(String filterValue, List<Short> statusList) {
		return getDepartmentIdValues(getDao(QiEntryScreenDao.class).findAllByFilter(filterValue, statusList, getSiteName()));
	}

	private List<QiEntryScreenDto> getDepartmentIdValues(List<QiEntryScreenDto> qiEntryScreenDtoList) {
		Map<String, QiEntryScreenDto> qiEntryScreenDtosTempMap = new HashMap <String, QiEntryScreenDto>();
		for (int i = 0; i < qiEntryScreenDtoList.size(); i++) {
			if (qiEntryScreenDtosTempMap.get(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel() +qiEntryScreenDtoList.get(i).getIsUsedVersion()) == null) {
				qiEntryScreenDtosTempMap.put(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel() +qiEntryScreenDtoList.get(i).getIsUsedVersion(), qiEntryScreenDtoList.get(i));
				continue;
			} else {
				QiEntryScreenDto dto = qiEntryScreenDtosTempMap.get(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel() +qiEntryScreenDtoList.get(i).getIsUsedVersion());
				dto.setDivisionId(dto.getDivisionId().trim() + "," + qiEntryScreenDtoList.get(i).getDivisionId().trim());
			}
		}
		return new ArrayList<QiEntryScreenDto>(qiEntryScreenDtosTempMap.values());
	}

	public List<String> findEntryModelByProductType(String productType){
		return getDao(QiEntryModelDao.class).findEntryModelsByProductType(productType);
	}

	public List<Division> findAllDivisionByPlant(String siteName,String plantName){
		return getDao(DivisionDao.class).findById(siteName,plantName);
	}

	public QiEntryScreen createEntryScreen(QiEntryScreen qiEntryScreen) {
		QiEntryScreenDao dao = getDao(QiEntryScreenDao.class);
		if(dao.findByKey(qiEntryScreen.getId()) != null){
			return null;
		}
		else
			return((QiEntryScreen)dao.insert(qiEntryScreen));
	}

	public QiEntryScreenModel createEntryScreenModel(QiEntryScreenModel qiEntryScreenModel) {
		return (QiEntryScreenModel) getDao(QiEntryScreenModelDao.class).insert(qiEntryScreenModel);
	}

	public void createEntryScreenDept(List<QiEntryScreenDept> qiEntryScreenDept) {
		getDao(QiEntryScreenDeptDao.class).insertAll(qiEntryScreenDept);
	}
		
	public void updateByEntryScreen(QiEntryScreen qiEntryScreen, QiEntryScreenDto oldQiEntryScreenDto) {
		getDao(QiEntryScreenDao.class).updateByEntryScreen(qiEntryScreen, 
				oldQiEntryScreenDto.getEntryScreen(), oldQiEntryScreenDto.getEntryModel(), oldQiEntryScreenDto.getIsUsedVersion());
	}
					
	public List<QiEntryScreenDept> updateEntryScreenDepartment(List<QiEntryScreenDept> qiEntryScreenDeptList) {	
		return (List<QiEntryScreenDept>) getDao(QiEntryScreenDeptDao.class).saveAll(qiEntryScreenDeptList);
	}
	
	public void updateAllEntryScreenDept(List<QiEntryScreenDept> qiEntryScreenDeptList, QiEntryScreenDto oldQiEntryScreenDto) {
			getDao(QiEntryScreenDeptDao.class).updateAllEntryScreenDept(qiEntryScreenDeptList, oldQiEntryScreenDto);		
	}

	public void deleteAllEntryScreenDept(List<QiEntryScreenDept> deleteEntryScreenDeptList) {
		getDao(QiEntryScreenDeptDao.class).removeAll(deleteEntryScreenDeptList);
	}
		
	public List<ProductTypeData> getAllProductType() {
		return getDao(ProductTypeDao.class).findAll();
	}

	public List<QiEntryModel> getEntryModelForProductType(String selectedProductType) {
		return FXCollections.observableArrayList(getDao(QiEntryModelDao.class).findAllByProductType(selectedProductType));
	}

	public void updateEntryScreenStatus(List<QiEntryScreen> qiEntryScreen)
	{
		getDao(QiEntryScreenDao.class).updateAll(qiEntryScreen);
	}

	public boolean isEntryScreenExists(QiEntryScreenId entryScreenId) {
		return getDao(QiEntryScreenDao.class).findByKey(entryScreenId)!=null;
	}	

	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();

	}

	public List<String> findPlantBySite(String productType){
		return getDao(DivisionDao.class).findPlantBySite(productType);
	}

	public QiEntryScreen findEntryScreenByKey(QiEntryScreenId entryScreenId) {
			return getDao(QiEntryScreenDao.class).findByKey(entryScreenId);
	}
	
	public List<QiEntryScreenDefectCombination> findAllAssociatedPartsByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findAllByEntryScreen(qiEntryScreenDto.getEntryScreen(), 
				qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getIsUsedVersion());
	}
	
	public void removeAllPartsByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		getDao(QiEntryScreenDefectCombinationDao.class).removeByEntryScreenModelAndVersion(qiEntryScreenDto.getEntryScreen(),
				qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getIsUsedVersion());
	}
		
	public List<QiTextEntryMenu> findAllAssociatedMenusByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		return getDao(QiTextEntryMenuDao.class).findAllByEntryScreenAndModel(qiEntryScreenDto.getEntryScreen(), 
				qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getIsUsedVersion());
	}
	
	public void removeAllMenusByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		getDao(QiTextEntryMenuDao.class).removeAllMenusByEntryScreenAndModel(qiEntryScreenDto.getEntryScreen(), 
				qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getIsUsedVersion());
	}
	
	public List<QiStationEntryScreen> findAllAssociatedStationsByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		return getDao(QiStationEntryScreenDao.class).findAllByEntryScreen(qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getEntryScreen());
	}
			
	public List<QiLocalDefectCombination> findAllAssociatedLocalDefectsByEntryScreen(QiEntryScreenDto qiEntryScreenDto) {
		return getDao(QiLocalDefectCombinationDao.class).findAllByEntryScreenAndModel(qiEntryScreenDto.getEntryScreen(), 
				qiEntryScreenDto.getEntryModel(), qiEntryScreenDto.getIsUsedVersion());
	}
		
	public void updateAllByEntryScreen(QiEntryScreenId newEntryScreen, QiEntryScreenDto oldEntryScreenDto) {
		getDao(QiEntryScreenDefectCombinationDao.class).updateEntryScreenNameAndModel(newEntryScreen, 
				oldEntryScreenDto.getEntryScreen(),  oldEntryScreenDto.getEntryModel(), oldEntryScreenDto.getIsUsedVersion(), getUserId());
		getDao(QiTextEntryMenuDao.class).updateEntryScreenAndModel(newEntryScreen, oldEntryScreenDto.getEntryScreen(), 
				oldEntryScreenDto.getEntryModel(), oldEntryScreenDto.getIsUsedVersion(), getUserId());
		if( oldEntryScreenDto.getIsUsedVersion() == (short)1)
		getDao(QiStationEntryScreenDao.class).updateEntryScreenAndModel(newEntryScreen, oldEntryScreenDto.getEntryScreen(),
				oldEntryScreenDto.getEntryModel(), getUserId());
		getDao(QiLocalDefectCombinationDao.class).updateEntryScreenAndModel(newEntryScreen, 
				oldEntryScreenDto.getEntryScreen(), oldEntryScreenDto.getEntryModel(), oldEntryScreenDto.getIsUsedVersion(),  getUserId());
	}

	public void updateEntryScreenForImageName(String imageName, String updateUser, QiEntryScreenDto oldEntryScreenDto) {
		getDao(QiEntryScreenDao.class).updateEntryScreenForImageName(imageName,updateUser, 
				oldEntryScreenDto.getEntryScreen(), oldEntryScreenDto.getEntryModel(), oldEntryScreenDto.getIsUsedVersion());
	}
	
	public boolean isVersionCreated(String entryModel) {
		 return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
	public String findEntryModelVersionStatus(String entryModel) {
		List<QiEntryModel> qiEntryModel = getDao(QiEntryModelDao.class).findAllByName(entryModel);
		if (qiEntryModel.size() > 1) {
			return QiEntryModelVersioningStatus.getType((short) 0).getName();
		} else if (qiEntryModel.size() == 1) {
			return qiEntryModel.get(0).getIsUsedVersion();
		}
		return QiEntryModelVersioningStatus.getType((short) 0).getName();
	}
}
