package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <h3>ColumnMappings Class description</h3>
 * <p> ColumnMappings description </p>
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
public class ColumnMappings {
	
	private List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
	
	public ColumnMappings() {
		
	}
	
	public static ColumnMappings with(String name) {
		
		ColumnMappings columnMappings = new ColumnMappings();
		return columnMappings.put(name);
		
	}
	
	public static ColumnMappings with(String name,String methodName) {
		
		ColumnMappings columnMappings = new ColumnMappings();
		return columnMappings.put(name,methodName);
		
	}
	
	public static ColumnMappings with(String name,Class<?> type,String methodName) {
		
		ColumnMappings columnMappings = new ColumnMappings();
		return columnMappings.put(name,type,methodName);
		
	}
	
	public static ColumnMappings with(String[] headings){
		ColumnMappings columnMappings = new ColumnMappings();
		for(int i=0;i<headings.length;i++)
			columnMappings.put(headings[i]);
		return columnMappings;
	}
	public static ColumnMappings with(String[] headings, String[] methodNames) {
		ColumnMappings columnMappings = new ColumnMappings();
		int count = Math.min(headings.length, methodNames.length);
		for (int i = 0; i<count;i++)
			columnMappings.put(headings[i], methodNames[i]);
		return columnMappings;
	}
	
	public ColumnMappings put(String name) {
		columnMappings.add(new ColumnMapping(name));
		return this;
	}
	
	public ColumnMappings put(String name,String methodName) {
		columnMappings.add(new ColumnMapping(name,methodName));
		return this;
	}
	
	public ColumnMappings put(String name,String methodName,boolean isEditable) {
		columnMappings.add(new ColumnMapping(name,methodName,isEditable));
		return this;
	}	
	
	public ColumnMappings put(String name,Class<?> type,String methodName) {
		columnMappings.add(new ColumnMapping(name,type,methodName));
		return this;
	}
	
	public ColumnMappings put(String name,Class<?> type,String methodName,boolean isEditable) {
		columnMappings.add(new ColumnMapping(name,type,methodName,isEditable));
		return this;
	}
	
	public List<ColumnMapping> get(){
		return columnMappings;
	}
	
}
