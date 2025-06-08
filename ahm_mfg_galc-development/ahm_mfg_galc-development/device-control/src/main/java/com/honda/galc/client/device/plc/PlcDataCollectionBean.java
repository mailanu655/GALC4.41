/**
 * 
 */
package com.honda.galc.client.device.plc;

import java.util.concurrent.ConcurrentHashMap;

import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Nov 21, 2012
 */
public class PlcDataCollectionBean {
	
	private String _productId = "";
	private String _productSpecCode = "";
	
	protected ConcurrentHashMap<String, PlcDataField> _plcDataFields = new ConcurrentHashMap<String, PlcDataField>();
	protected ConcurrentHashMap<String, StringBuilder> _substitutionList = new ConcurrentHashMap<String, StringBuilder>();
	protected String _applicationId = "";
	protected String _terminalId = "";
	
	public PlcDataCollectionBean() {
	}
	
	public void put(String id, StringBuilder val) {
		PlcDataField dataField = null;
		if (getPlcDataFields().containsKey(id)){
			dataField = getPlcDataFields().get(id);
			if (dataField.getDeviceDataType()==DeviceDataType.INTEGER)
				 dataField.setValue(new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(val.toString().trim())),16,'0'))));
			else
				dataField.setValue(val);
		} else {
			
			getPlcDataFields().put(id, new PlcDataField(id, val));
		}
	}
	
	public void put(String id, StringBuilder val, DeviceDataType dataType) {
		PlcDataField dataField = null;
		if (getPlcDataFields().containsKey(id)){
			dataField = getPlcDataFields().get(id);
			if (dataField.getDeviceDataType()==DeviceDataType.INTEGER)
				 dataField.setValue(new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(val.toString().trim())),16,'0'))));
			else
				dataField.setValue(val);
		} else {
			if (dataType==DeviceDataType.INTEGER)
				getPlcDataFields().put(id, new PlcDataField(id,(new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(val.toString().trim())),16,'0')))), dataType));
			else
				getPlcDataFields().put(id, new PlcDataField(id, val, dataType));
			
		}
	}
	
	public void put(String id, StringBuilder val, DeviceTagType tagType) {
		PlcDataField dataField = null;
		if (getPlcDataFields().containsKey(id)){
			dataField = getPlcDataFields().get(id);
			if (dataField.getDeviceDataType()==DeviceDataType.INTEGER)
				 dataField.setValue(new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(val.toString().trim())),16,'0'))));
			else
				dataField.setValue(val);
			dataField.setDeviceTagType(tagType);
		} else {
			getPlcDataFields().put(id, new PlcDataField(id, val, tagType));
		}
	}
	
	public void put(String id, StringBuilder val, DeviceTagType tagType, DeviceDataType dataType) {
		PlcDataField dataField = null;
		if (getPlcDataFields().containsKey(id)){
			dataField = getPlcDataFields().get(id);
			if (dataField.getDeviceDataType()==DeviceDataType.INTEGER)
				 dataField.setValue(new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(val.toString().trim())),16,'0'))));
			else
				dataField.setValue(val);
			dataField.setDeviceTagType(tagType);
		} else {
			if (dataType==DeviceDataType.INTEGER)
				getPlcDataFields().put(id, new PlcDataField(id,(new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(val.toString().trim())),16,'0')))),tagType, dataType));
			else
				getPlcDataFields().put(id, new PlcDataField(id, val, tagType, dataType));
		}
	}

	public void remove(String id) {
		if (getPlcDataFields().containsKey(id))
			getPlcDataFields().remove(id);
	}

	public void setPlcDataFields(ConcurrentHashMap<String, PlcDataField> plcDataFields) {
		_plcDataFields = plcDataFields;
	}

	public ConcurrentHashMap<String, PlcDataField> getPlcDataFields() {
		return _plcDataFields;
	}
	
	public void setSubstitutionList(ConcurrentHashMap<String, StringBuilder> substitutionList) {
		_substitutionList = substitutionList;
	}

	public ConcurrentHashMap<String, StringBuilder> getSubstitutionList() {
		return _substitutionList;
	}
	
	public String getApplicationId() {
		return _applicationId;
	}
	
	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}
	
	public String getTerminalId() {
		return _terminalId;
	}
	
	public void setTerminalId(String terminalId) {
		_terminalId = terminalId;
	}

	public void setProductId(String productId) {
		_productId = productId;
	}

	public String getProductId() {
		return _productId;
	}

	public void setProductSpecCode(String productSpecCode) {
		_productSpecCode = productSpecCode;
	}

	public String getProductSpecCode() {
		return _productSpecCode;
	}
}
