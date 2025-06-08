package com.honda.galc.client.teamleader.hold.qsr.put.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>RemoveSelectedProductsAction</code> is ...
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
public class RemoveSelectedProductsAction extends BaseListener<HoldProductPanel> implements ActionListener {

	public RemoveSelectedProductsAction(HoldProductPanel panel) {
		super(panel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {
		List<Map<String, Object>> selectedValues = getView().getProductPanel().getSelectedItems();
		if (selectedValues == null || selectedValues.isEmpty()) {
			return;
		}
		List<Map<String, Object>> data = getView().getProductPanel().getItems();
		for (Object o : selectedValues) {
			if (data.contains(o)) {
				data.remove(o);
			}
		}
		getView().getProductPanel().reloadData(data);
		getView().getProductPanel().clearSelection();
		// TODO review it
		if (getView().getInputPanel().getCommandButton() != null) {
			String text = getView().getInputPanel().getCommandButton().getText();
			if (!text.contains("*")) {
				text = "*  " + text;
			}
			getView().getInputPanel().getCommandButton().setText(text);
		}
	}
}
