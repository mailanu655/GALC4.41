package com.honda.galc.client.qi.history;

import java.util.Collection;

import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class HistoryModel extends QiProcessModel {
	
	public Collection<? extends ProductHistory> selectProductHistory() {
		return ProductTypeUtil.getProductHistoryDao(getProductType()).findAllByProductId(getProductId());
	}


}
