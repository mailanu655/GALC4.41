package com.honda.galc.notification.service;

public interface IStampedCountChangedNotification extends INotificationService {

	/**
	 * Event notifying that the stamped count changed for the lot
	 */
	public void stampedCountChanged(String productionLot, int stampedCount);
}
