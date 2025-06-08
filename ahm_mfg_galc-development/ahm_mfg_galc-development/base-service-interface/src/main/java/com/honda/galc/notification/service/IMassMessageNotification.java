package com.honda.galc.notification.service;

public interface IMassMessageNotification extends INotificationService {

	public void execute(String plantName, String divisionId, String lineId);
	
}
