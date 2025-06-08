package com.honda.galc.client.headless;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.device.DeviceTagAttributeByMtocProvider;
import com.honda.galc.client.device.DeviceTagAttributeProvider;
import com.honda.galc.client.device.DeviceTagConstantProvider;
import com.honda.galc.client.device.DeviceTagSqlProvider;
import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.client.device.plc.omron.AggregateMemoryWriter;
import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.client.device.plc.omron.PlcMemory;
import com.honda.galc.data.memorymap.MemoryMapCache;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.conf.PlcMemoryMapItem;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.util.StringUtil;

public class PlcDataCollectionController implements DeviceListener {

	public static final String SEPERATOR = ".";

	private String _applicationId;
	private ApplicationContext _appContext;
	private PlcDataCollectionBean _bean = new PlcDataCollectionBean();

	private volatile static ConcurrentHashMap<String, PlcDataCollectionController> _instances = new ConcurrentHashMap<String, PlcDataCollectionController>();

	private PlcDataCollectionController(String applicationId) {
		_applicationId = applicationId;
	}

	public static PlcDataCollectionController getInstance(String applicationId) {
		if(!_instances.containsKey(applicationId)){
			_instances.put(applicationId, new PlcDataCollectionController(applicationId));
		}
		return _instances.get(applicationId);
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData data) {
		if (!(data instanceof IPlcDeviceData)) {
			return null;
		}

		IPlcDeviceData deviceData = (IPlcDeviceData) data;
		Class<?> processorClass = DataReadyEventProcessorCache.getMap(getApplicationId()).get(deviceData.getEventGenerator().getName());

		try {
			IPlcDataReadyEventProcessor<IDeviceData> processor = getDataCollectionProcessor(deviceData, processorClass);
			getBean().setApplicationId(getApplicationId());
			getBean().setTerminalId(getApplicationContext().getTerminalId());

			populateStaticFields(deviceData);
			processor.execute(deviceData);

			populateSqlFields(deviceData);
			// get ATTR_BY_MTOC fields
			populateAttributeFields(deviceData);
			processor.validate();

			boolean writeSuccess = writeDataToPlc(deviceData);
			processor.postPlcWrite(writeSuccess);

			getLogger().debug("Processor " + processorClass.getCanonicalName() + " successfully invoked");
		} catch (Exception ex){
			ex.printStackTrace();
			getLogger().debug("Unable to handle data ready event: " + processorClass.getCanonicalName());
		}
		return null;
	}

	private void populateStaticFields(IPlcDeviceData deviceData) {
		// populate IPlcDeviceData with constant values - DeviceTagType.PLC_READ_CONSTANT
		new DeviceTagConstantProvider().populateFields(deviceData);
	}

	private void populateSqlFields(IPlcDeviceData deviceData) {
		// collect data for tag type - DeviceTagType.SQL
		new DeviceTagSqlProvider().populateFields(getBean(), deviceData.getPlcDeviceId());
		// collect data for tag type - DeviceTagType.PLC_READ_ATTRIBUTE
		new DeviceTagAttributeProvider().populateFields(getBean(), deviceData.getPlcDeviceId());
	}

	private void populateAttributeFields(IPlcDeviceData deviceData) {
		// populate IPlcDeviceData with constant values - DeviceTagType.ATTR_BY_MTOC
		new DeviceTagAttributeByMtocProvider().populateFields(getBean(), deviceData.getPlcDeviceId());
	}

	private boolean writeDataToPlc(IPlcDeviceData deviceData) {
		// TODO include PLC_WRITE_ERRCODE, PLC_WRITE_ERRDESC
		try {
			writeTags(deviceData, getBean(), DeviceTagType.PLC_WRITE);
			writeTags(deviceData, getBean(), DeviceTagType.PLC_EQ_DATA_READY);
			writeTags(deviceData, getBean(), DeviceTagType.PLC_GALC_DATA_READY);
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private IPlcDataReadyEventProcessor<IDeviceData> getDataCollectionProcessor(IPlcDeviceData deviceData, Class<?> processorClass) 
			throws IllegalAccessException,	InstantiationException {

		IPlcDataReadyEventProcessor<IDeviceData> processor = (IPlcDataReadyEventProcessor<IDeviceData>) processorClass.newInstance();
		processor.setPlcDeviceId(deviceData.getPlcDeviceId().trim());
		processor.setApplicationId(getApplicationId().trim());
		return processor;
	}

	private void writeTags(IPlcDeviceData drEvent, PlcDataCollectionBean bean, DeviceTagType tagType) {
		Enumeration<PlcDataField> e = (Enumeration<PlcDataField>) bean.getPlcDataFields().elements();
		AggregateMemoryWriter aggWriter = new AggregateMemoryWriter(bean);
		while (e.hasMoreElements()) {
			PlcDataField field = e.nextElement();
			if (field.getDeviceTagType().equals(tagType)) {		// write only fields that match tag type
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

	private void setPlcMemory(PlcDataField field, String key) {
		PlcMemoryMapItem item = MemoryMapCache.getMap(getApplicationId()).get(key);
		String dataLoc = (item.getBitIndex() > -1) ? Integer.toString(item.getBitIndex()) : item.getByteOrder().trim();
		field.setPlcMemory(new PlcMemory(item.getMemoryBank().trim() + SEPERATOR 
				+ item.getStartAddress().trim()
				+ SEPERATOR
				+ dataLoc));
		field.setValue(StringUtil.padRight(field.getValue(), item.getLength(), ' ', true));
	}

	private String getMemoryMapItemKey(IPlcDeviceData drEvent, PlcDataField field) {
		return getApplicationId() + SEPERATOR + drEvent.getPlcDeviceId().trim() + SEPERATOR + field.getId().trim();
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}

	public ApplicationContext getApplicationContext() {
		return _appContext;
	}

	public void setApplicationContext(ApplicationContext appContext) {
		_appContext = appContext;
	}

	public void setBean(PlcDataCollectionBean bean) {
		_bean = bean;
	}

	public PlcDataCollectionBean getBean() {
		return _bean;
	}
}