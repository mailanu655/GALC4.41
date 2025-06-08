package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.TeamRotation;

/**
 * 
 * <h3>TeamRotationTableModel is the table model for team rotation data</h3>
 * <p>  </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Yang Xin<br>
 * Oct 10, 2014
 */
public class TeamRotationTableModel extends SortableTableModel<TeamRotation> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2230803702491632356L;

	/**
	 * Instantiates a new team rotation table model.
	 *
	 * @param table the table
	 * @param items the items
	 */
	public TeamRotationTableModel(JTable table, List<TeamRotation> items) {
		super(items, new String[] { "Production Date", "Plant", "Line",
				"Location", "Shift", "Team" }, table);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.ui.component.BaseTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		TeamRotation teamRotation = getItem(rowIndex);

		switch (columnIndex) {
		case 0:
			return teamRotation.getFormatProductionDate();
		case 1:
			return teamRotation.getId().getPlantCode();
		case 2:
			return teamRotation.getId().getLineNo();
		case 3:
			return teamRotation.getId().getProcessLocation();
		case 4:
			return teamRotation.getId().getShift();
		case 5:
			return teamRotation.getId().getTeam();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.ui.component.BaseTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int column) {
		if (row >= getRowCount() || column == 0)
			return;
		super.setValueAt(value, row, column);
		TeamRotation teamRotation = getItem(row);
		if (column == 1) {
			teamRotation.getId().setPlantCode(
					value == null ? "" : value.toString());
		} else if (column == 2) {
			teamRotation.getId().setLineNo(
					value == null ? "" : value.toString());
		} else if (column == 3) {
			teamRotation.getId().setProcessLocation(
					value == null ? "" : value.toString());
		} else if (column == 4) {
			teamRotation.getId()
					.setShift(value == null ? "" : value.toString());
		} else if (column == 5) {
			teamRotation.getId().setTeam(value == null ? "" : value.toString());
		}
		this.fireTableCellUpdated(row, column);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.ui.component.BaseTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		// label column is editable
		return column == 1 || column == 2 || column == 3 || column == 4
				|| column == 5;

	}

}
