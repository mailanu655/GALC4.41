package com.honda.galc.client.ui.component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class ComboBoxCellEditor extends DefaultCellEditor {
	
	private static final long serialVersionUID = 1L;

	public ComboBoxCellEditor(Object[] items) { 
		this(items,false);
	} 
	
	public ComboBoxCellEditor(Object[] items,boolean isEditable) { 
		super(new JComboBox(items));
		((JComboBox)getComponent()).setEditable(isEditable);
	} 
}
