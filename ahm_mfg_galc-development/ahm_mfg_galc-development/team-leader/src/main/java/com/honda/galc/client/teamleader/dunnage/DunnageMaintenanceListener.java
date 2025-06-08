package com.honda.galc.client.teamleader.dunnage;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.dunnage.DunnageUtils;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.DunnageHistoryUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DunnageMaintenanceListener</code> is ... .
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
 * @created Apr 26, 2013
 */
public class DunnageMaintenanceListener extends BaseListener<DunnageMaintenancePanel> implements ActionListener {

	private DunnageMaintenanceModel model;

	public static String ADD_TO_DUNNAGE_ACTION_ID = "ADD_TO_DUNNAGE";
	public static String REMOVE_FROM_DUNNAGE_ACTION_ID = "REMOVE_FROM_DUNNAGE";
	public static String PRINT_DUNNAGE_ACTION_ID = "PRINT_DUNNAGE";
	public static String SHIP_DUNNAGE_ACTION_ID = "SHIP_DUNNAGE";
	public static String REASSIGN_DUNNAGE_ACTION_ID = "REASSIGN_DUNNAGE";
	
	private AtomicInteger actionCounter;
	
	public DunnageMaintenanceListener(DunnageMaintenancePanel view, DunnageMaintenanceModel model) {
		super(view);
		this.model = model;
		setActionCounter(new AtomicInteger(0));
	}

