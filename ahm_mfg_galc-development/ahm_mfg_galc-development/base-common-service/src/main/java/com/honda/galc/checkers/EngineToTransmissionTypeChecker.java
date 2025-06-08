package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;

public class EngineToTransmissionTypeChecker extends AbstractBaseChecker<PartSerialScanData> {
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
		String transmissionType = inputData.getSerialNumber();
		
		if (StringUtils.isBlank(engineNo)){
			checkResults.add(createCheckResult("Engine To Transmission Type Check Failed - No Engine scanned."));
			return checkResults;
		}
		if (StringUtils.isBlank(transmissionType)){
			checkResults.add(createCheckResult("Engine To Transmission Type Check Failed - No Transmission Type serial number scanned."));
			return checkResults;
		}
		String result = checkTransmissionType(engineNo, transmissionType);
		if(!StringUtils.isBlank(result)){
			checkResults.add(createCheckResult(result));
		}
		
		return checkResults;
		
	}
	
	private String checkTransmissionType(String engineNo, String transmissionType){
		Engine engine = getDao(EngineDao.class).findByKey(engineNo);
		
		if(engine == null)	return "Engine To Transmission Type Check Failed - No Engine found in database.";
		EngineSpec engineSpec = getDao(EngineSpecDao.class).findByKey(engine.getProductSpecCode());
		
		if(engineSpec == null) return "Engine To Transmission Type Check Failed - No Engine specification found in the database";
		
		if(!transmissionType.equals(engineSpec.getActualMissionType())) 
			return "Engine To Transmission Type Check Failed - Scanned transmission type :"+transmissionType+" does not match actual transmission type :"+engineSpec.getActualMissionType();
		
		return null;
	}

}
