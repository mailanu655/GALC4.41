package com.honda.galc.service.productattribute;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductAttributeDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ProductAttribute;
import com.honda.galc.entity.product.ProductAttributeId;
import com.honda.galc.service.ProductAttributeService;
import com.honda.galc.service.ServiceFactory;

public class ProductAttributeServiceImpl implements ProductAttributeService {

	protected DataContainer retList = new DefaultDataContainer();
	
	private static final int DATA_FOUND = 0;
	private static final int DATA_NOT_FOUND = 1;
	private static final int ERROR = 2;
	
	public DataContainer getProductAttributes(String productId,String attribute){
		try {
			retList.clear();
			ProductAttributeId productAttributeId = new ProductAttributeId();
			productAttributeId.setProductId(productId);
			productAttributeId.setAttribute(attribute);
			
			ProductAttribute prodAttribute = ServiceFactory.getDao(ProductAttributeDao.class).findByKey(productAttributeId);
			if(prodAttribute == null){
				retList.put(TagNames.ERROR_CODE,DATA_NOT_FOUND);
				retList.put(TagNames.ERROR_MESSAGE, "Product Attribute Not Found For - "+attribute);
				retList.put(TagNames.PRODUCT_ID,productId);
				retList.put(TagNames.DATA, "No Data Found");
			}else{
				retList.put(TagNames.ERROR_CODE,DATA_FOUND);
				retList.put(TagNames.ERROR_MESSAGE, "");
				retList.put(TagNames.PRODUCT_ID, prodAttribute.getId().getProductId());
				retList.put(TagNames.DATA, prodAttribute.getAttributeValue());
			}
			Logger.getLogger().info("ProductAttribute service completed for product:"+productId);
		}catch(Exception e){
			Logger.getLogger().error("ProductAttribute service failed for product:"+productId,e.getMessage());
			retList.put(TagNames.ERROR_CODE,ERROR);
			retList.put(TagNames.ERROR_MESSAGE, e.getMessage());
		}
		return retList;
	}
			
	
}
