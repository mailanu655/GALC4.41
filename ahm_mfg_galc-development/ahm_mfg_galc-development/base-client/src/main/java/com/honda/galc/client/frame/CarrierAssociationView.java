package com.honda.galc.client.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ProductPanel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.data.TagNames;
import com.honda.galc.util.KeyValue;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * <h3>CarrierAssociationView</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Carrier Association is a generic class designed to handle product and carrier/pallet/RFID etc 
 * identifier association </p>
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
 * <TD>P.Chou</TD>
 * <TD>Oct 4, 2018</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Oct 4, 2018
 */
public class CarrierAssociationView extends ApplicationMainPanel{
	private static final long serialVersionUID = 1L;

	protected ProductPanel productPane;
	protected LabeledUpperCaseTextField virtualCarierTextField;
	protected JButton doneButton;
	private JPanel buttonPane;
	private JPanel compnentPane;
	private CarrierAssociationController controller;
	
	public CarrierAssociationView(DefaultWindow window) {
		super(window);
		controller = new CarrierAssociationController(this);
		initComponents();
		mapActions();
		
		reset();
	}

	private void mapActions() {
		
		getProductPanel().getProductIdField().addActionListener(controller);
		if(hasCarrierId()) getVirtualCarierTextField().getComponent().addActionListener(controller);
		getDoneButton().addActionListener(controller);
		getDoneButton().addKeyListener(controller);
		
		if (controller.getCarrierAssociationPropertyBean().isEditableAlways()) {
			getProductPanel().getProductIdField().addFocusListener(controller);
			if (hasCarrierId()) 
				getVirtualCarierTextField().getComponent().addFocusListener(controller);
		}
		
		
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(getProductPanel(),BorderLayout.NORTH);
		add(getComponentPane(),BorderLayout.CENTER);
		add(getButtonPane(), BorderLayout.SOUTH);
		
	}
	
	public void reset() {
		getProductIdField().setText(StringUtils.EMPTY);
		if(hasCarrierId()) 
			getLabeledComponent().getComponent().setText(StringUtils.EMPTY);
		TextFieldState.EDIT.setState(getProductIdField());
		TextFieldState.EDIT.setState(getLabeledComponent().getComponent());
		productPane.getProductSpecField().setText(StringUtils.EMPTY);
		getMainWindow().clearMessage();
		getDoneButton().setEnabled(false); 
		
	}

	private boolean hasCarrierId() {
		return controller.getCarrierAssociationPropertyBean().isCarrierAssociationRequired();
	}
	private LabeledUpperCaseTextField getLabeledComponent() {
		if(virtualCarierTextField == null) {
			virtualCarierTextField = new LabeledUpperCaseTextField(controller.getCarrierAssociationPropertyBean().getCarrierIdLabel());
			virtualCarierTextField.setFont(new Font("sansserif", 1,35)); 
			
			virtualCarierTextField.getComponent().setMaximumLength(40);
			virtualCarierTextField.setInsets(0, 10, 0, 10);
			virtualCarierTextField.getComponent().setFixedLength(false);
			virtualCarierTextField.getComponent().setPreferredSize(
					new Dimension(controller.getCarrierAssociationPropertyBean().getPreferredCarrierIdlength(), productPane.getProductIdField().getHeight()));
			virtualCarierTextField.getComponent().setHorizontalAlignment(JTextField.LEFT);
			virtualCarierTextField.getComponent().setBackground(Color.BLUE);
			virtualCarierTextField.getComponent().setEnabled(true);
			
		}
		return virtualCarierTextField;
	}

	public ProductPanel getProductPanel() {
		if(productPane == null)
			productPane = new ProductPanel(window,window.getApplicationContext().getProductTypeData(),
					controller.getCarrierAssociationPropertyBean().getPreferredProductIdLength());
		
		return productPane;
	}

	public LabeledUpperCaseTextField getVirtualCarierTextField() {
		return virtualCarierTextField;
	}

	public JButton getDoneButton() {
		if(doneButton == null){
			doneButton = new JButton(controller.getCarrierAssociationPropertyBean().getDoneButtonLabel());
			doneButton.setPreferredSize(new Dimension(100, 32));
			doneButton.setFont(new Font("sansserif", 1, 15));
		}
		return doneButton;
	}
	
	protected JPanel getButtonPane() {
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
			buttonPane.add(getDoneButton());
			buttonPane.add(Box.createHorizontalStrut(120));
			
		}
		return buttonPane;
	}

	public JPanel getComponentPane() {
		if(compnentPane == null) {
			compnentPane = new JPanel();
			compnentPane.setLayout(new MigLayout("","","100 []"));
			if(hasCarrierId()) compnentPane.add(getLabeledComponent(),"wrap");
			
		}
		return compnentPane;
	}

	public void setProductId(String productId) {
		getProductPanel().getProductIdField().setText(productId);
		
	}

	public void setComponentText(String partSn) {
		getVirtualCarierTextField().getComponent().setText(partSn);
	}

	public void clearMessage() {
		getMainWindow().clearMessage();
		
	}

	public UpperCaseFieldBean getProductIdField() {
		
		return productPane.getProductIdField();
	}
	
	public UpperCaseFieldBean getVirtualRfidField() {
		
		return getVirtualCarierTextField().getComponent();
	}
	
	public void setValue(String fieldId, KeyValue<String, String> keyValue, boolean isLastError) {
		if(TagNames.PRODUCT_ID.name().equals(fieldId))
				setValue(keyValue, productPane.getProductIdField(), isLastError);

		else if(TagNames.CARRIER_ID.name().equals(fieldId))
				setValue(keyValue, getLabeledComponent().getComponent(), isLastError);
	}

	private void setValue(KeyValue<String, String> keyValue, UpperCaseFieldBean field, boolean isLastError) {
		field.setText(keyValue.getKey());
		if(StringUtils.EMPTY.equals(keyValue.getValue())) {
			TextFieldState.READ_ONLY.setState(field);
			if (controller.getCarrierAssociationPropertyBean().isEditableAlways()) {
				TextFieldState.EDITABLE.setState(field);
			}
			if(isLastError) getMainWindow().clearMessage();
		} else if(keyValue.getValue() == null) {
			TextFieldState.EDIT.setState(field);
			getMainWindow().clearMessage();
		} else if(!StringUtils.isEmpty(keyValue.getValue())) {
			getMainWindow().setErrorMessage(keyValue.getValue());
			TextFieldState.ERROR.setState(field);
			field.setSelectionStart(0);
			field.setSelectionEnd(field.getText().length());
			field.requestFocus();
		}
				
		
	}
	
	public void setProductSpec(String productSpecCode) {
		productPane.getProductSpecField().setText(productSpecCode);
	}
	
}
