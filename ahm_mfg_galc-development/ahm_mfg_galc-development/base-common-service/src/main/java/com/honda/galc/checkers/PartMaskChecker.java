package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public class PartMaskChecker extends AbstractBaseChecker<PartSerialScanData> {

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(PartSerialScanData partSerialScanData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		
		//bak - 20150702 - get the product to pass to part mask verification
		BaseProduct product = null;
		if ((partSerialScanData.getProductType() != null) && (partSerialScanData.getProductId() != null)) {
			product = ProductTypeUtil.getProductDao(partSerialScanData.getProductType()).findBySn(partSerialScanData.getProductId());
		}		
				
		if (!CommonPartUtility.verification(partSerialScanData.getSerialNumber(), partSerialScanData.getMask(), PropertyService.getPartMaskWildcardFormat(), product)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("Part mask mismatch");
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
		}
		return checkResults;
	}
}
