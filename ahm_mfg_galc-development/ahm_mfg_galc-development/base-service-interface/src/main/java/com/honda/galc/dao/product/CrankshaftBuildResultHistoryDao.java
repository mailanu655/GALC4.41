package com.honda.galc.dao.product;


import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResultHistory;
import com.honda.galc.entity.product.CrankshaftBuildResultHistoryId;
import com.honda.galc.service.IDaoService;

public interface CrankshaftBuildResultHistoryDao extends IDaoService<CrankshaftBuildResultHistory, CrankshaftBuildResultHistoryId> {
	public void saveHistory(CrankshaftBuildResult result);

}
