package com.honda.galc.dao.product;

import com.honda.galc.entity.product.CounterByProductSpec;
import com.honda.galc.entity.product.CounterByProductSpecId;
import com.honda.galc.service.IDaoService;

public interface CounterByProductSpecDao extends IDaoService<CounterByProductSpec, CounterByProductSpecId>{
	int incrementCounter(CounterByProductSpecId id);
}
