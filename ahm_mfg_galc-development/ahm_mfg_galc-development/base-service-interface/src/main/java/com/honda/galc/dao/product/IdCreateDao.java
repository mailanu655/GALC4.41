package com.honda.galc.dao.product;

import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.IdCreateId;
import com.honda.galc.service.IDaoService;

public interface IdCreateDao extends IDaoService<IdCreate, IdCreateId> {

	IdCreate incrementId(String tableName, String columnName);
	int incrementAndGetStatus(String tableName, String columnName);
	IdCreate getIdCreate(String tableName, String columnName);
	IdCreate incrementIdWithNewTransaction(String tableName, String columnName);
	
}
