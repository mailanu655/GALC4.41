package com.honda.galc.dao.product;

import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadBuildResultHistory;
import com.honda.galc.entity.product.HeadBuildResultHistoryId;
import com.honda.galc.service.IDaoService;

public interface HeadBuildResultHistoryDao extends IDaoService<HeadBuildResultHistory, HeadBuildResultHistoryId> {
	public void saveHistory(HeadBuildResult result);

}
