/**
 * 
 */
package com.honda.galc.client.device.plc.omron;

import com.honda.galc.client.device.plc.IPlcDataField;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Nov 20, 2012
 */
public class PlcDataField implements IPlcDataField {
	
	public static final String PAD_LEFT = "left";
	public static final String PAD_RIGHT = "right";
	
	private String _id;
	
	private StringBuilder _value;

	private PlcMemory _plcMemory;
	
	private boolean _isPad;

	private String _padChar;
	
	private DeviceDataType _deviceDataType;
	
	private DeviceTagType _deviceTagType;
	
	public PlcDataField (String id, StringBuilder val) {
		this(id, val, DeviceTagType.PLC_WRITE);
	}
	
	public PlcDataField (String id, StringBuilder val, DeviceDataType dataType) {
		this(id, val, DeviceTagType.PLC_WRITE, dataType);
	}

	public PlcDataField (String id, StringBuilder val, DeviceTagType tagType) {
		_id = id;
		_value = val;
		_deviceTagType = tagType;
	}
	
	public PlcDataField (String id, StringBuilder val, DeviceTagType tagType, DeviceDataType dataType) {
		_id = id;
		_value = val;
		_deviceTagType = tagType;
		_deviceDataType = dataType;
	}

	public void setId(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	public void setValue(StringBuilder val) {
		_value = val;
	}
	
	public StringBuilder getValue() {
		return _value;
	}
	
	public void setPlcMemory(PlcMemory plcMemory) {
		_plcMemory = plcMemory;
	}

	public PlcMemory getPlcMemory() {
		return _plcMemory;
	}
	
	public void setIsPad(boolean isPad) {
		_isPad = isPad;
	}

	public boolean isPad() {
		return _isPad;
	}
	
	public void setPadChar(String padChar) {
		_padChar = padChar;
	}

	public String getPadChar() {
		if (_padChar == null || _padChar.trim().equals(""))
			_padChar = " ";
		return _padChar;
	}
	
	public void setDeviceDataType(DeviceDataType deviceDataType) {
		_deviceDataType = deviceDataType;
	}

	public DeviceDataType getDeviceDataType() {
		return _deviceDataType;
	}
	
	public void setDeviceTagType(DeviceTagType deviceTagType) {
		_deviceTagType = deviceTagType;
	}

	public DeviceTagType getDeviceTagType() {
		return _deviceTagType;
	}
	
	@Override
	public String toString() {
		return _id + ": " + StringUtil.replaceNonPrintableCharacters(_value) + ": " + _plcMemory.toString();
	}
}
