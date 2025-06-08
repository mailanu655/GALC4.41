package com.honda.galc.client.teamleader.hold.qsr.put.vinseq;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.product.controller.listener.BaseListener;


public class ClearVinInputAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private BaseListener<VinSeqPanel> action;

	public ClearVinInputAction(VinSeqPanel parentPanel) {
		super();
		putValue(Action.NAME, "Clear");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		action = new BaseListener<VinSeqPanel>(parentPanel) {
			@Override
			public void executeActionPerformed(ActionEvent e) {
				getView().getProductPanel().removeData();
				getView().getInputPanel().resetInput();
				getView().getInputPanel().getCommandButton().setAction(getView().getSelectProductAction());
				
			}
		};
	}

	public void actionPerformed(ActionEvent ae) {
		getAction().actionPerformed(ae);
	}

	protected BaseListener<VinSeqPanel> getAction() {
		return action;
	}
}
