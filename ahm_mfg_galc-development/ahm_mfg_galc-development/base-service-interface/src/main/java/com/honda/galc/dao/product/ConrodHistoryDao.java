package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ConrodHistory;
import com.honda.galc.entity.product.ConrodHistoryId;
import com.honda.galc.service.IDaoService;

public interface ConrodHistoryDao extends
		ProductHistoryDao<ConrodHistory, ConrodHistoryId>,
		IDaoService<ConrodHistory, ConrodHistoryId> {

}
