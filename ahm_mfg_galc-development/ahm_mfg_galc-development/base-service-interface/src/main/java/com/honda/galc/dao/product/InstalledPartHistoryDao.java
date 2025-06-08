package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartHistory;
import com.honda.galc.entity.product.InstalledPartHistoryId;
import com.honda.galc.service.IDaoService;

public interface InstalledPartHistoryDao extends
		IDaoService<InstalledPartHistory, InstalledPartHistoryId> {

	public void savePartHistory(InstalledPart part);
	
	public int moveAllData(String newProductId,String currentProductId);
	
	public List<InstalledPartHistory> findAllByProductIdAndOperationName(String newProductId,String operationName);

}
