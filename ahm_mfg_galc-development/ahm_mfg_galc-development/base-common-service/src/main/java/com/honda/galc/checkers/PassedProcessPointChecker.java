package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * @author Clint Maxwell
 * @date Aug 13, 2015
 */


public class PassedProcessPointChecker extends AbstractBaseChecker<BaseProductCheckerData> {
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
		
		//add
		if (product == null) {
			checkResults.add(createCheckResult("PassedProcessPointChecker Failed - The product does not exist."));
			return checkResults;
		}
				
		ProcessPoint currentProcessPoint = null;
		if (inputData.getCurrentProcessPoint() != null){
			currentProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(inputData.getCurrentProcessPoint());
		}

		if (currentProcessPoint == null) {
		//change failed message
			checkResults.add(createCheckResult("PassedProcessPointChecker Failed - The specified process point does not exist."));
			return checkResults;
		}
		ProductCheckUtil checkUtil = new ProductCheckUtil(product,currentProcessPoint);	
		 if (checkUtil.atCheck()) {
				CheckResult checkResult = new CheckResult();
				checkResult.setCheckMessage("Product (" + inputData.getProductId() + ") has passed all required process points");
				checkResult.setReactionType(getReactionType());
				checkResults.add(checkResult);
			}

				
		return checkResults;
	}
}
