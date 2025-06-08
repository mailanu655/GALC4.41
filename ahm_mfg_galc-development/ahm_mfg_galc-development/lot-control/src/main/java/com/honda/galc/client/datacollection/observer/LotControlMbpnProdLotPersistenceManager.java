package com.honda.galc.client.datacollection.observer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.utils.ProductTypeUtil;

public class LotControlMbpnProdLotPersistenceManager extends LotControlMbpnPersistenceManager {

	public LotControlMbpnProdLotPersistenceManager(ClientContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void getExpectedProductId(ProcessProduct state) {
		getExpectedProductManger().getExpectedProductId(state);
	}
}
