package com.honda.galc.client.teamleader.recovery.frame;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>TextInputChangeListener</code> is ...
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
 * <TD>Jul 11, 2008</TD>
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
public class TextInputChangeListener implements DocumentListener, CaretListener {

	private ProductRecoveryPanel panel;
	private JTextField textField;
	JButton button;

	public TextInputChangeListener(ProductRecoveryPanel panel, JTextField textField, JButton button) {
		this.panel = panel;
		this.textField = textField;
		this.button = button;
	}

	public void changedUpdate(DocumentEvent e) {
	}

	public void insertUpdate(DocumentEvent e) {
		processDocumentChange(e);
	}

	public void removeUpdate(DocumentEvent e) {
		processDocumentChange(e);
	}

	public void caretUpdate(CaretEvent e) {
		processCarretChange(e);
	}

	protected void processDocumentChange(DocumentEvent e) {
		processChange();
	}

	protected void processCarretChange(CaretEvent e) {
		processChange();
	}

	protected void processChange() {
		if (getPanel().isIdle()) {
			Utils.setTextFieldInputColors(getTextField());
			clearMessage();
			String txt = getTextField().getText();
			if (txt != null && txt.trim().length() > 0) {
				getButton().setEnabled(true);
			} else {
				getButton().setEnabled(false);
			}
		}
	}

	protected void clearMessage() {
		getPanel().getController().getMainWindow().clearMessage();
	}

	// === get/set === //
	protected ProductRecoveryPanel getPanel() {
		return panel;
	}

	protected void setPanel(ProductRecoveryPanel panel) {
		this.panel = panel;
	}

	protected JTextField getTextField() {
		return textField;
	}

	protected JButton getButton() {
		return button;
	}

}
