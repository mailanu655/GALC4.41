package com.honda.galc.client.teamleader.hold.qsr.put.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.HoldPanel;
import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.report.TableReport;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ExportAction</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
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
public class ExportAction extends BaseListener<HoldPanel> implements ActionListener {

	private boolean exportAll;

	public ExportAction(HoldPanel parentPanel, boolean exportAll) {
		super(parentPanel);
		this.exportAll = exportAll;
	}

	@Override
	protected void executeActionPerformed(ActionEvent ae) {
		TableReport report = getView().getReport();
		String currentDirectoryPath = (String) getView().getCache().get("currentDirectoryPath");
		File file = HoldUtils.popupSaveDialog(getView(), currentDirectoryPath, report.getFileName());
		if (file == null || file.getAbsolutePath() == null) {
			return;
		}
		getView().getCache().put("currentDirectoryPath", file.getParent());
		List<Map<String, Object>> data = null;
		if (isExportAll()) {
			data = getView().getProductPanel().getItems();
		} else {
			data = getView().getProductPanel().getSelectedItems();
		}

		report.setData(data);
		report.export(file.getAbsolutePath());
	}

	public boolean isExportAll() {
		return exportAll;
	}
}
