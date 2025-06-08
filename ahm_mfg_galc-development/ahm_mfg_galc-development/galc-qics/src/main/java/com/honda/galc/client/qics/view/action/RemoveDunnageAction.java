package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Action;

import com.honda.galc.client.qics.view.screen.DunnagePanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>RemoveDunnageAction</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 7, 2009</TD>
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
public class RemoveDunnageAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;

	public RemoveDunnageAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Remove Product");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
	}

	@Override
	protected void execute(ActionEvent e) {
		List<BaseProduct>  products= getQicsPanel().getProductPane().getSelectedItems();
		if (products != null && !products.isEmpty()) {

			if(!MessageDialog.confirm(getQicsPanel(), "Are you sure to remove dunnage ?")) return;

			for (Object o : products) {
				BaseProduct product = (BaseProduct) o;
				if (product == null || product.getProductId() == null || product.getProductId().trim().length() == 0) {
					continue;
				}
				getQicsController().removeDunnage(product);
			}

		}
		getQicsPanel().loadDunnageProducts(getQicsController().getClientModel().getDunnageNumber());
		getQicsPanel().setButtonsState();
	}

	@Override
	protected DunnagePanel getQicsPanel() {
		return (DunnagePanel) super.getQicsPanel();
	}
}
