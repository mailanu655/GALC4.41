package com.honda.galc.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.util.GPCSData;
import com.honda.galc.util.OIFConstants;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * The class to get parsing information.<br>
 * <h4>Usage and Example</h4>
 * Insert the usage and example here.
 * <h4>Special Notes</h4>
 * Insert the special notes here if any.
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR></TABLE>
 * @see 
 * @ver 0.1
 * @author Larry Karpov
 * Since 11/14/2013
 * 
 */
public class OIFSimpleParsingHelper<T> {
	
	/**
	 * Constructor
	 * @param Class<T>
	 * @param String
	 * @param Logger
	 */
	public OIFSimpleParsingHelper(Class<T> clazz, String pParseDefs, Logger pLogger) {
		this.parseDefs = pParseDefs;
		this.logger = pLogger;
		this.clazz = clazz;
		this.parsingDataDefList = new ArrayList<ParsingData>();
		this.propertyHelper = new PropertyHelper(parseDefs);
	}

	private Class<T> clazz;
	private Logger logger = null;
	private List<ParsingData> parsingDataDefList = null;
	private String parseDefs = null;
	private PropertyHelper propertyHelper = null;
	
	public List<ParsingData> getParsingDataDef() {
		return parsingDataDefList;
	}

	public String getParseDefs() {
		return parseDefs;
	}

	public void setParseDef(String parseDefs) {
		this.parseDefs = parseDefs;
	}

	public void getParsingInfo() {
		parseFields(this.clazz);
	}

	/**
	 * create parsing information for incoming file.
	 */
	private void parseFields(Class<?> pClazz) {
//	Get column names with @Column annotation for the class
		String className = pClazz.getSimpleName();
		for(Field field : pClazz.getDeclaredFields()) {
			int startingPosition = 0;
			int length  = 0;
			Class<?> dataType = field.getType();
			String parseFieldName = getParseFieldName(field, className);
			if(parseFieldName != null) {
				String parseDef = propertyHelper.getProperty(parseFieldName);
//			Add parsing definition for column name 
				if(parseDef != null) {
					StringTokenizer vRecordDef = new StringTokenizer(parseDef, ",");
					try {
						startingPosition = Integer.parseInt(vRecordDef.nextToken());
						length = Integer.parseInt(vRecordDef.nextToken());
					} catch (NumberFormatException nfe) {
						logger.error("Cann't get data position for " + parseFieldName);
					}
					parsingDataDefList.add(new ParsingData(field.getName(), parseFieldName, dataType, startingPosition, length));
				}
			}
		}
	}
	
	public void parseData(Object obj, String receivedRecord) {
		for(ParsingData parsingDataDef : this.parsingDataDefList) {
			parseData(obj, receivedRecord, parsingDataDef);
		}
	}
	