	// === ActionListener handler === //
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			setWaitCursor();
			super.actionPerformed(ae);
		} finally {
			setDefaultCursor();
		}
	}	
	
	@Override
	protected void executeActionPerformed(ActionEvent ae) {
		getView().getMainWindow().clearMessage();
		if (ae.getSource().equals(getView().getDunnageNumberTextField())) {
			processDunnageNumber(true);
		} else if (ae.getSource().equals(getView().getProductNumberTextField())) {
			processProductNumber();
		} else if (ae.getSource().equals(getView().getClearButton())) {
			clearScreen();
		} else if (ae.getSource().equals(getView().getProductTablePopupMenu().getSubElements()[0])) {
			removeProductsFromDunnage();
		} else if (ae.getSource().equals(getView().getProductTablePopupMenu().getSubElements()[1])) {
			displayReassignDialog();
		} else if (ae.getSource().equals(getView().getShipButton())) {
			shipProducts();
		} else if (ae.getSource().equals(getView().getPrintButton())) {
			print();
		} else if (ae.getSource().equals(getView().getReassignDialog().getNumberTextField())) {
			reassignDunnage();
		} else if (ae.getSource().equals(getView().getReassignDialog().getSubmitButton())) {
			reassignDunnage();
		} else if (ae.getSource().equals(getView().getReassignDialog().getCancelButton())) {
			closeReassignDialog();
		}  
	}

	// === implementation === //
	protected void processDunnageNumber(boolean validate) {
		getView().getProductPane().removeData();
		JTextField textField = getView().getDunnageNumberTextField();
		String dunnageNumber = textField.getText();
		dunnageNumber = StringUtils.trim(dunnageNumber);
		textField.setText(dunnageNumber);

		if (validate && !UiUtils.isValid(textField, getView().getMainWindow())) {
			return;
		}

		TextFieldState.READ_ONLY.setState(textField);

		
		List<Map<String, Object>> data = getModel().selectDunnageProductsData(dunnageNumber);

		getView().getProductPane().reloadData(data);
		boolean validOption = true;
		if (getView().getProductPane().getTable().getRowCount() >= getModel().getDunnageCapacity()) {
			String msg = String.format("Dunnage is full, max capacity: %s", getModel().getDunnageCapacity());
			getView().getMainWindow().setMessage(msg);
			validOption = false;
		}
		getView().getShipButton().setEnabled(true);
		if (getModel().isPrintSupported()) {
			getView().getPrintButton().setEnabled(true);
		}
		if (getModel().isInsertDunnageContext() && validOption) {
			if(getModel().isNewDunnage(dunnageNumber)){
				String expectedQty = (String)JOptionPane.showInputDialog(getView(),
	                    "Enter Expected Quantity:",
	                    "Customized Dialog",
	                    JOptionPane.PLAIN_MESSAGE
	                    );
				if(expectedQty==null || StringUtils.isEmpty(expectedQty)) expectedQty = String.valueOf(getModel().getDunnageCapacity());
				String productSpec = (String)JOptionPane.showInputDialog(getView(),
	                    "Enter Product Spec Code:",
	                    "Customized Dialog",
	                    JOptionPane.PLAIN_MESSAGE
	                    );
				if(productSpec==null) productSpec = "";
				getModel().updateDunnageInfo(dunnageNumber, expectedQty, productSpec);
			}
		}
	}

	protected void processProductNumber() {
		JTextField productTextField = getView().getProductNumberTextField();
		String pin = productTextField.getText();
		pin = StringUtils.trim(pin);
		productTextField.setText(pin);

		if (!UiUtils.isValid(productTextField, getView().getMainWindow())) {
			return;
		}

		BaseProduct product = getModel().findProduct(pin);
		if (product == null) {
			String msg = "Product does not exist";
			getMainWindow().setErrorMessage(msg);
			TextFieldState.ERROR.setState(productTextField);
			productTextField.selectAll();
			return;
		}

		getView().getProductPane().getTable().repaint();

		if (!TextFieldState.READ_ONLY.isInState(getView().getDunnageNumberTextField())) {
			if (StringUtils.isBlank(product.getDunnage())) {		
				String msg = "No valid Dunnage found";
				TextFieldState.ERROR.setState(getView().getDunnageNumberTextField());
				getView().getDunnageNumberTextField().selectAll();
				getView().getMainWindow().setErrorMessage(msg);
			} else {
				getView().getDunnageNumberTextField().setText(product.getDunnage());
				processDunnageNumber(false);
				String msg = String.format("%s is assigned to Dunnage: %s", product, product.getDunnage());
				getView().getMainWindow().setMessage(msg);
			}
			return;
		}

		if (TextFieldState.READ_ONLY.isInState(getView().getDunnageNumberTextField())) {
			String dunnageNumber = getView().getDunnageNumberTextField().getText();
			if (dunnageNumber.equals(product.getDunnage())) {
				String msg = String.format("%s is already assigned to Dunnage: %s", product, dunnageNumber);
				getView().getMainWindow().setMessage(msg);
				return;
			}

			if (getView().getProductPane().getTable().getRowCount() >= getModel().getDunnageCapacity()) {
				String msg = String.format("Dunnage is full, max capacity: %s", getModel().getDunnageCapacity());
				getView().getMainWindow().setErrorMessage(msg);
				return;
			}
		
			String previousDunnage="";
			if (product.getDunnage() != null) {
				String msg = String.format("%s is already assigned to different Dunnage:%s, Are you sure you want to reassign ?", product, product.getDunnage());
				if (!MessageDialog.confirm(getView(), msg)) {
					return;
				}else previousDunnage= product.getDunnage();
			}

			// validate model code
			if (!validateModelCode(product.getModelCode(), dunnageNumber)) {
				String msg = String.format("product: %s model code: %s does not match Dunnage model.", product.getProductId(), product.getModelCode());
				getView().getMainWindow().setErrorMessage(msg);
				return;
			}
			
			if(getModel().isDunnageGroupedByModelType()){
				if (!validateModelAndTypeCode(product, dunnageNumber)){
					String msg = String.format("product: %s model code: %s and model type code: %s do not match Dunnage model/model type.", product.getProductId(), product.getModelCode(), ProductSpecUtil.extractModelTypeCode(product.getProductSpecCode()));
					getView().getMainWindow().setErrorMessage(msg);
					return;
				}				
			}
			
			if (getModel().isShippingSupported()) {
				List<BaseProduct> tmpList = new ArrayList<BaseProduct>();
				tmpList.add(product);
				List<Map<String, Object>> tmpMapList = DunnageUtils.mapDunnageMaintData(tmpList, getProductType(), getModel().getProperty());
				List<Map<String, Object>> tmpShippableMapList = DunnageUtils.filterShippable(tmpMapList);
				if (tmpShippableMapList.isEmpty()) {
					Map<String, Object> map = tmpMapList.get(0);
					String msg = "Product is not shippable - Defect : %s, Hold : %s, Off : %s";
					msg = String.format(msg, map.get(DunnageUtils.DEFECT_LABEL), map.get(DunnageUtils.ON_HOLD_LABEL), map.get(DunnageUtils.OFFED_LABEL));
					getView().getMainWindow().setErrorMessage(msg);
					return;
				}
			
				List<Map<String, Object>> allItems = getView().getProductPane().getItems();
				boolean dunnageShipped = DunnageUtils.isShipped(allItems);
				if (dunnageShipped) {
					if (!isAuthorized(ADD_TO_DUNNAGE_ACTION_ID)) {
						return;
					}
				}
			}			
			
			int count;
			if (getModel().isInsertDunnageContext()) {
				String dunnageRow = (String)JOptionPane.showInputDialog(getView(),
	                    "Enter Row:",
	                    "Enter Row",
	                    JOptionPane.PLAIN_MESSAGE
	                    );
				if(dunnageRow==null) dunnageRow = "";
				String dunnageColumn = (String)JOptionPane.showInputDialog(getView(),
	                    "Enter Column:",
	                    "Enter Column",
	                    JOptionPane.PLAIN_MESSAGE
	                    );
				if(dunnageColumn==null) dunnageColumn = "";
				String dunnageLayer = (String)JOptionPane.showInputDialog(getView(),
	                    "Enter Layer:",
	                    "Enter Layer",
	                    JOptionPane.PLAIN_MESSAGE
	                    );
				if(dunnageLayer==null) dunnageLayer = "";
				count =getModel().updateDunnage(getProductType().name(), product.getProductId(), dunnageNumber, dunnageRow, dunnageColumn, dunnageLayer);
			} else { 
				count = getModel().addToDunnage(product, dunnageNumber);
			}
			if (count > 0) {
				if(getModel().getProperty().isCreateDunnageHistory()) {
					if(StringUtils.isNotEmpty(previousDunnage)) DunnageHistoryUtil.updateDunnageHist(product.getProductId(), previousDunnage);
					DunnageHistoryUtil.createDunnageHist(product.getProductId(), dunnageNumber);
				}
				product = getModel().findProduct(product.getProductId());

				List<Map<String, Object>> data = getModel().selectDunnageProductsData(dunnageNumber);

				getView().getProductPane().reloadData(data);
				String msg = String.format("%s has been assigned to Dunnage: %s", product, dunnageNumber);
				getView().getMainWindow().setMessage(msg);
				Logger.getLogger().info(msg);
				getView().getProductNumberTextField().setText("");
			} else {
				String msg = String.format("Failed to assign %s to Dunnage: %s", product, dunnageNumber);
				getView().getMainWindow().setErrorMessage(msg);
				Logger.getLogger().warn(msg);
			}
		}
	}

	private boolean validateModelCode(String modelCode, String dunnageNumber) {
		List<? extends BaseProduct> findAllByDunnage = ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);
		for (BaseProduct aproduct : findAllByDunnage)
			if (modelCode == null || !modelCode.equals(aproduct.getModelCode())) {
				return false;
			}
		return true;
	}
	
	private boolean isModelCodeEquals(List<BaseProduct> products, List<BaseProduct> productsOnDunnage) {
		for (BaseProduct product : productsOnDunnage)
			for (BaseProduct prd : products) {
				if (!StringUtils.equals(prd.getModelCode(), product.getModelCode())) {
					return false;
				}
			}
		return true;
	}
	
	private boolean validateModelAndTypeCode(BaseProduct product, String dunnageNumber) {
		List<? extends BaseProduct> products = ProductTypeUtil.getProductDao(getProductType()).findAllByDunnage(dunnageNumber);
		for (BaseProduct prd : products) {
			if((ProductSpecUtil.extractModelTypeCode(product.getProductSpecCode()) == null || ProductSpecUtil.extractModelTypeCode(product.getProductSpecCode()).equals("*"))){
				if(!product.getModelCode().equals(prd.getModelCode())){
					return false;
				}
			}
			else{
				if (!( StringUtils.equals(prd.getModelCode(), product.getModelCode()) && StringUtils.equals(ProductSpecUtil.extractModelTypeCode(prd.getProductSpecCode()), ProductSpecUtil.extractModelTypeCode(product.getProductSpecCode())))) {
					return false;
				}
			}
		}
		return true;
	}	

	protected void removeProductsFromDunnage() {
		List<Map<String, Object>> list = getView().getProductPane().getSelectedItems();
		if (list != null && !list.isEmpty()) {
			if (!MessageDialog.confirm(getView(), "Are you sure to remove from Dunnage ?")) {
				return;
			}
			
			if (getModel().isShippingSupported()) {
				List<Map<String, Object>> allItems = getView().getProductPane().getItems();
				boolean dunnageShipped = DunnageUtils.isShipped(allItems);
				boolean selectedItemShipped = DunnageUtils.isShipped(list);

				if (dunnageShipped || selectedItemShipped) {
					if (!isAuthorized(REMOVE_FROM_DUNNAGE_ACTION_ID)) {
						return;
					}
				}
			}
			
			for (Map<String, Object> map : list) {
				BaseProduct product = (BaseProduct) map.get("product");
				if (product == null || product.getProductId() == null || product.getProductId().trim().length() == 0) {
					continue;
				}
				String dunnageId = product.getDunnage();
				getModel().removeFromDunnage(product);
				Logger.getLogger().info(String.format("%s was removed from Dunnage: %s", product, product.getDunnage()));
				
				if(getModel().getProperty().isCreateDunnageHistory()) DunnageHistoryUtil.updateDunnageHist(product.getProductId(), dunnageId);
			}
			processDunnageNumber(false);
		}
	}
	
	protected void displayReassignDialog() {
		getView().getReassignDialog().getNumberTextField().setText("");
		getView().getReassignDialog().setVisible(true);
		UiUtils.requestFocus(getView().getReassignDialog().getNumberTextField());
	}
	
	protected void closeReassignDialog() {
		getView().getReassignDialog().getNumberTextField().setText("");
		getView().getReassignDialog().getNumberTextField().repaint();
		getView().getReassignDialog().setVisible(false);
	}
	
	protected void reassignDunnage() {

		JTextField textField = getView().getReassignDialog().getNumberTextField();
		String targetDunnage = textField.getText();
		targetDunnage = StringUtils.trim(targetDunnage);
		textField.setText(targetDunnage);
		
		if (!isReassignDunnageNumberValid()) {
			return;
		}
		
		List<Map<String, Object>> selectedProductData = getView().getProductPane().getSelectedItems();
		List<BaseProduct> productsOnNewDunnage = getModel().selectDunnageProducts(targetDunnage);

		List<BaseProduct> selectedProducts = new ArrayList<BaseProduct>();
		for (Map<String, Object> map : selectedProductData) {
			BaseProduct product = (BaseProduct) map.get("product");
			if (product == null || product.getProductId() == null || product.getProductId().trim().length() == 0) {
				continue;
			}
			selectedProducts.add(product);
		}
		
		if (!isReassignDunnageProductsValid(selectedProductData, selectedProducts, productsOnNewDunnage)) {
			return;
		}

		if (!isReassignDunnageAuthorized(selectedProductData, productsOnNewDunnage)) {
			return;
		}
		
		List<String> productIds = new ArrayList<String>();
		for (BaseProduct p : selectedProducts) {
			productIds.add(p.getProductId());
		}

		int count = getModel().addToDunnage(productIds, targetDunnage);
		if (count == 0) {
			String msg = String.format("Failed to assign products to new Dunnage: %s, please refresh screen and try again.", targetDunnage);
			getView().getMainWindow().setErrorMessage(msg);
			Logger.getLogger().warn(msg + ", " + productIds);
			return;
		}
		
		String msg = String.format("%s product(s) have been assigned to new Dunnage: %s", count, targetDunnage);
		Logger.getLogger().info(msg + ", " + productIds);
		
		
		if(getModel().getProperty().isCreateDunnageHistory()) {
			DunnageHistoryUtil.updateDunnageHist(productIds, getView().getDunnageNumberTextField().getText().toString());
			DunnageHistoryUtil.createDunnageHist(productIds,targetDunnage);
		}		

		closeReassignDialog();
		getView().getMainWindow().setMessage(msg);
		
		String displayDunnage = null;
		int retCode = JOptionPane.showConfirmDialog(getView(), "Do you want to display new Dunnage ?", "Reassign Dunnge", JOptionPane.YES_NO_OPTION);
		if (retCode == JOptionPane.YES_OPTION) {
			displayDunnage = targetDunnage;
		} else {
			displayDunnage = getView().getDunnageNumberTextField().getText();
		}
		getView().getDunnageNumberTextField().setText(displayDunnage);
		processDunnageNumber(false);
	}
	
	protected boolean isReassignDunnageNumberValid() {
		JTextField textField = getView().getReassignDialog().getNumberTextField();
		String targetDunnage = textField.getText();
		if (StringUtils.isBlank(targetDunnage)) {
			textField.requestFocus();
			return false;
		}

		if (!UiUtils.isValid(textField, getView().getMainWindow())) {
			textField.requestFocus();
			return false;
		}

		String currentDunnage = getView().getDunnageNumberTextField().getText();
		if (targetDunnage.equals(currentDunnage)) {
			TextFieldState.ERROR.setState(textField);
			getView().getMainWindow().setErrorMessage("New Dunnage Number is the same as the current Dunnage Number!");
			textField.selectAll();
			textField.requestFocus();
			return false;
		}
		return true;
	}
	
	protected boolean isReassignDunnageProductsValid(List<Map<String, Object>> selectedProductData, List<BaseProduct> selectedProducts, List<BaseProduct> productsOnNewDunnage) {
		JTextField textField = getView().getReassignDialog().getNumberTextField();
		if (productsOnNewDunnage.size() >= getModel().getDunnageCapacity()) {
			TextFieldState.ERROR.setState(textField);
			getView().getMainWindow().setErrorMessage("New Dunnage is full !");
			textField.selectAll();
			textField.requestFocus();
			return false;
		}

		if (productsOnNewDunnage.size() + selectedProductData.size() > getView().getModel().getDunnageCapacity()) {
			TextFieldState.ERROR.setState(textField);
			getView().getMainWindow().setErrorMessage("There is not enough space on New Dunnage, max capacity :" + getView().getModel().getDunnageCapacity());
			textField.selectAll();
			textField.requestFocus();
			return false;
		}
		
		if (getModel().isShippingSupported()) {
			List<Map<String, Object>> tmpShippableMapList = DunnageUtils.filterShippable(selectedProductData);
			if (tmpShippableMapList.size() != selectedProductData.size()) {
				String msg = "There are selected not shippable Products";
				getView().getMainWindow().setErrorMessage(msg);
				return false;
			}
		}

		if (productsOnNewDunnage.size() > 0) {
			if (!isModelCodeEquals(selectedProducts, productsOnNewDunnage)) {
				String msg = String.format("Product model code does not match new Dunnage model !");
				getView().getMainWindow().setErrorMessage(msg);
				return false;
			}
		}
		return true;
	}
	
	protected boolean isReassignDunnageAuthorized(List<Map<String, Object>> selectedProductData, List<BaseProduct> productsOnNewDunnage) {
		List<String> requiredActions = new ArrayList<String>();
		requiredActions.add(DunnageMaintenanceListener.REASSIGN_DUNNAGE_ACTION_ID);
		if (getModel().isShippingSupported()) {
			List<Map<String, Object>> allProductsDataOnCurrentDunnage = getView().getProductPane().getItems();
			boolean dunnageShipped = DunnageUtils.isShipped(allProductsDataOnCurrentDunnage);
			if (dunnageShipped) {
				requiredActions.add(DunnageMaintenanceListener.REMOVE_FROM_DUNNAGE_ACTION_ID);
			}
			List<Map<String, Object>> productDataOnNewDunnage = DunnageUtils.mapDunnageMaintData(productsOnNewDunnage, getProductType(), getModel().getProperty());
			boolean targetDunnageShipped = DunnageUtils.isShipped(productDataOnNewDunnage);
			if (targetDunnageShipped) {
				requiredActions.add(DunnageMaintenanceListener.ADD_TO_DUNNAGE_ACTION_ID);
			}
		}
		if (!isAuthorized(requiredActions.toArray(new String[] {}))) {
			return false;
		}
		return true;
	}
	
	protected boolean isAuthorized(String... actionIds) {
		if (actionIds == null || actionIds.length == 0) {
			return true;
		}
		Map<String, String> groups = getModel().getProperty().getAuthorizationGroup();
		if (groups == null || groups.isEmpty()) {
			return true;
		}
		Map<String, String> requiredGroups = new LinkedHashMap<String, String>();
		for (String actionId : actionIds) {
			String authorizedGroups = groups.get(actionId);
			if (StringUtils.isBlank(authorizedGroups)) {
				continue;
			}
			requiredGroups.put(actionId, authorizedGroups);
		}
		if (requiredGroups.isEmpty()) {
			return true;
		}
		if (LoginDialog.login() != LoginStatus.OK) {
			return false;
		}
		String authenticatedUser = ClientMain.getInstance().getAccessControlManager().getUserName();
		for (String actionId : requiredGroups.keySet()) {
			String authorizedGroups = requiredGroups.get(actionId);
			if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(authorizedGroups)) {
				String msg = "User:" + authenticatedUser + " is not authorized to execute " + actionId + " action.";
				Logger.getLogger().info(msg);
				JOptionPane.showMessageDialog(getView(), "You are not authorized to execute " + actionId + " action !", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		Logger.getLogger().info("Authorized User:" + authenticatedUser + " executed " + requiredGroups.keySet() + " action(s).");
		return true;
	}
	
	protected void clearScreen() {
		TextFieldState.EDIT.setState(getView().getDunnageNumberTextField());
		getView().getDunnageNumberTextField().setText("");
		TextFieldState.EDIT.setState(getView().getProductNumberTextField());
		getView().getProductNumberTextField().setText("");
		getView().getProductPane().removeData();
		getView().getDunnageNumberTextField().requestFocus();
		getView().getShipButton().setEnabled(false);
		getView().getPrintButton().setEnabled(false);
	}

	protected void shipProducts() {

		List<Map<String, Object>> list = getView().getProductPane().getItems();
		if (list == null || list.isEmpty()) {
			String msg = "Nothing to be shipped.";
			getView().getMainWindow().setMessage(msg);
			return;
		}
		boolean partialDunnageShippable = getModel().getProperty().isPartialDunnageShippable();
		if (!partialDunnageShippable && list.size() < getModel().getDunnageCapacity()) {
			JOptionPane.showMessageDialog(getView(), "Dunnage is not full. Capacity: " + getModel().getDunnageCapacity() + ", items: " + list.size() + " !", "Ship Products", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		List<Map<String, Object>> notShippedProducts = DunnageUtils.filterNotShipped(list);
		if (notShippedProducts.isEmpty()) {
			JOptionPane.showMessageDialog(getView(), "All products are already shipped !", "Ship Products", JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<Map<String, Object>> shippableProducts = DunnageUtils.filterShippable(notShippedProducts);

		if (shippableProducts.size() < 1) {
			JOptionPane.showMessageDialog(getView(), "There are no shippable producst in the list", "Ship Products", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (shippableProducts.size() < notShippedProducts.size()) {
			JOptionPane.showMessageDialog(getView(), "Please remove not shippable producst from list", "Ship Products", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String msg = String.format("You are about to ship %s product%s ", shippableProducts.size(), shippableProducts.size() == 1 ? "" : "s");
		if (shippableProducts.size() < list.size()) {
			msg = msg + " out of " + list.size();
		}
		msg = msg + ". \n Are you sure ? ";
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Ship Products", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		
		if (list.size() < getModel().getDunnageCapacity()) {
			if (!isAuthorized(SHIP_DUNNAGE_ACTION_ID)) {
				return;
			}
		}
		
		List<BaseProduct> products = DunnageUtils.getProducts(shippableProducts);
		
		String dunnageNumber = getView().getDunnageNumberTextField().getText();
		List<Map<String, Object>> refreshedList = getModel().selectDunnageProductsData(dunnageNumber);
		List<Map<String, Object>> refreshedNotShipped = DunnageUtils.filterNotShipped(refreshedList);
		List<Map<String, Object>> refreshedShippable = DunnageUtils.filterShippable(refreshedNotShipped);
		List<BaseProduct> refreshedProducts = DunnageUtils.getProducts(refreshedShippable);

		if (!(products.containsAll(refreshedProducts) && refreshedProducts.containsAll(products))) {
			msg = String.format("Data might have been changed by another process, please refresh the list and try again.\nDo you want to refresh list now ?");
			retCode = JOptionPane.showConfirmDialog(getView(), msg, "Ship Products", JOptionPane.YES_NO_OPTION);
			if (retCode == JOptionPane.YES_OPTION) {
				processDunnageNumber(false);
			}
			return;
		}

		getModel().shipProducts(products);
		clearScreen();
	}

	protected void print() {

		if (getView().getProductPane().getTable().getRowCount() == 0) {
			JOptionPane.showMessageDialog(getView(), "There are not product to send to printer.");
			return;
		}

		String msg = String.format("Are you sure you want to print ?");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Print", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		if (getView().getProductPane().getTable().getRowCount() < getModel().getDunnageCapacity()) {
			if (!isAuthorized(PRINT_DUNNAGE_ACTION_ID)) {
				return;
			}
		}
		
		JTextField textField = getView().getDunnageNumberTextField();
		String dunnageNumber = textField.getText();

		DataContainer dc = new DefaultDataContainer();

		ProductType productType = getProductType();
		String produTypectName = productType.getProductName();

		dc.put("DUNNAGE_NUMBER", dunnageNumber);
		dc.put("PRODUCT_TYPE_NAME", produTypectName);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, "*");

		List<Map<String, Object>> data = getView().getProductPane().getItems();
		List<Object> products = new ArrayList<Object>();
		for (Map<String, Object> item : data) {
			products.add(item.get("product"));
		}
		dc.put("DATA_SOURCE", products);
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		for (BroadcastDestination bd : getModel().getPrinters()) {
			DataContainer result = getService(BroadcastService.class).broadcast(bd.getId().getProcessPointId(), bd.getId().getSequenceNumber(), dc);
			if(result != null && result.getErrorMessages() != null && !result.getErrorMessages().isEmpty() ){ 
				//added only the first message due the view only can shows one message at a time
				getView().getMainWindow().setErrorMessage(result.getErrorMessages().get(0));
			}						
		}
	}

	protected ProductType getProductType() {
		return getModel().getApplicationContext().getProductTypeData().getProductType();
	}

	// === get/set === //
	public DunnageMaintenanceModel getModel() {
		return model;
	}

	protected AtomicInteger getActionCounter() {
		return actionCounter;
	}

	protected void setActionCounter(AtomicInteger actionCounter) {
		this.actionCounter = actionCounter;
	}
	
	protected void setWaitCursor() {
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getActionCounter().incrementAndGet();
	}

	protected void setDefaultCursor() {
		getActionCounter().decrementAndGet();
		if (getActionCounter().get() < 1) {
			getMainWindow().setCursor(Cursor.getDefaultCursor());
		}
	}
	
	
}
