package com.honda.galc.common;

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
public class ParsingData {
	
	private String fieldName = null;
	private String methodName = null;
	private String columnName = null;
	private Class<?> dataType = null;
	private int start = 0;
	private int end = 0;
	
	public ParsingData() {
	}
	
	ParsingData(String strFieldName, String columnName, Class<?> pDataType, int startingPosition, int length) {
		this.fieldName = strFieldName;
		this.columnName = columnName;
		this.methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		this.dataType = pDataType;
		start = startingPosition;
		end = startingPosition + length;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
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

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

}
