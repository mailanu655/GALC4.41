package com.honda.galc.service;

import com.honda.galc.data.DataContainer;

public interface ProductAttributeService extends IService{
	public DataContainer getProductAttributes(String productId,String attribute);
}
