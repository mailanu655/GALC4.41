package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.teamleader.ProductUpdateStatusCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;

public class ProductUpdateStatusTableModel extends SortableTableModel<ProductUpdateStatus>{

	private static final long serialVersionUID = 1L;
	
	private CommonTlPropertyBean propertyBean;
	
	final static String[] columns =  new String[] {
		"Product Id","Product Spec","Status"
	};
	int[] columWidths = {180,180,180};
	
	public ProductUpdateStatusTableModel(List<ProductUpdateStatus> items, String[] columnNames, JTable table) {
		super(items, columnNames, table);
	}
	
	private void setColumnRenders() {
		for(int i=0;i<this.columns.length;i++){
			String column = this.columns[i];
			if(column.equalsIgnoreCase("STATUS")){
				table.getColumnModel().getColumn(i).setCellRenderer(new ProductUpdateStatusCellRender(this.propertyBean.getOkImage(),this.propertyBean.getNgImage()));
			}
		}
		
	}
	
	public ProductUpdateStatusTableModel(JTable table,List<ProductUpdateStatus> items, CommonTlPropertyBean property) {
		super(items, columns, table);
		this.propertyBean = property;
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		setColumnWidths(columWidths);
		setColumnRenders();
	}
	
	public boolean isCellEditable (int row, int column){
		return false;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		ProductUpdateStatus product = (ProductUpdateStatus)items.get(rowIndex);

		switch(columnIndex) {
			case 0: return product.getProduct();
			case 1: return product.getProductSpecCode();
			case 2: return product.isCreated();
		
		}
		return null;

	}
	
	public void setValueAt(Object value, int rowIndex, int column) {
		if(!isCellEditable(rowIndex, column)) return;
		super.setValueAt(value, rowIndex, column);
		ProductUpdateStatus product = items.get(rowIndex);
		this.fireTableCellUpdated(rowIndex, column);
	}
}
