package com.honda.galc.client.ui.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * <h3>ColumnMapping Class description</h3>
 * <p> ColumnMapping description </p>
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
 * May 4, 2011
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ColumnMapping {
	private String  name;
	private Class<?>   type = String.class;
	private String  methodName;
	private boolean isEditable = false;
	
	public ColumnMapping(String name) {
		this.name = name;
		this.methodName = deriveMethodName(convertToMethodName(name));
	}
	
	public ColumnMapping(String name, Class<?> type) {
		this(name);
		this.type = type;
		this.methodName = deriveMethodName(convertToMethodName(name));
	}
	
	public ColumnMapping(String name, String methodName) {
		this.name = name;
		this.methodName = deriveMethodName(methodName);
	}
	
	public ColumnMapping(String name, String methodName,boolean isEditable) {
		this(name,methodName);
		this.isEditable= isEditable;
	}

	public ColumnMapping(String name, Class<?> type,String methodName) {
		this.name = name;
		this.type = type;
		this.methodName = deriveMethodName(methodName);
	}
	
	public ColumnMapping(String name, Class<?> type,String methodName,boolean isEditable) {
		this(name,type,methodName);
		this.isEditable= isEditable;
	}
	
	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	private String deriveMethodName(String name) {
		 
		String result = type.equals(Boolean.class) ? "is" : "get";
		
		if(name.startsWith("get") || name.startsWith("is")) return name;
		
		 result +=StringUtils.upperCase(StringUtils.substring(name, 0, 1));
		 result +=StringUtils.substring(name, 1);
		 return result;
	}
	
	private String convertToMethodName(String name) {
		String result = "";
		String[] names = name.split(" _-");
		for(String str : names) {
			 String lowercaseStr = StringUtils.lowerCase(str);
			 result +=StringUtils.upperCase(StringUtils.substring(lowercaseStr, 0, 0));
			 result +=StringUtils.substring(lowercaseStr, 1);
		}
		 
		 return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getSetterMethodName() {
		if(StringUtils.isEmpty(methodName)) return "";
		if(methodName.startsWith("is")) return "set" + methodName.substring(2);
		if(methodName.startsWith("get")) return "set" + methodName.substring(3);
		return "";
	}
	
	public Object invoke(Object obj) throws SecurityException,NoSuchMethodException,
    IllegalArgumentException, IllegalAccessException, 
    InvocationTargetException{

		Method method = obj.getClass().getMethod(methodName, null);
		return method.invoke(obj, null);
		
	}

}
