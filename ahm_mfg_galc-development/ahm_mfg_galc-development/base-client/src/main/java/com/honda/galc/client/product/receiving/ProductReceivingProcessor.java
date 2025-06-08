package com.honda.galc.client.product.receiving;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

/**
 * <h3>Class description</h3>
 * The abstract super class for processing received products.
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
public abstract class ProductReceivingProcessor {

	private ApplicationContext applicationContext;
	private ReceivedProductData data;
	private TrackingService trackingService;
	
	public ProductReceivingProcessor() {
	}

	abstract void saveProduct(String productId) throws ProductReceivingException;
	
	abstract void saveBuildResult(String newId, String partName, String originalId) throws ProductReceivingException;
	
	public void track(String newId) {
		getTrackingService().track(data.getProductType(), newId, getApplicationContext().getProcessPointId());
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

	public TrackingService getTrackingService() {
		if(trackingService == null) {
			trackingService = ServiceFactory.getService(TrackingService.class);
		}
		return trackingService;
	}
}
