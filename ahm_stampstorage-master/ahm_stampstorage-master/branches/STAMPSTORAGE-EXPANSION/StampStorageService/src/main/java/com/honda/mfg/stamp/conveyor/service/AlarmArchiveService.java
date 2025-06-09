package com.honda.mfg.stamp.conveyor.service;

import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 4/2/12 Time: 1:24 PM To
 * change this template use File | Settings | File Templates.
 */
public interface AlarmArchiveService {

	void archiveAlarm(Long id, String user);

	void run();

	void setServiceRoleWrapper(ServiceRoleWrapper serviceRoleWrapper);
}
