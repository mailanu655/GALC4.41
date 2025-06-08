package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;

public class ValueTableModel extends SortableTableModel<String> {

	private static final long serialVersionUID = 1L;

	public ValueTableModel(List<String> items, String columnName, JTable table) {
		super(items, new String[] {"#",columnName}, table);
		
		setAlignment(JLabel.CENTER);
		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		String item = getItem(rowIndex);
		
		switch(columnIndex) {
			case 0: return rowIndex + 1;
			case 1: return item;
		}
		
		return null;
	}
}
