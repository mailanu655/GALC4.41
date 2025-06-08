package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.RequiredPart;

public class RequiredPartTableModel extends BaseTableModel<RequiredPart> {
	private static final long serialVersionUID = 1L;

	public RequiredPartTableModel(JTable table,List<RequiredPart> items) {
		super(items, new String[] {"#","Product Spec Code", "Process Point Id", "Sub_Id","PART_NAME"}, table);
		
		setSubIdComboBoxCell();
	}
	
	
	public boolean isCellEditable (int row, int column){
		return column >= 3;
    }
	
	 /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class<?> getColumnClass(int columnIndex) {
    	if(columnIndex >=5 && columnIndex <= 7) return Boolean.class;
    	return Object.class;
    }
    
    private void setSubIdComboBoxCell() {
		Object [] subIds = Product.getSubIds();
		TableColumn col = table.getColumnModel().getColumn(3);
		col.setCellEditor(new ComboBoxCellEditor(subIds,true));
		col.setCellRenderer(new ComboBoxCellRender(subIds));
		
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        
		RequiredPart requiredPart = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return requiredPart.getId().getProductSpecCodeValue();
            case 2: return requiredPart.getId().getProcessPointId();
            case 3: return requiredPart.getSubId();
            case 4: return requiredPart.getId().getPartName();
        }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <3 ) return;
		super.setValueAt(value, row, column);
		RequiredPart requiredPart = getItem(row);
		try{
			if(column == 3) requiredPart.setSubId(value.toString());
			else if(column == 4)requiredPart.getId().setPartName(value.toString());
		}catch(Throwable e) {
			return;
		}
		
		this.fireTableCellUpdated(row, column);
	}
	
	public RequiredPart findRequiredPart(RequiredPart requiredPart) {
		for (RequiredPart item : getItems()) {
			if(item.getId().equals(requiredPart.getId())) return item;
		}
		return null;
	}
	
	public boolean hasRequiredPart(RequiredPart requiredPart) {
		 return findRequiredPart(requiredPart) != null;
	}

}
