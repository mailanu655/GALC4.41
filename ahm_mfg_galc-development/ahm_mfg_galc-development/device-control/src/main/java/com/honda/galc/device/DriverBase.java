package com.honda.galc.device;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.DeviceFormat;


/**
 * 
 * <h3>DriverBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DriverBase description </p>
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
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public abstract class DriverBase extends AbstractDevice{
	private volatile Exception exception;
	protected byte[] input;
	private boolean waitRead;
	protected InetAddress inetAddress;
	
	public abstract void send(byte[] output);
	
	public byte[] sendAndWait(byte[] output) throws Exception{

		try {
			synchronized (this) {
				input = null;
				getLogger().info("sent:", DeviceUtil.toHex(output));
				send(output);
				this.waitRead = true;
				this.wait();
				if (this.exception != null)
					throw this.exception;
				if (input == null) {
					getLogger().warn("exception input is null.");
					throw new Exception("Input is null.");
				}
			}
			getLogger().info("recived " + input.length, ":", DeviceUtil.toHex(input));
			return input;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("TODO");
		}
	}
	
	public void notifySendAndWait(){
		synchronized (this) {
			if(this.waitRead){
				this.notify();
				this.waitRead = false;
			}
		}
	}
	
	public Object getDeviceFormatDataFromContainer(
			DataContainer dataContainer, DeviceFormat format) {
		
		Object object = dataContainer.get(format.getTag());
		return format.convert(object);
	}

	
	protected Object getValue(DeviceFormat format, IDeviceData data) {
		String methodName = getMethodName(format, data);
		try {
			Method method = data.getClass().getMethod(methodName, new Class[]{});
			Object obj = method.invoke(data, new Object[] {});
			return obj;
		} catch (Exception e) {
			getLogger().error(e, "Exception on get device data value.");
		} 
		
		return null;
		
	}

	private String getMethodName(DeviceFormat format, IDeviceData data) {
		String fieldName = getAnnotatedFieldName(format, data);
		if(fieldName != null){
			fieldName = fieldName.startsWith("_") ? fieldName.substring(1):fieldName;
			return "get" + StringUtils.capitalize(fieldName);
		}

		StringBuilder sb = new StringBuilder();

		String[] tokens = format.getTag().split("_");
		for(int i = 0; i < tokens.length; i++ ){
			String token = tokens[i].toLowerCase();
			sb.append(StringUtils.capitalize(token));
		}

		return "get" + sb.toString();
	}
	

	private String getAnnotatedFieldName(DeviceFormat format, IDeviceData data) {
		for(Field field : data.getClass().getDeclaredFields()){
			if(isStatic(field)) continue;
			Tag tag = field.getAnnotation(Tag.class);
			if(!StringUtils.isEmpty(tag.name()) && format.getTag().equals(tag.name()))
				return field.getName();
				
		}
		return null;
	}

	private boolean isStatic(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

}
