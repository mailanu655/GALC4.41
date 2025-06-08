package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public class DuplicatePartChecker extends AbstractBaseChecker<PartSerialScanData> {

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
		Logger.getLogger().info("Executing the Duplicate Part Check  ");
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		if(partSerialScanData == null || StringUtils.isBlank(partSerialScanData.getSerialNumber())){
			Logger.getLogger().info("Duplicate Part Check Failed - Part Serial number is empty or null : "+partSerialScanData);
			checkResults.add(createCheckResult("Duplicate Part Check Failed - Part Serial number is empty or null "));
			return checkResults;
		}
		Boolean isDuplicatePart = false;
		try {
			Logger.getLogger().info("Duplicate Part Check - Input data :"+partSerialScanData);
			isDuplicatePart = ServiceFactory.getDao(InstalledPartDao.class).isPartSerialNumberExists(partSerialScanData.getSerialNumber());
			
		} catch (Exception e) {
			Logger.getLogger().error(e,"Duplicate Part Check Failed - while retriving the part serial number information for serial number "+partSerialScanData.getSerialNumber());
			checkResults.add(createCheckResult("Duplicate Part Check Failed - while retriving the part serial number information "));
		}
		if (isDuplicatePart) {
			Logger.getLogger().info("Duplicate Part Check failed ");
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage("Duplicate Part");
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
		}
		return checkResults;
	}
}
