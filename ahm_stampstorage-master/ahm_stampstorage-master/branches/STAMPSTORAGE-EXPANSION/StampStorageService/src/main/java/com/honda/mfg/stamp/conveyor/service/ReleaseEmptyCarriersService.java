package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

/**
 * User: vcc30690 Date: 7/19/11
 */
public interface ReleaseEmptyCarriersService {

	void run();

	void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper);
}
