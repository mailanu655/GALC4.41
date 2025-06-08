package com.honda.galc.client.ui.component;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.ApplicationMainPanel;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>NumberInputDialog</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Feb 13, 2019
 */
public class NumberInputDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField numberTextField;

	private JButton cancelButton;
	private JButton submitButton;
	private ApplicationMainPanel parentPanel;

	public NumberInputDialog(ApplicationMainPanel panel, String label, int maxNumberLength) {
		this.parentPanel = panel;
		initComponents(label, maxNumberLength);
	}

	// === init == //
	private void initComponents(String label, int maxNumberLength) {
		setSize(600, 200);
		setResizable(false);
		setModal(true);
		UiFactory factory = UiFactory.getInfo();
		this.submitButton = factory.createButton("Submit", true);
		this.cancelButton = factory.createButton("Cancel", true);
		this.numberTextField = factory.createTextField(maxNumberLength, TextFieldState.EDIT);
		setLocationRelativeTo(getParentPanel());
		setLayout(new MigLayout("insets 10 10 10 10", "[][max][120!]", "[max][max]"));
		add(UiFactory.createLabel(label, Fonts.DIALOG_BOLD_16, SwingConstants.RIGHT));
		add(getNumberTextField(), "width max, height 50!");
		add(getSubmitButton(), "height 50!, grow, wrap");
		add(getCancelButton(), "height 50!, grow, cell 2 1");

		getNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(getParentPanel(), getNumberTextField()));
	}

	// === get/set === //
	public JButton getCancelButton() {
		return cancelButton;
	}

	protected ApplicationMainPanel getParentPanel() {
		return parentPanel;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public JTextField getNumberTextField() {
		return numberTextField;
	}
}
