package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Olvin Vaz
 * @date Jul 21, 2015
 */
public class EngineDockingChecker extends AbstractBaseChecker<BaseProductCheckerData> {

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
			checkResults.add(createCheckResult("Engine Docking Check Failed - The Product does not exist."));			
			return checkResults;
		}

		ProductCheckUtil checkUtil = new ProductCheckUtil();
		checkUtil.setProduct(product);

		if (!checkUtil.engineDockingCheck()) {
			checkResults.add(createCheckResult("The Engine type does not match."));			
		}

		return checkResults;
	}
}
