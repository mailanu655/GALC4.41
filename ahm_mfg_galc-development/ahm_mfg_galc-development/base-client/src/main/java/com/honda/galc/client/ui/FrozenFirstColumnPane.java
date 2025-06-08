package com.honda.galc.client.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * <h3>Class description</h3>
 * <p>
 * <code>FrozenFirstColumnPane</code> is a JScrollPane wrapper and will be used
 * to freeze first column of underlying JTable.
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
 * @author vcc01419
 */
public class FrozenFirstColumnPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private JTable frozenTable;

	public void freezeFirstColumn(String columnHeader) {

		JTable mainTable = (JTable) getViewport().getView();
		TableModel mainModel = mainTable.getModel();

		// create a frozen model
		TableModel frozenModel = new DefaultTableModel(mainModel.getRowCount(), 1);

		// populate the frozen model
		for (int i = 0; i < mainModel.getRowCount(); i++) {
			String value = (String) mainModel.getValueAt(i, 0);
			frozenModel.setValueAt(value, i, 0);
		}

		// remove the frozen columns from the original table
		mainTable.removeColumn(mainTable.getColumnModel().getColumn(0));

		// create frozen table
		frozenTable = new JTable(frozenModel);
		frozenTable.getColumnModel().getColumn(0).setHeaderValue(columnHeader);
		frozenTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		frozenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		frozenTable.setEnabled(false);
		frozenTable.setPreferredScrollableViewportSize(frozenTable.getPreferredSize());
		frozenTable.getTableHeader().addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				frozenTable.setPreferredScrollableViewportSize(frozenTable.getPreferredSize());
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}

		});

		// set frozen table as row header view
		JViewport viewport = new JViewport();
		viewport.setView(frozenTable);
		setRowHeaderView(viewport);
		setCorner(JScrollPane.UPPER_LEFT_CORNER, frozenTable.getTableHeader());
	}

	public JTable getFrozenTable() {
		return frozenTable;
	}
}
