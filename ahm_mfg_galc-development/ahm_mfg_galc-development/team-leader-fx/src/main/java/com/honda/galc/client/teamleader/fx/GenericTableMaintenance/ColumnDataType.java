
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.lang.reflect.Constructor;
import java.util.TreeMap;
/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public enum ColumnDataType {
	STRING ("java.lang.String"),
	INTEGER ("java.lang.Integer"),
	DOUBLE ("java.lang.Double"),
	LONG ("java.lang.Long"),
	BOOLEAN ("java.lang.Boolean");
	
	private String className = "java.lang.String";



	private static TreeMap<String, ColumnDataType> classNameMap;
	
	
	private ColumnDataType (String className) {
		setClassName(className);
	}
	
	static {
		classNameMap = new TreeMap<String, ColumnDataType>();		
		for (ColumnDataType clmDataType: ColumnDataType.values()) {
			classNameMap.put(clmDataType.getClassName(), clmDataType);
		}
	}
		
	public static ColumnDataType getEnum(String className) {
		return classNameMap.get(className);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}
	

	public Object getValidObj(String input) {
		try {
			Constructor<?>	constructor = Class.forName(getClassName()).getConstructor(String.class);
			return constructor.newInstance(input);
		} catch (Exception e) {
			return null;
		}
	

	}	
	
}
