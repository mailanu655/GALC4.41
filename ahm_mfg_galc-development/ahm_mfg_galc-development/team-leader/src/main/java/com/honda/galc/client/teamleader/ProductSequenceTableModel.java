package com.honda.galc.client.teamleader;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.ProductSequence;

public class ProductSequenceTableModel extends BaseTableModel<ProductSequence>{
	private static final long serialVersionUID = 1L;
	
	final static String[] columns =  new String[] {
		"#","PROCESS_POINT_ID", "PRODUCT_ID", "DATE"
	};
	int[] columWidths = {80, 270, 270, 180};

	private int editableCellRow = -1;
	private int editableCellColumn = -1;
	
	public ProductSequenceTableModel(JTable table,List<ProductSequence> items) {
		
		super(items, columns, table);
		setColumnWidths(columWidths);
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		ProductSequence productSequence = items.get(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return productSequence.getId().getProcessPointId();
		case 2: return productSequence.getId().getProductId();
		case 3:
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(productSequence.getReferenceTimestamp());
		default:
			return super.getValueAt(rowIndex, columnIndex);

		}
	}
	
	public void setValueAt(Object value, int row, int column) {
		if(!isCellEditable(row, column)) return;
		super.setValueAt(value, row, column);
		ProductSequence productSequece = getItem(row);
		try{
			if(column == 2) productSequece.getId().setProductId(value.toString());
			
		}catch(Exception e) {
		    Logger.getLogger().warn(e, "Exception to set value.");
			return;
		}
		
		this.fireTableCellUpdated(row, column);
	}
	
	
	public boolean isCellEditable (int row, int column){
		return editableCellRow  == row && editableCellColumn == column;
    }

	public ProductSequence findProductSequence(ProductSequence productSequence) {
		for(ProductSequence item : getItems()){
			if(productSequence.getId().equals(item.getId())) return item;
		}
		return null;
	}
	
	public void setEditableCell(int row, int column){
		editableCellRow = row;
		editableCellColumn = column;
	}
	
	public void disableEditableCell(){
		editableCellRow = -1;
		editableCellColumn = -1;
	}
	

}
