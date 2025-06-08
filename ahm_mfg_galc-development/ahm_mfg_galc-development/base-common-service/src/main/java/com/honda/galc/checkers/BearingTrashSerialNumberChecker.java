package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.InstalledPart;

import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class BearingTrashSerialNumberChecker extends AbstractBaseChecker<PartSerialScanData> {

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}

	public int getSequence() {
		return 0;
	}

	@Override
	public List<CheckResult> executeCheck(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		String platformId = inputData.getPartName().split(Delimiter.UNDERSCORE)[1];
		for (String bearingSelectPropertyValue : inputData.getBearingPickOperation().keySet()) {
			bearingSelectPropertyValue = ServiceFactory.getDao(MCOperationRevisionDao.class).getSelectOperationName(inputData.getStrucRev(), bearingSelectPropertyValue);
			if(bearingSelectPropertyValue != null && bearingSelectPropertyValue.contains(platformId)) {
				List<String> partNames = new ArrayList<String>();
				partNames.add(bearingSelectPropertyValue);
				List<InstalledPart> installParts = ServiceFactory.getService(InstalledPartDao.class).findAllByProductIdAndPartNames(inputData.getProductId(), partNames);
				if(installParts != null && installParts.size() > 0 ) {
					for (InstalledPart installedPart : installParts) {
						if(installedPart.isStatusOk()) {
							checkResults.add(createCheckResult("Can't Delete Serial number as Select Or Pick operation is completed for unit of operation"));
						} 
					}
				}
			}
		}
		return checkResults;
	}

}
