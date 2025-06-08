package com.honda.galc.service.msip.handler.inbound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.service.msip.dto.inbound.IPlanCodeDto;
import com.honda.galc.service.msip.handler.inbound.BaseMsipInboundHandler;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Subu Kathiresan
 * @date May 10, 2017
 */
public abstract class PlanCodeBasedMsipHandler<P extends BaseMsipPropertyBean, D extends IPlanCodeDto> extends BaseMsipInboundHandler<P, D> {

	protected Map<String, ArrayList<D>> createMapByPlanCode(List<D> recordList) throws IllegalAccessException, InstantiationException {
		Map<String, ArrayList<D>> planCodeDtoMap = new HashMap<String, ArrayList<D>>();

		for(D receivedRecord : recordList) {
			if(!validatePlanCode(receivedRecord))
				continue;
			
			if(!planCodeDtoMap.containsKey(receivedRecord.getPlanCode().trim()))
				planCodeDtoMap.put(receivedRecord.getPlanCode().trim(), new ArrayList<D>());
			
			planCodeDtoMap.get(receivedRecord.getPlanCode().trim()).add((D) receivedRecord);
		}
		
		if(planCodeDtoMap.isEmpty()) {
			StringBuffer sb = new StringBuffer("Records matching current plan code(s):")
					.append(StringUtils.join(getPropertyBean().getPlanCodes(), ","))
					.append(" not found");
			getLogger().info(sb.toString());
		}
		return planCodeDtoMap;
	}
	
	
	protected boolean validatePlanCode(D currentPlan) {		
		if(currentPlan == null) 
			return false;

		String planCode = currentPlan.getPlanCode();
		return validatePlanCode(planCode);
	}
	
	protected boolean validatePlanCode(String planCode) {		
		if(StringUtils.isBlank(planCode)) {
			getLogger().info("planCode not found in record, skipping record.");
			return false;
		}
		List<String> list = Arrays.asList(getPropertyBean().getPlanCodes());
		if(!list.contains(planCode.trim())) {
			getLogger().info("Not in current plan codes: " + 
					StringUtils.join(getPropertyBean().getPlanCodes(), ",") + " skipping record." );
			return false;
		}		
		return true;
	}
}
