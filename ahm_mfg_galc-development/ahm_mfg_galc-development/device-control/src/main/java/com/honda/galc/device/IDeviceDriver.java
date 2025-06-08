package com.honda.galc.device;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.mitsubishi.QnASubCommand;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;

/**
 * 
 * <h3>IDeviceDriver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IDeviceDriver description </p>
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
public interface IDeviceDriver extends IDevice{
	public void start();
	public void stop();
	
	public String getIpAddress();
	public int getPort();
	
	public DevicePoint read(DeviceFormat deviceFormat) throws Exception;
	public Object read(Device device) throws Exception;
	public byte[] read(String address, QnASubCommand subcmd, int points, boolean isLog) throws Exception;
	public byte[] read(DeviceFormatGroup group, boolean isLog) throws Exception;
	
	public void write(DeviceFormat format, Object data);
	public void write(Device device, DataContainer dataContainer);
	public void write(Device device, IDeviceData data);
	
	public LinkedBlockingQueue<IPlcData> getQueue();
	public void setDeviceData(List<DeviceData> deviceDataList);
	public void registerDeviceListener(DeviceListener listener,	List<IDeviceData> deviceDataList);
	
	
}
