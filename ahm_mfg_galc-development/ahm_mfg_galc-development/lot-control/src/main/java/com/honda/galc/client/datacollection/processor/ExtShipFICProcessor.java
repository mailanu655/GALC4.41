package com.honda.galc.client.datacollection.processor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.SaveEntity;
import com.honda.galc.service.ServiceFactory;

public class ExtShipFICProcessor extends VQShipFICProcessor {

	private final static int NOT_RETURN_FACTORY = 5;
	private final static int OVER_24_HOURS = 1;
	private final static int UNDER_24_HOURS = 0;

	public ExtShipFICProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected void updateShippingStatus(String productId, Timestamp afOffTimestamp, EntityList<AbstractEntity> afterTrackingEntityList) throws Exception {
	    ShippingStatusDao shippingStatusDao = ServiceFactory.getDao(ShippingStatusDao.class);
	    ShippingStatus shippingStatus = shippingStatusDao.findByKey(productId);
	    
	    if (shippingStatus == null) {
	        shippingStatus = new ShippingStatus();
	        shippingStatus.setVin(productId);
	        // Vin cannot be invoiced more than once, invoiced flag set to 'N' on initial record creation and updated to 'Y' when invoiced
	        shippingStatus.setInvoiced("N");
	    }

	    shippingStatus.setStatus(NOT_RETURN_FACTORY);
	    Timestamp actualShipTime = new Timestamp(System.currentTimeMillis());
	    shippingStatus.setActualTimestamp(actualShipTime);

	    // Calculate OTS target time
	    StringBuffer afOffTime = new StringBuffer(new SimpleDateFormat("HH:mm:ss").format(afOffTimestamp));
	    java.sql.Date nextProductionDate = getNextProductionDate(afOffTimestamp);

	    StringBuffer otsTarget = new StringBuffer(nextProductionDate + " " + afOffTime.toString());
	    Timestamp otsTargetStamp = Timestamp.valueOf(otsTarget.toString());

	    // Check if shipping occurred within 24 hours
	    if (actualShipTime.after(otsTargetStamp)) {
	        shippingStatus.setOnTimeShipping(OVER_24_HOURS);
	    } else {
	        shippingStatus.setOnTimeShipping(UNDER_24_HOURS);
	    }

	    afterTrackingEntityList.addEntity(new SaveEntity(shippingStatus, shippingStatus.toString(), shippingStatusDao));
		Logger.getLogger().info("Updated the Shipping Status for VIN:" + productId);
	}
}
