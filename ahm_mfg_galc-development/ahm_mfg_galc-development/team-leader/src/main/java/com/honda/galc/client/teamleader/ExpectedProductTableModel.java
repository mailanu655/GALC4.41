package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ExpectedProduct;

public class ExpectedProductTableModel extends SortableTableModel<ExpectedProduct>{
	private static final long serialVersionUID = 5216933095932361418L;
	final static String[] columns =  new String[] {
		"#","PROCESS_POINT_ID", "PRODUCT_ID"
	};
	int[] columWidths = {80, 360, 360};
	
	public ExpectedProductTableModel(JTable table,List<ExpectedProduct> items) {
		
		super(items, columns, table);
		setColumnWidths(columWidths);
	}
	
	public boolean isCellEditable (int row, int column){
		return column == 2;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		
		switch(columnIndex) {
		case 0: return rowIndex + 1;
		default:
			return super.getValueAt(rowIndex, columnIndex);

		}

	}
	
	public void setValueAt(Object value, int row, int column) {
		if(!isCellEditable(row, column)) return;
		super.setValueAt(value, row, column);
		ExpectedProduct expectedProduct = getItem(row);
		try{
			if(column == 2) expectedProduct.setProductId((String)value);
			
		}catch(Exception e) {
		    Logger.getLogger().warn(e, "Exception to set value.");
			return;
		}
		
		this.fireTableCellUpdated(row, column);

	}

	public ExpectedProduct findExpectedProduct(
			ExpectedProduct selectedExpectedProduct) {
		// TODO Auto-generated method stub
		return null;
	}

}
