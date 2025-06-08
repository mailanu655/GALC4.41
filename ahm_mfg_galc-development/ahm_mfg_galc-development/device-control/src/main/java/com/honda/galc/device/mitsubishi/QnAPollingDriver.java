package com.honda.galc.device.mitsubishi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.DeviceFormatGroup;
import com.honda.galc.device.DevicePlcData;
import com.honda.galc.device.DevicePoint;
import com.honda.galc.device.DevicePointBoolean;
import com.honda.galc.device.DevicePointInteger;
import com.honda.galc.device.DevicePointShort;
import com.honda.galc.device.DevicePointString;
import com.honda.galc.device.DeviceUtil;
import com.honda.galc.device.DriverDevice;
import com.honda.galc.device.IDataReadyListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.PlcDataBlock;
import com.honda.galc.device.dataformat.Acknowledgment;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;

/**
 * 
 * <h3>QnAPollingDriver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnAPollingDriver description </p>
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
public class QnAPollingDriver extends QnADriver implements IDataReadyListener{
	private DeviceScaner plcScanner;
	private List<DriverDevice> dataReadyDeviceList;
	private Map<String, DriverDevice> readDevices;
	
	public QnAPollingDriver() {
		super();
		dataReadyDeviceList = new ArrayList<DriverDevice>();
		readDevices = new HashMap<String, DriverDevice>();
	}

	public byte[] read(String address, QnASubCommand subcmd, int points, boolean log){
		ByteBuffer readCmd = createReadCommand(address, subcmd, points);
		
		if(log)
			getLogger().info("send " + readCmd.array().length, ":", DeviceUtil.toHex(readCmd.array()) );
		
		 byte[] read = access(readCmd.array());
		 
		if(log)
			getLogger().info("receive " + read.length, ":", DeviceUtil.toHex(read));
		 return read;
	}

	public DevicePoint read(DeviceFormat deviceFormat) {
		QnABinaryMessage qnaMessage = new QnABinaryMessage(deviceFormat);
		ByteBuffer readCmd = qnaMessage.createReadCommand();
		byte[] bytes = access(readCmd.array());

		Object returnObject = parser.decode(deviceFormat, bytes, qnaMessage.getSubCommand());
		return DeviceUtil.converToDevicePoint(deviceFormat, returnObject);
	}
	
	private DevicePlcData read(DriverDevice device) {//this is for data ready
		List<PlcDataBlock> dataList = new ArrayList<PlcDataBlock>();
		
		for(DeviceFormatGroup group : device.getDeviceFormatGroupMap().values()){
			byte[] read = read(group, true);
			
			dataList.add(new PlcDataBlock(group.getId(), QnAParser.getReadPacketData(read)));
		}
		
		if(dataList.size() == 0 ) return null;
		
		return new DevicePlcData(device.getDevice().getClientId(), dataList);
	}
	
	public void write(DeviceFormat deviceFormat, Object data) {
		QnABinaryMessage qnaMessage = new QnABinaryMessage(deviceFormat);
		QnADeviceDataType dataType = QnADeviceDataType.valueOf(deviceFormat.getDeviceDataType().toString());
		ByteBuffer writeCmd = qnaMessage.createWriteCommand(dataType, data);
		getLogger().info("sent:", DeviceUtil.toHex(writeCmd.array()));
		access(writeCmd.array());
		
	}
	
	public synchronized byte[] access(byte[] data){

		try {
			
			long startSnd = System.currentTimeMillis();
			send(data);
			//long completeSnd = System.currentTimeMillis();
			byte[] recieveDataFromPlc = doReceiveData();
			long completeRcv = System.currentTimeMillis();

			if(property.isDebugDriver())
				getLogger().info(this.getClass().getSimpleName(), " access plc time: " + (completeRcv - startSnd));
			
			return recieveDataFromPlc;
			
		} catch (Throwable e) {
			getLogger().error(e, "Error: exception on access plc.");
			return null;
		}

	}

	private byte[] doReceiveData() throws IOException, Throwable {
		byte[] receiveDataFromPlc = socket.receiveDataFromPlc();
		while(!isResponse(receiveDataFromPlc)){

			//handle unsolicited
			if(isUnsolicitedPacket(receiveDataFromPlc)){
				sendUnsolicitedReply();
				getLogger().info("Recived unsolicited data " + receiveDataFromPlc.length, ":", 
						DeviceUtil.toHex(receiveDataFromPlc));
				handleUnsolicitedPacket(receiveDataFromPlc);
			}else
				getLogger().warn("Received unknown data:", DeviceUtil.toHex(receiveDataFromPlc));
				receiveDataFromPlc = socket.receiveDataFromPlc();
		}
		
		return receiveDataFromPlc;
	}

	public void registerDataReadyDevice(List<DriverDevice> list){
		
		for(DriverDevice dev : list){
			if(dev.isDataReadyDevice() && isMyDevice(dev)){
				dataReadyDeviceList.add(dev);
				dev.registerListener(this);
			}
		}
		
		for(DriverDevice readyDevice : dataReadyDeviceList){
			for(DeviceFormat devFormat: readyDevice.getDevice().getDeviceDataFormats()){
				DriverDevice readDevice = getReadDevice(list, devFormat);
				if(readDevice == null){
					getLogger().warn("WARN: device is not defined for data ready tag:",devFormat.getTag());
				}
				readDevices.put(devFormat.getTag(), readDevice);
			}
		}
		
	}
	
	private DriverDevice getReadDevice(List<DriverDevice> list,	DeviceFormat devFormat) {
		for(DriverDevice dev : list){
			for(DeviceFormat format : dev.getDevice().getDeviceDataFormats()){
				//if(format.getTagName().trim().equals(DriverDevice.DATA_READY) &&
				if(format.getDeviceTagType() == DeviceTagType.NONE &&
						format.getTagValue().trim().equals(devFormat.getTag()))
						return dev;
			}
		}
		return null;
	}

	public List<DriverDevice> getDataReadyDeviceList() {
		return dataReadyDeviceList;
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		//This should work the same was as the current OPC EI data ready tag
		if(!(deviceData instanceof DevicePoint)){
			//Log error - unexpected data received 
			getLogger().warn("Unexpected data received:" + deviceData.getClass().getSimpleName());
			return Acknowledgment.success();
		};
		
		DevicePoint devicePoint = (DevicePoint)deviceData;
		getLogger().info("Received Data Ready:" + devicePoint.getClientId() + ":" + devicePoint.getName() + ":" + devicePoint.getValue());
		
		if(devicePoint instanceof DevicePointBoolean){
			if(!((DevicePointBoolean)deviceData).getValue()) return Acknowledgment.success();
		} else if(devicePoint instanceof DevicePointShort || deviceData instanceof DevicePointInteger){
			if((Integer)devicePoint.getValue() == 0) return Acknowledgment.success();
		} else if(devicePoint instanceof DevicePointString ){
			if(!devicePoint.getValue().toString().equals("OK")) return Acknowledgment.success();
		}
		
		if(isOneTag(devicePoint)){
			DriverDevice driverDevice = getDriverDevice(devicePoint.getClientId());
			driverDevice.dispatchPlcData();
			getLogger().info("One tag:", devicePoint.getClientId(), ":", devicePoint.getName(), " : ", devicePoint.getValue().toString());
		} else {
			DriverDevice devDevice = getDriverDevice(devicePoint);
			DevicePlcData plcData = readDeviceData(devDevice);
			
			if(plcData != null)
				devDevice.setData(plcData);
			else{
				getLogger().error("Error: Failed to read data from device:" + devicePoint.getName());
			}
		}
		
		return Acknowledgment.success();
	}

	private DriverDevice getDriverDevice(DevicePoint devicePoint) {
		 DriverDevice driverDevice = readDevices.get(devicePoint.getName());
		 if(driverDevice == null){
			 getLogger().error("Error: Can not find Driver Device for " + devicePoint.getName());
		 }
		 return driverDevice;
	}

	private DevicePlcData readDeviceData(DriverDevice driverDevice) {
		if(driverDevice == null) return null;
		
		return read(driverDevice);
	}

	public DataContainer received(String clientId, DataContainer dc) {
		for(Object readyTag : dc.keySet()){
			DriverDevice readDevice = readDevices.get(readyTag);
			DevicePlcData read = read(readDevice);
			
			if(read == null)
			{
				String msg = "Failed to read from plc for data ready tag:" + readyTag;
				getLogger().error(msg);
				return Acknowledgment.failed(msg).toDataContainer();
			}
				
			readDevice.setData(read);
		}
		
		return Acknowledgment.success().toDataContainer();
		
	}

	public byte[] read(DeviceFormatGroup group, boolean log) {
		return read(group.getStartAddress(), QnASubCommand.wordUnit, group.getPoints(), log);
	}

	private boolean isOneTag(DevicePoint devicePoint) {
		DriverDevice driverDevice = getDriverDevice(devicePoint.getClientId());
		List<DeviceFormat> allDeviceFormat = driverDevice.getAllDeviceFormat();
		return allDeviceFormat.size() == 1 && allDeviceFormat.get(0).getId().getTag().equals(devicePoint.getName());
	}
	
	
	private boolean isMyDevice(DriverDevice dev) {
		
		return (dev.getDevice().getEifIpAddress().trim().equals(getIpAddress()) &&
				dev.getDevice().getEifPort() == getPort());
	}

	
	public void start() {
		try {
			initialize();
			registerDataReadyDevice(getDriverDeviceList());
			startDataChangeEventQueueHandler();
			startPlcScanner();
		} catch (Exception e) {
			getLogger().error(e, "Error: exception on start driver " + this.getClass().getSimpleName());
		}
	}

	
	private void startPlcScanner() {
		plcScanner = new DeviceScaner(this, getDataReadyDeviceList(), property);
		Thread t = new Thread(plcScanner);
		t.start();
		
	}

	public void stop() {
		plcScanner.setRunning(false);
		stopChangeEventQueueHandler();
	}
	

	public boolean isNotifyChangedItemOnly() {
		return true;
	}

	public boolean isNotifyChangedOnly() {
		return false;
	}

	@Override
	public void deActivate() {
		super.deActivate();
		stop();
		socket.cleanup();
	}
	
}
