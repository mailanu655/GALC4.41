package com.honda.galc.service;

import java.util.List;

import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;

public interface ProductMarriage extends IService {

	public BaseProduct getProduct(String productId, String name);
		
	public void marryProduct(List<EntityList<AbstractEntity>> entities);
	
	public EntityList<AbstractEntity> addProductEntity(BaseProduct product);
	
	public EntityList<AbstractEntity> addEntity(List<? extends ProductBuildResult> buildResults, String productType);			
}
