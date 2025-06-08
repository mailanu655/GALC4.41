package com.honda.galc.client.datacollection.control.headless;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.PlcDataReadyEventMonitor;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.data.memorymap.MemoryMapCache;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.ServiceFactory;

public class PlcDataReadyMonitorFactory {

	private DeviceFormatDao _deviceFormatDao;
	private String _applicationId = "";

	public PlcDataReadyMonitorFactory(String applicationId) {
		_applicationId = StringUtils.trimToEmpty(applicationId);
	}
	
	/**
	 * creates plc DataReady monitors for the provided application id
	 * 
	 * @param observer
	 * @return
	 */
	public ArrayList<PlcDataReadyEventMonitor> createMonitors(DeviceListener deviceListener) {
		ArrayList<PlcDataReadyEventMonitor> drMonitors = new ArrayList<PlcDataReadyEventMonitor>();
		// create all configured data ready monitors for this application
		List<DeviceFormat> dataReadyItems = getDeviceFormatDao().findAllByTagType(getApplicationId(), DeviceTagType.PLC_EQ_DATA_READY);
		for (DeviceFormat deviceFormat: dataReadyItems) {
			String monitorId = getApplicationId() + "." + StringUtils.trimToEmpty(deviceFormat.getTag());
			ConcurrentHashMap<String, PlcMemoryMapItem> memItems = MemoryMapCache.getMap(getApplicationId());
			if (memItems.containsKey(monitorId)) {
				PlcMemoryMapItem item = memItems.get(monitorId);
				PlcDataReadyEventMonitor thisMonitor = create(deviceListener, deviceFormat.getTagValue().trim(), monitorId, item);
				thisMonitor.setApplicationId(getApplicationId());
				thisMonitor.setDataType(deviceFormat.getDeviceDataType());
				drMonitors.add(thisMonitor);
			}
		}
		return drMonitors;
	}

	public PlcDataReadyEventMonitor create(DeviceListener deviceListener, String eventClass, String monitorName, PlcMemoryMapItem memoryMapItem) {
		PlcMemory memory = null;
		try {
			memory = PlcMemory.createPlcMemory(memoryMapItem);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unable to convert PlcMemoryMapItem to PlcMemory: " + ex.getMessage());
			return null;
		}
		
		return create(deviceListener, eventClass, monitorName, memory);
	}
	
	public PlcDataReadyEventMonitor create(DeviceListener deviceListener, String eventClass, String monitorName, PlcMemory memory) {
		PlcDataReadyEventMonitor dataReadyMonitor = null;
		try {
			String[] tokens = monitorName.split("\\.");
			IPlcSocketDevice plcSocketDevice = (IPlcSocketDevice) DeviceManager.getInstance().getDevice(tokens[1]);
			if (!plcSocketDevice.isActive())
				plcSocketDevice.activate();
			
			dataReadyMonitor = new PlcDataReadyEventMonitor(plcSocketDevice, memory, eventClass);
			dataReadyMonitor.setName(monitorName);
			dataReadyMonitor.registerListener(deviceListener);
			dataReadyMonitor.setLogTraffic(false); // TODO make this a property
			dataReadyMonitor.activate();
			new Thread(dataReadyMonitor).start();
		
			getLogger().info("Created data ready monitor: " + monitorName);
		} catch(Exception ex) {
			getLogger().error("Unable to create data ready monitor: " + monitorName);
			getLogger().error(ex.getMessage());	
			return null;
		}
		return dataReadyMonitor;
	}
	
	public DeviceFormatDao getDeviceFormatDao() {
		if(_deviceFormatDao == null)
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return _deviceFormatDao;
	}
	
	public String getApplicationId() {
		return _applicationId;
	}
}