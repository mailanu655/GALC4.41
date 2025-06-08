package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResultId;

public interface CrankshaftBuildResultDao extends
		ProductBuildResultDao<CrankshaftBuildResult, CrankshaftBuildResultId> {

	List<CrankshaftBuildResult> findAllByProductId(String productId);

	public List<CrankshaftBuildResult> findAllByPartNameAndSerialNumber(List<String> partNames, String partSerialNumber);

	List<Long> findDefectRefIds(List<String> productIdList, List<String> partNameList);
}
