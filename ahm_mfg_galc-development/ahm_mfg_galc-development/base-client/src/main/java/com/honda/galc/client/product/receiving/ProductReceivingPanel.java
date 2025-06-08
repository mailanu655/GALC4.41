package com.honda.galc.client.product.receiving;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.events.ProductReceivingEvent;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.ApplicationMainWindow;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextPanel;

/**
 * <h3>Class description</h3>
 * The panel class for product receiving.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class ProductReceivingPanel extends ApplicationMainPanel implements ActionListener, EventSubscriber<ProductReceivingEvent> {

	private static final long serialVersionUID = 2943846203613853528L;
	private static final int ENTRY_DELAY = 3000;
	
	@SuppressWarnings("unchecked")
	private ProductReceivingController controller;
	private LabeledTextPanel productIdPanel;
	private LabeledTextPanel newProductIdPanel;
	private LabeledTextPanel lastProcessedPanel;
	private LabeledComboBox targetProduct;
	private LabeledComboBox originalProduct;
	
	public ProductReceivingPanel(ApplicationMainWindow window) {
		super(window);
		initialize();
	}
	
	@SuppressWarnings("unchecked")
	private void initialize() {
		controller = new ProductReceivingController();
		controller.setApplicationContext(getMainWindow().getApplicationContext());
		ReceivedProductData data = new ReceivedProductData();
		data.setProductType(getMainWindow().getProductType());
		controller.setData(data);
		initializePanels();
		initializeProducts();
		disableInputMode();
		EventBus.subscribe(ProductReceivingEvent.class, this);
	}

	private void initializePanels() {
		setLayout(new BorderLayout());
		this.add(createSelectionPanel(), BorderLayout.NORTH);

		this.add(createConversionPanel(), BorderLayout.CENTER);
	}
	
	public JPanel createSelectionPanel() {
		JPanel panel = new JPanel();
		
		targetProduct = new LabeledComboBox("Model", true);
		targetProduct.setFont(new Font("Dialog", Font.BOLD, 20));
		targetProduct.getComponent().setPreferredSize(new Dimension(200,30));
		targetProduct.getComponent().addActionListener(this);
		panel.add(targetProduct);
		
		
		originalProduct = new LabeledComboBox("Import From", true);
		originalProduct.setFont(new Font("Dialog", Font.BOLD, 20));
		originalProduct.getComponent().setPreferredSize(new Dimension(200,30));
		originalProduct.getComponent().addActionListener(this);
		panel.add(originalProduct);
		return panel;
	}
	
	public JPanel createConversionPanel() {
		JPanel panel = new JPanel();
		createProductIdPanels();
		GridBagLayout layout = new GridBagLayout();
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.EAST;
		c.weighty = 1.0;

		panel.setLayout(layout);
		layout.setConstraints(productIdPanel, c);
		panel.add(productIdPanel);
		
		layout.setConstraints(newProductIdPanel, c);
		panel.add(newProductIdPanel);
		
		c.weighty = 0.5;
		layout.setConstraints(lastProcessedPanel, c);
		panel.add(lastProcessedPanel);
		
		return panel;
	}
	
	public void createProductIdPanels() {
		productIdPanel = new LabeledTextPanel(getMainWindow().getProductType() + " ID");
		productIdPanel.setFont(new Font("Dialog", Font.BOLD, 30));
		productIdPanel.getTextField().setPreferredSize(new Dimension(500, 60));
		productIdPanel.clear();
		productIdPanel.getComponent().addActionListener(this);
		productIdPanel.disableMode();

		newProductIdPanel = new LabeledTextPanel("New ID");
		newProductIdPanel.setFont(new Font("Dialog", Font.BOLD, 30));
		newProductIdPanel.getTextField().setPreferredSize(new Dimension(500, 60));
		newProductIdPanel.clear();
		newProductIdPanel.getComponent().addActionListener(this);
		newProductIdPanel.disableMode();

		lastProcessedPanel = new LabeledTextPanel("Last Processed");
		lastProcessedPanel.setFont(new Font("Dialog", Font.BOLD, 26));
		lastProcessedPanel.getTextField().setPreferredSize(new Dimension(500, 50));
		lastProcessedPanel.setText("");
		lastProcessedPanel.disableMode();
	}

	@SuppressWarnings("unchecked")
	private void initializeProducts() {
		List<String> products = controller.findTargetProducts();
		targetProduct.setModel(products, 0);
	}
	
	@SuppressWarnings("unchecked")
	private void productSelectionChanged() {
		List<String> products = controller.findImportProducts((String) targetProduct.getComponent().getSelectedItem());
		originalProduct.setModel(products, 0);
	}

	private void productIdEntered() {
		String productId = productIdPanel.getComponent().getText();
		if(StringUtils.isEmpty(productId)) {
			clearScreen();
			return;
		}
		newProductIdPanel.clear();
		newProductIdPanel.disableMode();
		getMainWindow().displayMessage("");
		controller.setData(getReceivedProductData());
		if(controller.isProductIdValid(productId)) {
			validProductIdEntered(productId);
		} else {
			invalidProductIdEntered(productId);
		}
	}
	
	private void validProductIdEntered(String productId) {
		productIdPanel.okMode();
		newProductIdPanel.disableMode();
		String newId = controller.productIdEntered(productId, originalProduct.getComponent().getSelectedItem().toString());
		if(newId != null) {
			newProductIdPanel.setText(newId);
			getMainWindow().displayMessage("");
		} else {
			newProductIdPanel.setText("Error!");
			newProductIdPanel.errorMode();
		}
	}
	
	private ReceivedProductData getReceivedProductData() {
		ReceivedProductData data = new ReceivedProductData();
		data.setOriginalId(productIdPanel.getComponent().getText());
		data.setProductType(getMainWindow().getProductType());
		data.setNewModelCode(targetProduct.getComponent().getSelectedItem().toString());
		data.setConversionId(originalProduct.getComponent().getSelectedItem().toString());
		return data;
	}
	
	private void resetFields(String lastProcessedId) {
		if(lastProcessedId != null) {
			lastProcessedPanel.setText(lastProcessedId);
		}
		clearScreen();
	}
	
	private void invalidProductIdEntered(String productId) {
		productIdPanel.errorMode();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if(productIdPanel != null && object.equals(productIdPanel.getComponent())) {
			productIdEntered();
		} else if(targetProduct != null && object.equals(targetProduct.getComponent()))	{
			productSelectionChanged();
		} else if(originalProduct != null && object.equals(originalProduct.getComponent())) {
			if(targetProduct.getComponent().getSelectedItem() == null 
					|| originalProduct.getComponent().getSelectedItem() == null) {
				disableInputMode();
			} else {
				originalProduct.getComponent().requestFocus(false);
				clearScreen();
			}
		}
	}
	
	@Override
	public ApplicationMainWindow getMainWindow() {
		return (ApplicationMainWindow) super.getMainWindow();
	}
	
	public void clearScreen() {
		getMainWindow().displayMessage("");
		newProductIdPanel.clear();
		newProductIdPanel.disableMode();
		productIdPanel.clear();
		productIdPanel.inputMode();
		productIdPanel.requestFocus();
	}
	
	private void disableInputMode() {
		newProductIdPanel.clear();
		newProductIdPanel.disableMode();
		productIdPanel.clear();
		productIdPanel.disableMode();
	}

	public void onEvent(ProductReceivingEvent event) {
		if(event.getResult()) {
			getMainWindow().displayMessage(event.getMessage());
		} else {
			getMainWindow().displayErrorMessage(event.getMessage());
			newProductIdPanel.setText("Error!");
			newProductIdPanel.errorMode();
			productIdPanel.errorCorrectionMode();
			return;
		}
		if(event.isClearScreen()) {
			if(event.getLastId() != null) {
				lastProcessedPanel.setText(event.getLastId());
			}
			try {
				Thread.sleep(ENTRY_DELAY);
			} catch (InterruptedException e) {
			}
			resetFields(null);
			return;
		}
	}

}

