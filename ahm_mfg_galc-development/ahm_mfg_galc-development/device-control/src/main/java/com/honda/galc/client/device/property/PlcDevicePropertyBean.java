package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>PlcDevicePropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcDevicePropertyBean description </p>
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
@PropertyBean(componentId="Default_Plc_Device",prefix="device.plc")
public interface PlcDevicePropertyBean extends DevicePropertyBean{
	
	@PropertyBeanAttribute(propertyKey="hostname")
	String getIpAddress();
	
	@PropertyBeanAttribute(propertyKey="port")
	int getPort();
	
	@PropertyBeanAttribute(propertyKey="scanrate", defaultValue = "30")
	long getScanRate();
	
	@PropertyBeanAttribute(propertyKey="pinginterval")
	long getPingInterval();
	
	@PropertyBeanAttribute(propertyKey="type",defaultValue = "Mitshubishi")
	String getType();

	@PropertyBeanAttribute(propertyKey="pingpoint", defaultValue = "D0001")
	String getPingPoint();

	@PropertyBeanAttribute(propertyKey="connectiontimeout", defaultValue = "500")
	int getConnectionTimeout();
	
	@PropertyBeanAttribute(propertyKey="responsetimeout", defaultValue = "2000")
	int getResponseTimeout();
	
	@PropertyBeanAttribute(propertyKey="maxconnectionretry", defaultValue = "3")
	int getMaxConnectionRetry();
}
