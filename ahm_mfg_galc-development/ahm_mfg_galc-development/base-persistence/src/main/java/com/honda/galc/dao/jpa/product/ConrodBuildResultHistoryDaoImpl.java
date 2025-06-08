package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ConrodBuildResultHistoryDao;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ConrodBuildResultHistory;
import com.honda.galc.entity.product.ConrodBuildResultHistoryId;

public class ConrodBuildResultHistoryDaoImpl extends BaseDaoImpl<ConrodBuildResultHistory, ConrodBuildResultHistoryId>
		implements ConrodBuildResultHistoryDao {
	private static final long serialVersionUID = 1L;
	
	@Transactional
	public void saveHistory(ConrodBuildResult result) {
		ConrodBuildResultHistory history = new ConrodBuildResultHistory();
		save(history.Initialize(result));
	}
}
