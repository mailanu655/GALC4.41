package com.honda.galc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.honda.galc.common.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * @author Subu Kathiresan
 * @date Feb 13, 2015
 */
public class ToStringUtil {

	public static String generateToString(Object obj){
		return ToStringBuilder.reflectionToString(obj, new CustomToStringStyle());
	}
	
	public static String generateToString(Object obj, boolean deep) {
		StringBuilder toStringBuilder = new StringBuilder();
		for(Field field: obj.getClass().getDeclaredFields()) {
			try {
				if (doesMethodExist(obj, getAccessorName(field))) {
					Object attribVal = ReflectionUtils.invoke(obj, getAccessorName(field));
					getPropertyValue(toStringBuilder, field, attribVal, deep);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				toStringBuilder.append(obj.hashCode());
			}
		}
		return obj.getClass().getSimpleName() + " [" + toStringBuilder.toString() + "]";
	}

	public static void getPropertyValue(StringBuilder toStringBuilder, Field field, Object attribVal, boolean deep) {
		if (attribVal == null) {
			toStringBuilder.append(field.getName() + "=<null> ");
		} else if (attribVal instanceof String) {
			toStringBuilder.append(field.getName() + "=" + StringUtils.trim((String) attribVal) + " ");
		} else if (attribVal.getClass().isPrimitive()
				|| attribVal instanceof Boolean 
				|| attribVal instanceof Byte
				|| attribVal instanceof Character
				|| attribVal instanceof Short
				|| attribVal instanceof Integer
				|| attribVal instanceof Long
				|| attribVal instanceof Float
				|| attribVal instanceof Double) {
			toStringBuilder.append(field.getName() + "=" + attribVal + " ");
		} else {
			if (deep) {
				toStringBuilder.append(field.getName() + "={ "  + generateToString(attribVal) + " } ");
			} else {
				toStringBuilder.append(field.getName() + "={ "  + attribVal.hashCode() + " } ");
			}
		}
	}
	
	public static String getAccessorName(Field field) {
		if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
			return "is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		} else { 
			return "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		}
	}
	
	public static boolean doesMethodExist(Object obj, String methodName) {
		try {
			obj.getClass().getMethod(methodName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Write the object to a Base64 serialized string. 
	 */
	public static String serialize(Serializable obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		try {
			oos.writeObject(obj);
			oos.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex, "Unable to Serialize object" + obj);
		}
		return new String(Base64Coder.encode(baos.toByteArray()));
	}
	
	/** 
	 * Read the object from Base64 serialized string. 
	 */
	public static Object deserialize(String serObj) throws IOException, ClassNotFoundException {
		byte [] data = Base64Coder.decode(serObj);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object obj = null;
		try {
			obj  = ois.readObject();
			ois.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex, "Unable to Deserialize object" + serObj);
		}
		return obj;
	}
    
	public static String generateJsonToString(Object obj) {
		return getGson().toJson(obj);
	}
	
	public static Gson getGson() {
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setPrettyPrinting()
				.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")
				.create();
		return gson;
	}
	
	public static String generateToStringForAuditLogging(Object obj){
		return ToStringBuilder.reflectionToString(obj,ToStringStyle.SIMPLE_STYLE);
	}
}
