package com.honda.galc.client.ui.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.SubProductShippingDetail;

public class KnuckleShippingCartTableModel extends BaseTableModel<SubProductShippingDetail>{

	
	private static final long serialVersionUID = 1L;
	int currentKnucklePosition = 0;
	
	int rowCount = 0;
	
	public KnuckleShippingCartTableModel(List<SubProductShippingDetail> items, JTable table,int cartSize) {
		
		super(items, new String[]{"#","KD Lot","KSN"}, table);
		table.setDefaultRenderer(Object.class, new Renderer());
		this.rowCount = cartSize;
		setAlignment(JLabel.CENTER);
		
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	public int getRowCount() {
        return rowCount;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		SubProductShippingDetail shippingDetail = rowIndex <= getItems().size() ? getItem(rowIndex ) : null;
       
        switch(columnIndex) {
            case 0:return rowIndex + 1;
            case 1: return shippingDetail == null ? null:shippingDetail.getId().getKdLotNumber();
            case 2: return shippingDetail == null ? null: shippingDetail.getProductId();
        }
        return null;
    }
	
	public int getCurrentKnucklePosition() {
		return currentKnucklePosition;
	}

	public void setCurrentKnucklePosition(int currentKnucklePosition) {
		this.currentKnucklePosition = currentKnucklePosition;
	}
	
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(this.getHorizontalAlignment());
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(column == 2 && currentKnucklePosition != 0 && currentKnucklePosition ==  row + 1) {
				comp.setBackground(Color.GREEN);
			}
			return comp;
	   }
    }

}
