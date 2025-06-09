package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 4/4/12 Time: 10:22 AM To
 * change this template use File | Settings | File Templates.
 */
public interface AlarmAlertService {

	void run();

	void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper);

}
