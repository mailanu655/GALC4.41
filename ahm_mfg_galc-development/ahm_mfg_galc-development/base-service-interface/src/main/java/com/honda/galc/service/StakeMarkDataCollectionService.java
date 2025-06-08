package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

public interface StakeMarkDataCollectionService extends IService{

	public DataContainer execute(DefaultDataContainer data);
}
