package com.honda.galc.client.product.entry;

import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class SearchByTransactionIdModel {
	private ApplicationContext applicationContext;
	private ProductPropertyBean property;

	private List<String> products;

	public SearchByTransactionIdModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public List<String> findProductsByTransactionId(long transactionId,String prodType) {
		products = ServiceFactory.getDao(QiDefectResultDao.class).findProductIdsByGroupTransId(transactionId,prodType);
		return products;
	}
	
	public List<String> findProductsByTransactionIdProdType(long transactionId,String prodType, int defectStatus) {
		products = ServiceFactory.getDao(QiDefectResultDao.class).findProductIdsByGroupTransIdProdType(transactionId,prodType,defectStatus);
		return products;
	}

	public List<String> findOutstandingProductsByTransactionId(long transactionId) {
		products = ServiceFactory.getDao(QiDefectResultDao.class).findOutstandingProductIdsByGroupTransId(transactionId);
		return products;
	}
	
	public List<QiDefectResult> findAllByGroupTransactionId(long transactionId) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByGroupTransId(transactionId);
	}
	
	public List<QiDefectResult> findAllByCreateUser(String createUser) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByCreateUser(createUser);
	}
	
	public List<QiDefectResult> findAllByDefectType(String defectType) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByDefectType(defectType);
	}
	
	public List<QiDefectResult> findAllByProductId(String productId) {
		return ServiceFactory.getDao(QiDefectResultDao.class).findAllByProductId(productId);
	}
	
	public BaseProduct findProduct(String productId) {
		return ProductTypeUtil.getProductDao(applicationContext.getProductTypeData().getProductTypeName()).findBySn(productId);
	}

	public ProductPropertyBean getProperty() {
		if (property == null) {
			property = PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}
		return property;
	}

	public List<String> getProducts() {
		return this.products;
	}

	public String getProductType() {
		return applicationContext.getProductTypeData().getProductTypeName();
	}
	public boolean isManualProductEntryEnabled() {
		return getProductPropertyBean().isManualProductEntryEnabled();
	}

	public boolean isKeyboardButtonVisible() {
		return getProductPropertyBean().isKeyboardButtonVisible();
	}
	
	public ProductPropertyBean getProductPropertyBean() {
		if(property == null) {
			property = PropertyService.getPropertyBean(ProductPropertyBean.class, applicationContext.getProcessPointId());
		}
		return property;
	}

}
