package com.honda.galc.client.common.datacollection.data;

/**
 * 
 * <h3>DeviceMessage</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceMessage description </p>
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
 * Mar 18, 2010
 *
 */
public class StatusMessage extends EventMessage<Boolean> {
	public static final String SERVER_ON_LINE = "Server On Line";
	public static final String SERVER_OFF_LINE = "Server Off Line";
	public static final String DEVICE_CONNECT = "Device Connected";
	public static final String DEVICE_DISCONNECT = "Device Disconnected";

	public StatusMessage(String id, Boolean message) {
		super(id, message);
	}
	
	public boolean getDeviceStatus(){
		return getMessage();
	}

}
