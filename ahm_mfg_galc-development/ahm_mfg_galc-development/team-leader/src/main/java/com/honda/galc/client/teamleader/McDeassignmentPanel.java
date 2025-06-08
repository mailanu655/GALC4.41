package com.honda.galc.client.teamleader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.product.command.Command;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.validator.AlphaNumericValidator;
import com.honda.galc.client.product.validator.RequiredValidator;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>McDeassignmentPanel</code> is ... .
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
 * @created Jun 4, 2013
 */
public class McDeassignmentPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;
	private static final String PRODUCT_TYPES = "PRODUCT_TYPES";

	private JComboBox productTypeComboBox;
	private JTextField dcNumberTextField;
	private JTextField mcNumberTextField;
	private JButton cancelButton;
	private JButton searchByDcButton;
	private JButton submitButton;

	public McDeassignmentPanel(TabbedMainWindow mainWindow) {
		super("MC ON Deassignment", KeyEvent.VK_D, mainWindow);
		initView();
		initData();
		mapValidators();
		mapActions();
	}

	@Override
	public void onTabSelected() {

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	// === initialization ===//
	protected void initView() {

		UiFactory factory = UiFactory.getInfo();
		setLayout(new MigLayout("insets 50 15 5 15", "[]15[200!,fill][max,fill]15[150!,fill]"));
		this.productTypeComboBox = new JComboBox();
		this.dcNumberTextField = UiFactory.getIdle().createTextField(Math.max(ProductType.BLOCK.getProductIdLength(), ProductType.HEAD.getProductIdLength()), TextFieldState.EDIT);
		this.mcNumberTextField = UiFactory.getIdle().createTextField(20, TextFieldState.DISABLED);
		this.searchByDcButton = factory.createButton("Search", true);
		this.submitButton = factory.createButton("Deassign");
		this.cancelButton = factory.createButton("Cancel", true);

		getProductTypeComboBox().setFont(factory.getInputFont());

		add(factory.createLabel("Product Type"));
		add(getProductTypeComboBox());
		add(getCancelButton(), new CC().cell(3, 0).wrap("20").height("50!"));
		add(factory.createLabel("DC Number"));
		add(getDcNumberTextField(), new CC().span(2));
		add(getSearchByDcButton(), "wrap 20, height 50!");
		add(factory.createLabel("MC Number"));
		add(getMcNumberTextField(), new CC().span(2));
		add(getSubmitButton(), "height 50!");
	}

	protected void initData() {
		getProductTypeComboBox().setModel(new DefaultComboBoxModel(getProductTypes()));
	}
	
	protected ProductType[] getProductTypes() {
		List<ProductType> productTypes = new ArrayList<ProductType>();
		String names = PropertyService.getProperty(ApplicationContext.getInstance().getProcessPointId(), PRODUCT_TYPES);
		if(names != null && names.length() > 0) {
			for(String productName : names.split(",")) {
				productTypes.add(ProductType.getType(productName));
			}
		} else {
			for(ProductType type : ProductType.values()) {
				if(ProductTypeUtil.isInstanceOf(type, DieCast.class)) {
					productTypes.add(type);
				}
			}
		}
		return productTypes.toArray(new ProductType[] {});
	}

	public void mapValidators() {
		List<Command> validators = new ArrayList<Command>();
		validators.add(new RequiredValidator());
		validators.add(new AlphaNumericValidator());
		ChainCommand validator = ChainCommand.create(validators, "DC Number");
		validator.setShortCircuit(true);
		UiUtils.mapValidator(getDcNumberTextField(), validator);
	}

	protected void mapActions() {
		ActionListener listener = new McDeassignmentListner(this);
		getCancelButton().addActionListener(listener);
		getDcNumberTextField().addActionListener(listener);
		getSearchByDcButton().addActionListener(listener);
		getSubmitButton().addActionListener(listener);
		InputNumberChangeListener inputListner = new InputNumberChangeListener(this, getDcNumberTextField());
		getDcNumberTextField().getDocument().addDocumentListener(inputListner);
	}

	// === get/set === //
	@SuppressWarnings("unchecked")
	public DiecastDao<? extends DieCast> getDiecastDao(ProductType productType) {
		return (DiecastDao<? extends DieCast>) ProductTypeUtil.getProductDao(productType);
	}

	protected JTextField getDcNumberTextField() {
		return dcNumberTextField;
	}

	protected JTextField getMcNumberTextField() {
		return mcNumberTextField;
	}

	protected JButton getCancelButton() {
		return cancelButton;
	}

	protected JButton getSubmitButton() {
		return submitButton;
	}

	protected JComboBox getProductTypeComboBox() {
		return productTypeComboBox;
	}

	protected JButton getSearchByDcButton() {
		return searchByDcButton;
	}

	// === event handler === //
	class McDeassignmentListner extends BaseListener<McDeassignmentPanel> implements ActionListener {

		public McDeassignmentListner(McDeassignmentPanel view) {
			super(view);
		}

		@Override
		protected void executeActionPerformed(ActionEvent e) {
			clearErrorMessage();
			if (e.getSource().equals(getCancelButton())) {
				reset();
			} else if (e.getSource().equals(getSearchByDcButton()) || e.getSource().equals(getDcNumberTextField())) {
				findProduct();
			} else if (e.getSource().equals(getSubmitButton())) {
				removeMcNumber();
			}
		}

		// === action implementation === //
		protected void reset() {
			getProductTypeComboBox().setEnabled(true);
			getMcNumberTextField().setText("");
			TextFieldState.DISABLED.setState(getMcNumberTextField());
			getDcNumberTextField().setText("");
			TextFieldState.EDIT.setState(getDcNumberTextField());
			getSubmitButton().setEnabled(false);
			getSearchByDcButton().setEnabled(true);
			getDcNumberTextField().requestFocus();
		}

		protected void findProduct() {
			ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
			if (productType == null) {
				JOptionPane.showMessageDialog(getView(), "Please select product type.");
				return;
			}

			String dcn = getDcNumberTextField().getText();
			dcn = StringUtils.trim(dcn);
			getDcNumberTextField().setText(dcn);

			if (!UiUtils.isValid(getDcNumberTextField(), getView().getMainWindow())) {
				return;
			}

			DieCast product = getDiecastDao(productType).findByDCSerialNumber(dcn);
			if (product == null) {
				String msg = String.format("%s does not exist for DC Number %s.", productType.getProductName(), dcn);
				setErrorMessage(msg);
				TextFieldState.ERROR.setState(getDcNumberTextField());
				getDcNumberTextField().selectAll();
				return;
			}

			if (StringUtils.isBlank(product.getMcSerialNumber())) {
				String msg = String.format("%s, DC Number %s, is not associated with MC Number.", productType.getProductName(), dcn);
				setErrorMessage(msg);
				TextFieldState.ERROR.setState(getDcNumberTextField());
				getDcNumberTextField().selectAll();
				return;
			}
			getMcNumberTextField().setText(product.getMcSerialNumber());
			TextFieldState.READ_ONLY.setState(getDcNumberTextField());
			TextFieldState.READ_ONLY.setState(getMcNumberTextField());
			getSearchByDcButton().setEnabled(false);
			getProductTypeComboBox().setEnabled(false);
			getSubmitButton().setEnabled(true);
			getCancelButton().requestFocus();
		}

		@SuppressWarnings("unchecked")
		protected void removeMcNumber() {
			int retCode = JOptionPane.showConfirmDialog(getView(), "Are you sure ?", "Remove MC Number", JOptionPane.YES_NO_OPTION);
			if (retCode != JOptionPane.YES_OPTION) {
				return;
			}
			ProductType productType = (ProductType) getProductTypeComboBox().getSelectedItem();
			String dcn = getDcNumberTextField().getText();
			DiecastDao<DieCast> dao = (DiecastDao<DieCast>) ProductTypeUtil.getProductDao(productType);
			DieCast product = dao.findByDCSerialNumber(dcn);
			product.setMcSerialNumber(null);
			dao.update(product);
			logUserAction(UPDATED, product);
			reset();
		}
	}
}
