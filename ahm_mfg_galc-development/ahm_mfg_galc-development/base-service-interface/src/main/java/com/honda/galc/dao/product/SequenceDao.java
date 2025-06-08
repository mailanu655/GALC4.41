package com.honda.galc.dao.product;

import com.honda.galc.entity.product.Sequence;
import com.honda.galc.service.IDaoService;

public interface SequenceDao extends IDaoService<Sequence,String> {

	public Sequence getNextSequence(String sequenceName);
	
	public Sequence getNextSequence(String sequenceName, Boolean update);
}
