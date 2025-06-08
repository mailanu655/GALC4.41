package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entitypersister.AbstractEntity;
import com.honda.galc.entitypersister.EntityList;
import com.honda.galc.service.IDaoService;

public interface EntityProcessorDao<E, K> extends IDaoService<E, K>{
	
	void ProcessEntityList(List<EntityList<AbstractEntity>> masterList);

}
