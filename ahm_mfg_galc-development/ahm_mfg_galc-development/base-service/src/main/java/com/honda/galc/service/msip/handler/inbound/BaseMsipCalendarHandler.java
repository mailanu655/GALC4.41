package com.honda.galc.service.msip.handler.inbound;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.service.msip.dto.inbound.IMsipInboundDto;
import com.honda.galc.service.msip.handler.inbound.BaseMsipInboundHandler;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public abstract class BaseMsipCalendarHandler<T extends BaseMsipPropertyBean, D extends IMsipInboundDto> extends BaseMsipInboundHandler<T, D> {
	
	boolean planCodesConfigured = false;	
	
	protected List<String> getPlanCodesForPlantLine(String planCode, String[] planCodes)  {
		
		if(StringUtils.isBlank(planCode)) {
			getLogger().error("planCode not found in record, skipping record.");
			return null;
		}
		
		ArrayList<String> subset = new ArrayList<String>();
		//if the plant_code, line and product match, return true
		for(String thisPlanCode : planCodes)  {
			if(thisPlanCode.substring(0, 7).equalsIgnoreCase(planCode.trim().substring(0, 7)))  {
				subset.add(thisPlanCode);
			}
		}
		return subset;

	}
	
	protected List<String> getPlanCodesForPlantLine(String planCode)  {		
		return getPlanCodesForPlantLine(planCode, getPropertyBean().getPlanCodes());
	}
	
	protected boolean isPlansCodeConfigured(){
		String[] planCodes = getPropertyBean().getPlanCodes();
		if (planCodes == null) {
			planCodesConfigured = false;
		}
		else if (planCodes.length == 0) {
			planCodesConfigured = false;
		}
		else {
			planCodesConfigured = true;
		}
		return planCodesConfigured;
	}
}
