package com.honda.galc.dao.product;

import com.honda.galc.entity.product.ModelTypeHold;
import com.honda.galc.entity.product.ModelTypeHoldId;
import com.honda.galc.service.IDaoService;

public interface ModelTypeHoldDao extends IDaoService<ModelTypeHold, ModelTypeHoldId>{

	public int deleteAllByProductionLotRange(String start,String end);
}
