package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.service.ServiceFactory;

/**
 * Scanned Engine MTO to Frame MTO Checker
 * @author Max MacKay
 * @date Oct. 16, 2015
 */

public class EngineTypetoFrameChecker extends AbstractBaseChecker<PartSerialScanData> {

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
		String ein = inputData.getSerialNumber();
		String vin = inputData.getProductId();
		
		if (StringUtils.isBlank(ein)){
			checkResults.add(createCheckResult("Engine Type Check Failed - No Engine scanned."));
			return checkResults;
		}
		
		if (StringUtils.isBlank(vin)){
			checkResults.add(createCheckResult("Engine Type Check Failed - No VIN scanned."));
			return checkResults;
		}
		
		if(!checkEngineType(ein,vin)){
			checkResults.add(createCheckResult("Engine Type Check Failed - Engine MTO does not match Frame's MTO."));
			return checkResults;
		}

		
		//invoke the processor to store values
		return checkResults;
	}//End of List
	
	//Going to bring in the scanned values and use the EIN and VIN to look up the MTO in the 131 and 144 tables respectively
	private boolean checkEngineType(String ein,String vin) {
		
		Engine engine = null;
		Frame frame = null;
		FrameSpec frameSpec = null;
		
			engine = getDao(EngineDao.class).findByKey(ein);

			frame = ServiceFactory.getDao(FrameDao.class).findByKey(vin);	
	
		
		if(frame == null || engine == null){
			return false;
		}
		String frameProductSpecCode = frame.getProductSpecCode();
		
		
		if (frameProductSpecCode != null){
			frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(frameProductSpecCode);	
		}
		
		if(frameSpec == null){
			return false; 
		}
		
		String vinEngineMTO= frameSpec.getEngineMto();
		String engineProductSpecCode = engine.getProductSpecCode();
		
		if(vinEngineMTO == null || engineProductSpecCode == null){
			return false;
		}
		
		return vinEngineMTO.equals(engineProductSpecCode);   
		
	}


}
