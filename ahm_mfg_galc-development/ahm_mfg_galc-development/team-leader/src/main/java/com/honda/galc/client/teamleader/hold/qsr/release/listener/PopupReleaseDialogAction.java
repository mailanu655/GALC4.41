package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.dialog.ReleaseDialog;
import com.honda.galc.entity.product.HoldResult;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PopupReleaseDialogAction</code> is ...
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
public class PopupReleaseDialogAction extends QsrAction<ReleasePanel> implements ActionListener {

	private boolean selected;

	public PopupReleaseDialogAction(ReleasePanel panel) {
		super(panel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		List<HoldResult> qsrHoldResults = HoldUtils.extractHoldResults(getView().getProductPanel().getItems());
		List<Map<String, Object>> selectedRecords = getView().getProductPanel().getSelectedItems();
		List<HoldResult> finalHoldResults = new ArrayList<HoldResult>();
		List<HoldResult> releasedHoldResults = new ArrayList<HoldResult>();
		List<HoldResult> shippedHoldResults = new ArrayList<HoldResult>();
		
		if (selectedRecords == null || selectedRecords.isEmpty()) {
			return;
		}
		
		for (Map<String, Object> record : selectedRecords) {
			HoldResult holdResult = (HoldResult)record.get("holdResult");
			if (holdResult.getReleaseFlag() == 1)
				releasedHoldResults.add(holdResult);
			else if (record.get("ship") == Boolean.TRUE)
				shippedHoldResults.add(holdResult);
			else 
				finalHoldResults.add(holdResult);
		}

		StringBuilder message = new StringBuilder();
		
		int maxCount = Config.getProperty().getMaxReleaseBatchSize();
		if (finalHoldResults.size() > maxCount) {
			message.append("The total number of Holds to be released exceeds maximum allowed ").append(maxCount);
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (!releasedHoldResults.isEmpty()) {
			message.append(releasedHoldResults.size() == 1 ? "Released product\n" : "Released products\n");
			for (HoldResult releasedHoldResult : releasedHoldResults)
				message.append(releasedHoldResult.getId().getProductId() + "\n");
			message.append("will be skipped.\n\n");
		} 
		
		if (!shippedHoldResults.isEmpty()) {
			message.append(releasedHoldResults.size() == 1 ? "Shipped product\n" : "Shipped products\n");
			for (HoldResult holdResult : shippedHoldResults)
				message.append(holdResult.getId().getProductId() + "\n");
			message.append("will be skipped.\n");
		}

		if (message.length() > 0) {
			this.showScrollDialog(message.toString());
		}

		if (finalHoldResults.size() == 0) {
			message = new StringBuilder("The are no Holds to be released");
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		final ReleaseDialog dialog = new ReleaseDialog(getView(), "Release Holds", finalHoldResults, qsrHoldResults);
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
