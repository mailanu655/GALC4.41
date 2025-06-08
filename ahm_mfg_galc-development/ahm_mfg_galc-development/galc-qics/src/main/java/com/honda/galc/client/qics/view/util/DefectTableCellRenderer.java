package com.honda.galc.client.qics.view.util;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.qics.config.QicsClientConfig;
import com.honda.galc.entity.enumtype.DefectStatus;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Custom cell renderer for defect table. submit action. *
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class DefectTableCellRenderer extends DefaultTableCellRenderer {


	private static final long serialVersionUID = 1L;
	private QicsClientConfig clientConfig;

	public DefectTableCellRenderer(QicsClientConfig clientConfig) {
		this.clientConfig = clientConfig;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		Color backgroundColor = Color.white;
		if(column == 1) {
			isSelected = false;
			backgroundColor = getClientConfig().getDefectStatusColor((DefectStatus)value);
		}
		setBackground(backgroundColor);
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
	}
	

	protected QicsClientConfig getClientConfig() {
		return clientConfig;
	}
}
