package com.honda.galc.client.teamleader.hold.qsr.release.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ReleaseInputListener</code> is ...
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
public class ReleaseInputListener extends BaseListener<ReleasePanel> implements ActionListener, DocumentListener {

	private ReleaseDialog dialog;

	public ReleaseInputListener(ReleaseDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		String reason = (String) getDialog().getReasonInput().getSelectedItem();
		String name = getDialog().getAssociateNameInput().getText();
		String phone = getDialog().getPhoneInput().getText();

		String approver = getDialog().getApproverInput().getText();
		boolean isInputAssociateInfo = getDialog().getProperty().isInputAssociateInfo();

		boolean inputValid = false;
		if ( isInputAssociateInfo ) 
			inputValid = !isEmpty(reason) && !isEmpty(name) && !isEmpty(phone);
		else inputValid = !isEmpty(reason);
				
		if (inputValid) {
			inputValid = !ReleaseDialog.APPROVED_TO_SHIP.equals(reason) || ReleaseDialog.APPROVED_TO_SHIP.equals(reason) && !isEmpty(approver);
		}
		getDialog().getSubmitButton().setEnabled(inputValid);
	}

	public void changedUpdate(DocumentEvent e) {
		actionPerformed(null);
		resetInputField(e);
	}

	public void insertUpdate(DocumentEvent e) {
		actionPerformed(null);
		resetInputField(e);
	}

	public void removeUpdate(DocumentEvent e) {
		actionPerformed(null);
		resetInputField(e);
	}

	protected void resetInputField(DocumentEvent e) {

	}

	public ReleaseDialog getDialog() {
		return dialog;
	}
}
