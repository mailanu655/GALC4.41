package com.honda.galc.device;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * Regional-HMIN Merged Version 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */

public class DeviceDataConverter extends PersistentCache{
	
	
	private String processPointId;
	
	private static DeviceDataConverter deviceDataConverter;
	
	public static DeviceDataConverter getInstance() {
		return deviceDataConverter;
	}
	
	public static DeviceDataConverter createInstance(String ppId) {
		deviceDataConverter = new DeviceDataConverter(ppId);
		return deviceDataConverter;
	}
	
	public DeviceDataConverter(String ppId) {
		super();
		this.processPointId = ppId;
		
		loadDevices();
	}
	
	public IDeviceData convert(DataContainer dc) {
		
		for(DeviceData deviceData : getDeviceDataList()){
			
			if(deviceData.getDeviceId().trim().equalsIgnoreCase(dc.getClientID().trim())) 
				return deviceData.convert(dc);
			
		}
		
		return null;
		
	}
	
	/**
	 * convert IDeviceData data to DataContainer data
	 * The original request is from client app to device
	 * @param clientId
	 * @param deviceData
	 * @return
	 */
	public DataContainer convert(String clientId,IDeviceData deviceData) {
		
		
		for(DeviceData item : getDeviceDataList()) {
			if(item.getDeviceId().trim().equals(clientId.trim())) return item.convert(deviceData);
		}
		
		return null;
	}
	
	/**
	 * convert a reply IDeviceData data to DataContainer data
	 * The original request is from device to client app
	 * @param clientId
	 * @param deviceData
	 * @return
	 */
	public DataContainer convertReply(String clientId, IDeviceData deviceData)  {
		for(DeviceData item: getDeviceDataList()) {
			if(item.getDeviceId().trim().equals(clientId.trim())) return item.convertReply(deviceData);
		}
		return null;
	}
	
	public DeviceData getDeviceDataObject(IDeviceData deviceDataFormat) {
		List<DeviceData> matchedDeviceDataList = new ArrayList<DeviceData>();
		for(DeviceData item : getDeviceDataList()) {
			if(item.canConvert(deviceDataFormat)) matchedDeviceDataList.add(item);
		}
		if(matchedDeviceDataList.size() == 1) return matchedDeviceDataList.get(0);
		else if(matchedDeviceDataList.size() > 1) {
			for(DeviceData item : matchedDeviceDataList) {
				if(item.isOutput()) return item;
			}
		}
		return null;
	}
	
	
	public void registerDeviceData(List<IDeviceData> deviceDataList) {
		
		for(IDeviceData deviceData : deviceDataList) {
			registerDeviceData(deviceData);
		}
		
	}
	
public void registerOutputDeviceData(List<IDeviceData> deviceDataList) {
		for(IDeviceData deviceData : deviceDataList) {
			registerOutputDeviceData(deviceData);
		}
	}
	
	public void registerDeviceData(IDeviceData... deviceDataList) {
		for(IDeviceData deviceData : deviceDataList) {
			registerDeviceData(deviceData);
		}
	}
	
	public boolean containOutputDeviceData(Class<? extends IDeviceData>deviceDataClass) {
		for(DeviceData item:getDeviceDataList()) {
			if(item.isMapped(deviceDataClass)) return true;
		}
		return false;
	}
	
	public boolean containInputDeviceData(Class<? extends IDeviceData> deviceDataClass) {
		for(DeviceData item:getDeviceDataList()) {
			if(item.isMappedToTagName(deviceDataClass)) return true;
		}
		return false;
	}
	
	private void loadDevices() {
		
		if(!ServiceFactory.isServerAvailable()) return;
		
		List<DeviceData> deviceDataList = new ArrayList<DeviceData>();
		if(!StringUtils.isEmpty(processPointId)) {
			DeviceDao dao = ServiceFactory.getDao(DeviceDao.class);
			try{
				List<Device> devices = dao.findAllByProcessPointId(processPointId);
				
				for(Device device : devices) {
					Logger.getLogger().debug("Load device:" + device.getClientId());
					deviceDataList.add(new DeviceData(device));
				}
			}catch(Exception e) {
				System.out.println("Cannot load device data. Server is not available! Using cache data!");
				return;
			}
		}
		put("DeviceData",deviceDataList);
	}
	
	public String getReplyClientId(String clientId) {
		for(DeviceData item : getDeviceDataList()) {
			if(item.getDeviceId().equalsIgnoreCase(clientId)) return item.getDevice().getReplyClientId();
		}
		return null;
	}
	
	public boolean hasDeviceData() {
		return !getDeviceDataList().isEmpty();
	}
	
	public List<DeviceData> getDeviceDataList(){
		return getList("DeviceData",DeviceData.class);
	}
	
	private void registerDeviceData(IDeviceData deviceData) {
	
		for(DeviceData item : getDeviceDataList()) {
			if(!item.isRegistered())
				item.register(deviceData);
		}
	}
	
	private void registerOutputDeviceData(IDeviceData deviceData) {
		for(DeviceData item : getDeviceDataList()) {
			if(item.isMapped(deviceData.getClass())){
				item.register(deviceData);
				item.setOutput(true);
			}
		}
	}
	
	public boolean isConfigured(Class<? extends IDeviceData>deviceDataClass){
		for(DeviceData item:getDeviceDataList()) {
			if(item.getDeviceData() != null && 
			   item.getDeviceData().getClass() == deviceDataClass) return true;
		}
		return false;
	}
	
}
