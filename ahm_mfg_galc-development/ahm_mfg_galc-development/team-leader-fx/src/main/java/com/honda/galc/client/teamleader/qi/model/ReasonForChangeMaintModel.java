package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.qi.QiReasonForChangeCategoryDao;
import com.honda.galc.dao.qi.QiReasonForChangeDetailDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiReasonForChangeCategory;
import com.honda.galc.entity.qi.QiReasonForChangeDetail;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ReasonForChangeMaintModel Class description</h3>
 * <p>
 * ReasonForChangeMaintModel description
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

public class ReasonForChangeMaintModel extends QiModel{	
	public String getSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}
	
	public List<Plant> findAllPlantBySite(String siteName) {
		return getDao(PlantDao.class).findAllBySite(siteName);
	}
	
	public List<Division> findAllDeptBySiteAndPlant(String site, String plant) {
		return getDao(DivisionDao.class).findById(site, plant);
	}

	public List<QiReasonForChangeCategory> findAllCategoryByDept(String site, String plant, String dept) {
		return getDao(QiReasonForChangeCategoryDao.class).findAllByDept(site, plant, dept);
	}
	
	public List<QiReasonForChangeDetail> findAllDetailByCategory(int categoryId) {
		return getDao(QiReasonForChangeDetailDao.class).findAllByCategory(categoryId);
	}
	
	public boolean isCategoryExisted(String site, String plant, String dept, String category) {
		List<QiReasonForChangeCategory> existingCategoryList = getDao(QiReasonForChangeCategoryDao.class).findAllByCategory(site, plant, dept, category);
		if (existingCategoryList == null || existingCategoryList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isDetailExisted(int categoryId, String detail) {
		List<QiReasonForChangeDetail> existingDetailList = getDao(QiReasonForChangeDetailDao.class).findAllByDetail(categoryId, detail);
		if (existingDetailList == null || existingDetailList.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public QiReasonForChangeCategory createCategory(String site, String plant, String dept, String category, String createUser) {
		return((QiReasonForChangeCategory)getDao(QiReasonForChangeCategoryDao.class).createCategory(site, plant, dept, category, createUser));
	}
	
	public void updateCategory(int categoryId, String category, String updateUser) {
		getDao(QiReasonForChangeCategoryDao.class).updateCategory(categoryId, category, updateUser);
	}

	public void deleteCategory(QiReasonForChangeCategory category) {
		getDao(QiReasonForChangeCategoryDao.class).remove(category);
	}
	
	public QiReasonForChangeDetail createDetail(int categoryId, String detail, String createUser) {
		return((QiReasonForChangeDetail)getDao(QiReasonForChangeDetailDao.class).createDetail(categoryId, detail, createUser));
	}
	
	public void updateDetail(int detailId, String detail, String updateUser) {
		getDao(QiReasonForChangeDetailDao.class).updateDetail(detailId, detail, updateUser);
	}

	public void deleteDetail(QiReasonForChangeDetail detail) {
		getDao(QiReasonForChangeDetailDao.class).remove(detail);
	}
}
