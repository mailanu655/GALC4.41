package com.honda.galc.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.IEntity;
import com.honda.galc.service.property.PropertyHelper;
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
public class OIFParsingHelper<T extends IEntity> {
	
	/**
	 * Constructor
	 * @param Class<T>
	 * @param String
	 * @param Logger
	 */
	public OIFParsingHelper(Class<T> clazz, String pParseDefs, Logger pLogger) {
		this.parseDefs = pParseDefs;
		this.logger = pLogger;
		this.clazz = clazz;
		this.parsingDataDefList = new ArrayList<ParsingData>();
		this.primeParsingDataDefList = new ArrayList<ParsingData>();
		this.propertyHelper = new PropertyHelper(parseDefs);
	}

	private Class<T> clazz;
	private Logger logger = null;
	private ParsingData parsePrimary = null;
	private List<ParsingData> parsingDataDefList = null;
	private List<ParsingData> primeParsingDataDefList = null;
	private String parseDefs = null;
	private PropertyHelper propertyHelper = null;
	
	public List<ParsingData> getParsingDataDef() {
		return parsingDataDefList;
	}

	public List<ParsingData> getPrimeParsingDataDefList() {
		return primeParsingDataDefList;
	}

	public ParsingData getParsePrimary() {
		return parsePrimary;
	}

	public String getParseDefs() {
		return parseDefs;
	}

	public void setParseDef(String parseDefs) {
		this.parseDefs = parseDefs;
	}

	public void getParsingInfo() {
		Class<?> primeClazz = null;
		for(Field field : clazz.getDeclaredFields()) {
			if(field.getAnnotation(EmbeddedId.class) != null) {
				primeClazz = field.getType();
				break;
			}
		}
		if(primeClazz != null) {
			parseColumns(primeClazz, this.primeParsingDataDefList);
		}
		parseColumns(this.clazz, this.parsingDataDefList);
	}

	/**
	 * return either field name
	 * or @Column annotation name 
	 * !TODO - exclude static members 
	 */
	private String getColumnName(Field field, String className) {
		String columnName = null; 
		Annotation annotationColumn = field.getAnnotation(Column.class);
		Annotation annotationEmbeddedId = field.getAnnotation(EmbeddedId.class);
//		Skip a field with @EmbeddedId annotation
		if(annotationEmbeddedId == null) {
			if(annotationColumn != null) {
				try {
					columnName = (String) annotationColumn.annotationType().getMethod("name").invoke(annotationColumn);
				} catch (IllegalArgumentException e) {
					logger.error(e, "Cann't get field from " + className + " for " + columnName);
				} catch (SecurityException e) {
					logger.error(e, "Cann't get field from " + className + " for " + columnName);
				} catch (IllegalAccessException e) {
					logger.error(e, "Cann't get field from " + className + " for " + columnName);
				} catch (InvocationTargetException e) {
					logger.error(e, "Cann't get field from " + className + " for " + columnName);
				} catch (NoSuchMethodException e) {
					logger.error(e, "Cann't get field from " + className + " for " + columnName);
				}
			} else {
				columnName = field.getName().toUpperCase();
			}
		}
		return columnName;
	}

	/**
	 * look through clazz @Column annotations
	 * and create parsing information for incoming file.
	 */
	private void parseColumns(Class<?> pClazz, List<ParsingData> parsingData) {
//	Get column names with @Column annotation for the class
		String className = pClazz.getSimpleName();
		for(Field field : pClazz.getDeclaredFields()) {
			int startingPosition = 0;
			int length  = 0;
			Class<?> dataType = field.getType();
			String columnName = getColumnName(field, className);
			if(columnName != null) {
				String parseDef = propertyHelper.getProperty(columnName);
//			Add parsing definition for column name 
				if(parseDef != null) {
					StringTokenizer vRecordDef = new StringTokenizer(parseDef, ",");
					try {
						startingPosition = Integer.parseInt(vRecordDef.nextToken());
						length = Integer.parseInt(vRecordDef.nextToken());
					} catch (NumberFormatException nfe) {
						logger.error("Cann't get data position for " + columnName);
					}
					if(field.getAnnotation(Id.class) != null) {
//						Primary key parsing definition (annotation = @Id)
						parsePrimary = new ParsingData(field.getName(), columnName, dataType, startingPosition, length); 
					}
					parsingData.add(new ParsingData(field.getName(), columnName, dataType, startingPosition, length));
				}
			}
		}
	}
	
	public void parseData(Object obj, String receivedRecord) {
		for(ParsingData parsingDataDef : this.parsingDataDefList) {
			parseData(obj, receivedRecord, parsingDataDef);
		}
	}
	
	public void parsePrimaryDataList(Object obj, String receivedRecord) {
		for(ParsingData parsingDataDef : this.primeParsingDataDefList) {
			parseData(obj, receivedRecord, parsingDataDef);
		}
	}
	
	public void parsePrimaryData(Object obj, String receivedRecord) {
		parseData(obj, receivedRecord, parsePrimary);
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
					value = Integer.parseInt(strValue); 
				} catch (NumberFormatException ne) {
					logger.error(ne, "Cann't parse int value for " + parsingDataDef.getColumnName() + " from " + strValue);
				}
			}
			if(objDataType.equals(short.class)) {
				if("Y".equalsIgnoreCase(strValue)) {
					value = (short)1; 
				} else if("N".equalsIgnoreCase(strValue)) {
					value = (short)0;
				} else if("P".equalsIgnoreCase(strValue)) {
					value = (short)-1;
				}else {
					try {
						value = Short.parseShort(strValue);
					} catch (NumberFormatException ne) {
						logger.error(ne, "Cann't parse short value from: " + strValue);
					}
				}
			}
		} else if(java.sql.Time.class.isAssignableFrom(objDataType) ) {
			try {
				java.sql.Time sqlTime = new java.sql.Time(OIFConstants.stf1.parse(strValue).getTime()); 
				value = sqlTime; 
			} catch (ParseException e) {
				logger.error(e, "Cann't parse Time: " + methodName);
			}
		} else if(java.sql.Date.class.isAssignableFrom(objDataType) ) {
			try {
				java.sql.Date sqlDate = new java.sql.Date(OIFConstants.sdf1.parse(strValue).getTime()); 
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

}