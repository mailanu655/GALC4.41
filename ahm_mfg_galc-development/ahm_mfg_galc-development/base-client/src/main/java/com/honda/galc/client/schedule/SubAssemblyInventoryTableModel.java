package com.honda.galc.client.schedule;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.MbpnProduct;

/**
 * @author Zack chai
 * @date Nov 22, 2013
 */
public class SubAssemblyInventoryTableModel extends SortableTableModel<MbpnProduct> {

	private static final long serialVersionUID = 1L;
	public String message = null;
	public MbpnProduct _item = null;
	final static String[] columnNames = {"Cell/Zone", "Product", "Product Spec Code", "Inventory"};
	
	
	public SubAssemblyInventoryTableModel(List<MbpnProduct> items, JTable table) {
		super(items, columnNames, table);
		
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
	}
	

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		MbpnProduct item = getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return item.getCellZone();
            case 1: return item.getProduct();
            case 2: return item.getProductSpecCode();
            case 3: return item.getActual();
            case 4: return item.getCurrentOrderNo();
        }
        return null;
    }
	
	
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		Color backgroundToSet;
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(isSelected)
			{
				comp.setBackground(Color.YELLOW);
				backgroundToSet = table.getBackground();
			}
			else 
			{
				comp.setBackground(Color.LIGHT_GRAY);
			}
			
			return comp;
	   }
    }

}
