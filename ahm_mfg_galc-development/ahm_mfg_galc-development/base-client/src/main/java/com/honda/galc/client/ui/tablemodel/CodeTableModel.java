package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.Code;

public class CodeTableModel extends SortableTableModel<Code> {

	private static final long serialVersionUID = 1L;
	private static String[] columnHeaders = { "Code Type", "Code", "Code Description", "Division Id" };

	public CodeTableModel(JTable table, List<Code> items) {
		super(items, columnHeaders, table);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Code item = getItem(rowIndex);
		return getColumnValue(columnIndex, item);
	}

	private Object getColumnValue(int columnIndex, Code item) {
		switch(columnIndex) {
		case 0: return item.getCodeType();
		case 1: return item.getCode();
		case 2: return item.getCodeDescription();
		case 3: return item.getDivisionId();
		}
		return null;
	}
}
