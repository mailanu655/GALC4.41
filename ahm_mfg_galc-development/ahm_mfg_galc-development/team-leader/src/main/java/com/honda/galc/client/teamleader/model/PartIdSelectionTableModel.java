package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.teamleader.model.PartIdSelection;
import com.honda.galc.client.ui.component.BaseTableModel;


public class PartIdSelectionTableModel extends BaseTableModel<PartIdSelection> {
	
	private static final long serialVersionUID = 1L;
	
	private boolean editable = false;
	
	public PartIdSelectionTableModel(JTable table,List<PartIdSelection> items) {
		super(items, new String[] {"Part Id","Part Mask","Part Mark", "Part Number","Apply"},table);
		this.setAlignment(JLabel.CENTER);
		
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		PartIdSelection partIdSelection = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return partIdSelection.getPartId();
            case 1: return partIdSelection.getPartMask();
            case 2: return partIdSelection.getPartMark();
            case 3: return partIdSelection.getPartNumber();
            case 4: return partIdSelection.isApply();
         
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		PartIdSelection partIdSelection = getItem(row);
		String valueString = value == null ? null : value.toString().trim();
		try{
			switch(column) {
			    case 0: partIdSelection.setPartId(valueString);break;
				case 1: partIdSelection.setPartMask(valueString);break;
				case 2: partIdSelection.setPartMark(valueString);break;
				case 3:	partIdSelection.setPartNumber(valueString);break;
				case 4: partIdSelection.setApply((Boolean)value);break;
				

			}
		}catch(Exception e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}    

	/**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class<?> getColumnClass(int columnIndex) {
    	if(columnIndex ==4 ) return Boolean.class;
    	return Object.class;
    }
    
    public boolean isCellEditable (int row, int column){
        // label column is editable
		return column == 4 ? true  : false;
    }
}
