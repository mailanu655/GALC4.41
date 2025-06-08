package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.SubProduct;

public class SubProductTableModel extends BaseTableModel<SubProduct> {

	private static final long serialVersionUID = 1L;
	
	public SubProductTableModel(JTable table,List<SubProduct> items) {
		super(items, new String[] {"#","Product ID","Production Lot", "Sub Id"},table);
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		SubProduct item = getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return rowIndex + 1;
            case 1: return item.getProductId();
            case 2: return item.getProductionLot();
            case 3: return item.getSubId();
        }
        return null;
    }

}
