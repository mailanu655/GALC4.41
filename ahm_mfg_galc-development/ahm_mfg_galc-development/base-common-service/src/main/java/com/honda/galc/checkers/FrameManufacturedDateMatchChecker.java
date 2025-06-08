package com.honda.galc.checkers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.property.PartCheckerPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class FrameManufacturedDateMatchChecker extends AbstractBaseChecker<PartSerialScanData> {

	private PartCheckerPropertyBean propertyBean;
	
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
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		String msg = "";
		
		if(partSerialScanData == null || StringUtils.isEmpty(partSerialScanData.getSerialNumber()) ){
			msg = "Frame Manufactured Date Match Checker-Invalid Part SN.";
		}else{
			propertyBean = PropertyService.getPropertyBean(PartCheckerPropertyBean.class);
			SimpleDateFormat format = new SimpleDateFormat(propertyBean.getManufacturedDateFormat());
			Date manufacturedDate = null;
			try {
				manufacturedDate = format.parse(partSerialScanData.getSerialNumber());
				if(manufacturedDate != null){
					Calendar manufacturedCal = Calendar.getInstance();
					manufacturedCal.setTime(manufacturedDate);
					
					if(manufacturedCal.get(Calendar.MONTH) != Calendar.getInstance().get(Calendar.MONTH) || 
							manufacturedCal.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)){
						msg = "Frame Manufactured Date Match Checker-Manufacured date does not match with Actual AF OFF Date. Please reprint Cert Label.";
					}
				}else{
					msg = "Frame Manufactured Date Match Checker-Manufactured date is null. ";
				}
			} catch (ParseException e) {
				msg = "Frame Manufactured Date Match Checker-Manufactured date cannot be parsed. ";
				Logger.getLogger().error(e,"Error while validating the manufactured date ");
			}
			
		}
		
		
		if (StringUtils.isNotBlank(msg)) {
			CheckResult checkResult = new CheckResult();
			checkResult.setCheckMessage(msg);
			checkResult.setReactionType(getReactionType());
			checkResults.add(checkResult);
		}
		return checkResults;
	}


}
