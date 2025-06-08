package com.honda.galc.client.teamleader.qi.controller;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.DunnageMaintModel;
import com.honda.galc.client.teamleader.qi.view.DunnageContentDialog;
import com.honda.galc.client.teamleader.qi.view.DunnageDialog;
import com.honda.galc.client.teamleader.qi.view.DunnageMaintPanel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.DunnageUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.entity.product.DunnageContent;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.DunnageHistoryUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

/**
 * 
 * <h3>DunnageMaintController Class description</h3>
 * <p> DunnageMaintController description </p>
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
 *
 * </TABLE>
 *   
 * @author L&T Infotech<br>
 * April 20, 2017
 *
 *
 */
public class DunnageMaintController extends AbstractQiController<DunnageMaintModel, DunnageMaintPanel>
implements EventHandler<ActionEvent> {

	public DunnageMaintController(DunnageMaintModel model, DunnageMaintPanel view) {
		super(model, view);
	}

	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
			if (actionEvent.getSource().equals(getView().getDunnageTextField().getControl())
					&& !getView().getDunnageTextField().getControl().getText().isEmpty()) {
				processDunnageNumber();
			}

			else if (actionEvent.getSource().equals(getView().getBlockTextField().getControl())
					&& !getView().getBlockTextField().getControl().getText().isEmpty()) {
				processProductNumber();

			} else if (actionEvent.getSource().equals(getView().getDunnageTextField().getControl())
					&& getView().getDunnageTextField().getControl().getText().isEmpty()) {
				getView().getDunnageTablePane().getTable().getItems().clear();
				displayErrorMessage("Dunnage cannot be empty");

			} else if (actionEvent.getSource().equals(getView().getBlockTextField().getControl())
					&& getView().getBlockTextField().getControl().getText().isEmpty()) {
				getView().getDunnageTablePane().getTable().getItems().clear();
				displayErrorMessage("Block cannot be empty");
			}

		} else if (actionEvent.getSource().equals(getView().getClearButton())) {
			clearScreen();
		} else if (actionEvent.getSource().equals(getView().getShipButton())) {
			shipProducts();
		}
		else if (actionEvent.getSource().equals(getView().getPrintButton())) {
			printDunnage();
		}
		if (actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if ("Remove".equals(menuItem.getText())) {
				removeProductsFromDunnage();
			}
		}
	}

	/**
	 * This method is used to process dunnage number
	 */
	protected void processDunnageNumber() {
		clearMessage();
		String validationMessage = validateDunnageField(getView().getDunnageTextField().getControl());
		if(!StringUtils.isEmpty(validationMessage)) {
			displayErrorMessage(validationMessage);
			return;
		}
		String dunnageNumber = StringUtils.trimToEmpty(getView().getDunnageTextField().getControl().getText());
		boolean validOption = true;
		getView().getShipButton().setDisable(false);
		if(!getDunnagePrinters().isEmpty()){
			getView().getPrintButton().setDisable(false);
		}
		if (getModel().isInsertDunnageContent() && validOption) {
			if (getModel().isNewDunnage(dunnageNumber)) {
				DunnageDialog dialog = new DunnageDialog("Create Dunnage",getApplicationId(), getModel());
				boolean isOk = dialog.showDunnageDialog();
				if(isOk){
					getModel().updateDunnageInfo(dunnageNumber, dialog.getDunnageQuantityText(),
							dialog.getProductspecCodeText());
				}
			}
		}
		getView().reload(dunnageNumber);
	}
	/**
	 * this method is used to clear screen
	 */
	protected void clearScreen() {
		clearMessage();
		getView().getDunnageTablePane().getTable().getItems().clear();
		getView().getDunnageTextField().clear();
		getView().getBlockTextField().clear();
		getView().getShipButton().setDisable(true);
		getView().getDunnageTextField().getControl().setDisable(false);
		getView().getPrintButton().setDisable(true);
	}

	/**
	 * This method is used to process product number
	 */
	protected void processProductNumber() {
		clearMessage();
		BaseProduct product = getModel().findProduct(getView().getBlockTextField().getControl().getText());
		String dunnageNumber = StringUtils.trimToEmpty(getView().getDunnageTextField().getControl().getText());
		int count;
		if (product == null) {
			displayErrorMessage("Product does not exist");
			return;
		}
		if (StringUtils.isEmpty(dunnageNumber)) {
			if (StringUtils.isEmpty(product.getDunnage())) {
				displayErrorMessage("No valid Dunnage found");
			} 
			else {
				getView().getDunnageTextField().getControl().setText(product.getDunnage());
				processDunnageNumber();
				getView().getDunnageTextField().getControl().setDisable(true);
				getView().getShipButton().setDisable(false);
			}
			return;
		} else {
			if(getModel().findDunnageById(dunnageNumber)==null) {
				displayErrorMessage("Dunnage does not exist");
				return;
			}
			if (dunnageNumber.equals(product.getDunnage())) {
				displayErrorMessage(product + " is already assigned to Dunnage: " + dunnageNumber);
				return;
			}
		}

		if (getModel().findAllByDunnage(dunnageNumber).size() >= getModel().getDunnageCapacity()) {
			displayErrorMessage("Dunnage is full, max capacity:" + getModel().getDunnageCapacity());
			return;
		}
		String previousDunnage="";
		if (!StringUtils.isEmpty(product.getDunnage())) {
			boolean isReassign;
			isReassign = MessageDialog.confirm(getView().getStage(),
					product + " is already assigned to different Dunnage:" + product.getDunnage()
					+ ", Are you sure you want to reassign ?");
			if (!isReassign) {
				return;
			}else previousDunnage= product.getDunnage();
			if (!validateModelCode(product.getModelCode(), getView().getDunnageTextField().getText())) {
				displayErrorMessage(product.getProductId() + " model code does not match Dunnage model " + product.getModelCode());
				return;
			}
			if (getModel().isInsertDunnageContent()) {
				count = openDunnageDialog(product, dunnageNumber);
			} else
				count = getModel().addToDunnage(product, dunnageNumber);
		} else {
			if (!validateModelCode(product.getModelCode(), getView().getDunnageTextField().getText())) {
				displayErrorMessage(product.getProductId() + " model code does not match Dunnage model " + product.getModelCode());
				return;
			}
			count = openDunnageDialog(product, dunnageNumber);
		}
		
		if(count > 0) {			
			if(getModel().getProperty().isCreateDunnageHistory()) {
				if(StringUtils.isNotEmpty(previousDunnage)) DunnageHistoryUtil.updateDunnageHist(product.getProductId(), previousDunnage);
				DunnageHistoryUtil.createDunnageHist(product.getProductId(), dunnageNumber);
			}
			getView().reload(dunnageNumber);
			EventBusUtil.publish(new StatusMessageEvent(product + " is assigned to Dunnage: " + dunnageNumber, StatusMessageEventType.INFO));
		} else {
			displayErrorMessage(product + "Failed to assign to Dunnage:" + dunnageNumber);
		}

	}

	/**
	 * This method is used to open poup dialog
	 * @param product
	 * @param dunnageNumber
	 * @return
	 */
	private int openDunnageDialog(BaseProduct product, String dunnageNumber) {
		int count =0;
		DunnageContentDialog dunnageContentDialog = new DunnageContentDialog("Assign Dunnage",getApplicationId(), getModel());
		boolean isOk = dunnageContentDialog.showDunnageContentDialog();
		if(isOk){
			count = updateDunnage(getModel().getProductType(), product.getProductId(), dunnageNumber,dunnageContentDialog.getRowTextField().getText(),getModel().getDunnageCapacity(),
					dunnageContentDialog.getColumnTextField().getText(), dunnageContentDialog.getLayerTextField().getText());
		}
		return count;
	}

	@Override
	public void addContextMenuItems() {
		List<String> menuItemsList = new ArrayList<String>();
		ObservableList<Map<String,Object>> selectedDunnage = getView().getDunnageTablePane().getSelectedItems();
		if (selectedDunnage != null) {
			menuItemsList.add("Remove");
			getView().getDunnageTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}
		getView().getDunnageTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}

	private void addDunnageTableListener() {
		clearDisplayMessage();
		getView().getDunnageTablePane().getTable().getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<Map<String,Object>>() {
			public void changed(ObservableValue<? extends Map<String,Object>> observableValue, Map<String,Object> oldValue,
					Map<String,Object> newValue) {
				if(newValue!=null) {
					addContextMenuItems();
				} else {
					ObservableList<Map<String,Object>> selectedDunnage = getView().getDunnageTablePane().getSelectedItems();
					if(selectedDunnage == null || selectedDunnage.isEmpty()) {
						getView().getDunnageTablePane().getTable().getContextMenu().getItems().clear();
					}
				}
			}
		});
	}

	@Override
	public void initEventHandlers() {
		clearMessage();
		addDunnageTableListener();
	}

	/**
	 * This method is used to remove products from dunnage
	 */
	protected void removeProductsFromDunnage() {
		clearMessage();
		List<Map<String,Object>> list = getView().getDunnageTablePane().getSelectedItems();
		if (list != null && !list.isEmpty()) {
			if (!MessageDialog.confirm(getView().getStage(), "Are you sure you want to remove product from Dunnage ?")) {
				return;
			}
			for (Map<String,Object> map : list) {
				BaseProduct product = (BaseProduct) map.get("product");
				if (product == null || product.getProductId() == null || product.getProductId().trim().length() == 0) {
					continue;
				}
				String dunnageId= product.getDunnage().toString();
				getModel().removeFromDunnage(product);
				
				if(getModel().getProperty().isCreateDunnageHistory()) DunnageHistoryUtil.updateDunnageHist(product.getProductId(), dunnageId);
				Logger.getLogger().info(String.format( product+ " was removed from Dunnage: ", product.getDunnage()));
			}
			processDunnageNumber();
		}
	}
	/**
	 * This method is used to validate model code
	 * @param modelCode
	 * @param dunnageNumber
	 * @return
	 */
	private boolean validateModelCode(String modelCode, String dunnageNumber) {
		List<? extends BaseProduct> findAllByDunnage = ProductTypeUtil.getProductDao(getModel().getProductType())
				.findAllByDunnage(dunnageNumber);
		for (BaseProduct aproduct : findAllByDunnage)
			if (modelCode == null || !modelCode.equals(aproduct.getModelCode())) {
				return false;
			}
		return true;
	}
	/**
	 * This method is used to validate dunnage field
	 * @param textField
	 * @return
	 */
	private String validateDunnageField(TextField textField) {
		String text = StringUtils.trimToEmpty(textField.getText());
		if (text.length() != getModel().getProperty().getDunnageNumberLength()) {
			return "Dunnage Field should be "+getModel().getProperty().getDunnageNumberLength()+" characters long";
		} else if(getModel().getProperty().isCheckDunnageId()){
			String machineId = text.substring(0, 3);
			String date = text.substring(3, 9);
			String serial = text.substring(9, 12);

			Pattern machinePattern = Pattern.compile("[A-Z][A-Z][A-Z]");
			Matcher machineMatcher = machinePattern.matcher(machineId);
			if (!machineMatcher.matches()) {
				return "Not a valid Machine Name";
			}
			if (!isValidDate(date)) {
				return "Not a valid date. The date has to be in format 'yyMMdd'";
			}
			Pattern serialPattern = Pattern.compile("[0-9][0-9][0-9]");
			Matcher serialMatcher = serialPattern.matcher(serial);
			if (!serialMatcher.matches()) {
				return "Not a valid Serial Number";
			}
		}
		
		return StringUtils.EMPTY;
	}
	/**
	 * This method is used to validate date
	 * @param inDate
	 * @return
	 */
	private boolean isValidDate(String inDate) {
		Date inputDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		dateFormat.setLenient(false);
		try {
			inputDate = dateFormat.parse(inDate.trim());
		} catch (Exception pe) {
			return false;
		}
		if (inputDate.after(new Date()))
			return false;
		return true;
	}

	protected void shipProducts() {
		clearMessage();
		List<Map<String, Object>> list = getView().getDunnageTablePane().getTable().getItems();
		if (list == null || list.isEmpty()) {
			displayErrorMessage("Nothing to be shipped" );
			return;
		}

		List<BaseProduct> shippableProducts = getModel().filterShippable(list);

		if (shippableProducts.size() < 1) {
			displayErrorMessage("There are no shippable product in the list" );
			return;
		}

		if (shippableProducts.size() < list.size()) {
			displayErrorMessage("Please remove not shippable product from list");
			return;
		}

		boolean retCode=MessageDialog.confirm(getView().getStage(), "Are you sure you want to ship ?");
		if (!retCode) {
			return;
		}

		String dunnageNumber = getView().getDunnageTextField().getText();
		List<Map<String, Object>> refreshedList = getModel().selectDunnageProductsData(dunnageNumber);
		List<BaseProduct> products = getModel().filterShippable(refreshedList);
		if (products.size() < refreshedList.size()) {
			MessageDialog.confirm(getView().getStage(), "Data might have been changed by another process, please refresh the list and try again.\nDo you want to refresh list now ?");
			if (retCode) {
				processDunnageNumber();
			}
			return;
		}
		getModel().shipProducts(products);
		clearScreen();
	}

	public int updateDunnage(String productType,String productId, String dunnageNumber, String dunnageRow,int dunnageCapacity,String dunnageColumn, String dunnageLayer) {
		ProductType prodType = ProductType.valueOf(productType);
		switch (prodType) {
		case MBPN:
			getDao(MbpnProductDao.class).updateDunnage(productId, dunnageNumber, dunnageCapacity);
			break;
		case BLOCK: case HEAD: case CRANKSHAFT:case CONROD: case TCCASE: case MCASE:
			getModel().updateDieCaseDunnage(productType, productId, dunnageNumber);
			break;
		default:
			getModel().updateSubProductDunnage(productType, productId, dunnageNumber, dunnageCapacity);
		}
		if(getModel().isFirstProductInDunnage(dunnageNumber)){
			Dunnage dunnage = getModel().findDunnageById(dunnageNumber);
			switch (prodType) {
			case MBPN:
				dunnage.setProductSpecCode( getDao(MbpnProductDao.class).findBySn(productId).getProductSpecCode());
				break;
			case BLOCK: case HEAD: case CRANKSHAFT:case CONROD: case TCCASE: case MCASE:
				dunnage.setProductSpecCode(getModel().findDieCastByProductId(productType, productId).getProductSpecCode());
				break;
			default:
				dunnage.setProductSpecCode(getModel().findSubProductByProductId(productType, productId).getProductSpecCode());
				getModel().updateDunnage(dunnage);
			}
			DunnageContentId dunnageContentId = new DunnageContentId(dunnageNumber,productId);
			DunnageContent dunnageContent = new DunnageContent();
			dunnageContent.setId(dunnageContentId);
			dunnageContent.setDunnageColumn(dunnageColumn);
			dunnageContent.setDunnageLayer(dunnageLayer);
			dunnageContent.setDunnageRow(dunnageRow);

			getModel().updateDunnageContent(dunnageContent);
		}
		return 1;

	}

	private void printDunnage() {
		if (getView().getDunnageTablePane().getTable().getItems().size() == 0) {
			MessageDialog.showError(getView().getStage(), "There are no product(s) to send to printer.");
			return;
		}
		if (!MessageDialog.confirm(getView().getStage(), "Are you sure you want to print ?")) {
			return;
		}

		String dunnageNumber = getView().getDunnageTextField().getText();
		String productTypeName  = getModel().getProductType();
		String processPointId = getModel().getApplicationContext().getProcessPointId();
		List<Map<String, Object>> data = getView().getDunnageTablePane().getTable().getItems();
		List<Object> products = new ArrayList<Object>();
		for(Map<String, Object> item :data){
			products.add(item.get("product"));
		}
		DunnageUtils.print(dunnageNumber,productTypeName,products,processPointId,getDunnagePrinters());// Call to print Dunnage data
	}

	private List<BroadcastDestination> getDunnagePrinters(){
		String dunnagePrinter = getModel().getProperty().getDunnagePrinter();
		String dunnageForm = getModel().getProperty().getDunnageForm();
		String applicationId = getModel().getApplicationId();
		return DunnageUtils.getPrinters(dunnagePrinter,dunnageForm,applicationId);
	}
}
