package com.honda.galc.client.product.entry;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.IModel;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class SearchByProcessModel implements IModel{
	private ApplicationContext applicationContext;
	private ProductPropertyBean productPropertyBean;
	private QiPropertyBean qiPropertyBean;

	private List<String> products;
	
	public SearchByProcessModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void reset() {
	}

	public List<Division> getDepartments() {
		return ServiceFactory.getDao(DivisionDao.class).findAll();
	}
	
	public List<ProcessPoint> getProcessPoints(String divisionId) {
		return ServiceFactory.getDao(ProcessPointDao.class).findAllByDivisionId(divisionId);
	}

	public List<String> getMachines(String componentId) {
		String[] machineIds = PropertyService.getPropertyBean(QiPropertyBean.class, componentId).getMachineIds();
		return Arrays.asList(machineIds);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductHistory> getProductHistoyByDateRangeAndProcessPoint(String processPointId, Timestamp startTime, Timestamp endTime) {
		return (List<ProductHistory>) ProductTypeUtil.getProductHistoryDao(
				applicationContext.getProductTypeData().getProductType()).findAllByProcessPointAndTime(processPointId, startTime, endTime);
	}

	@SuppressWarnings("unchecked")
	public List<ProductHistory> getProductHistoyByDateRangeAndProcessPoint(String processPointId, String deviceId, Timestamp startTime, Timestamp endTime) {
		return (List<ProductHistory>) ProductTypeUtil.getProductHistoryDao(
				applicationContext.getProductTypeData().getProductType()).findAllByProcessPointTimeAndDeviceId(processPointId, deviceId, startTime, endTime);
	}
	
	public int getProductSearchTimeRangeLimit() {
		return getQiPropertyBean().getSearchTimeRangeLimit();
	}
	
	public ProductPropertyBean getProductPropertyBean() {
		if(productPropertyBean == null) {
			productPropertyBean= PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}
		return productPropertyBean;
	}
	
	public QiPropertyBean getQiPropertyBean() {
		if(qiPropertyBean == null) {
			qiPropertyBean= PropertyService.getPropertyBean(QiPropertyBean.class, applicationContext.getProcessPointId());
		}
		return qiPropertyBean;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public void setProperty(ProductPropertyBean property) {
		this.productPropertyBean = property;
	}
}
