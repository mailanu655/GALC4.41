package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;


/**
 * User: vcc30690
 * Date: 6/24/11
 */
public interface OrderEmptyCarriersService extends StampStorageService {

    void run();

	void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper);
}
