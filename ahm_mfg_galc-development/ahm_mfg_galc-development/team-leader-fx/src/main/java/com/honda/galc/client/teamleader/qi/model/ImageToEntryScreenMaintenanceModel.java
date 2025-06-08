package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.dao.qi.QiEntryScreenDao;
import com.honda.galc.dao.qi.QiEntryScreenDefectCombinationDao;
import com.honda.galc.dao.qi.QiEntryScreenDeptDao;
import com.honda.galc.dao.qi.QiImageDao;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class ImageToEntryScreenMaintenanceModel extends QiModel {
	
	/**
	 * Find list of productTypes
	 */
	public List<String> findAllProductTypes() {
		return getDao(ProductTypeDao.class).findAllProductTypes();
	}

	/**
	 * Find list of entryModels on the basis of productType
	 *
	 * @param productType
	 * @return
	 */
	public List<String> findEntryModelsByProductType(String productType) {
		return getDao(QiEntryModelDao.class).findEntryModelsByProductType(productType);
	}

	/**
	 * Find list of entryScreens on the basis of entryModel
	 *
	 * @param productType
	 * @param entryModel
	 * @return
	 */
	public List<String> findEntryScreensByEntryModel(String entryModel) {
		return getDao(QiEntryScreenDao.class).findEntryScreensByEntryModel( entryModel);
	}

	/**
	 * Find entryScreen using screenName
	 */
	public QiEntryScreen findEntryScreenDetails(QiEntryScreenDto entryScreenDto) {
		return getDao(QiEntryScreenDao.class).findByEntryScreenNameAndIsImage(entryScreenDto.getEntryScreen(), 
				entryScreenDto.getEntryModel(), entryScreenDto.getIsUsedVersion());
	}

	/**
	 * Filter Data on the basis of Image Name and status
	 *
	 * @param filterValue
	 * @return
	 */
	public List<QiImageSectionDto> findImageByFilter(String filterValue) {
		return getDao(QiImageDao.class).findAllByProductKind(filterValue, getProductKind());
	}

	/**
	 * Find Image on the basis of imageName
	 *
	 * @param imageName
	 * @return
	 */
	public QiImage findImageByImageName(String imageName) {
		return getDao(QiImageDao.class).findImageByImageName(imageName);
	}

	/**
	 * Find Departments for the Entry Screen
	 *
	 * @param entryScreen
	 * @return
	 */
	public String findDepartmentsForEntryScreen(String entryScreen) {
		List<String> deptList = getDao(QiEntryScreenDeptDao.class).findDepartmentsForEntryScreen(entryScreen);
		StringBuilder departments = new StringBuilder();
		if(!deptList.isEmpty()){
			for (String dept : deptList) {
				departments.append(StringUtils.trim(dept) + ", ");
			}
			return departments.deleteCharAt(departments.length() - 2).toString();
		}
		else{
			return "";
		}
	}

	/**
	 * Update and assign Image Name for the Entry Screen
	 *
	 * @param newImageName
	 * @param entryScreen
	 */
	public void updateAssignImage(QiEntryScreen qiEntryScreen) {
		getDao(QiEntryScreenDao.class).update(qiEntryScreen);
	}

	/**
	 * This method is used to get the Site Name
	 */
	public String findSiteName() {
        return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}
	
	/**
	 * This method is used to get the entry screens on the basis of entryModel
	 */
	public List<QiEntryScreenDto> findAllEntryScreensByEntryModel(String entryModel) {
		return getDao(QiEntryScreenDao.class).findAllEntryScreensByEntryModel(entryModel);
	}

	/**
	 * This method is used to get count of part defect combinations for the entry screen.
	 */
	public Long getPdcCountByEntryScreenName(String entryScreen) {
		return getDao(QiEntryScreenDefectCombinationDao.class).findCountByEntryScreenName(entryScreen);
	}

	/** This method is used to get entry model using name of entry model.
	 * @param entryModel
	 * @return
	 */
	public QiEntryModel findEntryModelByName(QiEntryModelId id) {
		return getDao(QiEntryModelDao.class).findByKey(id);
	}
	
	/**
	 * Find Image on the basis of imageName
	 *
	 * @param imageName
	 * @return
	 */
	public QiImage findQiImageByImageName(String imageName) {
		return getDao(QiImageDao.class).findImageByImageName(imageName);
	}
	
	/**
	 * This method is used to check version created or not.
	 */
	public boolean isVersionCreated(String entryModel) {
		return (getDao(QiEntryModelDao.class).findEntryModelCount(entryModel) > 1);
	}
	
}
