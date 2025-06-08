package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.check.PartResultData;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Brandon Kroeger
 * @date Jul 17, 2015
 */
public class LotControlRulesChecker extends AbstractBaseChecker<BaseProductCheckerData> {

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
			checkResults.add(createCheckResult("Lot Control Rules Check Failed - The Product does not exist."));
			return checkResults;
		}

		if (currentProcessPoint == null) {
			checkResults.add(createCheckResult("Lot Control Rules Check Failed - The specified process point does not exist."));
			return checkResults;
		}
		
		ProductCheckUtil checkUtil = new ProductCheckUtil(product, currentProcessPoint);	
		List<PartResultData> partResults = checkUtil.installedPartsInspectionCheck();
		if (partResults.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("Lot Control Rules Check Failed");
			
			for (PartResultData partResult : partResults) {
				sb.append("\n" + partResult.part_Name + " - "  + partResult.part_Reason);								
			}
			checkResults.add(createCheckResult(sb.toString()));
		}		
		
		return checkResults;
	}
}
