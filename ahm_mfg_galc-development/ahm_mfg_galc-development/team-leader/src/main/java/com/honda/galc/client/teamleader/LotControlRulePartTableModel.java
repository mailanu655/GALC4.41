package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.LotControlRule;

public class LotControlRulePartTableModel  extends SortableTableModel<LotControlRule>{

	private static final long serialVersionUID = 1L;
	public LotControlRulePartTableModel(Boolean partNameOnly, JTable table,List<LotControlRule> items) {
		super(items, new String[] {"#","Part Name"} ,table);
		setColumnWidths(new int[]{50, 550});
	}

	public LotControlRulePartTableModel(JTable table,List<LotControlRule> items) {
		this(false, table, items);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		LotControlRule rule = getItem(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return rule.getId().getPartName();
		}
		return null;
	}
}