package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.PreProductionLot;

public class PreProductionLotTableModel extends BaseTableModel<PreProductionLot> {

	private static final long serialVersionUID = 1L;
	
	public PreProductionLotTableModel(JTable table,List<PreProductionLot> items) {
		super(items, new String[] {"Plant","Production Lot","KD Lot", "Lot Size", "Product Spec Code"},table);
	}
	
	public PreProductionLotTableModel(JTable table,List<PreProductionLot> items, boolean displaySequence) {
			
		super(items, new String[] {"Plant","Production Lot","KD Lot", "Lot Size", "Product Spec Code","Sequence"},table);
	}

	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		PreProductionLot preProductionLot = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0:return preProductionLot.getProductionLot().substring(5,6);
            case 1: return preProductionLot.getProductionLot();
            case 2: return preProductionLot.getKdLot();
            case 3: return preProductionLot.getLotSize();
            case 4: return preProductionLot.getProductSpecCode();
            case 5: return preProductionLot.getLotPosition();
        }
        return null;
    }


}
