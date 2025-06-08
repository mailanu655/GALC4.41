package com.honda.galc.client.device.ei;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.DeviceData;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.DevicePoint;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDriver;
import com.honda.galc.device.dataformat.Acknowledgment;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.DevicePropertyBean;

/**
 * 
 * <h3>PlcDevice</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcDevice description </p>
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

public class PlcDevice extends EiDevice implements IPlcDevice{
	String NAME = "PLC_EI_DEVICE";
	public PlcDevice(String terminalName) {
		super(terminalName);
		initialize();
	}

	private void initialize() {
		setId(NAME);
		getLogger().info("Plc ei is started");
	}

	List<IDeviceDriver> driverList = new ArrayList<IDeviceDriver>();
	List<DeviceData> deviceList = new ArrayList<DeviceData>();
	
	public DevicePoint read(DevicePoint devicePoint) {
		try {
			Device device = getDevice(devicePoint.getClientId());
			IDeviceDriver driver = getDeviceDriver(device);
			return driver.read(device.getDeviceFormat(devicePoint.getName()));
		} catch (Exception e) {
			getLogger().error(e, "Failed to read device point:", devicePoint.getClientId(), devicePoint.getName());
		}
		return null;
	}
	
	public Object read(String deviceId){
		try {
			Device device = getDevice(deviceId);
			IDeviceDriver driver = getDeviceDriver(device);
			return driver.read(device);
		} catch (Exception e) {
			// TODO: handle exception
			getLogger().error(e, "Failed to read device:", deviceId);
		} 
		return null;
	}
	

	public void send(IDeviceData data) {
		Device device = getDeviceByAliasName(data.getClass().getSimpleName());
		getDeviceDriver(device).write(device, data);
	}
	
	
	public void send(DataContainer dc) {
		Device device = getDevice(dc.getClientID());
		getDeviceDriver(device).write(device, dc);
		
	}
	
	public IDeviceData syncSend(IDeviceData data) {
		try {
			Device device = getDeviceByAliasName(data.getClass().getSimpleName());
			getDeviceDriver(device).write(device, data);
		} catch (Exception e) {
			getLogger().error(e, "Failed on syncSend:", data.getClass().getSimpleName());
			return Acknowledgment.failed(e.toString());
		}
		return Acknowledgment.success();
	}
	
	public DataContainer syncSend(DataContainer dc) {
		try {
			Device device = getDevice(dc.getClientID());
			getDeviceDriver(device).write(device, dc);
		} catch (Exception e) {
			Acknowledgment.failed(e.toString()).toDataContainer();
		}
		return Acknowledgment.success().toDataContainer();
	}
	
	
	public void send(DevicePoint devicePoint) {
		Device device = getDevice(devicePoint.getClientId());
		IDeviceDriver driver = getDeviceDriver(device);
		driver.write(device.getDeviceFormat(devicePoint.getName()), devicePoint.getValue());
	}
	
	// Utility functions
	private Device getDeviceByAliasName(String aliasName) {
		for(DeviceData devdata : deviceList){
			if(aliasName.equals(devdata.getDevice().getAliasName()))
				return devdata.getDevice();
		}
		getLogger().warn("Failed to find device for device data:", aliasName);
		return null;
	}
	
	public IDeviceDriver getDeviceDriver(String deviceId){
		return getDeviceDriver(getDevice(deviceId));
	}

	public IDeviceDriver getDeviceDriver(Device device) {
		for (IDeviceDriver driver : driverList) {
			if (device.getEifIpAddress().trim().equals(driver.getIpAddress())
					&& device.getEifPort() == driver.getPort())
				return driver;
		}
		getLogger().error("Error: Can not find driver for device:", device.getClientId(), ":",
				device.getEifIpAddress(), ":" +device.getEifPort());
		return null;
	}

	public Device getDevice(String deviceId) {

		for (DeviceData devdata : deviceList)
			if (devdata.getDevice().getClientId().equals(deviceId))
				return devdata.getDevice();

		getLogger().error("Error: Can not find device:", deviceId);
		return null;
	}
	
	//Getter & Setters
	public List<IDeviceDriver> getDriverList() {
		return driverList;
	}
	
	public void setDriverList(List<IDeviceDriver> driverList) {
		this.driverList = driverList;
	}
	
	public List<DeviceData> getDeviceList() {
		return deviceList;
	}
	
	public void setDeviceDataList(List<DeviceData> deviceDataList){
		this.deviceList = deviceDataList;
		
		for(IDeviceDriver driver :  driverList){
			driver.setDeviceData(deviceDataList);
		}
	}

	@Override
	public void registerDeviceListener(DeviceListener listener,
			List<IDeviceData> deviceDataList) {
		for(IDeviceDriver driver : driverList){
			driver.registerDeviceListener(listener, deviceDataList);
		}
	}

	@Override
	public void start() {
		for(IDeviceDriver driver : driverList)
			driver.start();
	}
	

	public void setDeviceProperty(DevicePropertyBean devicePropertyBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deActivate() {
		super.deActivate();
		closeConnections();
	}

	private void closeConnections() {
		for(IDeviceDriver driver : driverList){
			driver.deActivate();
		}
		
	}
	
}
