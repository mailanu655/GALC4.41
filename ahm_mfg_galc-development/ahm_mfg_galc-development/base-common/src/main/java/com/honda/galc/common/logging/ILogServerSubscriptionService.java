package com.honda.galc.common.logging;

import com.honda.galc.notification.service.INotificationService;

public interface ILogServerSubscriptionService extends INotificationService {

	public void subscribe(String hostname, int port);
	
	public boolean isAlreadySubscribed(String hostname, int port);
	
	public void unSubscribe(String hostname, int port);
	
	public void unSubscribeAll();
}