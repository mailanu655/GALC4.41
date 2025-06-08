package com.honda.galc.client.device.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>QE71CommandsPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QE71CommandsPropertyBean description </p>
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
 * Nov 5, 2010
 *
 */

public interface QE71CommandsPropertyBean extends IProperty{
	
	@PropertyBeanAttribute( defaultValue = "0x0050")
	public String getCommandSubheader();
	
	@PropertyBeanAttribute( defaultValue = "0xD000")
	public String getResponseSubheader();
	
	@PropertyBeanAttribute( defaultValue = "0xFF")
	public String getPlcNo();
	
	@PropertyBeanAttribute( defaultValue = "0x00")
	public String getNetworkNo();
	
	@PropertyBeanAttribute( defaultValue = "0x03FF")
	public String getFixedValue1();
	
	@PropertyBeanAttribute( defaultValue = "0x00")
	public String getFixedValue2();
	
	@PropertyBeanAttribute( defaultValue = "0x0000")
	public String getCpuMonitoringTimerSubCommand();
	

}
