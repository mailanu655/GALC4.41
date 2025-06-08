package com.honda.galc.service;

import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;


public interface StragglerService extends IService{
	
	public DataContainer identifyStragglers(ProcessPoint processPoint, BaseProduct product);
	public DataContainer identifyStragglers(String processPointID, String productId, String ProductType);
	
}
