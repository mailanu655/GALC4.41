package com.honda.galc.client.product.entry;

import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.IModel;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.dao.product.DunnageDao;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class SearchByDunnageModel implements IModel{
	private ApplicationContext applicationContext;
	private ProductPropertyBean property;
	
	private List<String> products;
	private List<? extends Dunnage> dunnages;
	
	public SearchByDunnageModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public List<String> getProductsByDunnage(String dunnage) {
		 products = ServiceFactory.getDao(DunnageContentDao.class).findAllProductIdsInDunnage(dunnage);
		 return products;
	}
	
	public List<? extends Dunnage> findByDunnage(String dunnage) {
		dunnages = ServiceFactory.getDao(DunnageDao.class).findAllByPartialDunnage(dunnage);
		return dunnages;
	}
	
	public List<? extends Dunnage> findAllByProductId(String productId) {
		dunnages = ServiceFactory.getDao(DunnageDao.class).findAllByPartialProductId(productId);
		return dunnages;
	}
	
	public List<? extends Dunnage> findByProductSpecCode(String productSpecCode) {
		dunnages = ServiceFactory.getDao(DunnageDao.class).findAllByPartialMtoc(productSpecCode);
		return dunnages;
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

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}
	
	public List<? extends Dunnage> getDunnages() {
		return dunnages;
	}
	
	public void setDunnages(List<Dunnage> dunnages) {
		this.dunnages = dunnages;
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
