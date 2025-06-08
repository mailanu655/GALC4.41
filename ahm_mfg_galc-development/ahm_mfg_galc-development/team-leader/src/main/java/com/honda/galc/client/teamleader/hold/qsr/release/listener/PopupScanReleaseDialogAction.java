package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.dialog.ScanReleaseDialog;
import com.honda.galc.entity.product.HoldResult;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PopupScanReleaseDialogAction</code> is ...
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
 * <TD>Jan 15, 2010</TD>
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
public class PopupScanReleaseDialogAction extends BaseListener<ReleasePanel> implements ActionListener {

	private boolean selected;

	public PopupScanReleaseDialogAction(ReleasePanel panel) {
		super(panel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		List<Map<String, Object>> selectedItems = getView().getProductPanel().getSelectedItems();

		if (selectedItems == null || selectedItems.isEmpty()) {
			return;
		}

		List<HoldResult> qsrHoldResults = HoldUtils.extractHoldResults(getView().getProductPanel().getItems());
		List<HoldResult> selectedHoldResults = HoldUtils.extractHoldResults(selectedItems);

		List<HoldResult> releasedHoldResults = new ArrayList<HoldResult>();
		List<HoldResult> holdResults = new ArrayList<HoldResult>();

		for (HoldResult result : selectedHoldResults) {
			if (result.getReleaseFlag() == 1) {
				releasedHoldResults.add(result);
			} else {
				holdResults.add(result);
			}
		}

		StringBuilder message = new StringBuilder();

		if (releasedHoldResults.size() > 0) {
			message.append(releasedHoldResults.size()).append(" selected ");
			message.append(releasedHoldResults.size() == 1 ? "Hold is" : "Holds are");
			message.append(" already released \n");
		}

		int maxCount = Config.getProperty().getMaxReleaseBatchSize();

		if (holdResults.size() > maxCount) {
			message.append("The total number of Holds to be released exceeds maximum allowed ").append(maxCount);
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (holdResults.size() == 0) {
			message.append("There are no Holds to be released");
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (message.length() > 0) {
			JOptionPane.showMessageDialog(getView(), message);
		}

		final ScanReleaseDialog dialog = new ScanReleaseDialog(getView(), "Release Holds", holdResults, qsrHoldResults);
		dialog.setLocationRelativeTo(getMainWindow());
		dialog.setVisible(true);
	}

	protected boolean isSelected() {
		return selected;
	}

	protected void setSelected(boolean selected) {
		this.selected = selected;
	}
}
