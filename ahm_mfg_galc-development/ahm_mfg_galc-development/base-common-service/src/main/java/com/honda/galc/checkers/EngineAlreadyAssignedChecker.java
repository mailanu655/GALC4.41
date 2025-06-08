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
 * Engine Already Assigned Checker
 * @author Max MacKay
 * @date Oct. 16, 2015
 */
public class EngineAlreadyAssignedChecker extends AbstractBaseChecker<PartSerialScanData> {

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
		
		String vinResultString = duplicateEngineAssignmentCheck(ein, vin);
		
		if(!StringUtils.isBlank(vinResultString)){
			checkResults.add(createCheckResult(vinResultString));
			return checkResults;
		}
		
		//invoke the processor to store values
		return checkResults;
	}
	
	//Checking the 143TBX to see if there is an existing EIN there, If not then return true
	private String duplicateEngineAssignmentCheck(String ein, String vin) {
		
		List<Frame> list = getDao(FrameDao.class).findByEin(ein);
		if (list == null || list.size() == 0) {
			return null;
			
		}
		else if (list.size() == 1){
			Frame frame = list.get(0);
			
			if(frame.getProductId().equals(vin)){
				return null;
			}
			else{
				return "Engine Type Check Failed - Engine has been assigned to " + frame.getProductId();
			}
		}
		else{
			StringBuilder currentVinList = new StringBuilder();
			currentVinList.append("Engine Type Check Failed - Engine has assigned to different VIN. /n");
			for(Frame currentFrame: list){
				currentVinList.append("/n" + currentFrame.getProductId());
			}
			
			return currentVinList.toString();
		}
	
	}// end of Duplicate
}
