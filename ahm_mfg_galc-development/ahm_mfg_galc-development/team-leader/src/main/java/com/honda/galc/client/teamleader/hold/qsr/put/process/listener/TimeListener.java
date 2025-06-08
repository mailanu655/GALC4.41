package com.honda.galc.client.teamleader.hold.qsr.put.process.listener;

import java.util.Date;

import javax.swing.Action;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.process.ProcessPanel;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TimeListener</code> is ...
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
public class TimeListener extends BaseListener<ProcessPanel> implements DocumentListener {

	public TimeListener(ProcessPanel parentPanel) {
		super(parentPanel);
	}

	@Override
	public void executeDocumentListener(DocumentEvent e) {
		Date startDate = getView().getInputPanel().getStartTime();
		Date endDate = getView().getInputPanel().getEndTime();
		Action action = getView().getInputPanel().getCommandButton().getAction();
		Action clearAction = getView().getClearAction();
		if ((startDate == null || endDate == null) && !action.equals(clearAction)) {
			getView().getInputPanel().getCommandButton().setEnabled(false);
			return;
		}
		getView().getInputPanel().getCommandButton().setEnabled(true);
		resetInputField(e);

	}

	protected void resetInputField(DocumentEvent e) {
		if (e.getDocument().equals(getView().getInputPanel().getStartTimeInput().getDocument())) {
			// getParentComponent().getQsrHoldPanel().getStartProductInput().setText("");
		} else if (e.getDocument().equals(getView().getInputPanel().getEndTimeInput().getDocument())) {
			// getParentComponent().getQsrHoldPanel().getEndProductInput().setText("");
		}
	}
}
