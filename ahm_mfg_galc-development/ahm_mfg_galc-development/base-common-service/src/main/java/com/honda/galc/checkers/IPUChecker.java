package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.dataformat.MbpnData;
import com.honda.galc.util.IpuUtility;

public class IPUChecker extends AbstractBaseChecker<MbpnData> {

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
		String msg = IpuUtility.checkPrefix(inputData.getProductSpecCode(),inputData.getProductId());
		if (!StringUtils.isEmpty(msg)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage(msg);
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
		}
				
		return checkResults;
	}

}
