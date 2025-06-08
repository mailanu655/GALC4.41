package com.honda.galc.client.qics.view.action;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;

import com.honda.galc.client.qics.view.screen.DefectScanTextPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;




public class DefectScanRepairAllAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;


	public DefectScanRepairAllAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Repair All");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
	}
	
	@Override
	public void execute(ActionEvent e) {
		try {
			getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			getQicsFrame().clearMessage();

			getQicsController().submitRepairAll();
			resetTextFields();
			getQicsPanel().resetDefectTable();
			getQicsPanel().getDefectScanInputPane().getStatusTextField().setBackground(Color.green);
			getQicsPanel().getDefectScanInputPane().getStatusTextField().setText("REPAIRED");
		} finally {
			getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void submitAction(ActionEvent e) {
		getQicsController().submit();
	}

	private void resetTextFields() {
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setEnabled(true);
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setEnabled(true);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setEnabled(true);
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setEditable(true);
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setEditable(true);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setEditable(true);
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setColor(Color.blue);
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setColor(Color.blue);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setColor(Color.blue);
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setBackground(
				Color.blue);
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setBackground(
				Color.blue);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setBackground(
				Color.blue);
		getQicsPanel().getDefectScanInputPane().getEntryTextField().setText("");
		getQicsPanel().getDefectScanInputPane().getDefectTextField().setText("");
		getQicsPanel().getDefectScanInputPane().getStatusTextField().setText("");
		getQicsPanel().getDefectScanInputPane().getStatusTextField().grabFocus();
	}

	@Override
	protected DefectScanTextPanel getQicsPanel() {
		return (DefectScanTextPanel) super.getQicsPanel();
	}
}
