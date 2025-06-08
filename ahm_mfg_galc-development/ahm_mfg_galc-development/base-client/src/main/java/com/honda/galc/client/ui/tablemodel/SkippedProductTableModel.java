package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.enumtype.SkippedProductStatus;
import com.honda.galc.entity.product.SkippedProduct;

public class SkippedProductTableModel extends SortableTableModel<SkippedProduct> {

	private static final long serialVersionUID = 1L;
	
	public SkippedProductTableModel(JTable table,List<SkippedProduct> items) {
		
		super(items, new String[] {"#","PROCESS POINT ID","Product ID","SUB ID", "KD LOT NUMBER", "SPEC CODE", "STATUS"},table);
		
		setStatusIdComboBoxCell();
		
	}
	
	public boolean isCellEditable (int row, int column){
		return column == 6;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		SkippedProduct item = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return item.getId().getProcessPointId();
            case 2: return item.getId().getProductId();
            case 3: return item.getSubId();
            case 4: return item.getKdLotNumber();
            case 5: return item.getProductSpecCode();
            case 6: return item.getStatus().toString();
        }
        return null;
    }

	private void setStatusIdComboBoxCell() {
		
		Object [] status = SkippedProductStatus.values();
		TableColumn col = table.getColumnModel().getColumn(6);
		col.setCellEditor(new ComboBoxCellEditor(status,true));
		col.setCellRenderer(new ComboBoxCellRender(status));
		
	}
	
	public void setValueAt(Object value, int row, int column) {
		if(!isCellEditable(row, column)) return;
		super.setValueAt(value, row, column);
		SkippedProduct skippedProduct = getItem(row);
		if(skippedProduct.getStatus() != SkippedProductStatus.valueOf(value.toString())){
			skippedProduct.setStatus(SkippedProductStatus.valueOf(value.toString()));
			this.fireTableCellUpdated(row, column);
		}
	}
}
