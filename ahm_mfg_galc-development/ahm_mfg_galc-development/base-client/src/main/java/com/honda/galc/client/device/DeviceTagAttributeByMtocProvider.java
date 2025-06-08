package com.honda.galc.client.device;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.plc.IPlcDeviceData;
import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan, Steven Ware
 * @date Aug 18, 2016
 */
public class DeviceTagAttributeByMtocProvider extends DeviceTagBaseProvider {

	@Override
	public boolean populateFields(PlcDataCollectionBean bean, String deviceId) {
		try {
			SortedArrayList<DeviceFormat> deviceFormats = new SortedArrayList<DeviceFormat>("getSequenceNumber");
			deviceFormats.addAll(getDeviceFormatDao().findAllByTagType(bean.getApplicationId(), deviceId, DeviceTagType.ATTR_BY_MTOC));
			BuildAttributeDao buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
			for (DeviceFormat deviceFormat: deviceFormats) {
				String attribute = deviceFormat.getTagValue();
				BuildAttribute buildAttribute = buildAttributeDao.findById(attribute, bean.getProductSpecCode());
				if (buildAttribute != null && !StringUtils.isEmpty(buildAttribute.getAttributeValue())) {
					String fieldName = deviceFormat.getTag().trim().split("\\.")[1];
					StringBuilder value = new StringBuilder(buildAttribute.getAttributeValue());
					bean.put(fieldName, value);
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

