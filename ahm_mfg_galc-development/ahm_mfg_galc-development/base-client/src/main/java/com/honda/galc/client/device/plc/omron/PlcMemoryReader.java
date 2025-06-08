/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import static com.honda.galc.common.logging.Logger.getLogger;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.ByteOrder;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.memory.IPlcMemoryReader;
import com.honda.galc.dao.conf.PlcMemoryMapItemDao;
import com.honda.galc.data.memorymap.MemoryMapCache;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Jan 7, 2013
 */
public class PlcMemoryReader implements IPlcMemoryReader {
	
	protected IPlcSocketDevice _plcDevice;
	protected String _plcDeviceId;
	protected String _applicationId;

	private PlcMemoryMapItemDao _plcMemoryMapItemDao;
	
	public PlcMemoryReader(String applicationId, String plcDeviceId) {
		setApplicationId(applicationId);
		setPlcDeviceId(plcDeviceId);
	}
	
	public StringBuilder readPlcMemory(String memoryMapItemName) {
		PlcMemoryMapItem item = MemoryMapCache.getMap(getApplicationId()).get(getApplicationId() + "." + memoryMapItemName);
		if (item != null) {
			return readPlcMemory(item);
		}
		return null;
	}
	
    private StringBuilder readPlcMemory(PlcMemoryMapItem memItem) {
    	PlcMemory mem = new PlcMemory(memItem.getMemoryBank().trim() + "." + memItem.getStartAddress().trim());
    	mem.setNumberOfRegisters((memItem.getLength() + 1)/2);
		setByteOrder(memItem, mem);
    	StringBuilder val = getPlcDevice().readMemory(mem);
    	return new StringBuilder(val.subSequence(0, memItem.getLength()));
    }

	private void setByteOrder(PlcMemoryMapItem memItem, PlcMemory mem) {
		if (memItem.getByteOrder().trim().equalsIgnoreCase("H"))
			mem.setByteOrder(ByteOrder.high);
		else if (memItem.getByteOrder().trim().equalsIgnoreCase("L"))
			mem.setByteOrder(ByteOrder.low);
	}

	private IPlcSocketDevice getPlcDevice() {
		if (_plcDevice == null) {
			_plcDevice = (IPlcSocketDevice) DeviceManager.getInstance().getDevice(getPlcDeviceId());
			if (!_plcDevice.isInitialized()) {
				_plcDevice.activate();
			}
			getLogger().info("Found device " + _plcDevice.getId() + ": " +  _plcDevice.getHostName() + ":" + _plcDevice.getPort());
		}
		return _plcDevice;
	}

	public PlcMemoryMapItemDao getPlcMemoryMapItemDao() {
		if(_plcMemoryMapItemDao == null)
			_plcMemoryMapItemDao = ServiceFactory.getDao(PlcMemoryMapItemDao.class);
		return _plcMemoryMapItemDao;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;		
	}
	
	public String getPlcDeviceId() {
		return _plcDeviceId;
	}

	public void setPlcDeviceId(String plcDeviceId) {
		_plcDeviceId = plcDeviceId;
	}
}
