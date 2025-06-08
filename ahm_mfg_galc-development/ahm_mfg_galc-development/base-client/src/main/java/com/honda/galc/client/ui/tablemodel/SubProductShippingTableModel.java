package com.honda.galc.client.ui.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.SubProductShipping;

public class SubProductShippingTableModel extends BaseTableModel<SubProductShipping> {

	private static final long serialVersionUID = 1L;
	
	public SubProductShippingTableModel(JTable table,List<SubProductShipping> items) {
		super(items, new String[] {"#","KD Lot","Production Lot", "Product Spec Code", "Size", "Actual"},table);
		
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		SubProductShipping item = getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return rowIndex + 1;
            case 1: return item.getKdLotNumber();
            case 2: return item.getProductionLot();
            case 3: return item.getProductSpecCode();
            case 4: return item.getSchQuantity();
            case 5: return item.getActQuantity();
        }
        return null;
    }
	
	private boolean isRowSelected(int row) {
		int selectedRow = table.getSelectedRow();
		if(selectedRow == -1) return false;
		if(row == selectedRow) return true;
		
		SubProductShipping selectedProdLot = getItem(selectedRow);
		SubProductShipping lot = getItem(row);
		if(!selectedProdLot.isSameKdLot(lot)) return false;

		if(row > selectedRow) {
			for(int i = selectedRow;i<=row;i++) {
				SubProductShipping currentProdLot = getItem(i);
				if(currentProdLot.getLineNumber() == selectedProdLot.getLineNumber() 
						&& !selectedProdLot.isSameKdLot(currentProdLot)) return false;
			}
		}
		else if(row < selectedRow) {
			
			for(int i = selectedRow;i>=row;i--) {
				SubProductShipping currentProdLot = getItem(i);
				if(currentProdLot.getLineNumber() == selectedProdLot.getLineNumber() 
						&& !selectedProdLot.isSameKdLot(currentProdLot)) return false;
			}
		}
		return true;
	}
	
	public List<SubProductShipping> getSelectedItems() {
    	List<SubProductShipping> selectedItems = new ArrayList<SubProductShipping>();
    	for(int i=0;i<getRowCount();i++){
    		if(isRowSelected(i)) selectedItems.add(getItem(i));
    	}
    	return selectedItems;
    }
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(this.getHorizontalAlignment());
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			
			if(isRowSelected(row)) {
				comp.setBackground(Color.GREEN);
			}
			
			return comp;
	   }
    }

}
