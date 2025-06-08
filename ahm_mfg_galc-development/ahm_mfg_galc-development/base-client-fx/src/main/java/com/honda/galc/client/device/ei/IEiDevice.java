package com.honda.galc.client.device.ei;

import java.util.List;

import com.honda.galc.data.DataContainerListener;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;

/**
 * 
 * <h3>IEiDevice Class description</h3>
 * <p> IEiDevice description </p>
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
 * Apr 14, 2010
 *
 */
public interface IEiDevice extends IDevice{
	
	public IDeviceData syncSend(IDeviceData deviceDataFormat);
	
	public void send(IDeviceData deviceDataFormat);
	
	public void registerDataContainerListener(DataContainerListener listener);
	
	public void registerDeviceListener(DeviceListener listener,List<IDeviceData> deviceDataList);
	
	public void reqisterDeviceData(List<IDeviceData> deviceDataList);
	
	public void start();
		
		
}