	/**
	 * Call a setter for parsed data 
	 */
	private void parseData(Object obj, String receivedRecord, ParsingData parsingDataDef) {
		String methodName = parsingDataDef.getMethodName();
		String strValue = receivedRecord.substring(parsingDataDef.getStart(), parsingDataDef.getEnd());
		Object value = null;
		Class<?> objDataType = parsingDataDef.getDataType();
		if(objDataType.isAssignableFrom(String.class) ) {
			value = strValue;
		} else if(objDataType.isAssignableFrom(Integer.class) ) {
			try {
				value = Integer.getInteger(strValue); 
			} catch (NumberFormatException ne) {
				logger.error(ne, "Cann't parse Integer value from: " + strValue);
			}
		} else if(objDataType.isPrimitive()) {
			if(objDataType.equals(int.class)) {
				try {
					value = Integer.parseInt(StringUtils.trim(strValue)); 
				} catch (NumberFormatException ne) {
					logger.error(ne, "Cann't parse int value for " + parsingDataDef.getColumnName() + " from " + strValue);
				}
			}
			if(objDataType.equals(short.class)) {
				if("Y".equalsIgnoreCase(strValue)) {
					value = (short)1; 
				} else if("N".equalsIgnoreCase(strValue)) {
					value = (short)0;
				} else {
					try {
						value = Short.parseShort(StringUtils.trim(strValue));
					} catch (NumberFormatException ne) {
						logger.error(ne, "Cann't parse short value from: " + strValue);
					}
				}
			}
		} else if(java.sql.Time.class.isAssignableFrom(objDataType) ) {
			try {
				java.sql.Time sqlTime = new Time(OIFConstants.stf1.parse(strValue).getTime()); 
				value = sqlTime; 
			} catch (ParseException e) {
				logger.error(e, "Cann't parse Time: " + methodName);
			}
		}else if(java.sql.Timestamp.class.isAssignableFrom(objDataType)){ 
			try {
				java.sql.Timestamp sqlTimestamp = new Timestamp(OIFConstants.sdf1.parse(strValue).getTime());
				value = sqlTimestamp;
			} catch (ParseException e) {
				logger.error(e, "Cann't parse Timestamp: " + methodName);
			}
		} 
		else if(java.sql.Date.class.isAssignableFrom(objDataType) ) {
			try {
				//If the expected date field came in as 99999999, replace it with 20991231 before insert into GALC.
				//The expected data from TISAF2 is Integer, here we have to convert it to DATE type.
				//It is currently for column EFF_END_DATE of BOM_TBX in GALC DB
				if(strValue.equals("99999999")){
					strValue = "20991231";
				}
				java.sql.Date sqlDate = new Date(OIFConstants.sdf1.parse(strValue).getTime()); 
				value = sqlDate; 
			} catch (ParseException e) {
				logger.error(e, "Cann't parse Date: " + methodName);
			}
		} else if(java.util.Date.class.isAssignableFrom(objDataType) ) {
			try {
				java.util.Date utilDate = OIFConstants.sdf1.parse(strValue); 
				value = utilDate; 
			} catch (ParseException e) {
				logger.error(e, "Cann't parse Date: " + methodName);
			}
		}
//				Call setter for specified field
		if(value != null) {
			try {
				Method method = obj.getClass().getMethod(methodName, new Class[] {objDataType});
				method.invoke(obj, value);
			} catch (IllegalArgumentException e) {
				logger.error(e, "Cann't call method: " + methodName);
			} catch (SecurityException e) {
				logger.error(e, "Cann't call method: " + methodName);
			} catch (NoSuchMethodException e) {
				logger.error(e, "Cann't call method: " + methodName);
			} catch (IllegalAccessException e) {
				logger.error(e, "Cann't call method: " + methodName);
			} catch (InvocationTargetException e) {
				logger.error(e, "Cann't call method: " + methodName);
			}
		}
	}

	/**
	 * return annotation name for OIFParsable
	 */
	private String getParseFieldName(Field field, String className) {
		String nameToParse = null; 
		Annotation annotationField = field.getAnnotation(GPCSData.class);
		if(annotationField != null) {
			String annotationName = ((GPCSData) annotationField).value();
			if(!annotationName.equalsIgnoreCase("Undefined")) {
				try {
					nameToParse = (String) annotationField.annotationType().getMethod("value").invoke(annotationField);
				} catch (IllegalArgumentException e) {
					logger.error(e, "Cann't get field from " + className + " for " + nameToParse);
				} catch (SecurityException e) {
					logger.error(e, "Cann't get field from " + className + " for " + nameToParse);
				} catch (IllegalAccessException e) {
					logger.error(e, "Cann't get field from " + className + " for " + nameToParse);
				} catch (InvocationTargetException e) {
					logger.error(e, "Cann't get field from " + className + " for " + nameToParse);
				} catch (NoSuchMethodException e) {
					logger.error(e, "Cann't get field from " + className + " for " + nameToParse);
				}
			} else {
				nameToParse = field.getName().toUpperCase();
			}
		}
		return nameToParse;
	}

}