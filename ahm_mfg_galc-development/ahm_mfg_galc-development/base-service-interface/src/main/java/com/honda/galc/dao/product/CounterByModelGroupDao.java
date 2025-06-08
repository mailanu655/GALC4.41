package com.honda.galc.dao.product;

import com.honda.galc.entity.product.CounterByModelGroup;
import com.honda.galc.entity.product.CounterByModelGroupId;
import com.honda.galc.service.IDaoService;

public interface CounterByModelGroupDao extends IDaoService<CounterByModelGroup, CounterByModelGroupId>{
	int incrementCounter(CounterByModelGroupId id);
}
