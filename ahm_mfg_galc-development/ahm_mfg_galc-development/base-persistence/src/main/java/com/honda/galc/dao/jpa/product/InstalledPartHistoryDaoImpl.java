package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.InstalledPartHistoryDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartHistory;
import com.honda.galc.entity.product.InstalledPartHistoryId;
import com.honda.galc.service.Parameters;

public class InstalledPartHistoryDaoImpl extends
		BaseDaoImpl<InstalledPartHistory, InstalledPartHistoryId> implements
		InstalledPartHistoryDao {

	private static final long serialVersionUID = 1L;

	private static final String UPDATE_PRODUCT_ID = "update InstalledPartHistory e set e.id.productId = :productId where e.id.productId = :oldProductId";		

	@Transactional
	public void savePartHistory(InstalledPart installedPart) {
		InstalledPartHistory history = new InstalledPartHistory();
		save(history.Initialize(installedPart));

	}

	@Transactional
	public int moveAllData(String newProductId, String currentProductId) {
		Parameters params = Parameters.with("productId", newProductId);
		params.put("oldProductId", currentProductId);
		return executeUpdate(UPDATE_PRODUCT_ID, params);
	}
	
	public List<InstalledPartHistory> findAllByProductIdAndOperationName(String productId, String operationName) {
		Parameters params = Parameters.with("id.productId", productId);
		params.put("id.partName", operationName);
		return findAll(params);
	}

}
