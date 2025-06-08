/**
 * 
 */
package com.honda.galc.client.device;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.device.DeviceData;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Nov 27, 2012
 */
public class DeviceTagSqlProvider extends DeviceTagSqlBaseProvider {
	
	@Override
	public boolean populateFields(PlcDataCollectionBean bean, String deviceId) {
		super.populateFields(bean, deviceId);
		try {
			SortedArrayList<DeviceFormat> deviceFormats = new SortedArrayList<DeviceFormat>("getSequenceNumber");
			deviceFormats.addAll(getDeviceFormatDao().findAllByTagType(bean.getApplicationId(), deviceId, DeviceTagType.SQL));
			for(DeviceFormat deviceFormat: deviceFormats){
				String sql = deviceFormat.getTagValue();
				if (sql != null && !sql.trim().equals("")) {
					sql = replaceParams(bean, deviceFormat.getTagValue());
					String qryResult = executeSql(sql);
					StringBuilder value = convertBitArray(deviceFormat, qryResult);
					String tagName = deviceFormat.getTagName() == null ? "" : deviceFormat.getTagName().trim();
					if (!tagName.equals("")) {
						bean.getSubstitutionList().put(deviceFormat.getTagName().trim(), value);
					}

					String fieldName = deviceFormat.getTag().trim().split("\\.")[1];
					addFieldToWrite(bean, fieldName, value);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public IPlcDeviceData populateFields(IPlcDeviceData deviceData) {
		// TODO Auto-generated method stub
		return null;
	}
}
