package com.honda.galc.dto;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * 
 * <h3>DtoUtil Class description</h3>
 * <p> DtoUtil description </p>
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
 * @author Jeffray Huang<br>
 * Feb 13, 2015
 *
 *
 */
public class DtoUtil {
	
	public static <T> List<String> output(Class<T> dtoClass,List<T> dtoList,Map<String,String> formats,int outLength){
		List<String> outputList = new ArrayList<String>();
		for(T dto : dtoList) {
			outputList.add(output(dtoClass,dto, formats,outLength));
		}
		return outputList;
	}
	
	public static <T> String output(Class<T> dtoClass, T dto, Map<String,String> formats,int outLength){
		
		StringBuffer sb = new StringBuffer(StringUtils.repeat(" ", outLength));
		
		for(Map.Entry<String, String> item : formats.entrySet()) {
			String[] formatStr = item.getValue().split(Delimiter.COMMA);
			int startPosition = Integer.parseInt(formatStr[0]);
			int length = Integer.parseInt(formatStr[1]);
			int endPosition = startPosition + length;
			String defaultValue = formatStr.length > 2 ? formatStr[2] : null;
			Object obj = getObject(dto,dtoClass, item.getKey());
			if(obj == null)obj = defaultValue;
			if(obj != null) {
				String result = ObjectUtils.toString(obj);
				int pos1 = Integer.parseInt(formatStr[0]);
				int pos2 = pos1 + Integer.parseInt(formatStr[0]);
				if(startPosition < outLength && endPosition < outLength) {
					if(result.length() > length) {
						sb.replace(pos1, pos2, StringUtils.repeat("*", length));
					}else 
						sb.replace(pos1, pos1 + result.length(), result);
				}
			}
		}
		
		
		return sb.toString();
		
	}
	
	/**
	 * create pojo object from objects input based on dtotag
	 * @param <T>
	 * @param dtoClass
	 * @param objects
	 * @return
	 */
	public static <T> T createDto(Class<T> dtoClass, Map<String,Object> objects) {
		T dtoObject = ReflectionUtils.createInstance(dtoClass);
		if(objects!= null && !objects.isEmpty()){
			List<Field> fields = getAllFieldsWithInputDtoTag(dtoClass);
			for(Field field : fields) {
				String name = DtoUtil.findInputName(field);
				if(StringUtils.isEmpty(name)) continue;
				
				Object obj = null;
				try{
					obj = convert(objects.get(name),field.getType());
				}catch(DataConversionException ex) {
					if(!field.getAnnotation(DtoTag.class).optional()){
						Logger.getLogger().error(ex,
							"Could not convert value for " + dtoClass.getSimpleName(),
							" field:" + field.getName(),
							" value: + " + ObjectUtils.toString(objects.get(name))
						);
					};
					continue;
				}
				try {
					field.setAccessible(true);
					field.set(dtoObject, obj);
				} catch (Exception e) {
					if(!field.getAnnotation(DtoTag.class).optional()){
						Logger.getLogger().error(
							"Could not set value for " + dtoClass.getSimpleName(),
							" field:" + field.getName(),
							" value: + " + obj );
					};
				}
			}
		}
		return dtoObject;
	}
	
	public static String findOutputName(Method method){
		DtoTag dtoTag = method.getAnnotation(DtoTag.class);
		if(dtoTag == null || dtoTag.type().equals(DtoType.IN)) return null;
		if(!dtoTag.outputName().equals(DtoTag.DEF_UNNASSIGNED)) return dtoTag.outputName();
		if(!dtoTag.name().equals(DtoTag.DEF_UNNASSIGNED)) return dtoTag.name();
		return deriveName(method);
	}
	
	public static String findInputName(Method method) {
		DtoTag dtoTag = method.getAnnotation(DtoTag.class);
		if(dtoTag == null) throw new DataConversionException("Method " + method.getName() + " does not have DtoTag");
		if(dtoTag.type().equals(DtoType.OUT)) throw new DataConversionException("Method " + method.getName() + "''s DtoType is not INPUT");
		if(!dtoTag.name().equals(DtoTag.DEF_UNNASSIGNED)) return dtoTag.name();
		return deriveName(method);
	}
	
	private static <T> Object getObject(T dto,Class<T> dtoClass, String outputName){
		Method method = findOutputMethod(dtoClass, outputName);
		if(method != null) return  invokeMethod(method, dto);
		Field field = findOutputField(dtoClass,outputName);
		if(field != null) return getFieldValue(field, dto); 
		return null;
	}
	
	private static String findInputName(Field field) {
		DtoTag dtoTag = field.getAnnotation(DtoTag.class);
		if(!dtoTag.name().equals(DtoTag.DEF_UNNASSIGNED)) return dtoTag.name();
		return deriveName(field);
	}
	
