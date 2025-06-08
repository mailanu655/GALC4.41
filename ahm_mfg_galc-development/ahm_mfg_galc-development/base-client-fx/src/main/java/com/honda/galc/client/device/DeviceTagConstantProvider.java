/**
 * 
 */
package com.honda.galc.client.device;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 7, 2013
 */
public class DeviceTagConstantProvider extends DeviceTagBaseProvider {

	public IPlcDeviceData populateFields(IPlcDeviceData deviceData) {
		try{
			SortedArrayList<DeviceFormat> deviceFormats = new SortedArrayList<DeviceFormat>("getSequenceNumber");
			deviceFormats.addAll(getDeviceFormatDao().findAllByTagType(deviceData.getApplicationId(), deviceData.getPlcDeviceId(), DeviceTagType.PLC_READ_CONSTANT));
			for(DeviceFormat deviceFormat: deviceFormats){
				String value = deviceFormat.getTagValue();
				String setterName = StringUtil.setterName(deviceFormat.getTag().trim().split("\\.")[1]);
				Class<?> dataType = deviceFormat.getDeviceDataType().getClazz();
				ReflectionUtils.invoke(deviceData, setterName, createParam(value, dataType));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return deviceData;
	}

	@Override
	public boolean populateFields(PlcDataCollectionBean bean, String deviceId) {
		return false;
	}
		
	public Object createParam(String input, Class<?> targetClass) throws Exception {
	   if (input == null)
		   return null;
		  
	   return targetClass.getConstructor(String.class).newInstance(input);
	}
}
