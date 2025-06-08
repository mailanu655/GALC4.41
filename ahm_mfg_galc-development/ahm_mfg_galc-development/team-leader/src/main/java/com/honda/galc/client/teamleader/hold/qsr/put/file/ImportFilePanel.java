package com.honda.galc.client.teamleader.hold.qsr.put.file;

import java.awt.event.KeyEvent;

import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.report.TableReport;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImportFilePanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
public class ImportFilePanel extends HoldProductPanel {

	private static final long serialVersionUID = 1L;

	public ImportFilePanel(QsrMaintenanceFrame mainWindow) {
		super("Hold By Import List", KeyEvent.VK_I, mainWindow);
	}

	// === factory methods === //
	@Override
	protected ImportFileInputPanel createInputPanel() {
		ImportFileInputPanel panel = new ImportFileInputPanel(this);
		return panel;
	}

	@Override
	protected void mapActions() {
		super.mapActions();

		getInputPanel().getBrowseButton().addActionListener(new SelectFileAction(this));
		getInputPanel().getImportButton().addActionListener(new ImportFileAction(this));
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
	public ImportFileInputPanel getInputPanel() {
		return (ImportFileInputPanel) super.getInputPanel();
	}
}
