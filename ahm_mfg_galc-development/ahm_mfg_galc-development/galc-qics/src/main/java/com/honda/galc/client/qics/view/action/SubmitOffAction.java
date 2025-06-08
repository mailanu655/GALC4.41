package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;

import com.honda.galc.client.qics.view.screen.MainPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>SubmitOffAction</code> is ...
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
public class SubmitOffAction extends SubmitAction {

	private static final long serialVersionUID = 1L;

	public SubmitOffAction(QicsPanel qicsPanel) {
		super(qicsPanel);
	}

	@Override
	protected void execute(ActionEvent e) {

		submitAction(e);

		if (getQicsFrame().displayDelayedMessage()) {
			getQicsController().playNgSound();
			return;
		}

		executeForwardToOffProcessPoint(e);

		getQicsController().playOkSound();
		getQicsFrame().displayIdleView();
		sendDataCollectionCompleteToPlcIfDefined();
		getQicsFrame().displayDelayedMessage();
	}

	// === business utility methods === //
	protected boolean executeForwardToOffProcessPoint(ActionEvent e) {
		if (getClientConfig().isOffProcessPointIdDefined()) {
			getQicsController().trackOffProcessPoint();
		}
		if (getClientConfig().isMissionShippingTransactionEnabled()) {
			getQicsController().sendMissionShippingTransaction(getClientConfig().getShippingTransactionUrl(), getQicsController().getProductModel().getProduct(), getClientConfig().getShippingTransactionProcessPoint());
		}
		return true;
	}
	
	protected boolean executeCheckProductState(ActionEvent e) {
		getQicsController().submitWarnCheckProductState();
		if (getQicsFrame().displayDelayedMessage()) {
			return false;
		}
		getQicsController().submitItemCheckProductState();
		if (getQicsFrame().displayDelayedMessage()) {
			return false;
		}
		return true;
	}	

	// === get/set === //
	@Override
	protected MainPanel getQicsPanel() {
		return (MainPanel) super.getQicsPanel();
	}
}
