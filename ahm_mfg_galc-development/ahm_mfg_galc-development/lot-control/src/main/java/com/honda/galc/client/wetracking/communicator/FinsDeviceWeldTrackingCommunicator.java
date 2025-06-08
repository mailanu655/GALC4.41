package com.honda.galc.client.wetracking.communicator;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.control.headless.PlcDataReadyMonitorFactory;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.IPlcSocketDevice;
import com.honda.galc.client.device.plc.omron.AggregateMemoryWriter;
import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.client.events.wetracking.PlcDeviceWeldTrackingResponse;
import com.honda.galc.client.events.wetracking.WeldTrackingRequest;
import com.honda.galc.client.events.wetracking.WeldTrackingResponse;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.memorymap.MemoryMapCache;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class FinsDeviceWeldTrackingCommunicator implements WeldTrackingDeviceCommunicator {
	public static final String SEPERATOR = ".";

	private DeviceFormatDao deviceFormatDao;
	private Application _application;
	private DeviceListener _listener;

	private boolean active;
	private long interval;

	public void start(Application application, DeviceListener listener) {
		_application = application;
		_listener = listener;
		createDataReadyMonitors(StringUtils.trimToEmpty(application.getApplicationId()));
	}

	public WeldTrackingResponse convertToResponse(DataContainer dc,
			WeldTrackingRequest request) {
		return new PlcDeviceWeldTrackingResponse(dc, request);
	}

	/**
	 * Creates the data ready monitors to keep watch on PLC device status.
	 *
	 * @param applicationId the application id
	 */
	private void createDataReadyMonitors(String applicationId) {
		PlcDataReadyMonitorFactory drFactory = new PlcDataReadyMonitorFactory(applicationId);
		drFactory.createMonitors(_listener);
	}

	/**
	 * Write data to PLC memory.
	 *
	 * @param deviceData the device data
	 * @param dataFields the data fields
	 * @return true, if successful/no exception
	 */
	private boolean writeDataToPlc(IPlcDeviceData deviceData, Collection<PlcDataField> dataFields) {
		try {
			writeTags(deviceData, dataFields, DeviceTagType.PLC_WRITE);
			writeTags(deviceData, dataFields, DeviceTagType.PLC_GALC_DATA_READY);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Write tags which match the rquired type.
	 *
	 * @param drEvent the dr event
	 * @param dataFields the data fields
	 * @param tagType the tag type
	 */
	private void writeTags(IPlcDeviceData drEvent, Collection<PlcDataField> dataFields, DeviceTagType tagType) {
		AggregateMemoryWriter aggWriter = new AggregateMemoryWriter(getApplicationId()); //applicationId used by logger
		Iterator<PlcDataField> iterator = dataFields.iterator();
		while (iterator.hasNext()) {
			PlcDataField field = iterator.next();
			//filter fields only match the require type
			if (field.getDeviceTagType().equals(tagType)) { 
				String key = getMemoryMapItemKey(drEvent, field);
				if (MemoryMapCache.getMap(getApplicationId()).containsKey(key)) {
					setPlcMemory(field, key);
					aggWriter.addItem(field, drEvent.getPlcDeviceId());
				} else {
					getLogger().debug("Key: " + key + " not found");
				}
			}
		}
		aggWriter.flush(drEvent.getPlcDeviceId());
	}

	/**
	 * Sets the plc memory to PlcDataField.
	 *
	 * @param field the field
	 * @param key the key
	 */
	private void setPlcMemory(PlcDataField field, String key) {
		PlcMemoryMapItem item = MemoryMapCache.getMap(getApplicationId()).get(key);
		String dataLoc = (item.getBitIndex() > -1) 
				? Integer.toString(item.getBitIndex()) 
				: item.getByteOrder().trim();
		field.setPlcMemory(new PlcMemory(item.getMemoryBank().trim()
				+ SEPERATOR + item.getStartAddress().trim() + SEPERATOR
				+ dataLoc));
		field.setValue(StringUtil.padRight(field.getValue(), item.getLength(),
				' ', true));
	}

	/**
	 * Gets the memory map item key. Format: appId.deviceId.item
	 *
	 * @param drEvent the dr event
	 * @param field the field
	 * @return the memory map item key
	 */
	private String getMemoryMapItemKey(IPlcDeviceData drEvent, PlcDataField field) {
		return getApplicationId() + SEPERATOR + drEvent.getPlcDeviceId().trim()
				+ SEPERATOR + field.getId().trim();
	}

	public void stop() {
		for (IPlcSocketDevice device : getPlcSocketDevice()) {
			device.deActivate();
		}
		setActive(false);
	}

	public boolean processResponse(WeldTrackingResponse response) {
		if (!(response instanceof PlcDeviceWeldTrackingResponse)) {
			getLogger().debug("Illegal response object. The response should be instance of PlcDeviceWeldTrackingResponse.");
			return false;
		}
		PlcDeviceWeldTrackingResponse plcDeviceResponse = (PlcDeviceWeldTrackingResponse)response;
		List<DeviceFormat> deviceFormats = getDeviceFormatDao().findAllByDeviceId(getApplicationId());
		Collection<PlcDataField> dataFields = plcDeviceResponse.getDataFields(deviceFormats);
		WeldTrackingRequest request = response.getRequest();
		if (request instanceof IPlcDeviceData) {
			writeDataToPlc((IPlcDeviceData) request, dataFields);
		} else {
			return false;
		}
		return true;
	}

	public DeviceFormatDao getDeviceFormatDao() {
		if (deviceFormatDao == null)
			deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return deviceFormatDao;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * Gets the plc socket devices base on DB property configuration.
	 * @return the plc socket devices
	 */
	public List<IPlcSocketDevice> getPlcSocketDevice() {
		List<IPlcSocketDevice> devices = new ArrayList<IPlcSocketDevice>();
		List<ComponentProperty> properties = PropertyService.getProperties(getApplicationId(), "device.plc.deviceId\\d*");
		for(ComponentProperty property : properties) {
			IPlcSocketDevice device = (IPlcSocketDevice) DeviceManager.getInstance().getDevice(property.getPropertyValue());
			devices.add(device);
		}
		return devices;
	}

	private String getApplicationId() {
		return _application.getApplicationId();
	}

}
