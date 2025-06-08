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
import com.honda.galc.client.teamleader.hold.qsr.release.ScanReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.vinseq.MassPartDataRemovalDialog;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.HoldResult;


public class PopUpRemovePartsDialogAction extends BaseListener<ReleasePanel> implements ActionListener {

	public PopUpRemovePartsDialogAction(ReleasePanel view) {
		super(view);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		List<Map<String, Object>> selectedProducts = getView().getProductPanel().getSelectedItems();
		ProductType productType = getView().getProductType();
		if (productType == null || selectedProducts == null) {
			return;
		}

		List<HoldResult> qsrHoldResults = HoldUtils.extractHoldResults(getView().getProductPanel().getItems());
		List<HoldResult> selectedHoldResults = HoldUtils.extractHoldResults(selectedProducts);

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
			message.append("The total number of Holds exceeds maximum allowed ").append(maxCount);
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (holdResults.size() == 0) {
			message.append("The are no Holds to remove parts");
			JOptionPane.showMessageDialog(getView(), message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (message.length() > 0) {
			JOptionPane.showMessageDialog(getView(), message);
		}
		
		MassPartDataRemovalDialog dialog = new MassPartDataRemovalDialog(getView(),"Remove Parts", productType.toString(),holdResults);
		dialog.setLocationRelativeTo(getView().getMainWindow());
		dialog.setVisible(true);
	}
}