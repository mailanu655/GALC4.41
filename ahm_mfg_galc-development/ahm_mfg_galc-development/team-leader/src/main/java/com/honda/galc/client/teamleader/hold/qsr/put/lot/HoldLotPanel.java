package com.honda.galc.client.teamleader.hold.qsr.put.lot;

import java.awt.event.KeyEvent;

import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.report.TableReport;

public class HoldLotPanel extends HoldProductPanel {
	private static final long serialVersionUID = 1L;

	public HoldLotPanel(QsrMaintenanceFrame mainWindow) {
		super("Hold Lot", KeyEvent.VK_T, mainWindow);
	}

	// === factory methods === //
	@Override
	protected HoldLotInputPanel createInputPanel() {
		HoldLotInputPanel panel = new HoldLotInputPanel(this);
		return panel;
	}

	// === mappings === //
	@Override
	protected void mapActions() {
		super.mapActions();
		getInputPanel().getProdLotSearchComboBox().getComponent().addActionListener(new SearchLotAction(this));
		getInputPanel().getProdLotComboBox().getComponent().addActionListener(new SearchLotAction(this));
	}

	@Override
	public TableReport getReport() {
		TableReport report = super.getReport();
		String fileName = String.format("QSR-HOLD-%s-%s.xlsx", getDivision().getDivisionId(), getProductType());
		String headerLine = String.format("QSR-HOLD-%s-%s", getDivision().getDivisionId(), getProductType());
		String sheetName = "QSR-HOLD";
		report.setFileName(fileName);
		report.setTitle(headerLine);
		report.setReportName(sheetName);
		return report;
	}

	@Override
	public HoldLotInputPanel getInputPanel() {
		return (HoldLotInputPanel) super.getInputPanel();
	}
}
