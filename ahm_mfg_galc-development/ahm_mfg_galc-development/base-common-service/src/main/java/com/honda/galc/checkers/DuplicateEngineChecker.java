package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Mike Endsley
 * @date Jul 31, 2015
 */

public class DuplicateEngineChecker extends AbstractBaseChecker<BaseProductCheckerData> {
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Application;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(BaseProductCheckerData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		
		BaseProduct product = null;
		if ((inputData.getProductType() != null) && (inputData.getProductId() != null)) {
			product = ProductTypeUtil.getProductDao(inputData.getProductType()).findBySn(inputData.getProductId());
		}
		
		if (product == null) {
			checkResults.add(createCheckResult("Duplicate Engine Check Failed - The Product does not exist."));
			return checkResults;
		}
				
		ProductCheckUtil productCheckUtil=new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		
		if (productCheckUtil.engineNumberEmptyCheck()) {			
			checkResults.add(createCheckResult("Duplicate Engine Check Failed - No Engine Assigned to Product."));
			return checkResults;
		}

		if (productCheckUtil.duplicateEngineAssignmentCheck()) {
			checkResults.add(createCheckResult("Duplicate engine # for frame " + inputData.getProductId()));
		}
				
		return checkResults;
	}
}
