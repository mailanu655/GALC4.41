package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

/**
 * User: vcc30690
 * Date: 6/24/11
 */
public interface OrderFulfillmentService extends StampStorageService {

   void startFulfillment();

   void run();

   void setServiceRoleWrap(ServiceRoleWrapper serviceRoleWrap);
}
