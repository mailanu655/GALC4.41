package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.property.PartCheckerPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class SeatSensorTemperatureChecker  extends AbstractBaseChecker<PartSerialScanData> {
	
	private PartCheckerPropertyBean propertyBean;

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

	@Override
	public List<CheckResult> executeCheck(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		String productId = inputData.getProductId();
		String partSerialNumber = inputData.getSerialNumber();
		
		if (StringUtils.isBlank(productId)){
			checkResults.add(createCheckResult("Seat Weight temperature Check Failed - No productId scanned."));
			return checkResults;
		}
		propertyBean = PropertyService.getPropertyBean(PartCheckerPropertyBean.class);
		String floorBoardUnitName=propertyBean.getFloorBoardUnit();
		if(StringUtils.isBlank(floorBoardUnitName)){
			checkResults.add(createCheckResult(" Seat Weight temperature Check Failed - No property 'FLOOR_BOARD_UNIT' defined for component 'DEFAULT_PART_CHECKER'. "));
			return checkResults;
		}
		InstalledPart result = getInstalledPart(productId,floorBoardUnitName);
		
		if(result == null || StringUtils.isEmpty(result.getPartSerialNumber())){
			checkResults.add(createCheckResult(" Seat Weight temperature Check Failed - No floor board temperature was recorded. "));
			return checkResults;
		}
		Double floorBoardTemp = Double.valueOf(result.getPartSerialNumber());
		//get temp range from properties
		String floorBoardTempRange = propertyBean.getFloorBoardTemperatureRange();
		
		Double tempRange = Double.valueOf(floorBoardTempRange);
		Double seatTemp = Double.valueOf(partSerialNumber);
		
		if(seatTemp > floorBoardTemp+tempRange || seatTemp < floorBoardTemp-tempRange) {
			checkResults.add(createCheckResult(" Seat Weight temperature Check Failed - Seat temperature should be in +/-"+tempRange+"  degree range of floor :"+floorBoardTemp));
			return checkResults;
		}
		
		return checkResults;
		
	}
	
	
	private InstalledPart getInstalledPart(String productId, String sourcePart)
	{
		return getDao(InstalledPartDao.class).findByUnitOrCommonName(productId,sourcePart);
		
		
	}

	
}
