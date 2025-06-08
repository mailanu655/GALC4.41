/**
 * 
 */
package com.honda.galc.client.headless;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Feb 22, 2013
 */
public class DataReadyEventProcessorCache {
	
	private static DeviceFormatDao _deviceFormatDao;
	private volatile static ConcurrentHashMap<String, ConcurrentHashMap<String, Class<IPlcDataReadyEventProcessor<IDeviceData>>>> _cache = 
		new ConcurrentHashMap<String, ConcurrentHashMap<String, Class<IPlcDataReadyEventProcessor<IDeviceData>>>>(); 

	public static ConcurrentHashMap<String, Class<IPlcDataReadyEventProcessor<IDeviceData>>> getMap(String applicationId) {
		if (!getCache().containsKey(applicationId)) {
			addNewMap(applicationId);
		}
		return getCache().get(applicationId);
	}
	
	/**
	 * creates a map of dataready event processors for the provided application
	 */
	private static void addNewMap(String applicationId) {
		ConcurrentHashMap<String, Class<IPlcDataReadyEventProcessor<IDeviceData>>> drEventProcessors = 
				new ConcurrentHashMap<String, Class<IPlcDataReadyEventProcessor<IDeviceData>>>();
		if (!getCache().containsKey(applicationId)) {
			List<DeviceFormat> dataReadyItems = getDeviceFormatDao().findAllByTagType(applicationId, DeviceTagType.PLC_EQ_DATA_READY);
			for (DeviceFormat deviceFormat: dataReadyItems) {
				String monitorId = applicationId + "." + StringUtils.trimToEmpty(deviceFormat.getTag());
				if (!drEventProcessors.containsKey(monitorId)) {
					drEventProcessors.put(monitorId, getEventProcessorClass(StringUtils.trimToEmpty(deviceFormat.getTagName())));
				}
			}
			getCache().put(applicationId, drEventProcessors);
		}
	}
		 
	@SuppressWarnings("unchecked")
	private static Class<IPlcDataReadyEventProcessor<IDeviceData>> getEventProcessorClass(String className) {
		Class<IPlcDataReadyEventProcessor<IDeviceData>> eventProcessorClass = null;
		try {
			eventProcessorClass =  (Class<IPlcDataReadyEventProcessor<IDeviceData>>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return eventProcessorClass;
	}
    
	public static DeviceFormatDao getDeviceFormatDao() {
		if(_deviceFormatDao == null)
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return _deviceFormatDao;
	}
	
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, Class<IPlcDataReadyEventProcessor<IDeviceData>>>> getCache() {
		return _cache;
	}
}
