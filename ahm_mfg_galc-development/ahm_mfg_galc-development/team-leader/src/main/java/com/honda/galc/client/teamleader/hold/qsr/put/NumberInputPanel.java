package com.honda.galc.client.teamleader.hold.qsr.put;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>NumberInputPanel</code> is ... .
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
 * @created Apr 30, 2013
 */
public class NumberInputPanel extends InputPanel {

	private static final long serialVersionUID = 1L;

	private JLabel numberLabel;
	private JTextField numberTextField;
	private ChainCommand validator;

	public NumberInputPanel(HoldProductPanel parentPanel) {
		super(parentPanel);
	}

	@Override
	protected void initView() {
		super.initView();
		this.numberLabel = UiFactory.getInput().createLabel("Number", JLabel.RIGHT);
		this.numberTextField = UiFactory.getInput().createTextField(17, TextFieldState.DISABLED);
		remove(getDepartmentElement());
		remove(getProductTypeElement());
		setLayout(new MigLayout("insets 3", "[fill]10[fill]10[][grow,fill]", ""));
		add(getDepartmentElement(), "width 200::");
		add(getProductTypeElement(), "width 200::");
		add(getNumberLabel());
		add(getNumberTextField());
	}

	// === mappings === //
	@Override
	protected void mapActions() {
		super.mapActions();
		getProductTypeComboBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();

				getParentPanel().getProductPanel().removeData();
				getParentPanel().getMainWindow().clearMessage();
				getNumberTextField().setText("");

				boolean enableInput = productType != null;

				if (enableInput) {
					TextFieldState.EDIT.setState(getNumberTextField());
				} else {
					TextFieldState.DISABLED.setState(getNumberTextField());
				}
				getNumberTextField().requestFocus();
			}
		});

		getNumberTextField().getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				processDocumentChange(e);
			}

			public void insertUpdate(DocumentEvent e) {
				processDocumentChange(e);
			}

			public void removeUpdate(DocumentEvent e) {
				processDocumentChange(e);
			}

			protected void processDocumentChange(DocumentEvent e) {
				if (TextFieldState.ERROR.isInState(getNumberTextField())) {
					TextFieldState.EDIT.setState(getNumberTextField());
					getParentPanel().getMainWindow().clearMessage();
				}
			};
		});
	}

	// === get/set === //
	public JTextField getNumberTextField() {
		return numberTextField;
	}

	public JLabel getNumberLabel() {
		return numberLabel;
	}

	public ChainCommand getValidator() {
		return validator;
	}

	public void setValidator(ChainCommand validator) {
		this.validator = validator;
	}
}
