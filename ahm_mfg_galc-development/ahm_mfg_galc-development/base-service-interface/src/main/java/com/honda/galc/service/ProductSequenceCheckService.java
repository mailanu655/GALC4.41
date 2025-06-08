package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;

public interface ProductSequenceCheckService extends IService {
	DataContainer getProductSequenceDetail(DefaultDataContainer data);
	DataContainer getProductSequenceDetail(String productType, String productId, String[] lineIds, Boolean isInProcessProduct);
	
}
