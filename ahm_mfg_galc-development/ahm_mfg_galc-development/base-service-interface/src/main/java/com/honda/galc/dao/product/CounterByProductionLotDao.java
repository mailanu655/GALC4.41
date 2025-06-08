package com.honda.galc.dao.product;

import com.honda.galc.entity.product.CounterByProductionLot;
import com.honda.galc.entity.product.CounterByProductionLotId;
import com.honda.galc.service.IDaoService;

public interface CounterByProductionLotDao extends IDaoService<CounterByProductionLot, CounterByProductionLotId>{
	int incrementCounter(CounterByProductionLotId id);
}
