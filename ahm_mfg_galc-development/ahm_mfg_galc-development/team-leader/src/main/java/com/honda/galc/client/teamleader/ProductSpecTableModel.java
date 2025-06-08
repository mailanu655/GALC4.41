package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;


public class ProductSpecTableModel  extends BaseTableModel<String>{
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {
		"Spec Code"
	};
	int[] columWidths = { 180};
	public ProductSpecTableModel(JTable table,List<String> items) {
		super(items, columns, table);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		setColumnWidths(columWidths);
	}
	
	public boolean isCellEditable (int row, int column){
		return false;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		String specCode = (String)items.get(rowIndex);

		switch(columnIndex) {
			case 0: return specCode;
		}
		return null;

	}
	
	public void setValueAt(Object value, int rowIndex, int column) {
		if(!isCellEditable(rowIndex, column)) return;
		super.setValueAt(value, rowIndex, column);
		String specCode = items.get(rowIndex);
		this.fireTableCellUpdated(rowIndex, column);
	}

}
