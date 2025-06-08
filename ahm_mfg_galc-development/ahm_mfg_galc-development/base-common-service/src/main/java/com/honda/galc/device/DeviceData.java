package com.honda.galc.device;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.dataformat.Acknowledgment;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.DeviceType;

/**
 * 
 * <h3>DeviceData</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DeviceData description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 11, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 11, 2011
 */

public class DeviceData implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String ReplyDataReady="REPLY_DATA_READY";

	Device device;
	
	IDeviceData deviceData;
	IDeviceData replyDeviceData;
	
	private boolean isOutput = false;
	
	
	public DeviceData(Device device){
		this.device = device;
	}
	
	public String getDeviceId() {
		return device.getClientId().trim();
	}
	
	public String getIoProcessPointId() {
		return device.getIoProcessPointId();
	}

	public Device getDevice() {
		return device;
	}
	
	public boolean isOutput() {
		return isOutput;
	}

	public void setOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}

	public boolean register(IDeviceData deviceData) {
		
		if(deviceData == null) return false;
		
		if(!isMappedToTagName(deviceData.getClass())
				&& !isMappedToTagValue(deviceData.getClass())) return false;
		
		this.deviceData = deviceData;
		Logger.getLogger().debug("register device:" + this.getDeviceId());
		return true;
			
	}
	
	public boolean isMappedToTagValue(Class<? extends IDeviceData> deviceDataClass) {
		for (DeviceFormat deviceFormat : device.getDeviceDataFormats()) {
			if(!isNameMapped(deviceFormat.getTagValue().trim(),deviceDataClass)) return false;
		}
		return true;
	}
	
	public boolean isMappedToTagName(Class<? extends IDeviceData> deviceDataClass) {
		for (DeviceFormat deviceFormat : device.getDeviceDataFormats()) {
			if(!isExemptionTag(deviceFormat) && 
					!isNameMapped(deviceFormat.getTag().trim(),deviceDataClass)) return false;
		}
		return true;
	}

	private boolean isExemptionTag(DeviceFormat deviceFormat) {
		return deviceFormat.getDeviceTagType() == DeviceTagType.NONE || 
		   ReplyDataReady.equals(deviceFormat.getTag());
		       
	}
	
	private boolean isNameMapped(String name, Class<? extends IDeviceData> deviceDataClass) {
		
		for(Field field: deviceDataClass.getDeclaredFields()) {
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null && isDataOptional(field)) continue;
			if(dataFormatName != null && dataFormatName.equalsIgnoreCase(name)) return true;
		}	
		
		return false;
	}
	
	private boolean isDataOptional(Field field) {
		
		Tag tag = field.getAnnotation(Tag.class);
		if(tag != null) return tag.optional(); 
		else return false;
	}
	
	/**
	 * convert dataContainer data from Device to a registered device data. since data is initiated from Device, 
	 * tag name of deviceDataFormat is used to map the data item
	 * @param dc
	 * @return
	 */
	public IDeviceData convert(DataContainer dc) {
		return convert(dc, device.isOPCType() || device.isDeviceWiseType());
	}

	public IDeviceData convertByTag(DataContainer dc) {
		return convert(dc, false);
	}
	
	private IDeviceData convert(DataContainer dc, boolean convertByTagValue) {
		for(Field field: deviceData.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			DeviceFormat deviceFormat = getDeviceDataFormatFromTagName(dataFormatName);
			if(deviceFormat == null) return null;
			else {
				Object value = convertByTagValue ? dc.get(deviceFormat.getTagValue().trim()) :
					dc.get(deviceFormat.getTag().trim());
				if(value == null) return null;
				assignValue(field,value);
			}
		}
		
		if(deviceData instanceof InputData)
			((InputData) deviceData).setInputData(true);
		
		return deviceData;
	}
	

	public Object convertPlcData(DataContainer plcata) {
		for(Field field: deviceData.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			DeviceFormat deviceFormat = getDeviceDataFormatFromTagName(dataFormatName);
			if(deviceFormat == null) return null;
			else {
				Object value = plcata.get(deviceFormat.getTag());
				if(value == null) return null;
				assignValue(field,value);
			}
		}
		
		return deviceData;
	}
	
	
	
	private IDeviceData basicConvertReply(DataContainer dc) {
		
		for(Field field: replyDeviceData.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			DeviceFormat deviceFormat = getReplyDeviceDataFormatFromTagName(dataFormatName);
			if(deviceFormat == null) return null;
			else {
				String value = (String)dc.get(deviceFormat.getTagValue().trim());
				if(value == null) return null;
				assignValue(replyDeviceData,field,value);
			}
		}
		
		return deviceData;
	}
	
	/**
	 * convert a reply dataContainer data from Device to a registered device data. 
	 * DataContainer dc contains data which is a reply from a device to a request initiated from client application.
	 * tag value of deviceDataFormat is used to map the data item
	 * @param dc
	 * @return
	 */
	public IDeviceData convertReply(DataContainer dc) {
		
		return (replyDeviceData == null)? 
				convertDefaultReply(dc) : basicConvertReply(dc);
	
	}
	/**
	 * convert a reply IDeviceData data from client Applcation to a DataContainer data. 
	 * The original request is from Device to client app. So tag value of deviceDataFormat is used to map the data item
	 * @param dc
	 * @return
	 */
	public DataContainer convertReply(IDeviceData deviceData) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(DeviceDataConverter.getInstance().getReplyClientId(getDeviceId()));
		for(Field field: deviceData.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			DeviceFormat deviceFormat = getReplyDeviceDataFormat(dataFormatName);
			if(deviceFormat == null) return null;
			String value = getValue(field,deviceData);
			if(value == null) return null;
			dc.put(deviceFormat.getTag().trim(), value);
		}
		return dc;
	}
	
	public IDeviceData convertDefaultReply(DataContainer dc) {
		IDeviceData tmp = new Acknowledgment();
		for(Field field: tmp.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			if(!dc.containsKey(dataFormatName) && !isDataOptional(field)) return null; 
			String value = (String)dc.get(dataFormatName);
			assignValue(tmp,field,value);
		}
		
		return tmp;
			
	}
	
	
	/**
	 * convert IDeviceData data  to a DataContainer. Since data is initiated from client applcation, 
	 * tag value of deviceDataFormat is used to map the data item
	 * @param dc
	 * @return
	 */
	public DataContainer convert(IDeviceData deviceData) {
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(getDeviceId());
		for(Field field: deviceData.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			DeviceFormat deviceFormat = getDeviceDataFormatFromTagValue(dataFormatName);
			if (deviceFormat == null) {
				if (isDataOptional(field)) {
					continue;
				} else {
					return null;
				}
			} 
			String value = getValue(field,deviceData);
			if(value == null) return null;
			dc.put(deviceFormat.getTag(), value);
		}
		dc.put(DataContainerTag.TAG_LIST, device.getTagNameList());
		dc.put(DataContainerTag.PROCESS_POINT_ID, getIoProcessPointId());
		return dc;
	}
	
	public boolean isStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	public boolean canConvert(IDeviceData deviceData) {
		return this.deviceData != null && deviceData != null && 
		         this.deviceData.getClass() == deviceData.getClass();
	}
	
	private String getDataFormatName(Field field) {
		
		Tag tag = field.getAnnotation(Tag.class);
		if(tag != null) {
			if( StringUtils.isEmpty(tag.name())) return null;
			else return tag.name();
		}else return this.getDataFormatName(field.getName());
		
	}
	
	/**
	 * Helper method to recreate device data format name based on method name
	 * 
	 * @param methodName - method name
	 * @return device data format name
	 */
	private String getDataFormatName(String fieldName) {
			
		
		// getAaaBbbCcc -> AAA_BBB_CCC
		StringBuilder res = new StringBuilder();
		int ix = 0;
		boolean lastIsLowerCase = false;
		
		
		// In the loop
		for (int i = ix; i < fieldName.length(); i++) {
			char letter = fieldName.charAt(i);
			if(Character.isLowerCase(letter)) {
				//    if source letter is not capital
				//          convert to capital and add to result
				res.append(Character.toUpperCase(letter));
				lastIsLowerCase = true;
			} else {
				//    if source letter is capital (and not the 1st)
				//          add "_" and source letter	
				if(lastIsLowerCase) {
					res.append("_");
				}
				res.append(letter);
			}
		}
		
		return res.toString();
	}
	
	
	private DeviceFormat getDeviceDataFormatFromTagName(String dataFormatName) {
		for (DeviceFormat deviceFormat : device.getDeviceDataFormats()) {
			if(dataFormatName.equalsIgnoreCase(deviceFormat.getTag().trim())) return deviceFormat;
		}
		return null;
	}
	
	private DeviceFormat getReplyDeviceDataFormatFromTagName(String dataFormatName) {
		for (DeviceFormat deviceFormat : device.getReplyDeviceDataFormats()) {
			if(dataFormatName.equalsIgnoreCase(deviceFormat.getTag().trim())) return deviceFormat;
		}
		return null;
	}
	
	private DeviceFormat getDeviceDataFormatFromTagValue(String dataFormatName) {
		for (DeviceFormat deviceFormat : device.getDeviceDataFormats()) {
			String writingTag = (deviceFormat.getDeviceTagType() == DeviceTagType.DEVICE)? 
					deviceFormat.getTag() : deviceFormat.getTagValue();
					
			if(!StringUtils.isEmpty(writingTag))		
					if(dataFormatName.equalsIgnoreCase(writingTag.trim())) return deviceFormat;

		}
		return null;
	}
	
	
	public DeviceFormat getReplyDeviceDataFormat(String dataFormatName) {
		for (DeviceFormat deviceFormat : device.getReplyDeviceDataFormats()) {
			if(dataFormatName.equalsIgnoreCase(device.getReplyTag(deviceFormat))) return deviceFormat;
		}
		return null;
	}
	
	private void assignValue(IDeviceData deviceData, Field field, Object value) {

		String methodName = "set" + this.getMethodName(field);
		
		try {
			Method method = deviceData.getClass().getMethod(methodName, new Class[]{field.getType()});
			Object obj = convert(value,field);
			method.invoke(deviceData, new Object[] {obj});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	private void assignValue(Field field, Object value) {
		
		assignValue(deviceData,field,value);
		
	}

	
	private String getValue(Field field, IDeviceData deviceFormatData) {

		String methodName = "get" + this.getMethodName(field);
		
		try {
			Method method = deviceFormatData.getClass().getMethod(methodName, new Class[]{});
			Object obj = method.invoke(deviceFormatData, new Object[] {});
			if(obj == null) return null;
			else return obj.toString();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private String getMethodName(Field field) {
		
		String fieldName = field.getName();
		if(fieldName.substring(0,1).equals("_")) fieldName = fieldName.substring(1);
		return fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
	}
	
	@SuppressWarnings("unchecked")
	private Object convert(Object value,Field field) {
		if(value == null) return null;
		Class type = field.getType();
		if(type == String.class) return (value instanceof String)? value : value.toString();
		if(type == int.class) return (value instanceof Integer) ? value : Integer.parseInt(value.toString().trim());
		if(type == boolean.class) return (value instanceof Boolean)? value : convertToBoolean(value.toString().trim());
		if(type == double.class) 	return (value instanceof Double)? value : Double.parseDouble(value.toString().trim());
		if(type == short.class) return (value instanceof Short) ? value : Short.parseShort(value.toString().trim());
		if(type == float.class) return (value instanceof Float) ? value : Float.parseFloat(value.toString().trim());
		
		return null;
		
	}

	private boolean convertToBoolean(String value){
		return DeviceUtil.convertToBoolean(value);
	}
	
	public boolean isRegistered(){
		return deviceData != null;
	}

	public List<DeviceFormat> convertToDeviceFormats(IDeviceData deviceData) {
		List<DeviceFormat> list = new ArrayList<DeviceFormat>();
		for(Field field: deviceData.getClass().getDeclaredFields()) {
			if(isStatic(field)) continue;
			String dataFormatName = getDataFormatName(field);
			if(dataFormatName == null) continue;
			DeviceFormat deviceFormat = getDeviceDataFormatFromTagValue(dataFormatName);
			if(deviceFormat == null) return null;
			String value = getValue(field,deviceData);
			if(value == null) return null;
			list.add(deviceFormat);
		}
		
		return list;
	}
	
	/**
	 * Check if the device data class is properly mapped to device data formats 
	 * @param deviceDataClass
	 * @return
	 */
	public boolean isMapped(Class<? extends IDeviceData> deviceDataClass) {

		return (device.getDeviceType() == DeviceType.EQUIPMENT)
			? isMappedToTagName(deviceDataClass) : isMappedToTagValue(deviceDataClass);
	}
	
	public IDeviceData getDeviceData() {
		return deviceData;
	}
	
	
}
