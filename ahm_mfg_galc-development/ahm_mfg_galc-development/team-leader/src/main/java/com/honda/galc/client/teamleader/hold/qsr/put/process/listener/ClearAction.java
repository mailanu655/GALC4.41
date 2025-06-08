package com.honda.galc.client.teamleader.hold.qsr.put.process.listener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.process.ProcessPanel;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrClearAction</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class ClearAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private BaseListener<ProcessPanel> action;

	public ClearAction(ProcessPanel parentPanel) {
		super();
		putValue(Action.NAME, "Clear");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		action = new BaseListener<ProcessPanel>(parentPanel) {
			@Override
			public void executeActionPerformed(ActionEvent e) {
				getView().getProductPanel().removeData();
				getView().getInputPanel().getCommandButton().setAction(getView().getSelectProductAction());
				if (getView().getInputPanel().getStartTime() == null || getView().getInputPanel().getEndTime() == null) {
					getView().getInputPanel().getCommandButton().setEnabled(false);
				}
			}
		};
	}

	public void actionPerformed(ActionEvent ae) {
		getAction().actionPerformed(ae);
	}

	protected BaseListener<ProcessPanel> getAction() {
		return action;
	}
}
