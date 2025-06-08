package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class FilteredLabeledComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
	FilteredLabeledComboBox box ;
	Object[] values;
	boolean editable;	
	
	public FilteredLabeledComboBoxCellEditor(Object[] items, boolean edit)
	{
		editable = edit;
		values = items;		
		box = new FilteredLabeledComboBox("");
		box.setModel(values, -1);		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {			 
		 if(value != null){			 
			 box.getComponent().setSelectedItem(value);
		 }
		 return box.getComponent();
	}
	
	@Override
	public Object getCellEditorValue() {		
		return box.getComponent().getSelectedItem();		
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return editable;
	}
	
	@Override
	public boolean stopCellEditing() {
	    fireEditingStopped();
		return true;
	}
}
