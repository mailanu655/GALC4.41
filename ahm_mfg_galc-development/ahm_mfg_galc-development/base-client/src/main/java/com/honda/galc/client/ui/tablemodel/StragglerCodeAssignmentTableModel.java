package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.dto.StragglerCodeAssignmentDto;

public class StragglerCodeAssignmentTableModel extends SortableTableModel<StragglerCodeAssignmentDto> {

	private static final long serialVersionUID = 1L;
	private static final String CODE = "CODE";
	private static final String COMMENTS = "COMMENTS";
	private static final String[] columnHeaders = { "IDENTIFIED_TIMESTAMP", "PRODUCT_ID", "PP_DELAYED_AT","STRAGGLER_TYPE", "KD_LOT_NUMBER", "MODEL", "TYPE", "OPTION", "COLOR", "INTERIOR_COLOR", CODE, COMMENTS };

	private final boolean reassignCode;

	public StragglerCodeAssignmentTableModel(JTable table, List<StragglerCodeAssignmentDto> items, boolean reassignCode) {
		super(items, columnHeaders, table);
		this.reassignCode = reassignCode;
	}

	public void setCodeComboCell(List<String> codes) {
		final String[] codesArray;
		if (codes == null || codes.isEmpty()) {
			codesArray = new String[0];
		} else {
			codesArray = codes.toArray(new String[codes.size()]);
		}
		int colIndex = 0;
		while (!CODE.equals(columnHeaders[colIndex])) {
			colIndex++;
		}
		TableColumn col = table.getColumnModel().getColumn(colIndex);
		col.setCellEditor(new ComboBoxCellEditor(codesArray));
		col.setCellRenderer(new ComboBoxCellRender(codesArray));
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		if (CODE.equals(columnHeaders[column])) {
			getItem(row).setCode((String) value);
			fireTableCellUpdated(row, column);
		} else if (COMMENTS.equals(columnHeaders[column])) {
			getItem(row).setComments((String) value);
			fireTableCellUpdated(row, column);
		} else {
			throw new IllegalArgumentException(this.getClass().getSimpleName() + ": editing is not allowed for column " + columnHeaders[column]);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (CODE.equals(columnHeaders[column])) {
			return this.reassignCode ? true : StringUtils.isBlank(getItem(row).getCode());
		} else if (COMMENTS.equals(columnHeaders[column])) {
			return true;
		} else {
			return super.isCellEditable(row, column);
		}
	}
}
