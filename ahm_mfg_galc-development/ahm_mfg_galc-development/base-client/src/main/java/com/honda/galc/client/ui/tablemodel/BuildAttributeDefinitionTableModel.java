package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.conf.BuildAttributeDefinition;

public class BuildAttributeDefinitionTableModel extends SortableTableModel<BuildAttributeDefinition> {

	private static final long serialVersionUID = 1L;
	private static String[] columnHeaders = { "Attribute", "Label", "Attribute Group", "Auto Update" };

	public BuildAttributeDefinitionTableModel(JTable table, List<BuildAttributeDefinition> items) {
		super(items, columnHeaders, table);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		BuildAttributeDefinition item = getItem(rowIndex);
		return getColumnValue(columnIndex, item);
	}

	private Object getColumnValue(int columnIndex, BuildAttributeDefinition item) {
		switch(columnIndex) {
		case 0: return item.getAttribute();
		case 1: return item.getAttributeLabel();
		case 2: return item.getAttributeGroup();
		case 3: return item.getAutoUpdate();
		}
		return null;
	}
}
