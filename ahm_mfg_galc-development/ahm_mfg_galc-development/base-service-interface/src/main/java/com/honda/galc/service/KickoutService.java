package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

public interface KickoutService  extends IService{
	DataContainer validateProductsForKickout(DefaultDataContainer data);
	DataContainer validateProductsForRelease(DefaultDataContainer data);
	DataContainer kickoutProducts(DefaultDataContainer data);
	DataContainer kickoutByProductIds(DefaultDataContainer data);
	DataContainer releaseKickouts(DefaultDataContainer data);
	DataContainer releaseByProductIds(DefaultDataContainer data);
	boolean isMultipleKickout(String applicationId);
}
