package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Steven Ware
 * @date Jul 31, 2015
 */

public class LetChecker extends AbstractBaseChecker<BaseProductCheckerData> {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public CheckerType getType() {
		return CheckerType.Application;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	@Override
	public List<CheckResult> executeCheck(BaseProductCheckerData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();

		BaseProduct product = null;
		if ((inputData.getProductType() != null) && (inputData.getProductId() != null)) {
			product = ProductTypeUtil.getProductDao(inputData.getProductType()).findBySn(inputData.getProductId());
		}
		
		ProcessPoint currentProcessPoint = null;
		if (inputData.getCurrentProcessPoint() != null){
			currentProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(inputData.getCurrentProcessPoint());
		}		

		if (product == null) {
			checkResults.add(createCheckResult("LET Check Failed - The Product does not exist."));
			return checkResults;
		}

		if (currentProcessPoint == null) {
			checkResults.add(createCheckResult("LET Check Failed - The specified process point does not exist."));
			return checkResults;
		}

		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		productCheckUtil.setProcessPoint(currentProcessPoint);

		List<String> letFailList = productCheckUtil.letCheck();
		if(letFailList != null && letFailList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("LET Test Failed");
			for (String letFailReason : letFailList) {
				sb.append("\n" + letFailReason);								
			}
			
			checkResults.add(createCheckResult(sb.toString()));
		}

		return checkResults;
	}

}
