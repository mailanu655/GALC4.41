package com.honda.galc.client.datacollection.observer.knuckles;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.ProductSequenceManager;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;

public class KnucklesSequenceManager extends ProductSequenceManager{

	public KnucklesSequenceManager(ClientContext context) {
		super(context);
	}

	@Override
	public void getExpectedProductIdFromServer(ProcessProduct state) {
		super.getExpectedProductIdFromServer(state);
		
		SubProduct expectedProduct = state.getExpectedProductId() == null ? null : 
			ServiceFactory.getDao(SubProductDao.class).findByKey(state.getExpectedProductId());
		
		if(expectedProduct == null)
			Logger.getLogger().error("Error ", "expected product:", state.getExpectedProductId(), " does not exist.");
		else
			state.setExpectedSubId(expectedProduct.getSubId());
	}

}
