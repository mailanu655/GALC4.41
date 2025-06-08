package com.honda.galc.service.property;

import java.awt.Color;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.util.KeyValue;


/**
 * 
 * Regional-HMIN Merged Version 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */

public class PropertyBeanInvocationHandler implements InvocationHandler {
	
	private static final String WORD_IS = "is";
	private static final String WORD_GET = "get";
	private static final String SEPARATOR = "\\s*,\\s*";
	private static final String MAP_KEY_REGEX = "\\s*(?:(?:\\{(.*)})|(?:\\[(.*)]))\\s*";

	
	PropertyService propertyService;
	private String componentId;
	private String suffix = "";
	private List<KeyValue<Class<?>,String>> componentIds = new ArrayList<KeyValue<Class<?>,String>>();
	private List<KeyValue<Class<?>,String>> reversedComponentIds = new ArrayList<KeyValue<Class<?>,String>>();
	
	<T extends IProperty> PropertyBeanInvocationHandler(String componentId, Class<T> intfClass, PropertyService service,String suffix){
		
		this.componentId = componentId;
		this.propertyService = service;
		if(suffix != null) this.suffix = suffix;
		componentIds.add(new KeyValue<Class<?>,String>(intfClass,componentId));
		
		findInterfaces(intfClass);
		reversedComponentIds.addAll(componentIds);
		Collections.reverse(reversedComponentIds);
	}
	
	
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		KeyValue<String,String> propertyKeyValue = derivePropertyKey(method);
		String propertyKey = propertyKeyValue.getKey();
		if(propertyKey == null) {
		    throw new PropertyException("Cannot derive property key from method name : " + method.getName()); 
		}
		
		Class returnType = method.getReturnType();
		
		if(returnType == Map.class) {
			return getMap(propertyKey,args);
		}
		String propertyValue = getPropertyValue(propertyKey);
		if(propertyValue == null) propertyValue = propertyKeyValue.getValue();
		
		if(propertyValue == null) {
		    throw new PropertyException("Property not found: " + propertyKey);
		}
		
		if(returnType == String.class) return propertyValue;
		if(returnType == int.class) return Integer.parseInt(propertyValue.trim());
		if(returnType == long.class) return Long.parseLong(propertyValue.trim());
		if(returnType == boolean.class) return Boolean.parseBoolean(propertyValue.trim());
		if(returnType == double.class) 	return Double.parseDouble(propertyValue.trim());
		if(returnType == Color.class) 	return deriveColor(propertyValue.trim());
		
		
		// For all complex types

		// Check for Enum
		Object[] consts = returnType.getEnumConstants();
		if(consts != null) {
			// It is Enum
			return getEnumProperty(propertyValue, consts);			
		}
		
