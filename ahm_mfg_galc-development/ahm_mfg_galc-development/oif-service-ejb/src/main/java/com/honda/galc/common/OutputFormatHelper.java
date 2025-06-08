package com.honda.galc.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.oif.dto.IOutputFormat;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OutputData;

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
 * </TR> 
 * <TR>
 * <TD>YX</TD>
 * <TD>2014.8.20</TD>
 * <TD>0.1</TD>
 * <TD>TASK0013687</TD>
 * <TD>parse default value from Configuration parameters</TD>
 * </TR></TABLE>
 * @see 
 * @ver 0.1
 * @author Larry Karpov
 * Since 11/14/2013
 * @param <T>
 * 
 */
public class OutputFormatHelper<T extends IOutputFormat> {
	
	/**
	 * Constructor
	 * @param Class<T>
	 * @param String
	 * @param Logger
	 * @param OifErrorsCollector
	 */
	public OutputFormatHelper(String outputFormatDef, Logger pLogger, OifErrorsCollector errorsCollector) {
		this.outputFormatDef = outputFormatDef;
		this.logger = pLogger;
		this.errorsCollector = errorsCollector;
	}

	private Logger logger = null;
	private OifErrorsCollector errorsCollector;
	private String outputFormatDef = null;
	List<OutputFormatData> outputFormatList = new ArrayList<OutputFormatData>();
	HashMap<String, OutputFormatData> outputDefMap = new HashMap<String, OutputFormatData>();

	public String getParseDefs() {
		return outputFormatDef;
	}

	public void setParseDef(String outputFormatDef) {
		this.outputFormatDef = outputFormatDef;
	}

//	Get OutputFormatData information based on class  
//	and add formatting information from Configuration parameters
	public void initialize(Class<T> clazz) {
		Map<String, String> odAnnotated = new HashMap<String, String>(); 
		for(Field field : clazz.getDeclaredFields()) {
			OutputData od = field.getAnnotation(OutputData.class);
			if(od != null) {
            	odAnnotated.put(field.getName(), od.value());
			}
		}
		try {
			BeanInfo info = Introspector.getBeanInfo(clazz);
			for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if(odAnnotated.containsKey(pd.getName()) && 
						pd.getReadMethod() != null && 
						!"class".equals(pd.getName())) {
	            	Method getter = pd.getReadMethod();
	        		OutputFormatData outputParams = new OutputFormatData();
	        		outputParams.setAnnotationName(odAnnotated.get(pd.getName()));
	        		outputParams.setFieldName(pd.getName());
	            	outputParams.setMethod(getter);
	            	outputParams.setDataType(pd.getPropertyType());
	            	this.outputFormatList.add(outputParams);
	            }
			} 
		} catch (IntrospectionException e) {
			logger.error("Introspection error for " + clazz.getSimpleName());
			errorsCollector.error("Introspection error for " + clazz.getSimpleName());
		}
		int startingPosition = 0;
		int length  = 0;
		for(OutputFormatData formatData : this.outputFormatList) {
			String fieldName = formatData.getAnnotationName();
			String parseDef = PropertyService.getProperty(outputFormatDef, fieldName);
			if(parseDef != null) {
				StringTokenizer vRecordDef = new StringTokenizer(parseDef, ",");
				try {
					startingPosition = Integer.parseInt(vRecordDef.nextToken());
					length = Integer.parseInt(vRecordDef.nextToken());
					formatData.setStart(startingPosition);
					formatData.setEnd(startingPosition + length);
					//TASK0013687
					if(vRecordDef.hasMoreTokens()) {
						formatData.setDefaultValue(vRecordDef.nextToken());
					}
				} catch (NumberFormatException nfe) {
					logger.error("Cann't get data position for " + fieldName);
					errorsCollector.error("Cann't get data position for " + fieldName);
				}
			}
		}
		for (OutputFormatData formatData : this.outputFormatList) {
			outputDefMap.put(formatData.getAnnotationName(), formatData);
		}
	}

//	using OutputFormatData create a string from instance of class data
	public String formatOutput(T lotResult, char[] charArray) {
		StringBuilder sb = new StringBuilder(new String(charArray));
		for(OutputFormatData outputFormat : outputFormatList) {
			Object result = null;
			try {
				result = outputFormat.getMethod().invoke(lotResult);
			} catch (IllegalArgumentException e) {
				logger.error(e, "Cann't call method: " + outputFormat.getMethod().getName());
				errorsCollector.error(e, "Cann't call method: " + outputFormat.getMethod().getName());
			} catch (IllegalAccessException e) {
				logger.error(e, "Cann't call method: " + outputFormat.getMethod().getName());
				errorsCollector.error(e, "Cann't call method: " + outputFormat.getMethod().getName());
			} catch (InvocationTargetException e) {
				logger.error(e, "Cann't call method: " + outputFormat.getMethod().getName());
				errorsCollector.error(e, "Cann't call method: " + outputFormat.getMethod().getName());
			}
			Class<?> objDataType = outputFormat.getDataType();
			//TASK0013687
			if(result == null) {
				result = outputFormat.getDefaultValue();
				objDataType = String.class;
			} 
			
			String strResult = null;
			if(objDataType.isAssignableFrom(String.class) ) {
				strResult = (String) result;
			} else if(objDataType.isPrimitive()) {
				if(objDataType.equals(int.class)) {
					strResult = String.format("%d", result);
				}
			} else if(java.sql.Date.class.isAssignableFrom(objDataType) ) {
				strResult = String.format("%TD", result);
			} else if(java.sql.Timestamp.class.isAssignableFrom(objDataType) ) {
				strResult = String.format("%1$TF %1$TT.%1$TN", result).substring(0, 26);
			} else {
				strResult = result.toString();
			}
			int pos1 = outputFormat.getStart();
			int pos2 = outputFormat.getEnd();
			if(strResult.length() > pos2-pos1) {
				char[] charsAsterisk = new char[pos2-pos1];
				Arrays.fill(charsAsterisk, '*');
				sb.replace(pos1, pos2, new String(charsAsterisk));		
			} else {
				sb.replace(pos1, pos1 + strResult.length(), strResult);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param aName: annotation name
	 * @return OutputFormatData: format definition for specified annotation
	 */
	public OutputFormatData getOutputDef(String aName)  {
		OutputFormatData outDef = null;
		if(aName != null && outputDefMap != null && !outputDefMap.isEmpty())  {
			outDef = outputDefMap.get(aName);
		}
		return outDef;
	}
	
	/**
	 * @param aName: annotation name
	 * @return int length in output format
	 * 
	 * Return length specified in format definition (end-start)
	 */
	public int getDataLength(String aName)  {
		int len = 0;
		OutputFormatData outDef = getOutputDef(aName);
		if(outDef != null)  {
			len = outDef.getEnd() - outDef.getStart();
		}
		return len;
	}
	
}