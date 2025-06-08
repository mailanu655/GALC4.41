package com.honda.galc.notification.service;

/**
 * @author hat0926
 * @date Mar 21, 2017
 * 
 */
public interface IProductReceivedNotification extends INotificationService{
	
	/**
	 * Event notifying a product passing a process point
	 * @param mcNumer
	 * @param productId
	 * @param message
	 * @param detail
	 */
	public void execute(String productId,String mcNumber, String message, String detail);
	public void execute(String productId,String message, String exception);
}
