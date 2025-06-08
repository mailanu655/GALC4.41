package com.honda.galc.client.teamleader.model;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.teamleader.ProductPrintQueueItem;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.BaseProduct;

/** * * 
* 
* @author Gangadhararao Gadde 
* @since May 16, 2016
*/

public abstract class ProductTableModel extends SortableTableModel<BaseProduct> {

	private static final long serialVersionUID = 1L;
	public String message = null;
	public ProductPrintQueueItem _printItem = null;
	public List <ProductPrintQueueItem> _selectedPrintList= null;
	
	public ProductTableModel(List<BaseProduct> items, JTable table) {
		super(items, new String[] {"#", "AfOnSeq","Product ID", "ProductSpecCode", "ProductionDate"},table);
		
		for(int i= 0; i<getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(new Renderer());
		}
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
	}
	
	public void sendPrintStatusList(List<ProductPrintQueueItem> selectedPrintList)
	{
		
			_selectedPrintList = new ArrayList<ProductPrintQueueItem>();
			_selectedPrintList = selectedPrintList;
		
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		Product item = (Product)getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return rowIndex + 1;
          //  case 1: return Integer.toString(item.getAfOnSequenceNumber());
        	
            case 1: return item.getProductId();
            case 2: return item.getProductSpecCode();
            case 3: return item.getProductionDate() != null ? item.getProductionDate().toString() : "";
           
        }
        return null;
    }
	
	public boolean greenColorCheck(JTable table, int row, int column)
	{
		if(_selectedPrintList != null && _selectedPrintList.size()>0)
		{
			for(ProductPrintQueueItem selectedItem: _selectedPrintList)
			{
				BaseProduct product = 	items.get(row);
				if(selectedItem.getProductId().equals(product.getProductId()) && selectedItem.getStatus().equals("SUCCESS"))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean redColorCheck(JTable table, int row, int column)
	{
		if(_selectedPrintList != null && _selectedPrintList.size()>0)
		{
			for(ProductPrintQueueItem selectedItem: _selectedPrintList)
			{
				BaseProduct product = 	items.get(row);
				if(selectedItem.getProductId().equals(product.getProductId()) && selectedItem.getStatus().equals("FAIL"))
				{
					return true;
				}
			}
		}
		return false;
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
			if(isSelected && greenColorCheck(table, row, column))
				comp.setBackground(Color.GREEN);
			if(isSelected && redColorCheck(table, row, column))
					comp.setBackground(Color.RED);
			
			return comp;
	   }
    }

}
