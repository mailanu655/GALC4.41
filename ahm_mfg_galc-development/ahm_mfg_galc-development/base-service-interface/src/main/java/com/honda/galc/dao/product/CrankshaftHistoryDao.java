package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.CrankshaftHistory;
import com.honda.galc.entity.product.CrankshaftHistoryId;
import com.honda.galc.service.IDaoService;

public interface CrankshaftHistoryDao extends
		ProductHistoryDao<CrankshaftHistory, CrankshaftHistoryId>,
		IDaoService<CrankshaftHistory, CrankshaftHistoryId> {

}
