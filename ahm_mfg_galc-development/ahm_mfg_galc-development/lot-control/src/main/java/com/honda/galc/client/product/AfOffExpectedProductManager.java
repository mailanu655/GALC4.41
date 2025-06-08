package com.honda.galc.client.product;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.service.ServiceFactory;

public class AfOffExpectedProductManager extends ExpectedProductManager {

	public AfOffExpectedProductManager(ClientContext context) {
		super(context);
	}

	public List<String> getIncomingProducts(DataCollectionState state) {
		String productId = (state instanceof ProcessProduct) ? null : state.getProductId(); 
		productId= StringUtils.isEmpty(productId)? getNextExpectedProductId(state.getExpectedProductId()) : getNextExpectedProductId(productId);
		
		if(!StringUtils.isEmpty(productId)) {
			InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
			return inProcessProductDao.findIncomingExpectedProductIds(productId);
		} else {
			return new ArrayList<String>();
		}
	}
}