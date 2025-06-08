package com.honda.galc.notification.service;

/**
 * @author Alex Johnson
 * @date Jul 21, 2015
 * 
 */
public interface IProductPassedNotification extends INotificationService{
	
	/**
	 * Event notifying a product passing a process point
	 * @param processPoint
	 * @param productId
	 */
	public void execute(String processPointId, String productId);
}
