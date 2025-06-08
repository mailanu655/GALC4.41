package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.MeasurementSpec;

public class MeasurementSpecListTableModel extends BaseTableModel<MeasurementSpec> {
	
	private static final long serialVersionUID = 1L;
	
	public MeasurementSpecListTableModel(JTable table,List<MeasurementSpec> items) {
		super(items, new String[] {"#","Min","Max","Attempts"},table);
	}
	
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		MeasurementSpec spec = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return spec.getMinimumLimit();
            case 2: return spec.getMaximumLimit();
            case 3: return spec.getMaxAttempts();
        }
        return null;
    }
}
