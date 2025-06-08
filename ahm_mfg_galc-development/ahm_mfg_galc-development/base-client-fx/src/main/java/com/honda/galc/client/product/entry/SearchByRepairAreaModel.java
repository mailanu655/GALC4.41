package com.honda.galc.client.product.entry;

import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.IModel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class SearchByRepairAreaModel implements IModel{
	private ApplicationContext applicationContext;
	private ProductPropertyBean property;
	
	public SearchByRepairAreaModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public List<String> getProductsByRepairArea(String repairAreaName) {
		 List<String> products = ServiceFactory.getDao(QiRepairAreaSpaceDao.class).getProductsByRepairArea(repairAreaName);
		 return products;
	}
	
	
	public List<? extends QiRepairArea> findRepairAreaByProductId(String processPointId, String productId){
		ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if(pp != null) {
			String divisionName = pp.getDivisionName();
			String siteName = pp.getSiteName();
			String plantName = pp.getPlantName(); 
			List<QiRepairArea> repairAreaNames = ServiceFactory.getDao(QiRepairAreaDao.class)
					.findRepairAreaBySitePlantDivisionProductId(siteName, plantName, divisionName, productId);
			return repairAreaNames;
		}else return null;
	}
	
	public List<? extends QiRepairArea> findRepairAreaByLocation(String processPointId, char location){
		ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if(pp != null) {
			String divisionName = pp.getDivisionName();
			String siteName = pp.getSiteName();
			String plantName = pp.getPlantName(); 
			List<QiRepairArea> repairAreaNames = ServiceFactory.getDao(QiRepairAreaDao.class)
					.findRepairAreaBySitePlantDivisionLocation(siteName, plantName, divisionName, location);
			return repairAreaNames;
		}else return null;
	}
	
	public List<? extends QiRepairArea> findRepairAreaByRepairAreaName(String processPointId, String repairAreaName){
		ProcessPoint pp = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if(pp != null) {
			String divisionName = pp.getDivisionName();
			String siteName = pp.getSiteName();
			String plantName = pp.getPlantName(); 
			List<QiRepairArea> repairAreaNames = ServiceFactory.getDao(QiRepairAreaDao.class)
					.findRepairAreaBySitePlantDivisionPartialName(siteName, plantName, divisionName, repairAreaName);
			return repairAreaNames;
		}else return null;
	}
	
	@Override
	public void reset() {
		
	}
	
	public ProductPropertyBean getProductPropertyBean() {
		if(property == null) {
			property = PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}
		return property;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setProperty(ProductPropertyBean property) {
		this.property = property;
	}

	public boolean isManualProductEntryEnabled() {
		return getProductPropertyBean().isManualProductEntryEnabled();
	}

	public boolean isKeyboardButtonVisible() {
		return getProductPropertyBean().isKeyboardButtonVisible();
	}

}
