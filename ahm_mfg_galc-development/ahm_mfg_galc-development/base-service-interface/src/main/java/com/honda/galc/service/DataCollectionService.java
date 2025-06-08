package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

public interface DataCollectionService extends IoService{
	
	/**
	 * Refresh Lot Control Rule cache on server side. Mostly for HeadLess data collection
	 * @param processPointId
	 */
	void refreshLotControlRuleCache(String processPointId);
	
	/**
	 * Retrieve server name
	 * @return
	 */
	String getShortServerName();
	
	
	public DataContainer execute(DefaultDataContainer data);
}
