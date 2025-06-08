package com.honda.galc.notification.service;

import java.util.List;

import com.honda.galc.entity.product.HoldResult;

public interface IProductHoldResultNotification extends INotificationService{
	
	/**
	 * Event notifying a product passing a process point
	 * @param processPoint
	 * @param productId
	 */
	public void holdResultChanged(List<HoldResult> holdResults);

}
