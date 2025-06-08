package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BlockBuildResultHistoryDao;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockBuildResultHistory;
import com.honda.galc.entity.product.BlockBuildResultHistoryId;

public class BlockBuildResultHistoryDaoImpl extends BaseDaoImpl<BlockBuildResultHistory, BlockBuildResultHistoryId>
		implements BlockBuildResultHistoryDao {
	private static final long serialVersionUID = 1L;
	
	@Transactional
	public void saveHistory(BlockBuildResult result) {
		BlockBuildResultHistory history = new BlockBuildResultHistory();
		save(history.Initialize(result));
	}
}
