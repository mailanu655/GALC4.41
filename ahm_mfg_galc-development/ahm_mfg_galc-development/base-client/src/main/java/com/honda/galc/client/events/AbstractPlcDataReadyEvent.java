/**
 * 
 */
package com.honda.galc.client.events;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.IPlcEventGenerator;
import com.honda.galc.client.device.plc.PlcDataReadyEventMonitor;
import com.honda.galc.client.device.plc.memory.IPlcMemoryReader;
import com.honda.galc.client.device.plc.memory.PlcMemoryReaderFactory;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Nov 19, 2012
 */
public class AbstractPlcDataReadyEvent implements IPlcDeviceData {

	private DeviceFormatDao _deviceFormatDao;
	
	protected Date _eventTimeStamp = new Date();
	protected PlcDataReadyEventMonitor _monitor = null;
	protected String _plcDeviceId;
	protected String _applicationId;

	protected IPlcMemoryReader _plcMemReader;
	protected ConcurrentHashMap<String, Double> _measurements = new ConcurrentHashMap<String, Double>();
	protected ConcurrentHashMap<String, PlcBoolean> _judgements = new ConcurrentHashMap<String, PlcBoolean>();
	protected ConcurrentHashMap<String, StringBuilder> _attributes = new ConcurrentHashMap<String, StringBuilder>();
	
	public AbstractPlcDataReadyEvent() {}
	
	public AbstractPlcDataReadyEvent(String applicationId, String plcDeviceId) {
		_applicationId = applicationId;
		_plcDeviceId = plcDeviceId;
		_plcMemReader = PlcMemoryReaderFactory.getReader(applicationId, plcDeviceId);
	}
	
	public void collectDeviceData(String plcDeviceId) {
		// read all PLC_READ DeviceTagType fields from plc and invoke corresponding setters in the model
		setInputData(plcDeviceId);

		// read all PLC_READ_ATTRIBUTE DeviceTagType fields from plc and add them to attribute list
		setAttributes(plcDeviceId);
		
		// read all PLC_READ_MEASUREMENT DeviceTagType fields from plc and add them to measurement list
		setMeasurements(plcDeviceId);
		
		// read all PLC_READ_JUDGEMENT DeviceTagType fields from plc and add them to judgement list
		setJudgements(plcDeviceId);
	}

	private void setInputData(String plcDeviceId) {
		setBeanProperties(plcDeviceId, DeviceTagType.PLC_READ);
	}
	
	private void setMeasurements(String plcDeviceId) {
		setBeanProperties(plcDeviceId, DeviceTagType.PLC_READ_MEASUREMENT);
	}
	
	private void setJudgements(String plcDeviceId) {
		setBeanProperties(plcDeviceId, DeviceTagType.PLC_READ_JUDGEMENT);
	}

	private void setAttributes(String plcDeviceId) {
		setBeanProperties(plcDeviceId, DeviceTagType.PLC_READ_ATTRIBUTE);
		setBeanProperties(plcDeviceId, DeviceTagType.ATTR_BY_MTOC);
	}
	
	private void setBeanProperties(String plcDeviceId, DeviceTagType tagType) {
		List<DeviceFormat> deviceFormats = getDeviceFormatDao().findAllByTagType(getApplicationId(), plcDeviceId, tagType);
		for (DeviceFormat deviceFormat: deviceFormats) {
			String val = null;
			StringBuilder strbVal = getMemoryReader().readPlcMemory(deviceFormat.getTag().trim());
			
			// TODO refactor this section
			if (deviceFormat.getDeviceDataType()==DeviceDataType.INTEGER){
				val = StringUtil.stringToBitArray(strbVal);
				val = String.valueOf(Integer.parseInt(val.toString(),2)); 
			}else{ 
				val = strbVal.toString().trim();
			}
			invokeSetter(tagType, deviceFormat, val, strbVal);
		}
	}

