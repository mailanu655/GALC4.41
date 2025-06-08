package com.honda.galc.device;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.honda.galc.client.common.NotificationInvoker;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;

/**
 * 
 * <h3>PlcDataChangeEventQueueHandler</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcDataChangeEventQueueHandler description </p>
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
public class PlcDataChangeEventQueueHandler implements Runnable{
	private LinkedBlockingQueue<IPlcData> queue;
	private List<DriverDevice> devices;
	private boolean stop = false;
	private IDeviceDriver driver;
	private IPlcData plcData;
	private NotificationInvoker invoker;
	

	public PlcDataChangeEventQueueHandler(IDeviceDriver driver,
			List<DriverDevice> driverDeviceList) {
		super();
		this.driver = driver;
		this.queue = driver.getQueue();
		this.devices = driverDeviceList;
	}

	public void run() {
		while(!isStop()){
			handleQueuedData();
		}
		
	}
	
	private void handleQueuedData() {
		try {
			plcData = queue.take();
			
			processPlcData();
			
		} catch (Exception e) {
			getLogger().error(e, "Error: exception on handle queued data.");
		}
		
	}
	
	private void processPlcData() {
		if(plcData instanceof PlcDataBlock){//Unsolicited Data
			handlePlcDataBlock((PlcDataBlock)plcData);
		} else 
		if(plcData instanceof DevicePlcData){
			handleDevicePlcData((DevicePlcData)plcData);
		} else if(plcData instanceof DevicePoint){
			handleDevicePointData((DevicePoint)plcData);
		} else {
			getLogger().warn("WARN: unsupport data from queue:" + plcData.getClass().getSimpleName());
		}
	}
	


	private void handlePlcDataBlock(PlcDataBlock plcData) {
		DriverDevice driverDevice = getDriverDevice(plcData);
		if (driverDevice != null) {
			driverDevice.setData(plcData.getData());
		} else {
			getLogger().error("Error: unsolicited device is not defined.");
		}
		
	}
	
	private void handleDevicePlcData(DevicePlcData plcData) {
		DriverDevice driverDevice = getDriverDevice(plcData.getId());
		
		if(!plcData.isChangedOnly()){
			logMessage(plcData);
			DataContainer returnDc = driverDevice.setData(plcData);

			//Not sure we need this ... keep it for now. we remove this send reply?
			DriverDevice replyDevice = getDriverDevice(driverDevice.getDevice().getReplyClientId());

			if(replyDevice != null)
				driver.write(replyDevice.getDevice(), returnDc);
		} else {//handle data ready
			driverDevice.setDataChangedOnly(plcData);
		}
		
	}
	
	private void handleDevicePointData(DevicePoint plcData) {
		DriverDevice driverDevice = getDriverDevice(plcData.getClientId());
		DataContainer returnDc = getInvoker().syncSend(driverDevice.getDevice().getEifIpAddress(), 
				driverDevice.getDevice().getEifPort(), plcData);
		
		//Not sure we need this ... keep it for now. we remove this send reply?
		DriverDevice replyDevice = getDriverDevice(driverDevice.getDevice().getReplyClientId());
		
		if(replyDevice != null)
			driver.write(replyDevice.getDevice(), returnDc);
		
	}
	
	
	private DriverDevice getDriverDevice(PlcDataBlock plcData) {
		for(DriverDevice dev : devices){
			/**
			 * To define unsolicited device:
			 * 1. UNSOLICITED_DEVICE must present in the device description
			 * 2. Same as the style in Alccom, only one group can be defined
			 */
			if(dev.isUnsolicitedDevice() && dev.getDeviceFormatGroupMap().get(plcData.getAddress()) != null)
				return dev;
		}
		getLogger().error("Error: Failed to find device for " + plcData.getAddress());
		return null;
		
	}

	

	private DriverDevice getDriverDevice(String id) {
		for(DriverDevice dev : devices){
			if(dev.getDevice().getClientId().trim().equals(id.trim()))
				return dev;
		}
		return null;
	}
	
	public Logger getLogger(){
		return driver.getLogger();
	}

	private void logMessage(DevicePlcData plcData) {
		for(PlcDataBlock block: plcData.getDataList()){
			getLogger().info("recived " + block.getData().length, ":", DeviceUtil.toHex(block.getData()));
		}
		
	}
	
	
	//Getter && Setters
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public NotificationInvoker getInvoker() {
		if(invoker == null)
			invoker = new NotificationInvoker(getLogger());
		return invoker;
	}
	
	
	
}
