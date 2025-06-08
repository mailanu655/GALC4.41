package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;

public class BuildAttributeGroupDefinitionTableModel extends SortableTableModel<BuildAttributeGroupDefinition> {

	private static final long serialVersionUID = 1L;
	private static String[] columnHeaders = { "Attribute Group", "Screen Id" };

	public BuildAttributeGroupDefinitionTableModel(JTable table, List<BuildAttributeGroupDefinition> items) {
		super(items, columnHeaders, table);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		BuildAttributeGroupDefinition item = getItem(rowIndex);
		return getColumnValue(columnIndex, item);
	}

	private Object getColumnValue(int columnIndex, BuildAttributeGroupDefinition item) {
		switch(columnIndex) {
		case 0: return item.getAttributeGroup();
		case 1: return item.getScreenId();
		}
		return null;
	}
}
