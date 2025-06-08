package com.honda.galc.dao.jpa.product;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.CrankshaftBuildResultHistoryDao;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResultHistory;
import com.honda.galc.entity.product.CrankshaftBuildResultHistoryId;

public class CrankshaftBuildResultHistoryDaoImpl extends BaseDaoImpl<CrankshaftBuildResultHistory, CrankshaftBuildResultHistoryId>
		implements CrankshaftBuildResultHistoryDao {
	private static final long serialVersionUID = 1L;
	
	@Transactional
	public void saveHistory(CrankshaftBuildResult result) {
		CrankshaftBuildResultHistory history = new CrankshaftBuildResultHistory();
		save(history.Initialize(result));
	}
}
