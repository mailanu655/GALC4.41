package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;

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
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * Scanned Engine Serial number to Frame EIN Checker
 * @author Max MacKay
 * @date Oct. 16, 2015
 */

public class EngineToFrameChecker extends AbstractBaseChecker<PartSerialScanData> {

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
		
		String einResultString = einCheck(vin,ein);
		
		if(!StringUtils.isBlank(einResultString)){
			checkResults.add(createCheckResult(einResultString));
			return checkResults;
		}
				
		return checkResults;
	}
//Checks Scanned EIN against what is in 143TBX(FRAME)
private String einCheck(String vin, String ein) {
		
		Frame frame = null;
		
		if (vin != null){
			frame = ServiceFactory.getDao(FrameDao.class).findByKey(vin);	//get Frame object from 143TBX
		}
		if(frame == null){
			String noCarFound = null;
			noCarFound = "Engine to Frame Check Failed - Was not able to find an object associated with VIN: " + vin;
			return noCarFound;
		}
		
		String vinEngineSerialNumber= frame.getEngineSerialNo(); //pulls Engine Serial Number from 143TBX
		
		if(vinEngineSerialNumber == null || ein == null){
			String missingEin = null;
			missingEin = "Engine to Frame Check Failed - Missing Engine Serial Number value in 143TBX";
			return missingEin;
		}
		
		
		if(vinEngineSerialNumber.equals(ein) == true)  //Checks EINs
		{
			return null;
		}
		else{
			String currentEinAssigned = null;
			currentEinAssigned = "Engine to Frame Check Failed - Scanned Engine Serial Number " + ein + " does not match Frame's "+ vin + " EIN" +frame.getEngineSerialNo();
			return currentEinAssigned;
		}
	}


}
