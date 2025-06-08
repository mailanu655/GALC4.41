package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.MeasurementSpec;

public class MeasurementSpecCopyTableModel extends BaseTableModel<MeasurementSpec>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MeasurementSpecCopyTableModel(JTable table,List<MeasurementSpec> items) {
		super(items,new String[] {"Sequence Number","Min Limit","Max Limit", "Max Attempts"}, table);
		// TODO Auto-generated constructor stub
	}

	public boolean isCellEditable (int row, int column){
        // label column is editable
		return false;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		MeasurementSpec spec = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return  spec.getId().getMeasurementSeqNum();
            case 1: return spec.getMinimumLimit();
            case 2: return spec.getMaximumLimit();
            case 3: return spec.getMaxAttempts();
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <3) return;
		super.setValueAt(value, row, column);
		MeasurementSpec spec = getItem(row);
		try{
			switch(column) {
				case 1: spec.setMinimumLimit(parseDouble(value.toString()));break;
				case 2: spec.setMaximumLimit(parseDouble(value.toString()));break;
				case 3: spec.setMaxAttempts(parseInt(value.toString()));break;
			}
		}catch(NumberFormatException e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}    

}
