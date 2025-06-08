package com.honda.galc.notification.service;

import com.honda.galc.data.DataContainer;

public interface IProductScrapNotification extends INotificationService {
	
	/**Event notifying Product is scrapped in GALC
	 * 
	 */
	public void productScrapped(String productId,String associateId);
	/**
	 * Event notifying Product unscrap in GALC
	 */
	public void productUnscrapped(String productId,String associateId,String unscrapReason);
	
	public DataContainer productUnscrapped(DataContainer requestDc);
	
	

}