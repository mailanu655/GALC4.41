package com.honda.galc.client.ui.component;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboBoxCellRender extends JComboBox implements TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	public ComboBoxCellRender(Object[] items) { 
		super(items); 
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
		if (isSelected) { 
			setForeground(table.getSelectionForeground()); 
			super.setBackground(table.getSelectionBackground()); 
		} else { 
			setForeground(table.getForeground()); 
			setBackground(table.getBackground()); 
		}
		
		DefaultComboBoxModel model = (DefaultComboBoxModel)getModel();
		int index = ((DefaultComboBoxModel)getModel()).getIndexOf(value);
		if(index < 0) model.addElement(value);

		setSelectedItem(value); 
			
		return this; 
	}  
}
