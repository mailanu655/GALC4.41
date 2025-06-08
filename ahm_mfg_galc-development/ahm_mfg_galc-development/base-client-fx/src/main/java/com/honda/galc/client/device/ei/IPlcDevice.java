package com.honda.galc.client.device.ei;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.DevicePoint;

/**
 * 
 * <h3>IPlcDevice</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IPlcDevice description </p>
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
public interface IPlcDevice extends IEiDevice{
	public void send(DataContainer dc);
	public DataContainer syncSend(DataContainer dc);

	public void send(DevicePoint devicePoint);
	
	public DevicePoint read(DevicePoint devicePoint);
	public Object read(String deviceId);
}
