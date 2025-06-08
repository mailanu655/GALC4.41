package com.honda.galc.client.qics.view.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;

import javax.swing.Action;

import com.honda.galc.client.qics.view.dialog.ScrapDialog;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.entity.product.ExceptionalOut;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ScrapAction</code> is ...
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
 * <TD>Feb 21, 2008</TD>
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
public class ScrapAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;

	public ScrapAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Scrap");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
	}

	@Override
	protected void beforeExecute(ActionEvent e) {
		super.beforeExecute(e);
		((Component) e.getSource()).setEnabled(false);
	}

	@Override
	protected void execute(ActionEvent e) {

		String productId = getQicsController().getProductModel().getInputNumber();
		String productNumberLabel = getQicsController().getProductTypeData().getProductTypeLabel();
		String msg = productNumberLabel + " " + productId + " is being scrapped";

		ScrapDialog dialog = new ScrapDialog(getQicsFrame(), "Scrap Product", false, msg);
		dialog.setLocationRelativeTo(getQicsFrame());
		if (!dialog.isErrorOnInit())
			dialog.setVisible(true);
		boolean cancelled = dialog.isCancelled();
		if (cancelled || dialog.getReturnValue()==null) {
			return;
		}

		ExceptionalOut scrap = (ExceptionalOut) dialog.getReturnValue();

		scrap = setScrapProperties(scrap);
		getQicsController().submitScrap(scrap);

		if (getQicsFrame().displayDelayedMessage()) {
			getQicsController().playNgSound();
			return;
		} else {
			getQicsController().playOkSound();
			getQicsFrame().displayIdleView();
			sendDataCollectionCompleteToPlcIfDefined();
		}
	}

	@Override
	protected void afterExecute(ActionEvent e) {
		super.afterExecute(e);
		((Component) e.getSource()).setEnabled(true);
	}

	protected ExceptionalOut setScrapProperties(ExceptionalOut scrap) {
		String productId = getQicsController().getProductModel().getProduct().getProductId();
		scrap.setProductId(productId);
		scrap.getId().setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		scrap.setProcessPointId(getQicsController().getProcessPointId());
		scrap.setAssociateNo(getQicsFrame().getUserId());
		scrap.setProductionDate(new java.sql.Date(new java.util.Date().getTime()));
		return scrap;
	}
}
