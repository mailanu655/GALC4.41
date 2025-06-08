package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * <h3>ColumnSpecList Class description</h3>
 * <p> ColumnSpecList description </p>
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
 * @author Suriya Sena  JavaFx Migration
 * Feb 12, 2014
 *
 *
 */
public class ColumnMappingList {
	
	private List<ColumnMapping> columnMappingList = new ArrayList<ColumnMapping>();
	
	public ColumnMappingList() {
	}

	public static ColumnMappingList with(String title) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		return columnMappingList.put(title);
	}
	
	public static ColumnMappingList with(String title, String accessor) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		return columnMappingList.put(title,accessor);
	}

	public static ColumnMappingList with(String title,String accessor, Class<?> type) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		return columnMappingList.put(title,accessor,type);
	}

	public static ColumnMappingList with(String title, String accessor, Class<?> type, String format) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		return columnMappingList.put(title,accessor,type,format);
    }
	
	public static ColumnMappingList with(String title,String accessor, Class<?> type,String format, boolean isEditable) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		return columnMappingList.put(title,accessor,type,format,isEditable);
	}

	public static ColumnMappingList with(String title, String accessor, Class<?> type,String format, boolean isEditable, boolean isSortable) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		return columnMappingList.put(title,accessor,type,format,isEditable,isSortable);
	}
	
	public static ColumnMappingList with(String[] title, String[] accessor) {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		int count = Math.min(title.length, accessor.length);
		for (int i = 0; i<count;i++) {
			columnMappingList.put(title[i], accessor[i], String.class);
		}
		return columnMappingList;
	}
	
	
	public ColumnMappingList put(String title) {
		columnMappingList.add(new ColumnMapping(title));
		return this;
	}
	
	public ColumnMappingList put(String title, String accessor) {
		columnMappingList.add(new ColumnMapping(title,accessor));
		return this;
	}

	public ColumnMappingList put(String title,String accessor, Class<?> type) {
		columnMappingList.add(new ColumnMapping(title,accessor,type));
		return this;
	}

	public ColumnMappingList put(String title, String accessor, Class<?> type, String format) {
		columnMappingList.add(new ColumnMapping(title,accessor,type,format));
		return this;
    }
	
	public ColumnMappingList put(String title,String accessor, Class<?> type,String format, boolean isEditable) {
		columnMappingList.add(new ColumnMapping(title,accessor,type,format,isEditable));
		return this;
	}

	public ColumnMappingList put(String title, String accessor, Class<?> type,String format, boolean isEditable, boolean isSortable) {
		columnMappingList.add(new ColumnMapping(title,accessor,type,format,isEditable,isSortable));
		return this;
	}
	
	public List<ColumnMapping> get(){
		return columnMappingList;
	}
}
