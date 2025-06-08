package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.fif.Bom;


public class PartSpecFromBomTableModel extends SortableTableModel<Bom> {
	
	private static final long serialVersionUID = 1L;
	
	private boolean editable = false;
	private final TableColumn partColorTableColumn;
	
	public PartSpecFromBomTableModel(JTable table,List<Bom> items) {
		super(items, new String[] {"Model Year","Part Number","Part Color","Apply"},table);
		this.setAlignment(JLabel.CENTER);
		this.partColorTableColumn = getTable().getColumnModel().getColumn(2);
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		Bom partSpecFromBom = getItem(rowIndex);
        
        switch(columnIndex) {
        	case 0: return partSpecFromBom.getId().getMtcModel().substring(0, 1);
            case 1: return partSpecFromBom.getId().getPartNo();
            case 2: return StringUtils.isBlank(partSpecFromBom.getId().getPartColorCode()) ? "*" : partSpecFromBom.getId().getPartColorCode();
            case 3: return partSpecFromBom.isApply();
         }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		Bom partSpecFromBom = getItem(row);
		String valueString = value == null ? null : value.toString().trim();
		switch(column) {
			 	case 0:	partSpecFromBom.setModelYear(valueString.substring(0, 1));break;
			    case 1:	partSpecFromBom.getId().setPartNo(valueString);break;
			    case 2: partSpecFromBom.getId().setPartColorCode(valueString);break;
				case 3: partSpecFromBom.setApply((Boolean)value);break;
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
    	if(columnIndex == 3) return Boolean.class;
    	return Object.class;
    }
    
    public boolean isCellEditable (int row, int column){
        // label column is editable
		return column == 3 ? true  : false;
    }
    
    public void showPartColor(boolean show) {
    	boolean hasPartColorColumn = false;
    	for (int i = 0; i < getTable().getColumnCount(); i++) {
    		if (ObjectUtils.equals(this.partColorTableColumn.getIdentifier(), getTable().getColumnModel().getColumn(i).getIdentifier())) {
    			hasPartColorColumn = true;
    			break;
    		}
    	}
    	if (show && !hasPartColorColumn) {
    		getTable().getColumnModel().addColumn(this.partColorTableColumn);
    		getTable().getColumnModel().moveColumn(getTable().getColumnModel().getColumnIndex(this.partColorTableColumn.getIdentifier()), 2);
    	} else if (!show && hasPartColorColumn) {
    		getTable().getColumnModel().removeColumn(this.partColorTableColumn);
    	}
    }
}
