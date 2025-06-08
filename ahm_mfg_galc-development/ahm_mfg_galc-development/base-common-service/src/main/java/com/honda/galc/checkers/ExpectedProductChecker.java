package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class ExpectedProductChecker extends AbstractBaseChecker<BaseProductCheckerData> {

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
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		productCheckUtil.setProcessPoint(ServiceFactory.getDao(ProcessPointDao.class).findByKey(inputData.getCurrentProcessPoint().trim()));
		List<String> results = productCheckUtil.nextExpectedProductCheck();
		if (results.size()> 0) {
			CheckResult result = createCheckResult(results.get(0));
			result.setResult(false);
			result.setReactionType(ReactionType.UNEXPECTED_PRODUCT);
			checkResults.add(result);
		}

		return checkResults;
	}

}
