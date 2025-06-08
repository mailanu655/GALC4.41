package com.honda.galc.client.qics.view.fragments;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.model.ProductModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * UI component with product number.
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProductNumberPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton productNumberButton = null;
	protected JLabel productNumberLabel;
	protected UpperCaseFieldBean productNumberTextField;

	private QicsController qicsController;

	public final static int DEFAULT_PRODUCT_NUMBER_LENGTH = 20;
	public final static int DEFAULT_WIDTH = 950;
	public final static int DEFAULT_HEIGHT = 100;

	public ProductNumberPanel() {
		super();
		initialize();
	}

	public ProductNumberPanel(QicsController controller) {
		super();
		this.qicsController = controller;
		initialize();
	}

	protected void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		productNumberTextField = createProductNumberTextField();
		if(getQicsController().getQicsPropertyBean().isProductButtonEnabled())
		{
			add(getProductNumberButton(), getProductNumberLabelContraints());
		}
		else
		{
			productNumberLabel = createProductNumberLabel();			
			add(getProductNumberLabel(), getProductNumberLabelContraints());		
		}
		add(getProductNumberTextField(), getProductNumberTextFieldContraints());
	}


	// === factory methods === //
	protected JLabel createProductNumberLabel() {
		JLabel productNumberLabel = new JLabel();
		productNumberLabel.setFont(Fonts.DIALOG_PLAIN_18);
		String label = getQicsController().getProductTypeData().getProductTypeLabel();
		
		if (label == null || label.trim().length() == 0) label = "PRD ";
		productNumberLabel.setText(label);
		productNumberLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		return productNumberLabel;
	}

	protected UpperCaseFieldBean createProductNumberTextField() {
		UpperCaseFieldBean productNumberTextField = new UpperCaseFieldBean();
		productNumberTextField.setName("ProductNumberTextField");
		TextFieldState.EDIT.setState(productNumberTextField);
		productNumberTextField.setEditable(true);
		productNumberTextField.setFont(Fonts.DIALOG_PLAIN_36);
		productNumberTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
		productNumberTextField.setRequestFocusEnabled(true);
		productNumberTextField.setDocument(new UpperCaseDocument(getProductNumberLength()));
		return productNumberTextField;
	}

	protected GridBagConstraints getProductNumberLabelContraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(0, 5, 5, 5);
		return constraints;
	}

	protected GridBagConstraints getProductNumberTextFieldContraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 5, 5, 0);
		return constraints;
	}

	// === controlling api === //
	public void resetPanel(ProductModel product) {
		if (product == null) return;
		String txt = product.getInputNumber();
		getProductNumberTextField().setText(txt == null ? "" : txt);
	}

	// === get/set === //
	public JLabel getProductNumberLabel() {
		return productNumberLabel;
	}
	
	public UpperCaseFieldBean getProductNumberTextField() {
		return productNumberTextField;
	}
	
	protected int getProductNumberLength() {
		int length = getQicsController().getProductNumberLength();
		return length < 1 ? DEFAULT_PRODUCT_NUMBER_LENGTH : length;
	}

	public QicsController getQicsController() {
		return qicsController;
	}
	
	public JButton getProductNumberButton()
    {
        if (productNumberButton == null)
        {
            productNumberButton = new JButton();
            productNumberButton.setFont(Fonts.DIALOG_PLAIN_24);    
            String label = getQicsController().getProductTypeData().getProductNumberDefs().get(0).getName();
            if (label == null || label.trim().length() == 0) {
                label = "PRD#";
            }
            productNumberButton.setText(label);
            productNumberButton.setPreferredSize(new Dimension(100, 32));
            productNumberButton.setName(label);
            productNumberButton.setBorderPainted(false);
            productNumberButton.setForeground(this.getForeground());
        }
        return productNumberButton;
    }

}
