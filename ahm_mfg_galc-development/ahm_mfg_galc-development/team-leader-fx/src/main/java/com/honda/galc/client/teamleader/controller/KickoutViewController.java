/**
 * Controller class for the Kickout Teamleader Screen
 *
 * @author Bradley Brown
 * @version 1.0
 * @since 2.43
 */
package com.honda.galc.client.teamleader.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.teamleader.model.KickoutViewModel;
import com.honda.galc.client.teamleader.view.KickoutView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedMenuItem;
import com.honda.galc.client.ui.event.ProcessCompleteEvent;
import com.honda.galc.client.ui.event.ProductInvalidEvent;
import com.honda.galc.client.ui.event.ProductValidEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.event.SelectionEventType;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.ui.event.TextFieldEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.util.StringUtil;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class KickoutViewController implements EventHandler<ActionEvent> {
	private static final String PRODUCTS_ALREADY_ADDED_TO_TABLE = "Products already added to table.";
	private static final String PRODUCT_ALREADY_ADDED_TO_TABLE = "Product already added to table.";
	private static final String PRODUCT_SEARCH_COMPLETE = "Product search complete.";
	private static final String COULD_NOT_RELEASE_KICKOUT = "Could not release kickout.";
	private static final String SUCCESSFULLY_RELEASED_KICKOUT = "Successfully released kickout";
	private KickoutView view;
	private KickoutViewModel model;
	private boolean isProcessPointSelected = false;
	private boolean isKickoutReasonEntered = false;
	private boolean isReleaseReasonEntered = false;

	public KickoutViewController(KickoutView view) {
		this.view = view;
		this.model = new KickoutViewModel(view.getMainWindow().getApplicationContext());
	}

	public void init() {
		initTableListeners();
		initListeners();
		setViewState();
		EventBusUtil.register(this);
	}

	private void initTableListeners() {
		view.getKickoutTable().getTable().setRowFactory(new Callback<TableView<KickoutDto>, TableRow<KickoutDto>>() {

			@Override
			public TableRow<KickoutDto> call(TableView<KickoutDto> tableRow) {
				final TableRow<KickoutDto> row = new TableRow<KickoutDto>();		

				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						view.getKickoutTable().getTable().requestFocus();
						if(e.getButton() == MouseButton.PRIMARY) {
							final int index = row.getIndex();
							if(index >= 0
									&& index < view.getKickoutTable().getTable().getItems().size()
									&& view.getKickoutTable().getTable().getSelectionModel().isSelected(index)) {
								view.getKickoutTable().getTable().getSelectionModel().clearSelection(index);

							} else {
								view.getKickoutTable().getTable().getSelectionModel().select(index);
							}
							e.consume();
						}
					}
				});

				row.contextMenuProperty().bind(
						Bindings.when(row.emptyProperty())
						.then((ContextMenu)null)
						.otherwise(view.getKickoutTableMenu())
						);
				return row;
			}
		});

		view.getRemoveKickoutTable().getTable().setRowFactory(new Callback<TableView<KickoutDto>, TableRow<KickoutDto>>() {

			@Override
			public TableRow<KickoutDto> call(TableView<KickoutDto> tableRow) {
				final TableRow<KickoutDto> row = new TableRow<KickoutDto>();		

				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						view.getRemoveKickoutTable().getTable().requestFocus();
						if(e.getButton() == MouseButton.PRIMARY) {
							final int index = row.getIndex();
							if(index >= 0
									&& index < view.getRemoveKickoutTable().getTable().getItems().size()
									&& view.getRemoveKickoutTable().getTable().getSelectionModel().isSelected(index)) {
								view.getRemoveKickoutTable().getTable().getSelectionModel().clearSelection(index);

							} else {
								view.getRemoveKickoutTable().getTable().getSelectionModel().select(index);
							}
							e.consume();
						}
					}
				});

				row.contextMenuProperty().bind(
						Bindings.when(row.emptyProperty())
						.then((ContextMenu)null)
						.otherwise(view.getRemoveKickoutTableMenu())
						);
				return row;
			}
		});
	}

	private void initListeners() {
		view.getKickoutAllMenuItem().setOnAction(this);
		view.getKickoutSelectedMenuItem().setOnAction(this);
		view.getDeselectKickoutMenuItem().setOnAction(this);
		view.getRemoveKickoutAllMenuItem().setOnAction(this);
		view.getRemoveKickoutSelectedMenuItem().setOnAction(this);

		view.getReleaseKickoutAllMenuItem().setOnAction(this);
		view.getReleaseKickoutSelectedMenuItem().setOnAction(this);
		view.getDeselectReleaseMenuItem().setOnAction(this);
		view.getReleaseKickoutRemoveAllMenuItem().setOnAction(this);
		view.getReleaseKickoutRemoveSelectedMenuItem().setOnAction(this);
	}

	private void setViewState() {
		updateKickoutTabPaneState();
		updateKickoutContextMenuState();
		updateReleaseKickoutContextMenuState();
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() instanceof MenuItem) {
			LoggedMenuItem menuItem = (LoggedMenuItem) event.getSource();
			if(ClientConstants.KICKOUT_ALL_ID.equalsIgnoreCase(menuItem.getId())) createKickoutAllAction();
			else if(ClientConstants.KICKOUT_SELECTED_ID.equalsIgnoreCase(menuItem.getId())) createKickoutSelectedAction();
			else if(ClientConstants.DESELECT_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createDeselectKickoutAction();
			else if(ClientConstants.REMOVE_ALL_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createRemoveAllKickoutAction();
			else if(ClientConstants.REMOVE_SELECTED_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createRemoveSelectedKickoutAction();
			else if(ClientConstants.RELEASE_ALL_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createReleaseAllKickoutAction();
			else if(ClientConstants.RELEASE_SELECTED_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createReleaseSelectedKickoutAction();
			else if(ClientConstants.DESELECT_RELEASE_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createDeselectReleaseKickoutAction();
			else if(ClientConstants.REMOVE_ALL_RELEASE_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createRemoveAllReleaseKickoutAction();
			else if(ClientConstants.REMOVE_SELECTED_RELEASE_KICKOUT_ID.equalsIgnoreCase(menuItem.getId())) createRemoveSelectedReleaseKickoutAction();
		}
	}

	private void createKickoutAllAction() {
		List<KickoutDto> kickoutDtoList = view.getKickoutTable().getTable().getItems().stream().collect(Collectors.toList());
		try {
			model.removeProducts(kickoutDtoList);
			kickoutProducts(kickoutDtoList);
			setInfoStatus("Successfully applied kickout");
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			getLogger().error(e, "Could not perform kickout.");
		} finally {
			updateKickoutContextMenuState();
			updateKickoutTabPaneState();
			updateKickoutTable();
			notifyInputPane();
		}
	}

	private void createKickoutSelectedAction() {
		List<KickoutDto> kickoutDtoList = view.getKickoutTable().getSelectedItems().stream().collect(Collectors.toList());
		try {
			model.removeProducts(kickoutDtoList);
			kickoutProducts(kickoutDtoList);
			setInfoStatus("Successfully applied kickout");
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			getLogger().error(e, "Could not perform kickout.");
		} finally {
			updateKickoutContextMenuState();
			updateKickoutTabPaneState();
			updateKickoutTable();
			notifyInputPane();
		}
	}

	private void createReleaseAllKickoutAction() {
		List<KickoutDto> kickoutDtoList = view.getRemoveKickoutTable().getTable().getItems().stream().collect(Collectors.toList());
		try {
			model.removeProducts(kickoutDtoList);
			releaseProducts(kickoutDtoList);
			setInfoStatus(SUCCESSFULLY_RELEASED_KICKOUT);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			getLogger().error(e, COULD_NOT_RELEASE_KICKOUT);
		} finally {
			updateReleaseKickoutContextMenuState();
			updateKickoutTabPaneState();
			updateReleaseTable();
			notifyInputPane();
		}
	}

	private void createReleaseSelectedKickoutAction() {
		List<KickoutDto> kickoutDtoList = view.getRemoveKickoutTable().getSelectedItems().stream().collect(Collectors.toList());
		try {
			model.removeProducts(kickoutDtoList);
			releaseProducts(kickoutDtoList);
			setInfoStatus(SUCCESSFULLY_RELEASED_KICKOUT);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			getLogger().error(e, COULD_NOT_RELEASE_KICKOUT);
		} finally {
			updateReleaseKickoutContextMenuState();
			updateKickoutTabPaneState();
			updateReleaseTable();
			notifyInputPane();
		}
	}

	private void createDeselectKickoutAction() {
		view.getKickoutTable().getTable().getSelectionModel().clearSelection();
		updateKickoutTabPaneState();
		clearErrorMessage();
		notifyInputPane();
	}

	private void createRemoveAllKickoutAction() {
		model.removeProducts(view.getKickoutTable().getTable().getItems());
		updateKickoutTable();
		clearErrorMessage();
		notifyInputPane();
	}

	private void createRemoveSelectedKickoutAction() {
		model.removeProducts(view.getKickoutTable().getSelectedItems());
		view.getKickoutTable().removeSelected();
		updateKickoutTabPaneState();
		clearErrorMessage();
		notifyInputPane();
	}

	public void createDeselectReleaseKickoutAction() {
		view.getRemoveKickoutTable().clearSelection();
		updateKickoutTabPaneState();
		clearErrorMessage();
		notifyInputPane();
	}

	public void createRemoveAllReleaseKickoutAction() {
		List<KickoutDto> productsToRemove = view.getRemoveKickoutTable().getTable().getItems().stream().collect(Collectors.toList());
		model.removeProducts(productsToRemove);
		view.getRemoveKickoutTable().getTable().getItems().clear();
		updateKickoutTabPaneState();
		clearErrorMessage();
		notifyInputPane();
	}

	public void createRemoveSelectedReleaseKickoutAction() {
		model.removeProducts(view.getRemoveKickoutTable().getSelectedItems());
		view.getRemoveKickoutTable().removeSelected();
		updateKickoutTabPaneState();
		clearErrorMessage();
		notifyInputPane();
	}

	@SuppressWarnings("unchecked")
	private boolean validateProductsForKickout(List<BaseProduct> productList) {
		DataContainer retData = new DefaultDataContainer();
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.APPLICATION_ID, model.getApplicationId());
		data.put(DataContainerTag.PRODUCT, productList);

		try {
			retData = model.validateForKickout(data);
		} catch (Exception e) {
			setErrorMessage("Failed to validate products. " + e.getMessage());
			getLogger().error(e.getStackTrace().toString(), " Exception occured during product validation.");
			return false;
		}
		
		if(retData.get(DataContainerTag.REQUEST_RESULT).equals(LineSideContainerValue.NOT_COMPLETE)) {
			List<String> errorList = retData.getErrorMessages();
			if(errorList != null && !errorList.isEmpty()) {
				if(errorList.size() == 1) {
					setErrorMessage(errorList.get(0));
				} else {
					setErrorDialog(errorList.toString(), "Errors during product validation");
				}
			}
			return false;
		}
		List<KickoutDto> kickoutDtoList = (List<KickoutDto>) retData.get(DataContainerTag.KICKOUT_PRODUCTS);
		List<KickoutDto> duplicateProducts = model.addProductsToSet(kickoutDtoList);
		if(!duplicateProducts.isEmpty()) {
			List<String> errorList = new ArrayList<String>(); 
			for(KickoutDto duplicateProduct : duplicateProducts) {
				errorList.add("Product : " + duplicateProduct.getProductId() + "  has already been added to table.\n");
			}
			if(errorList.size() == 1)  {
				setErrorMessage(PRODUCT_ALREADY_ADDED_TO_TABLE);
			} else {
				setErrorDialog(errorList.toString(), PRODUCTS_ALREADY_ADDED_TO_TABLE);
			}
			return false;
		}
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean validateProductsForKickoutWithId(List<BaseProduct> productList,boolean fixedDefectFlag, String transactionId) {
		DataContainer retData = new DefaultDataContainer();
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.PRODUCT, productList);
		data.put(DataContainerTag.APPLICATION_ID, model.getApplicationId());
		data.put(DataContainerTag.FIXED_DEFECT_FLAG, fixedDefectFlag);
		data.put(DataContainerTag.TRANSACTION_ID, transactionId);

		try {
			retData = model.validateForKickout(data);
		} catch (Exception e) {
			setErrorMessage("Failed to validate products. " + e.getMessage());
			getLogger().error(e.getStackTrace().toString(), " Exception occured during product validation.");
			return false;
		}
		
		if(retData.get(DataContainerTag.REQUEST_RESULT).equals(LineSideContainerValue.NOT_COMPLETE)) {
			List<String> errorList = retData.getErrorMessages();
			if(errorList != null && !errorList.isEmpty()) {
				if(errorList.size() == 1) {
					setErrorMessage(errorList.get(0));
				} else {
					setErrorDialog(errorList.toString(), "Errors during product validation");
				}
			}
			return false;
		}
		List<KickoutDto> kickoutDtoList = (List<KickoutDto>) retData.get(DataContainerTag.KICKOUT_PRODUCTS);
		List<KickoutDto> duplicateProducts = model.addProductsToSet(kickoutDtoList);
		if(!duplicateProducts.isEmpty()) {
			List<String> errorList = new ArrayList<String>(); 
			for(KickoutDto duplicateProduct : duplicateProducts) {
				errorList.add("Product : " + duplicateProduct.getProductId() + "  has already been added to table.\n");
			}
			if(errorList.size() == 1)  {
				setErrorMessage(PRODUCT_ALREADY_ADDED_TO_TABLE);
			} else {
				setErrorDialog(errorList.toString(), PRODUCTS_ALREADY_ADDED_TO_TABLE);
			}
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private boolean validateProductsForRelease(List<BaseProduct> productList) {
		DataContainer retData = new DefaultDataContainer();
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.APPLICATION_ID, model.getApplicationId());
		data.put(DataContainerTag.PRODUCT, productList);

		try {
			retData = model.validateForRelease(data);
		} catch (Exception e) {
			setErrorMessage("Failed to validate products. " + e.getMessage());
			getLogger().error(e.getStackTrace().toString(), " Exception occured during product validation.");
			return false;
		}

		if(retData.get(DataContainerTag.REQUEST_RESULT).equals(LineSideContainerValue.NOT_COMPLETE)) {
			List<String> errorList = retData.getErrorMessages();
			if(errorList != null && !errorList.isEmpty()) {
				setErrorDialog(errorList.toString(), "Validation Errors");
				return false;
			}
		}

		List<KickoutDto> kickoutDtoList = (List<KickoutDto>) retData.get(DataContainerTag.KICKOUT_PRODUCTS);

		List<KickoutDto> duplicateProducts = model.addProductsToSet(kickoutDtoList);
		if(!duplicateProducts.isEmpty()) {
			List<String> errorList = new ArrayList<String>(); 

			for(KickoutDto duplicateProduct : duplicateProducts) {
				errorList.add("Product : " + duplicateProduct.getProductId() + "  has already been added to table.\n");
			}

			if(errorList.size() == 1)  {
				setErrorMessage(PRODUCT_ALREADY_ADDED_TO_TABLE);
			} else {
				setErrorDialog(errorList.toString(), PRODUCTS_ALREADY_ADDED_TO_TABLE);
			}
			return false;
		}
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean validateProductsForReleaseWithId(List<BaseProduct> productList,boolean fixedDefectFlag, String transactionId) {
		DataContainer retData = new DefaultDataContainer();
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.PRODUCT, productList);
		data.put(DataContainerTag.APPLICATION_ID, model.getApplicationId());
		data.put(DataContainerTag.FIXED_DEFECT_FLAG, fixedDefectFlag);
		data.put(DataContainerTag.TRANSACTION_ID, transactionId);

		try {
			retData = model.validateForRelease(data);
		} catch (Exception e) {
			setErrorMessage("Failed to validate products. " + e.getMessage());
			getLogger().error(e.getStackTrace().toString(), " Exception occured during product validation.");
			return false;
		}

		if(retData.get(DataContainerTag.REQUEST_RESULT).equals(LineSideContainerValue.NOT_COMPLETE)) {
			List<String> errorList = retData.getErrorMessages();
			if(errorList != null && !errorList.isEmpty()) {
				setErrorDialog(errorList.toString(), "Validation Errors");
				return false;
			}
		}

		List<KickoutDto> kickoutDtoList = (List<KickoutDto>) retData.get(DataContainerTag.KICKOUT_PRODUCTS);

		List<KickoutDto> duplicateProducts = model.addProductsToSet(kickoutDtoList);
		if(!duplicateProducts.isEmpty()) {
			List<String> errorList = new ArrayList<String>(); 

			for(KickoutDto duplicateProduct : duplicateProducts) {
				errorList.add("Product : " + duplicateProduct.getProductId() + "  has already been added to table.\n");
			}

			if(errorList.size() == 1)  {
				setErrorMessage(PRODUCT_ALREADY_ADDED_TO_TABLE);
			} else {
				setErrorDialog(errorList.toString(), PRODUCTS_ALREADY_ADDED_TO_TABLE);
			}
			return false;
		}
		return true;
	}

	private List<BaseProduct> findProducts(List<String> productIdList) {
		List<BaseProduct> productList = new ArrayList<BaseProduct>();
		int batchSize = 0;
		int currentLocation = 0;
		int numProductToCreate = productIdList.size();

		while(numProductToCreate > 0) {
			batchSize = model.getBatchSize(numProductToCreate);
			List<String> productIdListForQuery = getProductIdListForQuery(productIdList, batchSize, currentLocation);
			productList.addAll(model.findProducts(productIdListForQuery, 0, batchSize));

			numProductToCreate -= batchSize;
			currentLocation += batchSize;
		}

		List<String> nonExistProducts = determineNonExistProducts(productList, productIdList);

		if(!nonExistProducts.isEmpty()) {
			String message = "The following product(s) do not exist: " + nonExistProducts.toString();
			setErrorDialog(message, "Non Exist Products");
		}
		return productList;
	}

	private List<String> determineNonExistProducts(List<BaseProduct> productList, List<String> productIdList) {
		int productIdCount = productIdList.size();
		List<String> nonExistProductIsList = new ArrayList<String>();

		if(productList.size() != productIdCount) {
			Map<String, BaseProduct> map = productList.stream().collect(Collectors.toMap(BaseProduct::getProductId, c -> c));
			for(String productId : productIdList) {
				if(!map.containsKey(productId)) {
					nonExistProductIsList.add(productId);
				}
			}
		}
		return nonExistProductIsList;
	}

	private List<String> getProductIdListForQuery(List<String> productIdList, int batchSize, int currentLocation) {
		List<String> returnProductIdList = new ArrayList<String>();
		for(int y = currentLocation, x = batchSize; x > 0; x--, y++) {
			if(productIdList.get(y) != null) {
				returnProductIdList.add(productIdList.get(y));
			}
		}
		return returnProductIdList;
	}

	private void kickoutProducts(List<KickoutDto> kickoutDtoList) {
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		data.put(DataContainerTag.KICKOUT_COMMENT, view.getReasonEntryPane().getReasonTextFieldControl().getText());
		data.put(DataContainerTag.DESCRIPTION, "Assocate TL Kickout");
		data.put(DataContainerTag.APPLICATION_ID, model.getApplicationId());
		data.put(DataContainerTag.ASSOCIATE_NO, model.getApplicationContext().getUserId());
		data.put(DataContainerTag.DIVISION_ID, getSelectedDivision().getDivisionId());
		data.put(DataContainerTag.LINE_ID, getSelectedLine().getLineId());
		data.put(DataContainerTag.PROCESS_POINT_ID, getSelectedProcessPoint().getProcessPointId());

		try {
			model.kickoutProducts(data);
		} catch (Exception e) {
			setErrorMessage("Failed to kickout products. " + e.getMessage());
			getLogger().error(e.getStackTrace().toString(), " Exception occured during kickout.");
		}
	}

	private void releaseProducts(List<KickoutDto> kickoutDtoList) {
		DefaultDataContainer data = new DefaultDataContainer();
		data.put(DataContainerTag.KICKOUT_PRODUCTS, kickoutDtoList);
		data.put(DataContainerTag.APPLICATION_ID, model.getApplicationId());
		data.put(DataContainerTag.RELEASE_COMMENT, view.getReleaseReasonEntryPane().getReasonTextField().getText());
		data.put(DataContainerTag.ASSOCIATE_NO,  model.getApplicationContext().getUserId());
		model.releaseKickouts(data);
	}

	@Subscribe()
	public void onTextFieldEvent(TextFieldEvent event) {
		if(ClientConstants.KICKOUT_REASON_PANE_ID.equalsIgnoreCase(event.getViewId())) {
			if(StringUtil.isNullOrEmpty(event.getText())) {
				setKickoutReasonEntered(false);
				updateKickoutContextMenuState();
			} else {
				setKickoutReasonEntered(true);
				updateKickoutContextMenuState();
			}
		} else if(ClientConstants.RELEASE_KICKOUT_REASON_PANE_ID.equalsIgnoreCase(event.getViewId())) {
			if(StringUtil.isNullOrEmpty(event.getText())) {
				setReleaseReasonEntered(false);
				updateReleaseKickoutContextMenuState();
			} else {
				setReleaseReasonEntered(true);
				updateReleaseKickoutContextMenuState();
			}
		}
	}

	@Subscribe() 
	public void onSelectionEvent(SelectionEvent event) {
		if(event.getTargetObject() == null) return;
		if(event.getSource().equals(PaneId.PROCESS_POINT_SELECT_PANE)) {
			if(event.getEventType().equals(SelectionEventType.PROCESS_POINT)) {
				setProcessPointSelected(true);
				updateKickoutContextMenuState();
			} else if(event.getEventType().equals(SelectionEventType.LINE) ||
					event.getEventType().equals(SelectionEventType.DIVISION)) {
				setProcessPointSelected(false);
				updateKickoutContextMenuState();
			}
		}
	}

	private void updateKickoutContextMenuState() {
		if(model.isKickoutReasonRequired()) {
			if(!(view.getReasonEntryPane().getReasonTextField().getText().isEmpty()) && isProcessPointSelected()) {
				updateKickoutContextMenuState(false);
			} else updateKickoutContextMenuState(true);
		} else { 
			if(isProcessPointSelected()) {
				updateKickoutContextMenuState(false);
			} else {
				updateKickoutContextMenuState(true);
			}
		}
	}

	private void updateReleaseKickoutContextMenuState() {
		if(isReleaseReasonEntered()) {
			updateReleaseKickoutContextMenuState(false);
		} else {
			updateReleaseKickoutContextMenuState(true);
		}
	}

	private void updateKickoutContextMenuState(boolean state) {
		view.getKickoutAllMenuItem().setDisable(state);
		view.getKickoutSelectedMenuItem().setDisable(state);
	}

	private void updateReleaseKickoutContextMenuState(boolean state) {
		view.getReleaseKickoutAllMenuItem().setDisable(state);
		view.getReleaseKickoutSelectedMenuItem().setDisable(state);
	}

	@SuppressWarnings("unchecked")
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if(event == null) {
			return;
		}
		if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_RECIEVED)) {

			if(getSelectedKickoutTab().getId().equals(ClientConstants.KICKOUT_TAB_ID)) {
				if(event.getTargetObject() instanceof List) {
					List<String> products = (List<String>) event.getTargetObject();
					getLogger().info("Product Id list received for kickout: " + products.toString());

					List<BaseProduct> productList = findProducts(products);
					boolean isValid = validateProductsForKickout(productList);
					updateKickoutTable();
					if(isValid) {
						setInfoStatus(PRODUCT_SEARCH_COMPLETE);
					}

				} else if(event.getTargetObject() instanceof String) {
					String productId = (String) event.getTargetObject();
					getLogger().info("Product Id received : " + productId);

					BaseProduct product = model.findProduct(productId);
					if(product == null) {
						String message = "Product : " + productId + " does not exist.";
						getLogger().warn(message);
						setErrorMessage(message);
						return;
					}
					List<BaseProduct> productList = new ArrayList<BaseProduct>();
					productList.add(product);
					boolean isValid = validateProductsForKickout(productList);
					updateKickoutTable();
					if(isValid) {
						setInfoStatus(PRODUCT_SEARCH_COMPLETE);
					}
				}
			} else if (getSelectedKickoutTab().getId().equals(ClientConstants.REMOVE_KICKOUT_TAB)) {
				if(event.getTargetObject() instanceof List) {
					List<String> products = (List<String>) event.getTargetObject();
					List<BaseProduct> productList = findProducts(products);
					getLogger().info("Product Id list received for kickout release: " + products.toString());

					boolean isValid = validateProductsForRelease(productList);
					updateReleaseTable();
					if(isValid) {
						setInfoStatus(PRODUCT_SEARCH_COMPLETE);
					} 
				} else if(event.getTargetObject() instanceof String) {
					String productId = (String) event.getTargetObject();
					getLogger().info("Product Id received : " + productId);
					BaseProduct product = model.findProduct(productId);
					if(product == null) {
						String message = "Product : " + productId + " does not exist.";
						getLogger().warn(message);
						setErrorMessage(message);
						return;
					}
					List<BaseProduct> productList = new ArrayList<BaseProduct>();
					productList.add(product);
					boolean isValid = validateProductsForRelease(productList);
					updateReleaseTable();
					if(isValid) {
						setInfoStatus(PRODUCT_SEARCH_COMPLETE);
					} 
				}
			}
		}else if(event.getEventType().equals(ProductEventType.TRANSACTION_ID_INPUT_RECIEVED)) {
			if(getSelectedKickoutTab().getId().equals(ClientConstants.KICKOUT_TAB_ID)) {
				List<String> contents = null;
				HashMap<String, Boolean> data = (HashMap<String, Boolean>) event.getTargetObject();
				Boolean fixedDefectFlag = false;
				String transactionId = null;
				for (String key : data.keySet())
				{			            
					fixedDefectFlag = data.get(key);
					transactionId = key;
					getLogger().info("Transaction Id received : " + transactionId);
				}
				if(fixedDefectFlag) {
					contents = model.findProductsByTransactionIdProdType((Long.parseLong(transactionId)),model.getProductType(),DefectStatus.FIXED.getId());
				}else {
					contents = model.findProductsByTransactionId(Long.parseLong(transactionId),model.getProductType());
				}
				List<BaseProduct> productList = findProducts(contents);
				
				boolean isValid = validateProductsForKickoutWithId(productList,fixedDefectFlag, transactionId);
				updateKickoutTable();
				if(isValid) {
					setInfoStatus(PRODUCT_SEARCH_COMPLETE);
				}
			} else if (getSelectedKickoutTab().getId().equals(ClientConstants.REMOVE_KICKOUT_TAB)) {

				List<String> contents = null;
				HashMap<String, Boolean> data = (HashMap<String, Boolean>) event.getTargetObject();
				Boolean fixedDefectFlag = false;
				String transactionId = null;
				for (String key : data.keySet())
				{			            
					fixedDefectFlag = data.get(key);
					transactionId = key;
					getLogger().info("Transaction Id received : " + transactionId);
				}
				contents = model.findProductsByTransactionId(Long.parseLong(transactionId),model.getProductType());
				List<BaseProduct> productList = findProducts(contents);
				
				boolean isValid = validateProductsForReleaseWithId(productList,fixedDefectFlag, transactionId);
				updateReleaseTable();
				if(isValid) {
					setInfoStatus(PRODUCT_SEARCH_COMPLETE);
				} 
			}
		}
	}

	private void updateKickoutTable() {
		view.getKickoutTable().getTable().getItems().clear();
		view.getKickoutTable().getTable().setItems(FXCollections.observableArrayList(model.getProductsAsList()));
		updateKickoutTabPaneState();
	}

	private void updateReleaseTable() {
		view.getRemoveKickoutTable().getTable().getItems().clear();
		view.getRemoveKickoutTable().getTable().setItems(FXCollections.observableArrayList(model.getProductsAsList()));
		updateKickoutTabPaneState();
	}

	private void updateKickoutTabPaneState() {
		if(view.getKickoutTab().isSelected()) {
			if(model.getProductSet().isEmpty()) {
				view.getKickoutTab().setDisable(false);
				view.getRemoveKickoutTab().setDisable(false);
			} else {
				view.getKickoutTab().setDisable(false);
				view.getRemoveKickoutTab().setDisable(true);
			}
		} else if(view.getRemoveKickoutTab().isSelected()) {
			if(model.getProductSet().isEmpty()) {
				view.getKickoutTab().setDisable(false);
				view.getRemoveKickoutTab().setDisable(false);
			} else {
				view.getKickoutTab().setDisable(true);
				view.getRemoveKickoutTab().setDisable(false);
			}
		}
	}

	private Division getSelectedDivision() {
		return view.getProcessPointSelectionPane().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem();
	}

	private Line getSelectedLine() {
		return view.getProcessPointSelectionPane().getLineComboBox().getControl().getSelectionModel().getSelectedItem();
	}

	private ProcessPoint getSelectedProcessPoint() {
		return view.getProcessPointSelectionPane().getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem();
	}

	private Tab getSelectedKickoutTab() {
		return view.getAddRemoveKickoutTabPane().getSelectionModel().getSelectedItem();
	}

	private Logger getLogger() {
		return view.getLogger();
	}

	public void notifyInputPane() {
		getLogger().debug("Sent : ProcessCompleteEvent");
		EventBusUtil.publish(new ProcessCompleteEvent(PaneId.getPaneId(view.getProductInputPane().getSelectedTabId())));
	}

	private void setInfoStatus(String status) {
		getLogger().debug("Sent : ProductValidEvent");
		EventBusUtil.publish(new ProductValidEvent(PaneId.getPaneId(view.getProductInputPane().getSelectedTabId())));
		clearErrorMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.INFO");
		EventBusUtil.publish(new StatusMessageEvent(status, StatusMessageEventType.INFO));
		updateKickoutTabPaneState();
	}

	public void clearErrorMessage() {
		getLogger().debug("cleared error message");
		view.getMainWindow().clearStatusMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.CLEAR");
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
	}

	private void setErrorMessage(String message) {
		getLogger().debug("Sent : ProductInvalidEvent");
		EventBusUtil.publish(new ProductInvalidEvent(PaneId.getPaneId(view.getProductInputPane().getSelectedTabId()), message));
		clearErrorMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.ERROR");
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
	}

	private void setErrorDialog(String message, String title) {
		clearErrorMessage();
		getLogger().debug("Sent : ProductInvalidEvent");
		EventBusUtil.publish(new ProductInvalidEvent(PaneId.getPaneId(view.getProductInputPane().getSelectedTabId()), message));
		MessageDialog.showScrollingInfo(view.getMainWindow().getStage(), message, title, 30, 80);
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.ERROR");
		EventBusUtil.publish(new StatusMessageEvent(title, StatusMessageEventType.ERROR));
	}

	public boolean isProcessPointSelected() {
		return this.isProcessPointSelected;
	}

	public void setProcessPointSelected(boolean isProcessPointSelected) {
		this.isProcessPointSelected = isProcessPointSelected;
	}

	public boolean isKickoutReasonEntered() {
		return this.isKickoutReasonEntered;
	}

	public void setKickoutReasonEntered(boolean isKickoutReasonEntered) {
		this.isKickoutReasonEntered = isKickoutReasonEntered;
	}

	public boolean isReleaseReasonEntered() {
		return this.isReleaseReasonEntered;
	}

	public void setReleaseReasonEntered(boolean isReleaseReasonEntered) {
		this.isReleaseReasonEntered = isReleaseReasonEntered;
	}

	public boolean isDcProduct() {
		return model.isDcProduct();
	}
}
