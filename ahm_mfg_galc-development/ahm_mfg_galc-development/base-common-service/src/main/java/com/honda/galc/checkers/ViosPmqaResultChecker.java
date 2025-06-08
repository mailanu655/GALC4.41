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

public class ViosPmqaResultChecker extends AbstractBaseChecker<BaseProductCheckerData> {

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
		if (inputData.getCurrentProcessPoint() != null) {
			currentProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(inputData.getCurrentProcessPoint());
		}

		if (product == null) {
			checkResults.add(createCheckResult("VIOS PMQA status Checker  - The Product does not exist."));
			return checkResults;
		}

		if (currentProcessPoint == null) {
			checkResults.add(createCheckResult("VIOS PMQA status Checker - The specified process point does not exist."));
			return checkResults;
		}

		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		productCheckUtil.setProcessPoint(currentProcessPoint);

		List<String> resultList = productCheckUtil.pmqaDefectsByCategoryCheck();
		
		if (!resultList.isEmpty()) {
			String result = joinList(resultList);
			checkResults.add(createCheckResult("VIOS PMQA status checker Failed.\nDefect Items: "+result));
		}

		return checkResults;
	}
	
	private String joinList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}