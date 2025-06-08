package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HeadBuildResultHistoryDao;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadBuildResultHistory;
import com.honda.galc.entity.product.HeadBuildResultHistoryId;

public class HeadBuildResultHistoryDaoImpl extends BaseDaoImpl<HeadBuildResultHistory, HeadBuildResultHistoryId>
		implements HeadBuildResultHistoryDao {
	private static final long serialVersionUID = 1L;
	
	@Transactional
	public void saveHistory(HeadBuildResult result) {
		HeadBuildResultHistory history = new HeadBuildResultHistory();
		save(history.Initialize(result));
	}
}