	private static String findOutputName(Field field){
		DtoTag dtoTag = field.getAnnotation(DtoTag.class);
		if(dtoTag == null || dtoTag.type().equals(DtoType.IN)) return null;
		if(!dtoTag.outputName().equals(DtoTag.DEF_UNNASSIGNED)) return dtoTag.outputName();
		if(!dtoTag.name().equals(DtoTag.DEF_UNNASSIGNED)) return dtoTag.name();
		return deriveName(field);
	}
	
	private static String deriveName(Field field) {
		String[] words = field.getName().split("(?=[A-Z])");
		
		String name ="";
		//filter first word for set,get, is, etc
		for(String word: words) {
			if(!StringUtils.isEmpty(name)) name+="_";
			name += word.toUpperCase();
		}
		return name;
	}
	
	private static <T> Method findOutputMethod(Class<T> dtoClass, String outputName) {
		for (Method method :dtoClass.getMethods()) {
			String name = findOutputName(method);
			if(outputName.equalsIgnoreCase(name)) return method;
		}
		return null;
	}
	
	private static String deriveName(Method method) {
		String[] words = method.getName().split("(?=[A-Z])");
		
		if(words.length <= 1) return null;
		String name ="";
		//filter first word for set,get, is, etc
		for(int i = 1; i<words.length;i++) {
			if(!StringUtils.isEmpty(name)) name+="_";
			name += words[i].toUpperCase();
		}
		return name;
	}
	
	private static List<Field> getAllFieldsWithInputDtoTag(Class<?> type) {
	    List<Field> result = new ArrayList<Field>();

	    Class<?> clazz = type;
	    while (clazz != null && clazz != Object.class) {
	        for (Field field : clazz.getDeclaredFields()) {
	        	DtoTag dtoTag = field.getAnnotation(DtoTag.class);
	        	if(dtoTag != null && (dtoTag.type().equals(DtoType.IN)|| dtoTag.type().equals(DtoType.IN_OUT)))
	            	result.add(field);
	        }
	        clazz = clazz.getSuperclass();
	    }
	    return result;
	}
	
	private static List<Field> getAllFieldsWithDtoTag(Class<?> type) {
		return getAllFieldsWithAnnotation(DtoTag.class, type);
	}
	
	public static <A extends Annotation> List<Field> getAllFieldsWithAnnotation(Class<A> annotationType, Class<?> type) {
	    List<Field> result = new ArrayList<Field>();

	    Class<?> clazz = type;
	    while (clazz != null && clazz != Object.class) {
	        for (Field field : clazz.getDeclaredFields()) {
	            if(field.getAnnotation(annotationType) != null)
	            	result.add(field);
	        }
	        clazz = clazz.getSuperclass();
	    }
	    return result;
	}
	
	private static <T> Field findOutputField(Class<T> dtoClass, String outputName) {
		List<Field> fields = getAllFieldsWithDtoTag(dtoClass);
		for(Field field : fields) {
			String name = findOutputName(field);
			if(outputName.equalsIgnoreCase(name)) return field;
		}
		return null;
	}
	
	private static <T> Object invokeMethod(Method method, T dto) {
		Object obj = null;
		try {
			obj = method.invoke(dto, (Object[]) null);
		} catch (Exception e) {
			Logger.getLogger().error(e,"Could not invoke method: " + method.getName() + ", dto: " + dto);
		}
		
		return obj;
	}
	
	public static <T> Object getFieldValue(Field field, T dto) {
		Object obj = null;
		try {
			field.setAccessible(true);
			obj = field.get(dto);
		} catch (Exception e) {
			Logger.getLogger().error(e,"Could not get field value: " + field.getName() + ", dto: " + dto);
		}
		return obj;
	}
	
	public static  Object convert(Object obj,Class<?> returnType) {
		if(obj == null) return null;
		if(returnType.isPrimitive()) return convertPrimitive(obj,returnType);
		Class<?> componentType = returnType.getComponentType();
		if(componentType != null && componentType == byte.class) {
			Blob blob = (Blob)obj;
			try {
				return blob.getBytes(1,(int)blob.length());
			} catch (SQLException e) {
				throw new DataConversionException("Could not convert", e);
			}
		}
		if(returnType.isAssignableFrom(obj.getClass())) return obj;
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static Object convertPrimitive(Object obj,Class<?> returnType) {
		String dataStr = ObjectUtils.toString(obj);
		try{
			Constructor<?>	constructor = 
				ClassUtils.primitiveToWrapper(returnType).getConstructor(String.class);
			return constructor.newInstance(dataStr);
		} catch (Exception e) {
			throw new DataConversionException(e.getMessage());
		} 
	}
}
