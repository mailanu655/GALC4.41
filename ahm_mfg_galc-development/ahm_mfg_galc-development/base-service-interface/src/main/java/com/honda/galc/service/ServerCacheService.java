package com.honda.galc.service;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.product.LotControlRule;

/**
 * 
 * <h3>ServerCacheService Class description</h3>
 * <p> ServerCacheService description </p>
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
public interface ServerCacheService extends IService{
	
	public List<ComponentProperty> getProperty(String componentId);
	
	public List<LotControlRule> getLotControlRule(String processPointId);
	
	public void refreshProperty(String componentId);
	
	public void refreshLotControlRule(String processPointId);
	
	
}
