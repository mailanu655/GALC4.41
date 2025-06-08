package com.honda.galc.client.product.entry;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.pane.SearchByRepairAreaPane;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ManualRepairAreaEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchByRepairAreaController {
	private SearchByRepairAreaPane view;
	private SearchByRepairAreaModel model;

	public SearchByRepairAreaController() {}

	public SearchByRepairAreaController(SearchByRepairAreaPane view, ApplicationContext context) {
		this.view = view;
		model = new SearchByRepairAreaModel(context);
		mapActions();
		EventBusUtil.register(this);
	}

	public List<String> getProductsByRepairArea(String repairAreaName) {
		List<String> contents = null;
		contents = model.getProductsByRepairArea(repairAreaName);

		if (contents == null || contents.isEmpty()) {
			view.setErrorMessage("No Products found in Repair Area: " + repairAreaName, view.getRepairAreaField());
		}
		return contents;
	}

	private void mapActions() {
		view.getRepairAreaButton().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent arg0) {
				view.getProductController().setKeyboardPopUpVisible(false);
				ManualRepairAreaEntryDialog manualRepairAreaEntryDialog = new ManualRepairAreaEntryDialog(
						"Manual Repair Area Entry Dialog", view.getProductController().getProductTypeData(),view.getProductController().getView().getMainWindow().getApplicationContext().getApplicationId());
				Logger.getLogger().check("Manual Repair Area Entry Dialog Box populated");
				manualRepairAreaEntryDialog.showDialog();
				String repairArea = manualRepairAreaEntryDialog.getResultRepairArea();
				Logger.getLogger().check("Repair Area: " + repairArea + " selected");
				if (!(repairArea == null || StringUtils.isEmpty(repairArea))) {
					view.getProductController().getView().getMainWindow().setWaitCursor();

					view.setRepairArea(repairArea);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (view.getProductController().getModel().getProperty().isAutoEnteredManualProductInput()) {
								view.getRepairAreaField().fireEvent(arg0);
								Logger.getLogger().check("Selected Repair Area Auto Entered");
							}
							view.getProductController().getView().getMainWindow().setDefaultCursor();
						}

					});
				}
			}
		});	

		view.getRepairAreaTextField().setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent e) {
				List<String> products = getProductsByRepairArea(view.getRepairArea());
				if(!products.isEmpty()) {
					EventBusUtil.publish(new ProductEvent(products, ProductEventType.PRODUCT_INPUT_RECIEVED));
				}
			}
		});
	}

	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if(event.getEventType().equals(ProductEventType.PRODUCT_INPUT_OK)) {
			view.getRepairAreaField().setText(null);
			view.setTestFieldState(view.getRepairAreaField(), TextFieldState.EDIT);
		} 
	}
}