		// check for array
		return convertValue(propertyValue,returnType);

	}
	
	@SuppressWarnings("unchecked")
	private <T extends IProperty> void findInterfaces(Class<T> intfClass) {
		//get default component id from the interface if defined
		PropertyBean annotation = intfClass.getAnnotation(PropertyBean.class);
		if(annotation != null && !StringUtils.isEmpty(annotation.componentId()) && !PropertyBean.DEF_UNNASSIGNED.equals(annotation.componentId()))
			componentIds.add(new KeyValue(intfClass, annotation.componentId()));
		
		PropertyBean propBeanConfig = (PropertyBean) intfClass.getAnnotation(PropertyBean.class);
		String componentId = null; 
		if(propBeanConfig == null || propBeanConfig.componentId()== null || propBeanConfig.componentId().length() <= 0) {
			componentId = PropertyService.getCannedComponentId(intfClass); 
		}else {
			componentId = propBeanConfig.componentId();
			// PropertyService.getPropertyBean(intfClass, componentId);
		};
		
		if(componentId != null && propBeanConfig != null && !PropertyBean.DEF_UNNASSIGNED.equals(componentId))
				componentIds.add(new KeyValue<Class<?>,String>(intfClass,propBeanConfig.componentId()));
		
		for(Class<T> intf :  (Class<T>[]) intfClass.getInterfaces()) {
			findInterfaces(intf);
		}	
		
		
	}
	
	private String getPropertyValue(String propertyKey) {
		
		for(KeyValue<Class<?>,String> keyValue : componentIds) {
			String currentComponentId = keyValue.getValue();
			String derivedSuffix = deriveSuffix(currentComponentId);
			String derivedPropertyKey = derivePropertyKey(keyValue.getKey(), propertyKey, derivedSuffix);
			String propertyValue = PropertyService.getProperty(keyValue.getValue(),derivedPropertyKey);
			if(propertyValue !=null) return propertyValue;
		}
		return null;
	}
	
	private String deriveSuffix(String currentComponentId) {
		return this.componentId.equals(currentComponentId) ? suffix : "";
	}
	
	private String derivePropertyKey(Class<?> intf,String propertyKey, String suffix) {
		PropertyBean propBeanConfig = (PropertyBean) intf.getAnnotation(PropertyBean.class);
		if(propBeanConfig == null || 
				propBeanConfig.prefix()== null || propBeanConfig.prefix().length() <= 0) 
			return propertyKey;
		else {
			String prefix = propBeanConfig.prefix();
			return prefix + "." + propertyKey + suffix;
		}
		
	}
	
	private KeyValue<String,String> derivePropertyKey(Method method) {
		String propKey = null;
		String defaultValue = null;
		PropertyBeanAttribute propAnnotation = method.getAnnotation(PropertyBeanAttribute.class);
		if (propAnnotation != null) {
			if(!propAnnotation.propertyKey().equals(PropertyBeanAttribute.DEF_UNNASSIGNED)) {
				propKey = propAnnotation.propertyKey();
			} else {
				propKey = getPropertyByMethodName(method.getName());
			}
			
			// Take care about defaults
			if(!propAnnotation.defaultValue().equals(PropertyBeanAttribute.DEF_UNNASSIGNED)) {
				defaultValue = propAnnotation.defaultValue();
			}
		} else {
			propKey = getPropertyByMethodName(method.getName());
		}
		
		return new KeyValue<String,String>(propKey, defaultValue);
	}
	
	/**
	 * Helper method to recreate property key based on Property Bean method name
	 * 
	 * @param methodName - method name
	 * @return property key based on Property Bean method name
	 */
	public static String getPropertyByMethodName(String methodName) {
		// getAaaBbbCcc -> AAA_BBB_CCC
		StringBuilder res = new StringBuilder();
		int ix = 0;
		boolean lastIsLowerCase = false;
		// remove "get"
		if(methodName.startsWith(WORD_GET)) {
			ix = WORD_GET.length();
		} else if(methodName.startsWith(WORD_IS)) {
			ix = WORD_IS.length();
		}
		
		// In the loop
		for (int i = ix; i < methodName.length(); i++) {
			char letter = methodName.charAt(i);
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
	
	/**
	 * @param propertyValue
	 * @param consts
	 * @return
	 * @throws PropertyException
	 */
	@SuppressWarnings("unchecked")
	private Object getEnumProperty(String propertyValue, Object[] consts) throws PropertyException {
		Enum returnEnum = (Enum) consts[0];
		Enum enumValue;
		try {
			enumValue = Enum.valueOf(returnEnum.getClass(), propertyValue);
			return enumValue;
		} catch (IllegalArgumentException e) {
			throw new PropertyException(e.getMessage());
		}
	}

	private Color deriveColor(String propertyValue) {
		return deriveColor(propertyValue.split(SEPARATOR));
	}

	private Color deriveColor(String[] stringValues) {
		return deriveColor(getIntArray(stringValues));
	}

	private Color deriveColor(int[] rgb) {
		if(rgb.length < 3)
			throw new PropertyException("Invalid color values : " + rgb);
		if(rgb[0] == -1 && rgb[1] == -1 && rgb[2] == -1)
			return null;
		return new Color(rgb[0],rgb[1],rgb[2]);
	}

	/**
	 * @param componentType
	 * @param stringValues
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws NegativeArraySizeException
	 * @throws PropertyException
	 */
	@SuppressWarnings("unchecked")
	private Object getArrayByStringArgConstructor(Class componentType, String[] stringValues) throws NoSuchMethodException, SecurityException, NegativeArraySizeException, PropertyException {
		
		if(componentType.equals(Color.class)) return deriveColor(stringValues);
		
		Constructor constructor = componentType.getConstructor(String.class);
		
		Object array = Array.newInstance(componentType, stringValues.length);
		Object[] elems = (Object[]) array;
		for (int i = 0; i < stringValues.length; i++) {
			try {
				elems[i] = constructor.newInstance(stringValues[i]);
			} catch (Exception e) {
				throw new PropertyException(e.getMessage());
			}					
		}
		return array;
	}


	/**
	 * @param stringValues
	 * @return
	 * @throws PropertyException
	 */
	private char[] getCharArray(String[] stringValues) throws PropertyException {
		if (stringValues.length == 1 && StringUtils.isEmpty(stringValues[0])) {
			return new char[0]; // return an empty array when stringValues consists of a single empty string
		}
		
		char[] result = new char[stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			try {
				if (stringValues[i].length() != 1) throw new RuntimeException("stringValue[" + i + "] \"" + stringValues[i] + "\" does not define a single character");
				result[i] = stringValues[i].charAt(0);
			} catch (Exception e) {
				throw new PropertyException(e.getMessage());
			}					
		}
		return result;
	}

	/**
	 * @param stringValues
	 * @return
	 * @throws PropertyException
	 */
	private double[] getDoubleArray(String[] stringValues) throws PropertyException {
		double[] result = new double[stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			try {
				result[i] = Double.parseDouble(stringValues[i]);
			} catch (Exception e) {
				throw new PropertyException(e.getMessage());
			}					
		}
		return result;
	}

	/**
	 * @param stringValues
	 * @return
	 */
	private boolean[] getBooleanArray(String[] stringValues) {
		boolean[] result = new boolean[stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			result[i] = Boolean.parseBoolean(stringValues[i]);
		}
		return result;
	}

	/**
	 * @param stringValues
	 * @return
	 * @throws PropertyException
	 */
	private int[] getIntArray(String[] stringValues) throws PropertyException {
		int[] result = new int[stringValues.length];
		
		for (int i = 0; i < stringValues.length; i++) {
			try {
				result[i] = Integer.parseInt(stringValues[i]);
			} catch (NumberFormatException e) {
				throw new PropertyException(e.getMessage());
			}						
		}
		return result;
	}
	
	private Object getMap(String propertyKey, Object[] args) throws Throwable{
		return getMatchedProperties(propertyKey, args);
	}

	private Pattern getPropertyKeyPattern(String propertyKey) {
		String regex = new StringBuilder(propertyKey).append(MAP_KEY_REGEX).toString();
		Pattern p = Pattern.compile(regex);
		return p;
	}

	private Map<String, Object> getMatchedProperties(String propertyKey, Object[] args) throws Throwable {
		Map<String, Object> result = null;
		Pattern p = getPropertyKeyPattern(propertyKey);
		for(KeyValue<Class<?>,String> keyValue : reversedComponentIds) {
			List<ComponentProperty> componentProperties = PropertyService.getComponentProperty(keyValue.getValue());
			for(ComponentProperty property : componentProperties){
				String mapKey = null;
				if(property.getId().getPropertyKey().equals(StringUtils.trim(propertyKey))) mapKey = "*";
				else {
					Matcher m = p.matcher(property.getId().getPropertyKey());
					if(m.matches()) mapKey = (m.group(1)!= null ? m.group(1): m.group(2));
				}
				if(mapKey != null) {
					if(result == null) result = new HashMap<String, Object>();
					boolean isArgClass = args != null && args.length > 0 && args[0].getClass().equals(Class.class);
					Class<?> clazz = isArgClass ? (Class<?>)args[0] : String.class;
					
					Object value = convertValue(property.getPropertyValue(), clazz); 
					
					result.put(mapKey.trim(), value);
				}
			}
		}
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	private Object convertValue(String propertyValue, Class<?> objectType){
		if(objectType == null) return propertyValue;
		Class<?> componentType = objectType.getComponentType();
		
		if(componentType != null) {
			// It is an array
			String[] stringValues = propertyValue.split(SEPARATOR);
			if(componentType == String.class) {
				return stringValues.length == 1 && StringUtils.isBlank(stringValues[0]) ? new String[0] :
				   stringValues;
			}
			else if(componentType == int.class)     return getIntArray(stringValues);
			else if(componentType == boolean.class) return getBooleanArray(stringValues);
			else if(componentType == double.class)  return getDoubleArray(stringValues);
			else if(componentType == char.class)    return getCharArray(stringValues);
			else {
				try {
					// Arbitrary String arg constructor bean					
					return getArrayByStringArgConstructor(componentType, stringValues);
				} catch (Exception e) {
				throw new PropertyException("Not Supported PropertyBean component type of method return type: " + componentType);				
				}				
			}
		}
		try {
			if(objectType.equals(Color.class)) return deriveColor(propertyValue);
			Constructor	constructor = objectType.getConstructor(String.class);
			return constructor.newInstance(propertyValue);
		} catch (Exception e) {
			throw new PropertyException(e.getMessage());
		} 
	}

}


