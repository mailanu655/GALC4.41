package com.honda.galc.client.ui.component;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	public CheckBoxCellRenderer() {
		super();
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
 
		if (value instanceof Boolean) {
			setSelected(((Boolean)value).booleanValue()); 
			setEnabled(true);
  
			if (isSelected) {
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} 	else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
		}
		else {
			return null;
		}
		return this; 
	}
	
}