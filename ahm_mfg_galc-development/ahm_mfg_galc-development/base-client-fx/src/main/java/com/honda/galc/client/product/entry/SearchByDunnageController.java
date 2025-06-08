package com.honda.galc.client.product.entry;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.pane.SearchByDunnagePane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ManualDunnageEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchByDunnageController {
	private SearchByDunnagePane view;
	private SearchByDunnageModel model;

	public SearchByDunnageController() {}

	public SearchByDunnageController(SearchByDunnagePane view, ApplicationContext context) {
		this.view = view;
		model = new SearchByDunnageModel(context);
		mapActions();
		EventBusUtil.register(this);
	}

	public List<String> getProductsByDunnage(String dunnage) {
		List<String> contents = null;
		contents = model.getProductsByDunnage(dunnage);

		if (contents == null || contents.isEmpty()) {
			view.setErrorMessage("No Products found in Dunnage: " + dunnage, view.getDunnageField());
		}
		return contents;
	}

	private void mapActions() {
		view.getDunnageButton().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent arg0) {
				view.getProductController().setKeyboardPopUpVisible(false);
				ManualDunnageEntryDialog manualDunnageEntryDialog = new ManualDunnageEntryDialog(
						"Manual Dunnage Entry Dialog", view.getProductController().getProductTypeData(),view.getProductController().getView().getMainWindow().getApplicationContext().getApplicationId());
				Logger.getLogger().check("Manual Dunnage Entry Dialog Box populated");
				manualDunnageEntryDialog.showDialog();
				String dunnage = manualDunnageEntryDialog.getResultDunnage();
				Logger.getLogger().check("Dunnage: " + dunnage + " selected");
				if (!(dunnage == null || StringUtils.isEmpty(dunnage))) {
					view.getProductController().getView().getMainWindow().setWaitCursor();

					view.setDunnage(dunnage);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (view.getProductController().getModel().getProperty().isAutoEnteredManualProductInput()) {
								view.getDunnageField().fireEvent(arg0);
								Logger.getLogger().check("Selected Dunnage Auto Entered");
							}
							view.getProductController().getView().getMainWindow().setDefaultCursor();
						}

					});
				}
			}
		});	

		view.getDunnageTextField().setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				List<String> products = getProductsByDunnage(view.getDunnage());
				if(!products.isEmpty()) {
					EventBusUtil.publish(new ProductEvent(products, ProductEventType.PRODUCT_INPUT_RECIEVED));
				}
			}
		});
	}

	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
			view.getDunnageField().setText(null);
			view.setTestFieldState(view.getDunnageField(), TextFieldState.EDIT);
		} 
	}
}
