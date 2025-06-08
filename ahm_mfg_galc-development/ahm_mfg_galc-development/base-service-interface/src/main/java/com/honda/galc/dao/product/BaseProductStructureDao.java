package com.honda.galc.dao.product;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BaseMCProductStructure;
import com.honda.galc.service.IDaoService;

public interface BaseProductStructureDao<E extends BaseMCProductStructure, K> extends IDaoService<E, K>  {

	public E findByKey(String productId, String modeId, String productSpecCode);
	
	public int getUnmappedProductCount(ProductType productType);
}

