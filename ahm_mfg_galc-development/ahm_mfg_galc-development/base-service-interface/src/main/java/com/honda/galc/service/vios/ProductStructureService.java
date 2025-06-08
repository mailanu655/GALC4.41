package com.honda.galc.service.vios;

import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.IService;

public interface ProductStructureService extends IService {
	
	public BaseMCProductStructure findOrCreateProductStructure(BaseProduct productObj, ProcessPoint processPoint, String mode) throws Exception;

}
