package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.common.logging.Logger;


public class RepairPartTableModel extends BaseTableModel<RepairPart> {
	
	private static final long serialVersionUID = 1L;
	
	private boolean editable = true;
	
	public RepairPartTableModel(JTable table,List<RepairPart> items, boolean editable, boolean autoSize) {
		super(items, new String[] {"Part Name", "Sequence No"},table);
		this.setAlignment(JLabel.CENTER);
		if(!autoSize)
			this.setColumnWidths(new int[] {120,80});
		
		this.editable = editable;
	}
	
	
	public RepairPartTableModel(JTable table,List<RepairPart> items) {
		this(table,items,true);
	}
	
	public RepairPartTableModel(JTable table,List<RepairPart> items,boolean editable) {
		super(items, new String[] {"Part Name", "Sequence No"},table);
		this.setAlignment(JLabel.CENTER);
		this.setColumnWidths(new int[] {120,80});
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
		RepairPart repairPart = getItem(rowIndex);
        if (repairPart == null)
        	return null;
        switch(columnIndex) {
            case 0: return repairPart.getPartName() == null ? null : repairPart.getPartName().getPartName();
            case 1: return repairPart.getSequenceNo();
            default: return null;
          }
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column == 0) return;
		super.setValueAt(value, row, column);
		RepairPart part = getItem(row);
		try{
			switch(column) {
				case 1:{
					if(value instanceof Integer)part.setSequenceNo(parseInt(value.toString()));
					if(value instanceof String)part.setSequenceNo(parseInt(value.toString()));
					break;
				}
			}
		}catch(Exception e) {
			Logger.getLogger().error(e.getCause().toString());
			return;
		}
		this.fireTableCellUpdated(row, column);
	}    

	
}
