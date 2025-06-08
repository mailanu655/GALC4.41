package com.honda.galc.client.events.wetracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.util.StringUtil;

/**
 * 
 * @author Gangadhararao Gadde
 * @date June 15, 2016
 */

public class PlcDeviceWeldTrackingResponse implements
		WeldTrackingResponse {

	private WeldTrackingRequest request;
	private DataContainer dc;

	public PlcDeviceWeldTrackingResponse(DataContainer dc, WeldTrackingRequest request) {
		this.dc = dc;
		this.request = request;
	}

	public Collection<PlcDataField> getDataFields(List<DeviceFormat> deviceFormats) {
		Collection<PlcDataField> dataFields = new ArrayList<PlcDataField>();
		for (DeviceFormat deviceFormat : deviceFormats) {
			String tag = deviceFormat.getTag();
			if (tag != null && tag.indexOf('.') > 0) {
				String clientId = tag.trim().split("\\.")[0];
				String tagName = tag.trim().split("\\.")[1];
				if(clientId.equals(dc.getClientID())) {
					dataFields.add(createDataField(tagName, deviceFormat));
				}
			}
		}
		return dataFields;
	}

	private PlcDataField createDataField(String id, DeviceFormat deviceFormat) {
		PlcDataField deviceData = new PlcDataField(id,
				getFieldValue(id, deviceFormat),
				deviceFormat.getDeviceTagType(),
				deviceFormat.getDeviceDataType());
		return deviceData;
	}

	private StringBuilder getFieldValue(String id, DeviceFormat deviceFormat) {
		StringBuilder value;
		switch(deviceFormat.getDeviceTagType()) {
		case PLC_EQ_DATA_READY : 
			value = new StringBuilder(deviceFormat.getTagValue());
			break;
		case PLC_GALC_DATA_READY:
			value = new StringBuilder(deviceFormat.getTagValue());
			break;
		default:
			value = new StringBuilder(dc.get(id)==null? "": dc.get(id).toString());
			if (deviceFormat.getDeviceDataType() == DeviceDataType.INTEGER)
				 value = new StringBuilder(StringUtil.bitArrayToChars(StringUtil.padLeft(Integer.toBinaryString(Integer.valueOf(value.toString().trim())),16,'0')));
		}
		return value;
	}

	@Override
	public void setRequest(WeldTrackingRequest request) {
		this.request = request;
	}

	@Override
	public WeldTrackingRequest getRequest() {
		return request;
	}

}
