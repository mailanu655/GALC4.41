package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.SubProductShippingDetail;

public class SubProductShippingDetailTableModel extends BaseTableModel<SubProductShippingDetail> {

	private static final long serialVersionUID = 1L;
	
	public SubProductShippingDetailTableModel(JTable table,List<SubProductShippingDetail> items) {
		super(items, new String[] {"#","KD Lot","Production Lot", "Sub Id", "Seq No", "Product Id"},table);
		
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		setAlignment(JLabel.CENTER);
		
		pack();
		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		SubProductShippingDetail item = getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return rowIndex +1;
            case 1: return item.getId().getKdLotNumber();
            case 2: return item.getProductionLot();
            case 3: return item.getId().getSubId();
            case 4: return item.getId().getProductSeqNo();
            case 5: return item.getProductId();
        }
        return null;
    }
	

}