	private void invokeSetter(DeviceTagType tagType, DeviceFormat deviceFormat,	String val, StringBuilder strbVal) {
		try {
			Class<?> dataType = deviceFormat.getDeviceDataType().getClazz();
			switch(tagType) {
			case PLC_READ_MEASUREMENT:
				setMeasurement(getName(deviceFormat), new Double(val));
				getLogger().info("Setting Measurement " + getName(deviceFormat) + " to: " + strbVal);
				break;
				
			case PLC_READ_JUDGEMENT:
				setJudgement(getName(deviceFormat), new PlcBoolean(val));
				getLogger().info("Setting Judgement " + getName(deviceFormat) + " to: " + strbVal);
				break;
				
			case PLC_READ_ATTRIBUTE:
				setAttribute(getName(deviceFormat), strbVal);
				getLogger().info("Setting Attribute " + getName(deviceFormat) + " to: " + strbVal);
				break;
				
			case ATTR_BY_MTOC:
				setAttribute(getName(deviceFormat, 1), strbVal);
				getLogger().info("Setting Attribute " + getName(deviceFormat, 1) + " to: " + strbVal);
				break;
				
			default:
				ReflectionUtils.invoke(this, getSetterName(deviceFormat), createParam(val, dataType));
				getLogger().info("Setting bean property " + this.getClass().getSimpleName() + "." + getSetterName(deviceFormat) + " to: " + strbVal);
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error invoking " + getSetterName(deviceFormat));
		}
	}

	private String getName(DeviceFormat deviceFormat) {
		return getName(deviceFormat, 2);
	}

	private String getName(DeviceFormat deviceFormat, int index) {
		return deviceFormat.getTag().trim().split("\\.")[index];
	}

	private String getSetterName(DeviceFormat deviceFormat) {
		return StringUtil.setterName(getName(deviceFormat, 1));		// discard the plc device name
	}
	
	public Object createParam(String input, Class<?> targetClass) throws Exception {
		if (input == null)
			return null;
			  
		return targetClass.getConstructor(String.class).newInstance(input);
	}
	
	public Date getEventTimeStamp() {
		return _eventTimeStamp;
	}

	public String getPlcDeviceId() {
		return _plcDeviceId;
	}

	public void setPlcDeviceId(String plcDeviceId) {
		_plcDeviceId = plcDeviceId;
	}
	
	public String getApplicationId() {
		return _applicationId;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;		
	}

	public DeviceFormatDao getDeviceFormatDao() {
		if(_deviceFormatDao == null)
			_deviceFormatDao = ServiceFactory.getDao(DeviceFormatDao.class);
		return _deviceFormatDao;
	}

	public IPlcEventGenerator getEventGenerator() {
		return _monitor;
	}

	public void setEventGenerator(IPlcEventGenerator eventGenerator) {
		_monitor = (PlcDataReadyEventMonitor) eventGenerator;		
	}

	public ArrayList<KeyValue<String, Double>> getMeasurements() {
		ArrayList<KeyValue<String, Double>> measurementsList = new ArrayList<KeyValue<String, Double>>();
		Enumeration<String> e = _measurements.keys();
		while(e.hasMoreElements()) {
			String key = e.nextElement();
			measurementsList.add(new KeyValue<String, Double>(key, _measurements.get(key)));
		}
		return measurementsList;
	}
	
	public ArrayList<KeyValue<String, PlcBoolean>> getJudgements() {
		ArrayList<KeyValue<String, PlcBoolean>> judgementsList = new ArrayList<KeyValue<String, PlcBoolean>>();
		Enumeration<String> e = _judgements.keys();
		while(e.hasMoreElements()) {
			String key = e.nextElement();
			judgementsList.add(new KeyValue<String, PlcBoolean>(key, _judgements.get(key)));
		}
		return judgementsList;
	}
	
	public ArrayList<KeyValue<String, StringBuilder>> getAttributes() {
		ArrayList<KeyValue<String, StringBuilder>> attributesList = new ArrayList<KeyValue<String, StringBuilder>>();
		Enumeration<String> e = _attributes.keys();
		while(e.hasMoreElements()) {
			String key = e.nextElement();
			attributesList.add(new KeyValue<String, StringBuilder>(key, _attributes.get(key)));
		}
		return attributesList;
	}

	public IPlcMemoryReader getMemoryReader() {
		return _plcMemReader;
	}

	public void setMeasurement(String measurementName, Double measurementVal) {
		_measurements.put(measurementName, measurementVal);
	}

	public void setJudgement(String judgementName, PlcBoolean judgement) {
		_judgements.put(judgementName, judgement);
	}
	
	public void setAttribute(String attributeName, StringBuilder value) {
		_attributes.put(attributeName, value);
	}

	public StringBuilder getAttribute(String attributeName) {
		return _attributes.get(attributeName);
	}

	public PlcBoolean getJudgement(String judgementName) {
		return _judgements.get(judgementName);
	}

	public Double getMeasurement(String measurementName) {
		return _measurements.get(measurementName);
	}
}
