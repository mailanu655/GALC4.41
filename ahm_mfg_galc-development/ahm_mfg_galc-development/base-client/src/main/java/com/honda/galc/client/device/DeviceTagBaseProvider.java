/**
 * 
 */
package com.honda.galc.client.device;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Jan 7, 2013
 */
public abstract class DeviceTagBaseProvider implements IDeviceTagDataProvider {
	
	private DeviceFormatDao _deviceFormatDao;
	
	public abstract boolean populateFields(PlcDataCollectionBean bean, String deviceId);
	public abstract IPlcDeviceData populateFields(IPlcDeviceData deviceData);
		
	public DeviceFormatDao getDeviceFormatDao() {
		if(_deviceFormatDao == null)
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return _deviceFormatDao;
	}
}
