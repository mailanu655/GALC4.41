package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

/**
 * 
 * <h3>ObjectTableModel Class description</h3>
 * <p> ObjectTableModel description </p>
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
public class SortableObjectTableModel<T> extends SortableTableModel<T>{
	private static final long serialVersionUID = 1L;
	List<ColumnMapping> columnMappings;
	
	public SortableObjectTableModel(JTable table,ColumnMapping[] columnMappings) {
		
		this(table,null,getColumnMappings(columnMappings));
		
	}
	
	public SortableObjectTableModel(JTable table,String[] columnNames) {
		
		this(table,null,getColumnMappings(columnNames));
		
	}	
	
	public SortableObjectTableModel(List<T> items, List<ColumnMapping> columnMappings) {
		super(items, getColumnNames(columnMappings));
		this.columnMappings = columnMappings;
	}
	
	public SortableObjectTableModel(JTable table, List<T> items, List<ColumnMapping> columnMappings) {
		super(items, getColumnNames(columnMappings),table);
		this.columnMappings = columnMappings;
	}

	private static String[] getColumnNames(List<ColumnMapping> columnMappings) {
		
		String[] columnNames = new String[columnMappings.size()]; 
		for(int i = 0;i < columnMappings.size();i++) {
			columnNames[i] = columnMappings.get(i).getName();
		}
		return columnNames;
	}
	
	private static List<ColumnMapping> getColumnMappings(ColumnMapping[] columnMappings) {
		
		List<ColumnMapping> items = new ArrayList<ColumnMapping>();
		
		for(ColumnMapping item : columnMappings) {
			items.add(item);
		}
		return items;
	}
	
	private static List<ColumnMapping> getColumnMappings(String[] columnNames) {
		
		List<ColumnMapping> items = new ArrayList<ColumnMapping>();
		
		for(String name : columnNames) {
			items.add(new ColumnMapping(name));
		}
		return items;
	}
	
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(rowIndex >= getRowCount()) return null;
    	T object = items.get(rowIndex);
    	if(object instanceof String) return object;
    	else  if(object instanceof TableRow) 
    		return ((TableRow)object).getValue(columnIndex);
    	else if(object instanceof Object[]){
    		Object[] item = (Object[]) object;
    		return columnIndex < item.length ? item[columnIndex] : null; 
    	}
    	ColumnMapping columnMapping = columnMappings.get(columnIndex);
    	if(columnMapping.getName().equals("#")) return rowIndex + 1;
    	try {
			return columnMappings.get(columnIndex).invoke(object);
		} catch (Exception e) {
			return null;
		}
	}  
	
	public Class<?> getColumnClass(int columnIndex) {
		
		return columnMappings.get(columnIndex).getType();
		
	}	
}
