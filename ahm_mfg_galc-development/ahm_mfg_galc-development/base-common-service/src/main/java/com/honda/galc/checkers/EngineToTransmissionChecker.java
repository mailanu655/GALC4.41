package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.property.PropertyService;

public class EngineToTransmissionChecker extends AbstractBaseChecker<PartSerialScanData> {
	
	private final String DEFAULT_LOT_CONTROL = "Default_LotControl";
	private final String MISSION_TYPE_PART_NAME = "MISSION_TYPE_PART_NAME";
	
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
		String engineNo = inputData.getProductId();
		
		if (StringUtils.isBlank(engineNo)){
			checkResults.add(createCheckResult("Engine To Transmission Check Failed - No Engine scanned."));
			return checkResults;
		}
		if (StringUtils.isBlank(inputData.getSerialNumber())){
			checkResults.add(createCheckResult("Engine To Transmission Check Failed - No Transmission serial number scanned."));
			return checkResults;
		}
		String transmissionPartName = PropertyService.getProperty(DEFAULT_LOT_CONTROL, MISSION_TYPE_PART_NAME);
		if(StringUtils.isBlank(transmissionPartName)){
			checkResults.add(createCheckResult("Engine To Transmission Check Failed - No property 'MISSION_TYPE_PART_NAME' defined for component 'Default_LotControl' "));
			return checkResults;
		}
		String result = hasValidTransmissionType(engineNo,transmissionPartName);
		if(!StringUtils.isBlank(result)){
			checkResults.add(createCheckResult(result));
		}
		
		return checkResults;
		
	}
	
	private String hasValidTransmissionType(String engineNo, String transmissionPartName)
	{
		InstalledPart transmissionTypePart = getDao(InstalledPartDao.class).findByProductIdAndPartialName(engineNo,transmissionPartName);
		
		if(transmissionTypePart ==  null ) return  "Engine To Transmission Check Failed - No Transmission Type part installed for engine :"+engineNo;
		
		return null;
	}

}
