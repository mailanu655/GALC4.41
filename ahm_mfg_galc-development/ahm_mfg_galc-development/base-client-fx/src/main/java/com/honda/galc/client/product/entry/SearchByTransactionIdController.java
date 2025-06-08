package com.honda.galc.client.product.entry;

import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.pane.SearchByTransactionIdPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ManualTransactionIdEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchByTransactionIdController {
	private SearchByTransactionIdPane view;
	private SearchByTransactionIdModel model;

	public SearchByTransactionIdController(SearchByTransactionIdPane view, ApplicationContext context) {
		this.view = view;
		model = new SearchByTransactionIdModel(context);
		mapActions();
		EventBusUtil.register(this);
	}

	private void mapActions() {
		view.getTransactionIdButton().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent arg0) {
				view.getProductController().setKeyboardPopUpVisible(false);
				ManualTransactionIdEntryDialog manualTransactionIdEntryDialog = new ManualTransactionIdEntryDialog(
						"Manual Transaction Id Entry Dialog", view.getProductController().getProductTypeData(),view.getProductController().getView().getMainWindow().getApplicationContext().getApplicationId());
				Logger.getLogger().check("Manual Tranasaction Id Entry Dialog Box populated");
				manualTransactionIdEntryDialog.showDialog();
				Long transactionId = manualTransactionIdEntryDialog.getResultTransactionID();
				Logger.getLogger().check("Transaction Id : " + transactionId + " selected");
				if (transactionId != null) {
					view.getProductController().getView().getMainWindow().setWaitCursor();

					view.setTransactionId(transactionId.toString());

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (view.getProductController().getModel().getProperty().isAutoEnteredManualProductInput()) {
								view.getTransactionIdField().fireEvent(arg0);
								Logger.getLogger().check("Selected Transaction Id Auto Entered");
							}
							view.getProductController().getView().getMainWindow().setDefaultCursor();
						}

					});
				}
			}
		});

		view.getTransactionIdField().setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				if(validateInput(view.getTransactionId())) {
					List<String> products = getProductsByTransactionId(Long.parseLong(view.getTransactionId()));
					if(products != null && !products.isEmpty()) {
						EventBusUtil.publish(new ProductEvent(products, ProductEventType.PRODUCT_INPUT_RECIEVED));
					}
				}
			}
		});
	}

	public boolean validateInput(String transactionId) {
		if(!transactionId.matches("\\d+")) {
			view.setErrorMessage("Invalid transaction ID " + transactionId + " search field can only contain numeric values", view.getTransactionIdField());
			return false;
		}

		return true;	
	}

	private List<String> getProductsByTransactionId(long transactionId) {
		List<String> contents = null;
		boolean isOnlyOutstanding= view.getOutstandingOnlyCheckBox().isSelected();
		if(!isOnlyOutstanding) {
			contents = model.findProductsByTransactionId(transactionId, model.getProductType());
		}else {
			contents = model.findOutstandingProductsByTransactionId(transactionId);
		}

		if (contents == null || contents.isEmpty()) {
			view.setErrorMessage("Transaction Id : " + transactionId + " not found", view.getTransactionIdField());
		}
		return contents;
	}

	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
			view.getTransactionIdField().setText(null);
			view.setTestFieldState(view.getTransactionIdField(), TextFieldState.EDIT);
		} 
	}

	public SearchByTransactionIdPane getView() {
		return view;
	}

	public SearchByTransactionIdModel getModel() {
		return model;
	}
}
