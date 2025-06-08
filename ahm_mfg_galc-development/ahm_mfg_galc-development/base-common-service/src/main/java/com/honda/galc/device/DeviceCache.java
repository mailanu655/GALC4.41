package com.honda.galc.device;

import java.util.HashSet;
import java.util.Set;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>DeviceService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceService description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 4, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 4, 2011
 */

public class DeviceCache extends PersistentCache{
	public static DeviceCache instance;
	
	private static Set<String> devices = new HashSet<String>();

	private DeviceCache() {
		super();
	}

	public DeviceCache(String cacheName) {
		super(cacheName);
	}

	public static DeviceCache getInstance() {

		if(instance == null ) instance = new DeviceCache();
		return instance;
	}
	
	public static Device getDevice(String deviceId) {
	
		try {
			deviceId = deviceId.trim();
			if (devices.contains(deviceId))
				return getInstance().get(deviceId, Device.class);
			else {
				
				return loadDevice(deviceId);
			}
		} catch (Throwable e) {
			Logger.getLogger().error(e, "Exception to get device.");
		}
		return null;
			
	}

	private static Device loadDevice(String deviceId) {
		if(ServiceFactory.isServerAvailable()){
		    	DeviceDao dao = ServiceFactory.getDao(DeviceDao.class);
				Device device = dao.findByKey(deviceId);
				devices.add(deviceId);
				getInstance().put(deviceId, device);
				
				return device;
		}
		
		Logger.getLogger().warn("Service is not available.");
		
		return null;
		
	}
	
	public static void refreshComponentProperties(String deviceId) {
		
		if(!devices.contains(deviceId)) return;
		loadDevice(deviceId);
		
	}
	
	
}
