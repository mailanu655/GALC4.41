package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.dataformat.MbpnData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.MbpnWeldUtility;

public class ProductMadeFromChecker extends AbstractBaseChecker<MbpnData> {

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
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class)
				.findByKey(inputData.getProcessPointId());
		CheckResult checkResult = MbpnWeldUtility.checkMadeFrom(inputData.getPddaPlatform(), inputData.getProductId(), 
				inputData.getProductSpecCode(), processPoint, getReactionType());
		if(checkResult!=null) {
			checkResults.add(checkResult);
		}
		return checkResults;
	}
}
