package com.honda.galc.dao.product;

import java.sql.Timestamp;

import com.honda.galc.entity.product.LetProgramResultValue;
import com.honda.galc.entity.product.LetProgramResultValueId;
import com.honda.galc.service.IDaoService;

public interface LetProgramResultValueDao extends IDaoService<LetProgramResultValue, LetProgramResultValueId>{
	
    public long count(String productId, Timestamp endTimestamp);


}
