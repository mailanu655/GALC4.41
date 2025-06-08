package com.honda.galc.service.common;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.product.EntityProcessorDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.entitypersister.SaveEntity;
import com.honda.galc.entitypersister.UpdateEntity;
import com.honda.galc.service.IService;
import com.honda.galc.service.ProductMarriage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class ProductMarriageImpl implements ProductMarriage, IService {
	public static final String CLASS_NAME = "ProductMarriage";

	public ProductMarriageImpl() {}

	public EntityList<AbstractEntity> addProductEntity(BaseProduct product) {
		EntityList<AbstractEntity> entityList = new EntityList<AbstractEntity>(CLASS_NAME, product.getProductId());
		entityList.addEntity(new UpdateEntity(product, product.toString(), ProductTypeUtil.getProductDao(product.getProductType())));
		return entityList;
	}

	public EntityList<AbstractEntity> addEntity(List<? extends ProductBuildResult> buildResults, String productType) {
		EntityList<AbstractEntity> entityList = new EntityList<AbstractEntity>(CLASS_NAME, productType);

		for(ProductBuildResult entity : buildResults) {
			entityList.addEntity(new SaveEntity(entity, entity.toString(), ProductTypeUtil.getProductBuildResultDao(productType)));
		}
		return entityList;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public void marryProduct(List<EntityList<AbstractEntity>> entities) {	
		getEntityProcessor().ProcessEntityList(entities);
	}

	private EntityProcessorDao<?,?>  getEntityProcessor() {
		return  ServiceFactory.getDao(EntityProcessorDao.class);
	}

	public BaseProduct getProduct(String productId, String productType) {
		return ProductTypeUtil.getProductDao(productType).findBySn(productId);
	} 
}