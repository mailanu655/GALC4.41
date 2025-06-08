/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.PartName;

/**
 * @author vf031824
 *
 */
public class ExtRequiredPartTableModel extends SortableTableModel<PartName>{
	
	private static final long serialVersionUID = 1L;
	
	public ExtRequiredPartTableModel(JTable table, List<PartName> items) {
		super(items, new String[] {"Part Name", "Ext. Required", "LET Required"}, table);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		PartName extRequiredPart = getItem(rowIndex);
		
		switch(columnIndex) {
		case 0: return extRequiredPart.getPartName();
		case 1: return Boolean.valueOf(extRequiredPart.getExternalRequired() == 1? true : false);
		case 2: return Boolean.valueOf(extRequiredPart.getLETCheckRequired() == 1? true : false);
		}
		return null;
	}
	
	public boolean isCellEditable (int row, int column){
		return column >= 1;
	}
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column < 1) return;
		super.setValueAt(value, row, column);
		PartName partName = getItem(row);
		if(column == 1) {
			int val = ((Boolean) value == true ? 1 : 0);
			partName.setExternalRequired(val);
		}
		if(column == 2){
		    int val = ((Boolean) value == true ? 1 : 0);
		    partName.setLETCheckRequired(val);
		}
		this.fireTableCellUpdated(row, column);
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		return ((columnIndex == 1 || columnIndex ==2) ? Boolean.class : super.getColumnClass(columnIndex));
	}
}