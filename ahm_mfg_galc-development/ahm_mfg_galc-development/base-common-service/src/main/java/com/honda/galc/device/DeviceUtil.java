package com.honda.galc.device;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;

/**
 * 
 * <h3>DeviceUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceUtil description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Nov 5, 2010
 *
 */

public class DeviceUtil {
	public static final String DEVICE_DATA_PACKAGE="com.honda.galc.device.dataformat";

	
	public static int indexOfFirstDigit(String device) {
		int index = -1;
		
		for(int i = 0; i < device.length(); i++)
			if(Character.isDigit(device.charAt(i)))
			{
				index = i;
				break;
			}

		return index;
	}
	
	public static IDeviceData covert(DataContainer dc, Device device){
		DeviceData deviceData = new DeviceData(device);
		deviceData.register(getDeviceAliasObject(device));
		return deviceData.convertByTag(dc);
		
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public static IDeviceData getDeviceAliasObject(Device device) {
		try {
			
			DeviceData deviceData = new DeviceData(device);
			String devicdDataClassName = DEVICE_DATA_PACKAGE + "." + device.getAliasName();
			Class devicdDataClass = Class.forName(devicdDataClassName);
			Constructor constructor = devicdDataClass.getConstructor(new Class[]{});
			return (IDeviceData)constructor.newInstance(new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
			//TODO log error
		}
		return null;
	}
	
	public static boolean convertToBoolean(String value){
		if(value.equals("OK") ||
				value.equals("1") ||
				value.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}
	
	public static DevicePoint converToDevicePoint(DeviceFormat deviceFormat,
			Object value) {
		if(deviceFormat.getDeviceDataType() == DeviceDataType.BOOLEAN){
			return new DevicePointBoolean(deviceFormat.getId().getClientId(),
					deviceFormat.getTag(), value);
		} else if(deviceFormat.getDeviceDataType() == DeviceDataType.FLOAT){
			return new DevicePointFloat(deviceFormat.getId().getClientId(),
					deviceFormat.getTag(), value);
		} else if(deviceFormat.getDeviceDataType() == DeviceDataType.STRING){
			return new DevicePointString(deviceFormat.getId().getClientId(),
					deviceFormat.getTag(), value);
		} else if(deviceFormat.getDeviceDataType() == DeviceDataType.INTEGER){
			return  new DevicePointInteger(deviceFormat.getId().getClientId(),
					deviceFormat.getTag(), value);
		} else if(deviceFormat.getDeviceDataType() == DeviceDataType.SHORT){
			return new DevicePointShort(deviceFormat.getId().getClientId(),
					deviceFormat.getTag(), value);
		}
		return null;
	}

	public static int getPoints(DeviceFormat deviceFormat) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static String toHex(byte[] source)
	{
		return toHex(source, new StringBuffer(source.length * 3), false);
	}
	

	public static String toHex(byte[] source, StringBuffer sb, boolean includeSpace)
	{
		sb.setLength(0);

		for (int i = 0; i < source.length; i++)
		{
			int ch = source[i];

			int left = (ch & 0xF0) >> 4;
		int right = ch & 0xF;

		if (left <= 9)
			left += 48;
		else {
			left += 55;
		}
		if (right <= 9)
			right += 48;
		else {
			right += 55;
		}
		sb.append((char)left);
		sb.append((char)right);

		if (includeSpace) {
			sb.append(' ');
		}
		}
		return sb.toString();
	}

	/**
	 * return a list of device data formats with value of OK or true
	 * @param device
	 * @return
	 */
	public static List<DeviceFormat> getOks(Device device) {
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		if(device != null && device.getDeviceDataFormats() != null){
			for(DeviceFormat format: device.getDeviceDataFormats()){
				if(format.isOk()) list.add(format);
			}
		}
		return list;
	}
	

}
