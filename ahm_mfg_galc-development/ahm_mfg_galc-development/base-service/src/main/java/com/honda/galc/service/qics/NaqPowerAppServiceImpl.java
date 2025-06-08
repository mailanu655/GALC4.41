package com.honda.galc.service.qics;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.NaqPowerAppService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class NaqPowerAppServiceImpl implements NaqPowerAppService {
		
	public static long LAST_REQUEST_TIME = 0; 
	
	public String saveDefectData(DefectMapDto defectMapDto) {
		if(!isRequestAllowed()) {
			return "Only " + getRequestPerMinute() + " request(s) per minute allowed. Please send it later.";
		}
		
		int result = 0;
		try {
			result = ServiceFactory.getService(HeadlessNaqService.class).saveDefectData(defectMapDto);
		} catch (Exception e) {
			Logger.getLogger().error("Unable to create defect. Exception: " + e.getMessage());
			return "Unable to create defect. Exception: " + e.getMessage();
		}
		
		String message = "";
		switch(result) {
			case QiConstant.SC_CREATED :
				message = "Defect created successfully.";
				break;
			case QiConstant.SC_NOT_ACCEPTABLE :
				message = "Following fileds connot be blank: externalSystemName, externalPartCode, externalDefectCode, productId, productType, processPointId";
				break;
			case QiConstant.SC_NOT_FOUND :
				message = "Unable to create defect. See logs for more details.";
				break;
			case QiConstant.SC_LENGTH_REQUIRED :
				message = "Defect data does not meet requirements.";
				break;
			case QiConstant.SC_PARTIAL_CONTENT :
				message = "Unable to save. See logs for more details.";
				break;
			default :
				message = "Unknown result. See logs for more details.";	
		}
		return message;
	}
	
//	Check number of requests allowed per minute
	private boolean isRequestAllowed() {
		long currentTime = System.currentTimeMillis();
		if(currentTime - LAST_REQUEST_TIME > 60000 / getRequestPerMinute()) {
			LAST_REQUEST_TIME = currentTime;
			return true;
		} else {
			return false;
		}
	}
	
	private int getRequestPerMinute() {
		return PropertyService.getPropertyBean(QiPropertyBean.class).getRequestPerMinute();
	}
		
}
