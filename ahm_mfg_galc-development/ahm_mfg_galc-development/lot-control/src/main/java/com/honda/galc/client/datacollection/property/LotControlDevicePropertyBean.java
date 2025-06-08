package com.honda.galc.client.datacollection.property;

import java.util.Map;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>LotContolDevicePropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotContolDevicePropertyBean description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jan 23, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 23, 2012
 */

@PropertyBean(componentId ="Default_LotControlDevice")
public interface LotControlDevicePropertyBean extends DevicePropertyBean{
	@PropertyBeanAttribute(defaultValue = "")
	public String getProductSubIdLotControlPartName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProductSubIdDeviceId();

	@PropertyBeanAttribute(defaultValue = "1000")
	public int getProductSubIdPollingInterval();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getBroadCastDeviceId();
	
	@PropertyBeanAttribute
	public String getBroadCastDeviceId1();
	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getRuleStatusDevice();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public Boolean isRuleDeviceEnabled();
	
}
