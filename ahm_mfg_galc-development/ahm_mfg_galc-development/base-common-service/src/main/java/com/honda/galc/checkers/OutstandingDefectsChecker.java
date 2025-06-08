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
 * @author Brandon Kroeger
 * @date Jul 20, 2015
 */
public class OutstandingDefectsChecker extends AbstractBaseChecker<BaseProductCheckerData> {

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
		
		ProcessPoint currentProcessPoint = null;
		if (inputData.getCurrentProcessPoint() != null){
			currentProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(inputData.getCurrentProcessPoint());
		}
				
		if (product == null) {
			checkResults.add(createCheckResult(getHeaderMessage() + " - The Product does not exist."));
			return checkResults;
		}

		if (currentProcessPoint == null) {
			checkResults.add(createCheckResult(getHeaderMessage() + " - The specified process point does not exist."));
			return checkResults;
		}
		
		List<String> defectResults = findDefects(product, currentProcessPoint);
		
		if (defectResults.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(getHeaderMessage());
			
			for (String defect : defectResults) {
				sb.append("\n" + defect.toString());								
			}
			checkResults.add(createCheckResult(sb.toString()));
		}		
		
		return checkResults;
	}
	
	protected String getHeaderMessage() {
		return "Outstanding Defects Check Failed";
	}
	
	protected List<String> findDefects(BaseProduct product, ProcessPoint processPoint) {
		ProductCheckUtil checkUtil = new ProductCheckUtil(product, processPoint);	
		List<String> defectResults = checkUtil.outstandingDefectsCheck();
		return defectResults;
	}
}
