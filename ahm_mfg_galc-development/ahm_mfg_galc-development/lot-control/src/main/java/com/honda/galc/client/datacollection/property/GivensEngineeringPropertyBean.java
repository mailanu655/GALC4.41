package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>GivensEngineeringPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> GivensEngineeringPropertyBean description </p>
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
 * @author Paul Chou
 * Mar.5, 2019
 *
 */
@PropertyBean(componentId ="Default_GivensEngineering")
public interface GivensEngineeringPropertyBean extends PlcForceLotControlPropertyBean{
	
	@PropertyBeanAttribute(defaultValue = "true") 
	public boolean isHeartbeatEnabled();
	
	@PropertyBeanAttribute(defaultValue = "10")
	public int getHeartbeatInterval();

}
