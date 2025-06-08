package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.BuildAttributeByBom;

public class BuildAttributeByBomTableModel extends SortableTableModel<BuildAttributeByBom> {

	private static final long serialVersionUID = 1L;
	private static String[] columnHeaders = { "Model Group","Part Number","Part Color", "Attribute", "Attribute Value", "Attribute Description" };

	public BuildAttributeByBomTableModel(JTable table, List<BuildAttributeByBom> items) {
		super(items, columnHeaders, table);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		BuildAttributeByBom item = getItem(rowIndex);
		return getColumnValue(columnIndex, item);
	}

	private Object getColumnValue(int columnIndex, BuildAttributeByBom item) {
		switch(columnIndex) {
		case 0: return item.getModelGroup();
		case 1: return item.getPartNo();
		case 2: return item.getPartColorCode();
		case 3: return item.getAttribute();
		case 4: return item.getAttributeValue();
		case 5: return item.getAttributeDescription();
		}
		return null;
	}
}
