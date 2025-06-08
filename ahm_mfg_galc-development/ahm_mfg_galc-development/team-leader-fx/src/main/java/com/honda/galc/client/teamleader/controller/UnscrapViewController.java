package com.honda.galc.client.teamleader.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.CharMatcher;
import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.teamleader.model.UnscrapViewModel;
import com.honda.galc.client.teamleader.view.UnscrapView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.event.ProcessCompleteEvent;
import com.honda.galc.client.ui.event.ProductInvalidEvent;
import com.honda.galc.client.ui.event.ProductValidEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ScrappedProductDto;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.StringUtil;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class UnscrapViewController {
	private UnscrapViewModel model;
	private UnscrapView view;

	public UnscrapViewController(UnscrapView unscrapPanel) {
		initController(unscrapPanel);
		model.setApplicationContext(view.getMainWindow().getApplicationContext());
		model.setApplicationId(view.getMainWindow().getApplicationContext().getApplicationId());
	}

	private void initController(UnscrapView unscrapPanel) {
		view = unscrapPanel;
		model = new UnscrapViewModel();
		EventBusUtil.register(this);
	}

	@SuppressWarnings("unchecked")
	@Subscribe() 
	public void onSelectionEvent(ProductEvent event) {

		if(event.getTargetObject() == null) return;
		if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_RECIEVED)) {
			if(event.getTargetObject() instanceof String) {
				List<String> productIdList = new ArrayList<String>();
				productIdList.add((String) event.getTargetObject());
				validateProductForUnscrap(productIdList);
			} else if (event.getTargetObject() instanceof List) {
				validateProductForUnscrap((List<String>) event.getTargetObject());
			}
		}
	}

	public void initListeners() {
		addScrappedProductsTabelEvents();
	}

	private void addScrappedProductsTabelEvents() {
		view.getScrappedProductsTable().getTable().setRowFactory(new Callback<TableView<ScrappedProductDto>, TableRow<ScrappedProductDto>>() {

			@Override
			public TableRow<ScrappedProductDto> call(TableView<ScrappedProductDto> tableRow) {
				final TableRow<ScrappedProductDto> row = new TableRow<ScrappedProductDto>();		

				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						view.getScrappedProductsTable().getTable().requestFocus();
						if(e.getButton() == MouseButton.PRIMARY) {
							final int index = row.getIndex();
							if(index >= 0
									&& index < view.getScrappedProductsTable().getTable().getItems().size()
									&& view.getScrappedProductsTable().getTable().getSelectionModel().isSelected(index)) {
								view.getScrappedProductsTable().getTable().getSelectionModel().clearSelection(index);

							} else {
								view.getScrappedProductsTable().getTable().getSelectionModel().select(index);
							}
							e.consume();
						}
					}
				});

				createRemoveAllMenuItemAction();
				createRemoveAllSelectedMenuItemAction();
				createUnscrapAllMenuItemAction();
				createUnscrapSelectedMenuItemAction();
				createDeselectMenuItemAction();

				row.contextMenuProperty().bind(
						Bindings.when(row.emptyProperty())
						.then((ContextMenu)null)
						.otherwise(view.getScrappedProductsTableMenu())
						);
				return row;
			}

			private void createDeselectMenuItemAction() {
				view.getDeselectMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						view.getScrappedProductsTable().getTable().getSelectionModel().clearSelection();
					}
				});
			}

			private void createUnscrapSelectedMenuItemAction() {
				view.getUnscrapSelectedMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						ObservableList<ScrappedProductDto> products = view.getScrappedProductsTable().getSelectedItems();

						unscrapProducts(products);
					}
				});
			}

			private void createUnscrapAllMenuItemAction() {
				view.getUnscrapAllMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						unscrapProducts(view.getScrappedProductsTable().getTable().getItems());
					}
				});
			}

			private void createRemoveAllSelectedMenuItemAction() {
				view.getRemoveSelectedMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						ObservableList<ScrappedProductDto> products = view.getScrappedProductsTable().getSelectedItems();
						clearScrappedProductsTable(products);
						clearErrorMessage();
						notifyInputPane();
					}
				});
			}

			private void createRemoveAllMenuItemAction() {
				view.getRemoveAllMenuItem().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void  handle(ActionEvent e) {
						view.getScrappedProductsTable().getTable().getItems().clear();
						view.getScrappedProductsTable().getTable().getSelectionModel().clearSelection();
						clearErrorMessage();
						notifyInputPane();
					}
				});
			}
		});
	}

	private void unscrapProducts(ObservableList<ScrappedProductDto> products) {
		if(products.size() > model.getMaxUnscrapCount()) {
			String message = "Cannot unscrap " + products.size() + " product, " + "the max allowed is " + model.getMaxUnscrapCount();
			getLogger().warn(message);
			setErrorMessage(message);
			return;
		}

		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.REASON.name(), view.getUnscrapReasonTextField().getText());
		requestDc.put(TagNames.APPLICATION_ID.name(), view.getApplicationId());
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), view.getMainWindow().getApplicationContext().getProcessPointId());
		requestDc.put(TagNames.PRODUCT_TYPE.name(), getProductTypeData().getProductTypeName());
		requestDc.put(TagNames.ASSOCIATE_ID.name(), view.getAssociateIdTextField().getText());

		boolean isSuccessfulUnscrap = model.unscrapProducts(requestDc, products);
		if(!isSuccessfulUnscrap) {
			String message = model.getUnscrapResultMessage();
			setErrorMessage(message);
		} else {
			setInfoStatus("Products sucessfully unscrapped");
			refreshView(products);
		}
	}

	private void clearScrappedProductsTable(ObservableList<ScrappedProductDto> products) {
		view.getScrappedProductsTable().getTable().getItems().removeAll(products);
		view.getScrappedProductsTable().getTable().getSelectionModel().clearSelection();		
	}

	public ColumnMappingList createScrappedProductTableColumns() {
		ColumnMappingList columnMappingList;
		if(isDieCastProduct()) {
			columnMappingList = view.getDiecastProductTableColumns();
		} else {
			columnMappingList = view.getNonDiecastProductTableColumns();
		}
		return columnMappingList;
	}

	private boolean isDieCastProduct() {
		return ProductTypeUtil.isDieCast(view.getMainWindow().getProductType());
	}

	@SuppressWarnings("unchecked")
	public boolean validateProductForUnscrap(List<String> productIdList) {
		boolean isValid = true;
		List<String> messageList = new ArrayList<String>();
		String message = null;
		view.getMainWindow().clearMessage();
		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.PRODUCT_ID.name(), productIdList);
		requestDc.put(TagNames.APPLICATION_ID.name(), view.getApplicationId());
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), view.getMainWindow().getApplicationContext().getProcessPointId());
		requestDc.put(TagNames.PRODUCT_TYPE.name(), view.getMainWindow().getApplicationContext().getProductTypeData().getProductTypeName());

		DataContainer resultDc = model.validateProductForUnscrap(requestDc);

		getLogger().info(resultDc.toString());
		if(resultDc.getString(TagNames.REQUEST_RESULT.name()).equals(LineSideContainerValue.NOT_COMPLETE)) {
			message = resultDc.getString(TagNames.MESSAGE.name());
			if(!StringUtil.isNullOrEmpty(message)) {
				messageList.add(message);
			}
		}
		List<ScrappedProductDto> scrappedProductList = (List<ScrappedProductDto>) resultDc.get(TagNames.PRODUCT_INFO_LIST.name());

		List<ScrappedProductDto> duplicates = scrappedProductList.stream()
				.filter(a -> isDuplicateScan(a.getProduct().getProductId()))
				.collect(Collectors.toList());
		duplicates.forEach(duplicate -> messageList.add("Product : " + duplicate.getProduct().getProductId() + " has already been scanned\n"));
		scrappedProductList.removeAll(duplicates);
		
		if(!messageList.isEmpty()) {
			if(messageList.size() > 1) {
				setErrorDialog(messageList.toString(), "Error during product validation");
			} else {
				setErrorMessage(StringUtils.chomp(messageList.get(0).replaceAll("[\\[\\]\n]", "")));
			}
			isValid =  false;
		} else {
			setInfoStatus("Product Search Complete");
		}
		addProductToScrappedTable();
		
		return isValid;
	}

	private boolean isDuplicateScan(String productId) {
		List<ScrappedProductDto> scannedProductList = view.getScrappedProductsTable().getTable().getItems();
		for(ScrappedProductDto currentScannedProduct : scannedProductList) {
			if(StringUtils.trim(productId).equals(StringUtils.trim(currentScannedProduct.getProduct().getProductId()))) {
				return true;
			}
		}
		return false;
	}

	private void addProductToScrappedTable() {
		ObservableList<ScrappedProductDto> productInfo = view.getScrappedProductsTable().getTable().getItems();
		for(ScrappedProductDto scrappedProductDto : model.getScrappedProductDto()) {
			scrappedProductDto.setProcessPointName(model.getProcessPointName(scrappedProductDto.getProduct().getLastPassingProcessPointId()));
			productInfo.add(scrappedProductDto);
		}
		view.getScrappedProductsTable().getTable().setItems(productInfo);
	}
	
	public void notifyInputPane() {
		getLogger().debug("Sent : ProcessCompleteEvent");
		EventBusUtil.publish(new ProcessCompleteEvent(PaneId.getPaneId(view.getProductScanPane().getSelectedTabId())));
	}
	
	private void setInfoStatus(String status) {
		getLogger().debug("Sent : ProductValidEvent");
		EventBusUtil.publish(new ProductValidEvent(PaneId.getPaneId(view.getProductScanPane().getSelectedTabId())));
		clearErrorMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.INFO");
		EventBusUtil.publish(new StatusMessageEvent(status, StatusMessageEventType.INFO));
	}
	
	private void clearErrorMessage() {
		getLogger().debug("cleared error message");
		view.getMainWindow().clearStatusMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.CLEAR");
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
	}

	private void refreshView(ObservableList<ScrappedProductDto> products) {
		EventBusUtil.publish(new ProcessCompleteEvent(PaneId.getPaneId(view.getProductScanPane().getSelectedTabId())));
		clearScrappedProductsTable(products);
		view.getUnscrapReasonTextField().clear();
		getLogger().debug("cleared error message");
		view.getMainWindow().clearStatusMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.CLEAR");
		EventBusUtil.publish(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
	}
	
	private void setErrorMessage(String message) {
		getLogger().debug("Sent : ProductInvalidEvent");
		EventBusUtil.publish(new ProductInvalidEvent(PaneId.getPaneId(view.getProductScanPane().getSelectedTabId()), message));
		clearErrorMessage();
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.ERROR");
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
	}

	private void setErrorDialog(String message, String title) {
		clearErrorMessage();
		getLogger().debug("Sent : ProductInvalidEvent");
		EventBusUtil.publish(new ProductInvalidEvent(PaneId.getPaneId(view.getProductScanPane().getSelectedTabId()), message));
		MessageDialog.showScrollingInfo(view.getMainWindow().getStage(), message, title, 30, 80);
		getLogger().debug("Sent : StatusMessageEvent - StatusMessageEventType.ERROR");
		EventBusUtil.publish(new StatusMessageEvent(title, StatusMessageEventType.ERROR));
	}


	public void initData() {
		view.getAssociateIdTextField().setText(view.getMainWindow().getUserId());
	}

	public ProductTypeData getProductTypeData() {
		return view.getMainWindow().getApplicationContext().getProductTypeData();
	}
	
	public Logger getLogger() {
		return view.getLogger();
	}
}
