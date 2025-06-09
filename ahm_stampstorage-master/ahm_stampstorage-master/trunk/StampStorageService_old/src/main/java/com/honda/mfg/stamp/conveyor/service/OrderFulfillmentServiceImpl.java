package com.honda.mfg.stamp.conveyor.service;


import com.honda.mfg.stamp.conveyor.newfulfillment.Fulfillment;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: vcc30690
 * Date: 5/25/11
 */

public class OrderFulfillmentServiceImpl implements OrderFulfillmentService, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(OrderFulfillmentServiceImpl.class);

    private Fulfillment fulfillment;
    private ServiceRoleWrapper serviceRoleWrap = null;
//	public ServiceRoleWrapper getServiceRoleWrap() {
//		return serviceRoleWrap;
//	}

	@Override
	public void setServiceRoleWrap(ServiceRoleWrapper serviceRoleWrap) {
		this.serviceRoleWrap = serviceRoleWrap;
	}

	public OrderFulfillmentServiceImpl(Fulfillment fulfillment) {
        this.fulfillment = fulfillment;
        serviceRoleWrap = ServiceRoleWrapperImpl.getInstance();
    }

    @Override
    public void run() {
    	if(!serviceRoleWrap.isPassive())  {
    		startFulfillment();
    	}
    }

    public void startFulfillment() {
      LOG.debug("Running fulfillment/delivery cycle for customer: " + fulfillment.getOrderManager().getLineName());
      fulfillment.run();
    }
}
