package com.honda.galc.service;

import java.util.List;

import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ServerCacheServiceImpl Class description</h3>
 * <p> ServerCacheServiceImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 13, 2012
 *
 *
 */
public class ServerCacheServiceImpl implements ServerCacheService{

	public List<LotControlRule> getLotControlRule(String processPointId) {
		return LotControlRuleCache.getLotControlRules(processPointId);
	}

	public List<ComponentProperty> getProperty(String componentId) {
		return PropertyService.getComponentProperty(componentId);
	}


	public void refreshLotControlRule(String processPointId) {
		LotControlRuleCache.removeLotCtrolRules(processPointId);
	}

	public void refreshProperty(String componentId) {
		PropertyService.refreshComponentProperties(componentId);
	}
	


}
