package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.utils.ProductTypeUtil;

public class LotControlPartSerialNumberPersistenceManager extends LotControlPersistenceManager {

	public LotControlPartSerialNumberPersistenceManager(ClientContext context) {
		super(context);
	}

	@Override
	protected void saveExpectedProduct(ProcessProduct state) {
		if(context.isOnLine()){
			getExpectedProductManger().saveNextExpectedProduct(state);
		} 
		else if(!context.isOnLine())
			state.setExpectedProductId(null);

		saveLastExpected(state);
	}

	@SuppressWarnings("unchecked")
	public List<? extends BaseProduct> findProductOnServer(String productId) {
		try {
			List<BaseProduct> test = (List<BaseProduct>) ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findAllBySN(productId);
			return test;
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Failed searching for proudcts by serial number.");
			return null;
		}
	}

}
