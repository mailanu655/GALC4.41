package com.honda.galc.notification.service;



public interface IKnuckleLoadNotification extends INotificationService {
	
	public void knuckleLoaded(String productionLot, int stampedCount);

}
