package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.MeasurementSpec;

public class MeasurementSpecTableModel extends BaseTableModel<MeasurementSpec> {

	
	private static final long serialVersionUID = 1L;
	private boolean editable = true;

	public MeasurementSpecTableModel(JTable table,List<MeasurementSpec> items) {
		super(items, new String[] {"Sequence Number","Part Name", "Part Id","Min Limit","Max Limit", "Max Attempts"},table);
	}
	
	
	public MeasurementSpecTableModel(JTable table, List<MeasurementSpec> measurementSpecs, boolean editable) {
		this(table, measurementSpecs);
		this.editable  = editable;
	}


	public boolean isCellEditable (int row, int column){
        // label column is editable
		return (column >= 3) && editable;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		MeasurementSpec spec = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return  spec.getId().getMeasurementSeqNum();
            case 1: return spec.getId().getPartName();
            case 2: return spec.getId().getPartId();
            case 3: return spec.getMinimumLimit();
            case 4: return spec.getMaximumLimit();
            case 5: return spec.getMaxAttempts();
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <3) return;
		super.setValueAt(value, row, column);
		MeasurementSpec spec = getItem(row);
		try{
			switch(column) {
				case 3: spec.setMinimumLimit(parseDouble(value.toString()));break;
				case 4: spec.setMaximumLimit(parseDouble(value.toString()));break;
				case 5: spec.setMaxAttempts(parseInt(value.toString()));break;
			}
		}catch(NumberFormatException e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}    

}
