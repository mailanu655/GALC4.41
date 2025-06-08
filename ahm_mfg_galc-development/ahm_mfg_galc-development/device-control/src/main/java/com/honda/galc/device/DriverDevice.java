package com.honda.galc.device;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.NotificationInvoker;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;

/**
 * 
 * <h3>DriverDevice</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DriverDevice description </p>
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
public class DriverDevice {
	private Device device;
	private byte[] data;
	private List<DeviceListener> listeners;
	private Map<String, DeviceFormatGroup> deviceFormatGroupMap;
	private DeviceFormatGroup currentGroup;
	private DeviceData deviceData;
	private Logger logger;
	private NotificationInvoker invoker;
	
	enum Operation{NOTIFYCHANGEDITEMONLY, NOTIFYCHANGEDONLY, NOTIFYANYWAY};
	private final String DEVICE_DATA_PACKAGE="com.honda.galc.device.dataformat";
	public static final String UNSOLICITED_DEVICE = "UNSOLICITEDDEVICE";
	public static final String DATA_READY = "DATAREADY";
	public static final String READ_BY_GROUP = "READBYGROUP";
	
	
	public DriverDevice(DeviceData deviceData, Logger logger) {
		super();
		this.device = deviceData.getDevice();
		this.deviceData = deviceData;
		this.logger = logger;
		
		initialize();
	}

	public DriverDevice(Device device, Logger logger) {
		super();
		this.device = device;
		this.deviceData = new DeviceData(device);
		this.logger = logger;
		
		initialize();
	}
	
	private void initialize() {
		listeners = new ArrayList<DeviceListener>();
		deviceFormatGroupMap = Collections.synchronizedMap(new LinkedHashMap<String, DeviceFormatGroup>());
		
		deviceData.register(getDeviceAliasObject());
		
		groupDeviceFormat();
	}

	private void groupDeviceFormat() {
		
		for(DeviceFormat format: device.getDeviceDataFormats()){
			if(format.getDeviceTagType() == DeviceTagType.DEVICE){
				if(!StringUtils.isEmpty(format.getTagValue()))
					currentGroup = getCurrentGroup(format.getTagValue());
				
				currentGroup.add(format);
			}
		}
		
	}

	private DeviceFormatGroup getCurrentGroup(String key) {
		if(!deviceFormatGroupMap.containsKey(key)){
			deviceFormatGroupMap.put(key, new DeviceFormatGroup(key, getLogger()));
		}
			
		return deviceFormatGroupMap.get(key);
	}

	public boolean containsBlockAddress(String address) {
		return deviceFormatGroupMap.keySet().contains(address);
	}
	
	public boolean isUnsolicitedDevice() {
		return checkDescription(UNSOLICITED_DEVICE);
	}

	public void setData(byte[] plcData) {
		
		processData(plcData);
		
		dispatchPlcData();
	}
	
	public DataContainer setData(DevicePlcData plcData) {
		List<PlcDataBlock> dataList = plcData.getDataList();
		for(PlcDataBlock dataBlock : dataList){
			getDeviceFormatGroup(dataBlock.getAddress()).setData(dataBlock.getData());
		}
		
		return dispatchPlcData();
	}
	
	public Object getData(DevicePlcData plcData) {
		List<PlcDataBlock> dataList = plcData.getDataList();
		for(PlcDataBlock dataBlock : dataList){
			getDeviceFormatGroup(dataBlock.getAddress()).setData(dataBlock.getData());
		}
		
		if(!StringUtils.isEmpty(device.getAliasName())){
			return deviceData.convertPlcData(getReceivedData());
		} else
			return getReceivedData();

	}
	
	public DataContainer setDataChangedOnly(DevicePlcData devicePlcData) {
		List<PlcDataBlock> dataList = devicePlcData.getDataList();
		boolean changedFound = false;
		for(PlcDataBlock dataBlock : dataList){
			changedFound = (getDeviceFormatGroup(dataBlock.getAddress()).setDataChangedOnly(dataBlock.getData()) || changedFound);
		}
		
		if(changedFound) return dispatchPlcData();
		
		return null;
		
		
	}

	private void processData(byte[] data) {
		DeviceFormatGroup deviceFormatGroup = deviceFormatGroupMap.get(getDevice().getDeviceDataFormats().get(0).getTagValue());
		deviceFormatGroup.setData(data); 
	}
	
	
	public DataContainer dispatchPlcData() {
		// IF listeners registered then process data as per request by listener
		// IF there is no listeners then process data as device specified
		DataContainer returnDc = null;
		if(listeners.size() > 0){
			returnDc = notifyListeners();
		} else {
			returnDc = sentToClient();
		}

		getLogger().info("dispatch plc data to client completed. return dc:" + returnDc);
		
		return returnDc;
		
	}
	
	private DataContainer notifyListeners() {
		//I would expected ONLY one listener for this data
		for(DeviceListener listener : listeners){
			return notifyListener(listener);
		}
		
		return null;
	}

	private DataContainer notifyListener(DeviceListener listener) {
		try {
			if (isNotifyChangedItemOnly(listener)) {
				notifyChangedItems(listener);
				return new DefaultDataContainer();

			} else {

				IDeviceData convert = deviceData.convertByTag(getReceivedData());
				IDeviceData reply = listener.received(device.getClientId(),	convert);
				if(reply == null) return null;
				return deviceData.convert(reply);
			}
		} catch (Exception e) {
			getLogger().error(e, "Error: notification failed for:" + deviceData.getDeviceId());
			 
		}
		return null;
	}
	
	@SuppressWarnings({"unchecked" })
	public IDeviceData getDeviceAliasObject() {
		try {
			
			if(StringUtils.isBlank(device.getAliasName())) return null;
			
			String devicdDataClassName = DEVICE_DATA_PACKAGE + "." + device.getAliasName();
			Class devicdDataClass = Class.forName(devicdDataClassName);
			Constructor constructor = devicdDataClass.getConstructor(new Class[]{});
			return (IDeviceData)constructor.newInstance(new Object[]{});
			
		} catch (Exception e) {
			getLogger().error(e, "Exception to get device alias object.");
		}
		
		return null;
	}

	private DataContainer getReceivedData() {
		DataContainer dc = new DefaultDataContainer();
		for(DeviceFormatGroup group : deviceFormatGroupMap.values()){
			dc.putAll(group.getDataMap());
		}
	
		return dc;
	}

	private void notifyChangedItems(DeviceListener listener) {
		for(DeviceFormatGroup group : deviceFormatGroupMap.values()){
			group.notifyChangedItems(listener);
		}
	}

	private DataContainer sentToClient() {
		
		if(!device.isAddressDefined()){
			getLogger().warn("Error: device address is not defined, data is not dispatched! Device:" + getDevice().getClientId());
			return new DefaultDataContainer();
		}
		
		if(isNotifyChangedItemOnly(null)){
			notifyChangedItems(device.getEifIpAddress(), device.getEifPort());
			return new DefaultDataContainer();
		}else {
			DataContainer dc = getReceivedData();
			dc.setClientID(getDevice().getClientId());
			return getInvoker().syncSend(device.getEifIpAddress(), device.getEifPort(), dc);
		}
	}

	

	private void notifyChangedItems(String address, int port) {
		for(DeviceFormatGroup group : deviceFormatGroupMap.values()){
			group.notifyChangedItems(address, port);
		}
	}

	public boolean isNotifyChangedOnly() {
		return checkDescription(Operation.NOTIFYCHANGEDONLY.toString());

	}

	private boolean isNotifyChangedItemOnly(DeviceListener listener) {
		boolean changedItemOnly = false;
		if(listener != null && listener instanceof IDeviceDataListener){
			changedItemOnly = ((IDeviceDataListener)listener).isNotifyChangedItemOnly();
		}
		
		return changedItemOnly || checkDescription(Operation.NOTIFYCHANGEDITEMONLY.toString());
		
	}

	public boolean checkDescription(String keyword) {
		String description = device.getDeviceDescription().replaceAll("_", "");
		description = description.replaceAll(" ", "");
		description = description.replaceAll("-", "");
		return description.toUpperCase().contains(keyword);
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public byte[] getData() {
		return data;
	}
	
	public DeviceData getDeviceData() {
		return deviceData;
	}

	public void registerListener(DeviceListener listener){
		listeners.add(listener);
	}

	public Map<String, DeviceFormatGroup> getDeviceFormatGroupMap() {
		return deviceFormatGroupMap;
	}

	public boolean isDataReadyDevice() {
		return checkDescription(DATA_READY);
	}

	
	
	public DataContainer decodeData(DevicePlcData plcData) {
		DataContainer result = new DefaultDataContainer();
		List<PlcDataBlock> dataList = plcData.getDataList();
		for(PlcDataBlock dataBlock : dataList){
			Map<String, Object> decodeData = getDeviceFormatGroup(dataBlock.getAddress()).decodeData(dataBlock.getData());
			result.putAll(decodeData);
		}
		
		return result;
	}
	
	public boolean isReadByGroup(){
		return checkDescription(READ_BY_GROUP);
	}

	private DeviceFormatGroup getDeviceFormatGroup(String address) {
		for(DeviceFormatGroup group : deviceFormatGroupMap.values()){
			if(group.getId().trim().equals(address))
				return group;
		}
		getLogger().error("Error: Failed to find device format for ", address);
		return null;
	}

	public List<DeviceFormat> getAllDeviceFormat() {
		List<DeviceFormat> list  = new ArrayList<DeviceFormat>();
		for(DeviceFormat format : getDevice().getDeviceDataFormats()){
			if(format.getDeviceTagType() == DeviceTagType.DEVICE ){
				list.add(format);
			}
		}
		
		return list;
	}
	
	public DataContainer convert(IDeviceData data){
		return deviceData.convert(data);
	}
	
	public Logger getLogger(){
		return logger;
	}

	public NotificationInvoker getInvoker() {
		if(invoker == null)
			invoker = new NotificationInvoker(getLogger());
		return invoker;
	}


}
