package com.honda.galc.client.teamleader.qi.model;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>QIMaintenanceModel Class description</h3>
 * <p> QIMaintenanceModel description </p>
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
 * May 16, 2016
 *
 *
 */
public class QiFxmlModel {
	
	private String productKind = "";
	
	private ApplicationContext applicationContext;

	public QiFxmlModel() {
		super();
	}
	
	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		setProductKind(PropertyService.getProperty(applicationContext.getApplicationId(),
				QiConstant.PRODUCT_KIND, QiConstant.AUTOMOBILE));
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	/** This is used to get the instance of Logger
	 * @return the logger
	 */
	public Logger getLogger() {
		return getApplicationContext().getLogger();
	}
	public String getUserId(){
		return getApplicationContext().getUserId().toUpperCase();
	}
	
	public String getApplicationId() {
		return getApplicationContext().getApplicationId();
	}
}


