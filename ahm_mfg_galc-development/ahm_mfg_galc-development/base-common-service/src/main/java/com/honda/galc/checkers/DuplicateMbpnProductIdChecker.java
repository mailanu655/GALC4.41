package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.MbpnData;
import com.honda.galc.util.MbpnWeldUtility;

public class DuplicateMbpnProductIdChecker extends AbstractBaseChecker<MbpnData> {

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
	public List<CheckResult> executeCheck(MbpnData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		if (MbpnWeldUtility.getMbpnProduct(inputData.getProductId()) != null) {
			// Product Id exists in mbpn_product_tbx
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("MBPN Product ("+inputData.getProductId()+") already exists");
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
		}
		return checkResults;
	}
}
