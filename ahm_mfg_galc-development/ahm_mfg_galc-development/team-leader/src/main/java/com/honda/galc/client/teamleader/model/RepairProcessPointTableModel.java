package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.RepairProcessPoint;


public class RepairProcessPointTableModel extends BaseTableModel<RepairProcessPoint> {
	
	private static final long serialVersionUID = 1L;
	
	private boolean editable = true;
	
	public RepairProcessPointTableModel(JTable table,List<RepairProcessPoint> items, boolean editable, boolean autoSize) {
		super(items, new String[] {"Part Id", "Device Id","Instruction Code"},table);
		this.setAlignment(JLabel.CENTER);
		if(!autoSize)
			this.setColumnWidths(new int[] {80,100,80,80});
		
		this.editable = editable;
	}
	
	
	public RepairProcessPointTableModel(JTable table,List<RepairProcessPoint> items) {
		this(table,items,true);
	}
	
	public RepairProcessPointTableModel(JTable table,List<RepairProcessPoint> items,boolean editable) {
		super(items, new String[] {"Part Id", "Device Id","Instruction Code"},table);
		this.setAlignment(JLabel.CENTER);
		this.setColumnWidths(new int[] {80,150,80});
		this.editable = editable;
	}	
		
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isCellEditable (int row, int column){
		return editable && column > 0;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		RepairProcessPoint repairProcess = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return repairProcess.getId().getPartId();
            case 1: return repairProcess.getDeviceId();
            case 2: return repairProcess.getInstructionCode();
          }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column == 0) return;
		super.setValueAt(value, row, column);
		RepairProcessPoint part = getItem(row);
		try{
			switch(column) {
				case 1: part.setDeviceId((String)value);break;
				case 2: part.setInstructionCode(parseString((String)value,3));break;
			}
		}catch(Exception e) {
			Logger.getLogger().error(e.getCause().toString());
			return;
		}
		this.fireTableCellUpdated(row, column);
	}    

	private String parseString(String valueString, int length) {
		int len = StringUtils.length(valueString);
		if(len == length || (len < length && len > 0) ) return valueString;
		else {
			if(len == 0){
				MessageDialog.showError(getParentComponent(),"Input cannot be empty");
				throw new DataConversionException("Input cannot be empty");
			}else{
				MessageDialog.showError(getParentComponent(),"Input must be equal to or less than " + length + " characters");
				throw new DataConversionException("Input must be equal to or less than " + length + " characters");
			}
		}
	}
	
}
