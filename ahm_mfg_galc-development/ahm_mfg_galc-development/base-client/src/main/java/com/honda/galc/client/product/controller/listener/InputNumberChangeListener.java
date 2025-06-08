package com.honda.galc.client.product.controller.listener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.component.TextFieldState;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>InputNumberChangeListener</code> is ... .
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
public class InputNumberChangeListener extends BaseListener<ApplicationMainPanel> implements DocumentListener {

	private JTextField textField;

	public InputNumberChangeListener(ApplicationMainPanel view, JTextField textField) {
		super(view);
		this.textField = textField;
	}

	public void changedUpdate(DocumentEvent e) {
	}

	public void insertUpdate(DocumentEvent e) {
		processChange();
	}

	public void removeUpdate(DocumentEvent e) {
		processChange();
	}

	protected void processChange() {
		if (!getTextField().isEditable()) {
			return;
		}
		if (!getTextField().isEnabled()) {
			return;
		}
		if (getView().getMainWindow().getStatusMessagePanel().isError()) {
			getView().getMainWindow().clearMessage();
		}
		TextFieldState.EDIT.setState(getTextField());
	}

	protected JTextField getTextField() {
		return textField;
	}
}
