package com.honda.galc.service;

import com.honda.galc.net.NotificationRequest;

public interface NotificationExecutionService extends IService{
	
	public void execute(NotificationRequest request);

}
