package com.honda.galc.common;

import java.lang.reflect.Method;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
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
 * <TD>Larry Karpov</TD>
 * <TD>(2013/11/14)</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2014.8.20</TD>
 * <TD>0.1</TD>
 * <TD>TASK0013687</TD>
 * <TD>add configurable default field value</TD>
 * </TR></TABLE>
 * @see 
 * @ver 0.1
 * @author Larry Karpov
 * 
 */
/**
 * The class to keep information about 
 * method name, field name, 
 * and where corresponding data are in a string
 * for specific field 
 * <p>
 * Field field - Member of the class
 * String methodName
 * int startingPosition, length define boundaries in the string to extract data from it
 */
public class OutputFormatData {
	
	private String fieldName = null;
	private String annotationName = null;
	private Method method = null;
	private Class<?> dataType = null;
	private int start = 0;
	private int end = 0;
	//TASK0013687
	private Object defaultValue = "";
	
	public OutputFormatData() {
	}
	
	OutputFormatData(Class<?> pDataType, int startingPosition, int length) {
		this.dataType = pDataType;
		start = startingPosition;
		end = startingPosition + length;
	}
	//TASK0013687
	OutputFormatData(Class<?> pDataType, int startingPosition, int length, String defaultValue) {
		this.dataType = pDataType;
		start = startingPosition;
		end = startingPosition + length;
		this.defaultValue = defaultValue;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setAnnotationName(String annotationName) {
		this.annotationName = annotationName;
	}

	public String getAnnotationName() {
		return annotationName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	//TASK0013687
	public Object getDefaultValue() {
		return defaultValue;
	}

	//TASK0013687
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
