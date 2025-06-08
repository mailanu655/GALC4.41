package com.honda.galc.data;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>DataContainerUtil Class description</h3>
 * <p> DataContainerUtil description </p>
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
 * Aug 26, 2013
 *
 *
 */
public class DataContainerUtil {
	
	private static final String START_DELIMITER ="{";
	private static final String END_DELIMITER ="}";
	
	
	/**
	 * replace the variables in the sql skelton with the data in the DataContainer
	 * Each variable is enclosed by { and } 
	 * example select * from galadm.gal131tbx where product_id = {PRODUCT_ID} where PRODUCT_ID is variable
	 * @param sqlSkelton - Original SQL Skelton
	 * @param aData - Data container containing values of the variables
	 * @return converted SQL statement
	 */
	
	public static String makeSQL(String sqlSkelton, Map<Object,Object> aData) {
		String resultSql = "";
		String sql = new String(sqlSkelton);
		
	    while(sql.length() > 0) {
	    	int indexS = sql.indexOf(START_DELIMITER);
	    	if(indexS < 0) {
	    		resultSql += sql;
	    		break;
	    	}
	    	int indexE = sql.indexOf(END_DELIMITER);
	    	if(indexE < 0) throw new DataConversionException("Invalid SQL statement : " + sqlSkelton + ". It does not have ending }");
	    	String dcTag = sql.substring(indexS + 1, indexE);
	    	if(StringUtils.isEmpty(dcTag))
	    		throw new DataConversionException("Invalid SQL variable. Empty variable in SQL statetment : " + sqlSkelton);
	    	
	    	//    	 Call DataContainer.get()
			if(!aData.containsKey(dcTag))
				throw new DataConversionException("No tag is defined in data container for SQL varialbe : " + dcTag);
			
			String value = (String) aData.get(dcTag);
			if(value == null) value = "NULL";
		    resultSql += sql.substring(0,indexS) + value;
		    sql = sql.substring(indexE + 1);
	    }
	    
	    return resultSql;
	}
	
	public static void populateObjectValues(Object obj, DataContainer dc,Logger logger) {
		Method[] methods = obj.getClass().getMethods();
		for (Method method: methods) {
			if(!method.getName().startsWith("set")) continue;
			String name = method.getName().substring(3);
			Class<?>[] classes = method.getParameterTypes();
			if(classes.length == 0 || classes.length > 1){
				logger.warn("method " + method.getName() + " has incorrect arguments" );
				continue;
			}
			Object value = dc.get(name);
			Object result = null;
			
			try{
				result = convert(value,classes[0]);	
			}catch(Exception e) {
				logger.warn(e,"Could not convert value " + value);
			}
			
			if(value == null) {
				logger.warn("data container does not have value for " + name + " when retrieve values for " + obj.getClass().getSimpleName());
			}else {
				try {
					method.invoke(obj, new Object[]{result});
					logger.info("set value " + result.toString() + " to " + name + " for " + obj.getClass().getSimpleName());
				} catch (Exception e) {
					logger.error(e,"Could not set value " + result.toString() +" for " + name + " when retrieve values for " + obj.getClass().getSimpleName());
				}
			}
		}
	}
	
	public static Object convert(Object value, Class<?> clazz) {
		if(value == null) return null;
		
		if(clazz.equals(Calendar.class)){
			Timestamp timestamp = Timestamp.valueOf(value.toString());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timestamp.getTime());
			return calendar;
		};
		
		if(clazz.isPrimitive()){
			clazz = ClassUtils.primitiveToWrapper(clazz);
		}
		// creates an object whose constructor has a string as argument
		// this includes all the wrapper classes of primitive types, BigDecimal etc
		return ReflectionUtils.createInstance(clazz, new Object[]{value.toString()});
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getAttributes(DataContainer dc) {
		Object object = dc.get(DataContainerTag.TAG_LIST);
		if(object == null || !(object instanceof List)) return new ArrayList<String>();
		return (List<String>) object;
	}
	
	public static Map<String,String> getAttributeMap(DataContainer dc) {
		Map<String,String> attributeMap = new LinkedHashMap<String,String>();
		for(String attribute : getAttributes(dc)) {
			attributeMap.put(attribute, dc.getString(attribute));
		}
		return attributeMap;
	}
	
	public static Map<String,Object> getAttributeObjectMap(DataContainer dc) {
		Map<String,Object> attributeMap = new LinkedHashMap<String,Object>();
		for(String attribute : getAttributes(dc)) {
			attributeMap.put(attribute, dc.get(attribute));
		}
		return attributeMap;
	}	
	
	public static boolean getBoolean(DataContainer dc, String tag, boolean defaultValue){
		String tagValue = dc.getString(tag);
		if(StringUtils.isEmpty(tagValue)) return defaultValue;
		return tagValue.equalsIgnoreCase("TRUE") || tagValue.equals("1");
	}
	
	public static String getString(DataContainer dc, String tag, String defaultValue){
		String tagValue = dc.getString(tag);
		return StringUtils.isEmpty(tagValue)? defaultValue : tagValue;
	}
	
	public static Integer getInteger(DataContainer dc, String tag, Integer defaultValue) {
		String tagValue = dc.containsKey(tag)? dc.getString(tag) : null;
		return StringUtils.isNumeric(tagValue) ? Integer.parseInt(tagValue) : defaultValue; 
	}

	public static DataContainer copyErrors(DataContainer dc) {
		DefaultDataContainer retDc = new DefaultDataContainer();
		if (dc == null) {
			return retDc;
		}
		List<String> msgs = dc.getErrorMessages();
		Exception ex = dc.getException();
		if (msgs != null) {
			for (String str : msgs) {
				retDc.addErrorMsg(str);
			}
		}
		if (ex != null) {
			retDc.putException(ex);
		}
		return retDc;
	}
	
	/**
	 * Utility method to log error message and put error message into DataContainer
	 * @param logger
	 * @param dc
	 * @param message
	 */
	public static void error(Logger logger, DataContainer dc, String... message) {
		if (message == null) {
			return;
		}
		if (logger != null) {
			logger.error(message);
		}
		if (dc != null) {
			dc.addErrorMsg(StringUtils.join(message));
		}
	}

	/**
	 * Utility method to log error message and put error message and Exception into DataContainer
	 * @param logger
	 * @param dc
	 * @param e
	 * @param userMessage
	 */
	public static void error(Logger logger, DataContainer dc, Exception e, String... message) {
		if (logger != null) {
			logger.error(e, message);
		}
		if (dc != null) {
			dc.addErrorMsg(StringUtils.join(message));
			dc.putException(e);
		}
	}
	
	public static boolean isEquipmentRead(DataContainer dataContainer)
	{
		Object eiOPMode = dataContainer.get(DataContainerTag.EI_OP_MODE);
		return(eiOPMode != null 
				&& eiOPMode.toString().equals(DataContainerTag.EI_OP_MODE_READ));
	}
	
	public static void setEquipmentRead(DataContainer dataContainer)
	{
		dataContainer.put(DataContainerTag.EI_OP_MODE, DataContainerTag.EI_OP_MODE_READ);		
	}

}
