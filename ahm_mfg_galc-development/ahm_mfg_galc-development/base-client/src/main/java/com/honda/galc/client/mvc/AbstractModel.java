package com.honda.galc.client.mvc;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

/**
 * 
 * 
 * <h3>AbstractModel Class description</h3>
 * <p> AbstractModel description </p>
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
 * @author Jeffray Huang<br>
 * Sep 12, 2014
 *
 *
 */
public abstract class AbstractModel implements IModel{
	
	protected ApplicationContext applicationContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public String getProcessPointId() {
		return getApplicationContext().getProcessPointId();
	}
	
	public ProcessPoint getProcessPoint() {
		return getApplicationContext().getProcessPoint();
	}
	
	public void track(ProductType productType, String productId) {
		ServiceFactory.getService(TrackingService.class)
			.track(productType, productId, getProcessPointId());
	}
	
	public Logger getLogger() {
		return getApplicationContext().getLogger();
	}
}
