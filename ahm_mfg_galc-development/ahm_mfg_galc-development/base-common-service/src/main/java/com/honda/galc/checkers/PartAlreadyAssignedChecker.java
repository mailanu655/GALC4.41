package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.InstalledPart;

/**
 * Part Already Assigned Checker
 * @author Saikrishna Ramineni
 * @date Jan. 05, 2024
 */

public class PartAlreadyAssignedChecker extends AbstractBaseChecker<PartSerialScanData> {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public CheckerType getType() {
		return CheckerType.Part;
	}

	@Override
	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		String partNumber = inputData.getSerialNumber();
		String vin = inputData.getProductId();
		
		
		if (StringUtils.isBlank(partNumber)){
			checkResults.add(createCheckResult("Part Type Check Failed - No Part scanned."));
			return checkResults;
		}
		
		if (StringUtils.isBlank(vin)){
			checkResults.add(createCheckResult("Part Type Check Failed - No VIN scanned."));
			return checkResults;
		}
		
		String vinResultString = duplicatePartAssignmentCheck(partNumber, vin);
		
		if(!StringUtils.isBlank(vinResultString)){
			checkResults.add(createCheckResult(vinResultString));
			return checkResults;
		}
		
		//invoke the processor to store values
		return checkResults;
	}
	
	//Checking the 185TBX to see if there is an existing part there, If not then return true
	private String duplicatePartAssignmentCheck(String partNumber, String vin) {

		List<InstalledPart> list = getDao(InstalledPartDao.class).findAllByProductIdAndPartSerialNo(vin, partNumber);
		if (list == null || list.size() == 0) {
			return "Part Type Check Failed - Part has been assigned to different VIN. /n";
		}

		if (list.size() == 1) {
			return null;
		} else {
			StringBuilder currentVinList = new StringBuilder();
			currentVinList.append("Part Type Check Failed - Part has assigned to different VIN. /n");
			for (InstalledPart currentFrame : list) {
				currentVinList.append("/n" + currentFrame.getProductId());
			}

			return currentVinList.toString();
		}

	}// end of Duplicate
}
