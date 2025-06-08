/**
 * 
 */
package com.honda.galc.client.device;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @date Dec 14, 2012
 */
public class DeviceTagAttributeProvider extends DeviceTagSqlBaseProvider {

	@Override
	public boolean populateFields(PlcDataCollectionBean bean, String deviceId) {
		super.populateFields(bean, deviceId);
		try {
			SortedArrayList<DeviceFormat> deviceFormats = new SortedArrayList<DeviceFormat>("getSequenceNumber");
			deviceFormats.addAll(getDeviceFormatDao().findAllByTagType(bean.getApplicationId(), deviceId, DeviceTagType.PLC_READ_ATTRIBUTE));
			for(DeviceFormat deviceFormat: deviceFormats){
				String sql = deviceFormat.getTagValue();
				if ((deviceFormat.getOffset() == 1) 
						&& (sql != null) 
						&& !sql.trim().equals("")) {
					sql = replaceParams(bean, deviceFormat.getTagValue());
					String qryResult = executeSql(sql);
					StringBuilder value = convertBitArray(deviceFormat, qryResult);
					String fieldName = deviceFormat.getTag().trim().split("\\.")[2];
					addAttribToSubstitutionList(bean, fieldName, value);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void addAttribToSubstitutionList(PlcDataCollectionBean bean, String fieldName, StringBuilder value) {
		bean.getSubstitutionList().put(fieldName, value);
	}

	@Override
	public IPlcDeviceData populateFields(IPlcDeviceData deviceData) {
		// TODO Auto-generated method stub
		return null;
	}
}

