package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.release.ScanReleasePanel;


public class RemoveSelectedProductsAction extends BaseListener<ScanReleasePanel> implements ActionListener {

	public RemoveSelectedProductsAction(ScanReleasePanel view) {
		super(view);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		List<Map<String, Object>> selectedItems = getView().getProductPanel().getSelectedItems();
		if(selectedItems == null || selectedItems.isEmpty()) {
			return;
		}
		List<Map<String, Object>> data = getView().getProductPanel().getItems();
		for (Object currentItem : selectedItems) {
			if (data.contains(currentItem)) {
				data.remove(currentItem);
			}
		}
		getView().getProductPanel().reloadData(data);
		getView().getProductPanel().clearSelection();
	}
}