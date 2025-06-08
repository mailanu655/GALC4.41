package com.honda.galc.service.defect;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.IService;


public interface UnscrapService extends IService {
	DataContainer unscrapProduct(DefaultDataContainer data);
	String getClassName();
	DataContainer validateProductsForUnscrap(DefaultDataContainer data);
	boolean isProductScrapped(BaseProduct product);
}
