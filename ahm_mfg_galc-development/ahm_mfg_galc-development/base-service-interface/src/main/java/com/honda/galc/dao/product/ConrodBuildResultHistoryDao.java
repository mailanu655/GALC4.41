package com.honda.galc.dao.product;


import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ConrodBuildResultHistory;
import com.honda.galc.entity.product.ConrodBuildResultHistoryId;
import com.honda.galc.service.IDaoService;

public interface ConrodBuildResultHistoryDao extends IDaoService<ConrodBuildResultHistory, ConrodBuildResultHistoryId> {
	public void saveHistory(ConrodBuildResult result);

}
