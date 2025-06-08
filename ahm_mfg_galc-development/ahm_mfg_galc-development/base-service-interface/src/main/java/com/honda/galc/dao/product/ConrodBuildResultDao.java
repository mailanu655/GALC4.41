package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.ConrodBuildResultId;

public interface ConrodBuildResultDao extends
		ProductBuildResultDao<ConrodBuildResult, ConrodBuildResultId> {

	List<ConrodBuildResult> findAllByProductId(String productId);
	
	public List<ConrodBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber);

	List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList);
}
