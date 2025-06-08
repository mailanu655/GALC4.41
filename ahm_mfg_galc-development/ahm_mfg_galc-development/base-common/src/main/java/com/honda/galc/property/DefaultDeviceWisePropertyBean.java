package com.honda.galc.property;

/**
 * 
 * 
 * <h3>DefaultDeviceWisePropertyBean Class description</h3>
 * <p> DefaultDeviceWisePropertyBean description </p>
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
 * @author Cesar X Martinez<br/>
 * Nov 02, 2017
 *
 *
 */
@PropertyBean(componentId = "DEFAULT_DEVICE_WISE")
public interface DefaultDeviceWisePropertyBean extends IProperty{

	/**
	 * Provides the default HTTP connection time out, 
	 * expressed in milliseconds unit.
	 */
	@PropertyBeanAttribute(propertyKey="DW_CONNECTION_TIMEOUT", defaultValue="5000")
	public int getDwConnectionTimeout();
	
	/**
	 * Provides the default HTTP read time out, 
	 * expressed in milliseconds unit.
	 */
	@PropertyBeanAttribute(propertyKey="DW_READ_TIMEOUT", defaultValue="15000")
	public int getDwReadTimeout();
	
	/**
	 * flag to use String value
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey="STRING_VALUE", defaultValue="false")
	public boolean isStringValue();
}
