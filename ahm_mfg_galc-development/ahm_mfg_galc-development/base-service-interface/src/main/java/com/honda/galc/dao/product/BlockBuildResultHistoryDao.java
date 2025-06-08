package com.honda.galc.dao.product;

import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockBuildResultHistory;
import com.honda.galc.entity.product.BlockBuildResultHistoryId;
import com.honda.galc.service.IDaoService;

public interface BlockBuildResultHistoryDao extends IDaoService<BlockBuildResultHistory, BlockBuildResultHistoryId> {
	public void saveHistory(BlockBuildResult result);
}
