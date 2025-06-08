package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Frame;

public class StopShipProductTableModel extends SortableTableModel<BaseProduct>{

	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"Product Id","Product SpecCode", "AF Sequence", "Tracking Status"};
	final static String[] columnsMbpn =  new String[] {	"Product Id","Product SpecCode", "Tracking Status"};
	
	int[] columWidths = {180,180,180,180};
	int[] columWidthsMbpn = {180,180,180};
	private ProductType productType;
	
	public StopShipProductTableModel(List<? extends BaseProduct> items, String[] columnNames, JTable table, ProductType type) {
		super((List<BaseProduct>)items, columnNames, table);
		this.productType = type;
		// TODO Auto-generated constructor stub
	}

	
	
	public StopShipProductTableModel(JTable table,List<? extends BaseProduct> items, ProductType type) {
		super((List<BaseProduct>)items, (ProductType.FRAME==type)? columns : columnsMbpn, table);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		setColumnWidths((ProductType.FRAME==type)? columWidths : columWidthsMbpn);
		this.productType = type;
	}
	
	public boolean isCellEditable (int row, int column){
		return false;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		
		if(ProductType.FRAME == productType) {
			Frame product = (Frame)items.get(rowIndex);

			switch(columnIndex) {
			case 0: return product.getId();
			case 1: return product.getProductSpecCode();
			case 2: return product.getAfOnSequenceNumber();
			case 3: return product.getTrackingStatus();
			}
		} else {
			BaseProduct product = (BaseProduct)items.get(rowIndex);

			switch(columnIndex) {
			case 0: return product.getId();
			case 1: return product.getProductSpecCode();			
			case 2: return product.getTrackingStatus();
			} 
		}
        return null;

    }
	
	public void setValueAt(Object value, int rowIndex, int column) {
		if(!isCellEditable(rowIndex, column)) return;
		super.setValueAt(value, rowIndex, column);
		BaseProduct product = items.get(rowIndex);
		this.fireTableCellUpdated(rowIndex, column);
	}
}
