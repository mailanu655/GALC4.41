package com.honda.galc.client.product.receiving;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.events.ProductReceivingEvent;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3>
 * The controller for product receiving.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class ProductReceivingController<T> {
	public static final String CONVERSION_BUILD_ATTR_NAME = "CONVERSION_BUILD_ATTR_NAME";
	public static final String IMPORT_SUFFIX = "_IMPORT";
	private ProductReceivingManager receivingManager;
	private ApplicationContext applicationContext;
	private ReceivedProductData data;

	public ProductReceivingController() {
	}
	
	public List<String> findTargetProducts() {
		List<ProductSpecCode> productSpecCodes = ServiceFactory.getDao(ProductSpecCodeDao.class).findAll();
		List<String> models = new ArrayList<String>();
		for(ProductSpecCode code : productSpecCodes) {
			if(code.getId().getProductType().trim().equalsIgnoreCase(data.getProductType().getProductName())) {
				models.add(code.getModelCode());
			}
		}
		return models;
	}
	
	public List<String> findImportProducts(String targetProduct) {
		List<BuildAttribute> buildAttributes = ServiceFactory.getDao(BuildAttributeDao.class)
													.findAllMatchBuildAttributes(buildAttributeName());
		Set<String> models = new HashSet<String>();
		List<String> attrs = new ArrayList<String>();
		for(BuildAttribute attribute : buildAttributes) {
			if(targetProduct.equalsIgnoreCase(attribute.getProductSpecCode().substring(1, 4))) {
				for(String item : attribute.getAttributeValue().split(",")) {
					models.add(item);
				}
			}
		}
		attrs.addAll(models);
		return attrs;
	}
	
	public String productIdEntered(String productId, String format) {
		ProductReceivingEvent event = new ProductReceivingEvent();
		String newId = null;
		try {
			newId = getReceivingManager().processReceivedProductId(productId, format);
		} catch (ProductReceivingException e) {
			event.setResult(false);
			event.setMessage(e.getMessage());
			processComplete(event);
			return null;
		} catch (Exception e) {
			event.setResult(false);
			event.setMessage("Unable to process new product. Please call help desk.");
			processComplete(event);
			return null;
		}
		event.setMessage("New product was created successfully.");
		event.setLastId(newId);
		processComplete(event);
		return newId;
	}

	private void processComplete(ProductReceivingEvent event) {
		event.setClearScreen(true);
		notifyUI(100, event);
	}
	
	private void notifyUI(int delay, ProductReceivingEvent event) {
		SleepTimer timer = new SleepTimer(delay, event);
		SwingUtilities.invokeLater(timer);
	}
	
	public boolean isProductIdValid(String productId) {
		return !StringUtils.isEmpty(productId);  //Additional validation is done by conversion.
	}
	
	
	
	private String buildAttributeName() {
		String attrName = getApplicationProperty(CONVERSION_BUILD_ATTR_NAME);
		if(StringUtils.isEmpty(attrName)) {
			attrName = StringUtils.upperCase(data.getProductType().getProductName()) + IMPORT_SUFFIX;
		}
		return attrName;
	}

	public String getApplicationProperty(String propertyName) {
		return PropertyService.getProperty(getApplicationContext().getApplicationId(), propertyName);
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ReceivedProductData getData() {
		return data;
	}

	public void setData(ReceivedProductData data) {
		this.data = data;
	}

	public ProductReceivingManager getReceivingManager() {
		if(receivingManager == null) {
			receivingManager = new ProductReceivingManager();
			receivingManager.setApplicationContext(getApplicationContext());
		}
		receivingManager.setData(getData());
		return receivingManager;
	}
}

class SleepTimer implements Runnable {
	private int delay;
	private ProductReceivingEvent event;
	
	public SleepTimer(int delay, ProductReceivingEvent event) {
		this.delay = delay;
		this.event = event;
	}

	public void run() {
		try {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			Thread.yield();
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		EventBus.publish(event);
	}
}


